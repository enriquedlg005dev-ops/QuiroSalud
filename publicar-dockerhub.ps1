param(
    [string] $UsuarioDockerHub = 'echavez85',
    [string] $Etiqueta = 'v1'
)
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
& (Join-Path $PSScriptRoot 'preparar-env.ps1')

$UsuarioDockerHub = $UsuarioDockerHub.Trim().ToLowerInvariant()
if ($UsuarioDockerHub -notmatch '^[a-z0-9][a-z0-9_-]*$') {
    throw 'El usuario de Docker Hub debe contener solo letras minusculas, numeros, guion o guion bajo.'
}
if ($Etiqueta -notmatch '^[A-Za-z0-9_][A-Za-z0-9_.-]{0,127}$') {
    throw 'Etiqueta Docker invalida. Ejemplo: v1'
}
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw 'No se encontro Docker. Abre Docker Desktop antes de continuar.'
}
docker info --format '{{.ServerVersion}}' | Out-Null
if ($LASTEXITCODE -ne 0) { throw 'Docker Desktop no esta iniciado.' }

$env:DOCKERHUB_USER = $UsuarioDockerHub
$env:IMAGE_TAG = $Etiqueta
$servicios = @('eureka','auth','pacientes','citas','ventas','gateway','frontend')
Write-Host "Construyendo 7 imagenes: $UsuarioDockerHub / $Etiqueta" -ForegroundColor Cyan
docker compose --env-file .env -f compose.yaml config --quiet
if ($LASTEXITCODE -ne 0) { throw 'Configuracion de Compose invalida. Revisa .env.' }
docker compose --env-file .env -f compose.yaml build @servicios
if ($LASTEXITCODE -ne 0) { throw 'Error al compilar. No se publicaron las imagenes.' }

Write-Host 'Publicando las siete imagenes. Debes haber ejecutado docker login.' -ForegroundColor Cyan
docker compose --env-file .env -f compose.yaml push @servicios
if ($LASTEXITCODE -ne 0) { throw 'No se pudieron subir todas las imagenes. Verifica docker login, usuario y repositorios.' }

# Guardar el usuario y la etiqueta para futuras ejecuciones con Docker Compose.
$envPath = Join-Path $PSScriptRoot '.env'
$lineas = [System.Collections.Generic.List[string]]::new()
foreach ($linea in [System.IO.File]::ReadAllLines($envPath)) {
    if ($linea -notmatch '^(DOCKERHUB_USER|IMAGE_TAG)=') { $lineas.Add($linea) }
}
$lineas.Add('DOCKERHUB_USER=' + $UsuarioDockerHub)
$lineas.Add('IMAGE_TAG=' + $Etiqueta)
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllLines($envPath, [string[]]$lineas.ToArray(), $utf8NoBom)
Write-Host 'Publicacion finalizada. Consulta tus repositorios en hub.docker.com.' -ForegroundColor Green
Write-Host 'Puedes probar la descarga: .\ejecutar-desde-dockerhub.ps1 -UsuarioDockerHub ' -NoNewline
Write-Host $UsuarioDockerHub -NoNewline
Write-Host ' -Etiqueta ' -NoNewline
Write-Host $Etiqueta
