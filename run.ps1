# Script para ejecutar el proyecto Spring Boot
# Configura JAVA_HOME y ejecuta la aplicación

$env:JAVA_HOME = "c:/Users/<user_home>/.sdkman/candidates/java/17.0.12-oracle"

Write-Host "==================================================" -ForegroundColor Green
Write-Host "  Iniciando Supermarket Management System" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host ""

# Ejecutar la aplicación
java -jar target/copilot-java-prompt-example-0.0.1-SNAPSHOT.jar
