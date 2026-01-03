# Script para compilar el proyecto Spring Boot

$env:JAVA_HOME = "c:/Users/<user_home>/.sdkman/candidates/java/17.0.12-oracle"

Write-Host "==================================================" -ForegroundColor Green
Write-Host "  Compilando Supermarket Management System" -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Java Home: $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host ""

# Compilar el proyecto
mvn clean package -DskipTests
