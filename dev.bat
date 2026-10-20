
@echo off
cd /d "%~dp0"

echo Iniciando Authentication API - DEV...

docker compose -f compose.yaml -f compose.dev.yaml up --build --watch

if errorlevel 1 (
    echo Erro ao iniciar o ambiente DEV.
    pause
    exit /b 1
)

echo Ambiente DEV iniciado.
pause
