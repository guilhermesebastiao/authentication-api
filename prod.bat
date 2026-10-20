
@echo off
cd /d "%~dp0"

echo Iniciando Authentication API - PROD...

docker compose --env-file .env -f compose.yaml -f compose.prod.yaml up -d --build

if errorlevel 1 (
    echo Erro ao iniciar o ambiente PROD.
    pause
    exit /b 1
)

echo Ambiente PROD iniciado.
pause
