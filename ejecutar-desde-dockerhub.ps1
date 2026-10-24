param(
    [string] $UsuarioDockerHub = 'echavez85',
    [string] $Etiqueta = 'v1'
)
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
& (Join-Path $PSScriptRoot 'preparar-env.ps1')

$UsuarioDockerHub = $UsuarioDockerHub.Trim().ToLowerInvariant()
if ($UsuarioDockerHub -notmatch '^[a-z0-9][a-z0-9_-]*$') { throw 'Usuario de Docker Hub invalido.' }
if ($Etiqueta -notmatch '^[A-Za-z0-9_][A-Za-z0-9_.-]{0,127}$') { throw 'Etiqueta invalida.' }
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) { throw 'No se encontro Docker.' }
docker info --format '{{.ServerVersion}}' | Out-Null
if ($LASTEXITCODE -ne 0) { throw 'Docker Desktop no esta iniciado.' }

$env:DOCKERHUB_USER = $UsuarioDockerHub
$env:IMAGE_TAG = $Etiqueta
Write-Host "Descargando imagenes $UsuarioDockerHub ($Etiqueta) desde Docker Hub..." -ForegroundColor Cyan
docker compose --env-file .env -f compose.hub.yaml config --quiet
if ($LASTEXITCODE -ne 0) { throw 'Configuracion de Compose invalida. Revisa .env.' }
docker compose --env-file .env -f compose.hub.yaml pull
if ($LASTEXITCODE -ne 0) { throw 'Fallo docker compose pull. Confirma que las siete imagenes existan y sean accesibles.' }
docker compose --env-file .env -f compose.hub.yaml up -d --no-build
if ($LASTEXITCODE -ne 0) { throw 'Fallo docker compose up. Ejecuta docker compose -f compose.hub.yaml logs --tail 100' }
docker compose --env-file .env -f compose.hub.yaml ps
Write-Host 'Frontend: http://localhost:4200' -ForegroundColor Green
Write-Host 'Eureka: http://localhost:8761' -ForegroundColor Green
Write-Host 'Las imagenes se descargaron del Docker Hub; no se compilaron localmente.' -ForegroundColor Green
