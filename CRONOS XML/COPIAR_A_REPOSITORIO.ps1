param(
    [string]$Destino = "$HOME\Herramientas-de-programaci-n-movil"
)

Write-Host "Destino: $Destino"

if (-not (Test-Path $Destino)) {
    Write-Error "No existe la carpeta destino. Clona primero el repositorio."
    exit 1
}

$items = Get-ChildItem -Path $PSScriptRoot -Force |
    Where-Object { $_.Name -notin @("COPIAR_A_REPOSITORIO.ps1") }

foreach ($item in $items) {
    Copy-Item -Path $item.FullName -Destination $Destino -Recurse -Force
}

Write-Host "Archivos copiados al repositorio."
Write-Host "Ahora abre Android Studio sobre: $Destino"
