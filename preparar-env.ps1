# QuiroSalud: generar o reparar la configuracion local para Docker Compose.
# Compatible con Windows PowerShell 5.1. No imprime contrasenas.
$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot

function NuevaClaveHex([int]$LongitudBytes) {
    $bytes = New-Object byte[] $LongitudBytes
    $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
    return [System.BitConverter]::ToString($bytes).Replace('-', '').ToLowerInvariant()
}

function NuevaClaveBase64([int]$LongitudBytes) {
    $bytes = New-Object byte[] $LongitudBytes
    $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
    return [System.Convert]::ToBase64String($bytes)
}

function EsInvalido([string]$Valor) {
    if ([string]::IsNullOrWhiteSpace($Valor)) { return $true }
    # Corrige tambien el archivo defectuoso generado por la version anterior:
    # distintas asignaciones podian concatenarse en una misma linea.
    if ($Valor -match '(?i)(?:MYSQL_ROOT_PASSWORD|DB_PASSWORD|JWT_SECRET_BASE64|BOOTSTRAP_EMAIL|BOOTSTRAP_PASSWORD|DOCKERHUB_USER|IMAGE_TAG)=' ) { return $true }
    if ($Valor -match '(?i)REEMPLAZAR|CHANGE_ME|CONFIGURE|TODO') { return $true }
    return $false
}

$ruta = Join-Path $PSScriptRoot '.env'
$datos = @{}
$existia = Test-Path -LiteralPath $ruta
if ($existia) {
    foreach ($linea in [System.IO.File]::ReadAllLines($ruta)) {
        $limpia = $linea.Trim()
        if (-not $limpia -or $limpia.StartsWith('#')) { continue }
        $pos = $linea.IndexOf('=')
        if ($pos -le 0) { continue }
        $clave = $linea.Substring(0, $pos).Trim()
        $valor = $linea.Substring($pos + 1).Trim()
        if (-not $datos.ContainsKey($clave)) { $datos[$clave] = $valor }
    }
}

$reparadas = New-Object 'System.Collections.Generic.List[string]'
if (EsInvalido $datos['MYSQL_ROOT_PASSWORD']) {
    $datos['MYSQL_ROOT_PASSWORD'] = NuevaClaveHex 24
    $reparadas.Add('MYSQL_ROOT_PASSWORD')
}
if (EsInvalido $datos['DB_PASSWORD']) {
    $datos['DB_PASSWORD'] = NuevaClaveHex 24
    $reparadas.Add('DB_PASSWORD')
}
$jwtCorrecto = $false
if (-not (EsInvalido $datos['JWT_SECRET_BASE64'])) {
    try {
        $secretBytes = [System.Convert]::FromBase64String($datos['JWT_SECRET_BASE64'])
        $jwtCorrecto = $secretBytes.Length -ge 32
    } catch { $jwtCorrecto = $false }
}
if (-not $jwtCorrecto) {
    $datos['JWT_SECRET_BASE64'] = NuevaClaveBase64 48
    $reparadas.Add('JWT_SECRET_BASE64')
}
if (EsInvalido $datos['BOOTSTRAP_EMAIL']) {
    $datos['BOOTSTRAP_EMAIL'] = 'admin@quirosalud.local'
    $reparadas.Add('BOOTSTRAP_EMAIL')
}
if (EsInvalido $datos['BOOTSTRAP_PASSWORD']) {
    $datos['BOOTSTRAP_PASSWORD'] = NuevaClaveHex 16
    $reparadas.Add('BOOTSTRAP_PASSWORD')
}
if (EsInvalido $datos['DOCKERHUB_USER']) {
    $datos['DOCKERHUB_USER'] = 'echavez85'
    $reparadas.Add('DOCKERHUB_USER')
}
if (EsInvalido $datos['IMAGE_TAG']) {
    $datos['IMAGE_TAG'] = 'v1'
    $reparadas.Add('IMAGE_TAG')
}

# NO usar una expresion de concatenacion separada por comas dentro de @(...).
# Cada renglon se construye por separado para asegurar 7 lineas reales.
$claves = @(
    'MYSQL_ROOT_PASSWORD', 'DB_PASSWORD', 'JWT_SECRET_BASE64',
    'BOOTSTRAP_EMAIL', 'BOOTSTRAP_PASSWORD', 'DOCKERHUB_USER', 'IMAGE_TAG'
)
$lineas = New-Object 'System.Collections.Generic.List[string]'
foreach ($clave in $claves) {
    $lineas.Add(('{0}={1}' -f $clave, $datos[$clave]))
}
$utf8SinBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines($ruta, [string[]]$lineas.ToArray(), $utf8SinBom)

# Las variables del proceso prevalecen sobre .env en Compose. Sincronizarlas
# evita que valores antiguos de PowerShell anulen el archivo reparado.
foreach ($clave in $claves) {
    [System.Environment]::SetEnvironmentVariable($clave, [string]$datos[$clave], 'Process')
}

if ($reparadas.Count -gt 0) {
    Write-Host ('Archivo .env creado/reparado. Variables reparadas: ' + ($reparadas -join ', ')) -ForegroundColor Green
} else {
    Write-Host 'Archivo .env validado (se conservaron las credenciales).' -ForegroundColor Green
}
