$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot
& (Join-Path $PSScriptRoot 'preparar-env.ps1')
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    throw 'No se encontro Docker. Instala/abre Docker Desktop antes de continuar.'
}
docker info --format '{{.ServerVersion}}' | Out-Null
if ($LASTEXITCODE -ne 0) { throw 'Docker Desktop no esta iniciado.' }

Write-Host 'Validando configuracion de Docker Compose...' -ForegroundColor Cyan
docker compose --env-file .env -f compose.yaml config --quiet
if ($LASTEXITCODE -ne 0) { throw 'Configuracion invalida. Comprueba el archivo .env (sin compartir contrasenas).' }

Write-Host 'Construyendo e iniciando QuiroSalud LOCALMENTE...' -ForegroundColor Cyan
docker compose --env-file .env -f compose.yaml up --build -d
if ($LASTEXITCODE -ne 0) { throw 'Docker Compose fallo. Ejecuta: docker compose --env-file .env -f compose.yaml logs --tail 100' }
docker compose --env-file .env -f compose.yaml ps
Write-Host 'Frontend: http://localhost:4200' -ForegroundColor Green
Write-Host 'Eureka:   http://localhost:8761' -ForegroundColor Green
Write-Host 'Gateway:  http://localhost:8080' -ForegroundColor Green
Write-Host 'Usuario de prueba: admin@quirosalud.local' -ForegroundColor Yellow
Write-Host 'Tu clave de prueba esta en .env (BOOTSTRAP_PASSWORD).' -ForegroundColor Yellow
