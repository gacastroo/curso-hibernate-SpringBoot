# ========================================================
# CHECKLIST AUTOMATIZADO COMPLETO — DIA 16 (PowerShell)
# ========================================================

Write-Host "====================================================================="
Write-Host "CHECKLIST AUTOMATIZADO COMPLETO — DIA 16"
Write-Host "====================================================================="

# ---------------------------
# PASO 1: VERSION DE JAVA
# ---------------------------
Write-Host "`nPASO 1: VERSION DE JAVA"
$javaVersion = & java -version 2>&1 | Select-Object -First 1
Write-Host "Java instalado: $javaVersion"

$pomPath = "pom.xml"
if (Test-Path $pomPath) {
    [xml]$pom = Get-Content $pomPath
    $pomJava = $pom.project.properties.'java.version'
    Write-Host "Java en pom.xml: $pomJava"
    if ($pomJava -and ($javaVersion -notlike "*$pomJava*")) {
        Write-Host "⚠️  Atención: Java del sistema NO coincide con pom.xml"
    }
} else {
    Write-Host "pom.xml NO existe"
}

# ---------------------------
# PASO 2: APPLICATION.PROPERTIES — DATA.SQL
# ---------------------------
Write-Host "`nPASO 2: APPLICATION.PROPERTIES — DATA.SQL"
$propPath = "src/main/resources/application.properties"
if (Test-Path $propPath) {
    $props = Get-Content $propPath
    $sqlMode = $props | Where-Object { $_ -match "^spring.sql.init.mode=" }
    $deferInit = $props | Where-Object { $_ -match "^spring.jpa.defer-datasource-initialization=" }
    Write-Host ("spring.sql.init.mode: {0}" -f ($sqlMode -join "" -ne "" ? $sqlMode : "NO ENCONTRADO"))
    Write-Host ("spring.jpa.defer-datasource-initialization: {0}" -f ($deferInit -join "" -ne "" ? $deferInit : "NO ENCONTRADO"))
} else {
    Write-Host "application.properties NO existe"
}

# ---------------------------
# PASO 3: APPLICATION.PROPERTIES — CONSOLA H2
# ---------------------------
Write-Host "`nPASO 3: APPLICATION.PROPERTIES — CONSOLA H2"
if (Test-Path $propPath) {
    $h2Enabled = $props | Where-Object { $_ -match "^spring.h2.console.enabled=" }
    $h2Path = $props | Where-Object { $_ -match "^spring.h2.console.path=" }
    Write-Host ("spring.h2.console.enabled: {0}" -f ($h2Enabled -join "" -ne "" ? $h2Enabled : "NO ENCONTRADO"))
    Write-Host ("spring.h2.console.path: {0}" -f ($h2Path -join "" -ne "" ? $h2Path : "NO ENCONTRADO"))
} else {
    Write-Host "application.properties NO existe"
}

# ---------------------------
# PASO 4: DOCKER DESKTOP ARRANCADO
# ---------------------------
Write-Host "`nPASO 4: DOCKER DESKTOP ARRANCADO"
try {
    docker info | Out-Null
    Write-Host "Docker Desktop: ARRANCADO"
} catch {
    Write-Host "Docker Desktop: NO ARRANCADO"
}

# ---------------------------
# PASO 5: PUERTO LIBRE
# ---------------------------
Write-Host "`nPASO 5: PUERTO 8081"
$portCheck = Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue
if ($portCheck) {
    Write-Host "Puerto 8081 OCUPADO"
} else {
    Write-Host "Puerto 8081 LIBRE"
}

# ---------------------------
# PASO 6: COMPILAR Y ARRANCAR EN LOCAL
# ---------------------------
Write-Host "`nPASO 6: COMPILAR Y ARRANCAR EN LOCAL"
Write-Host "Compilando proyecto..."
$mvnCompile = Start-Process mvn -ArgumentList "clean","compile" -NoNewWindow -Wait -PassThru
if ($mvnCompile.ExitCode -eq 0) {
    Write-Host "Compilación: OK"
} else {
    Write-Host "⚠️  Compilación: FALLÓ"
}

Write-Host "Arrancando aplicación en segundo plano..."
$proc = Start-Process mvn -ArgumentList "spring-boot:run" -NoNewWindow -PassThru
Start-Sleep -Seconds 10

# ---------------------------
# Verificar endpoint API
# ---------------------------
Write-Host "`nVerificando endpoint API: http://localhost:8081/api/pizzas"
try {
    $response = Invoke-RestMethod http://localhost:8081/api/pizzas -UseBasicParsing
    if ($null -eq $response -or $response.Count -eq 0) {
        Write-Host "⚠️  API devuelve array vacío [] — revisar data.sql"
    } else {
        Write-Host "✅ API devuelve datos:"
        $response | ConvertTo-Json -Depth 5
    }
} catch {
    Write-Host "⚠️  No se pudo conectar a la API"
}

# Detener la app
if ($proc -ne $null) {
    Stop-Process -Id $proc.Id -Force
}

# ---------------------------
# PASO 7: DOCKERFILE
# ---------------------------
Write-Host "`nPASO 7: DOCKERFILE"
if (Test-Path "Dockerfile") {
    Write-Host "Dockerfile EXISTE"
    Get-Content Dockerfile | Select-String "FROM"
} else {
    Write-Host "Dockerfile NO existe"
}

# ---------------------------
# PASO 8: DOCKER-COMPOSE VARIABLES
# ---------------------------
Write-Host "`nPASO 8: DOCKER-COMPOSE VARIABLES"
if (Test-Path "docker-compose.yml") {
    Write-Host "docker-compose.yml EXISTE"
    Get-Content docker-compose.yml | Select-String "SPRING_DATASOURCE|SPRING_JPA|SPRING_SQL_INIT_MODE"
} else {
    Write-Host "docker-compose.yml NO existe"
}

# ---------------------------
# PASO 9: LEVANTAR DOCKER
# ---------------------------
Write-Host "`nPASO 9: LEVANTAR DOCKER"
Write-Host "Comandos recomendados:"
Write-Host "  docker compose down"
Write-Host "  docker compose up --build -d"
Write-Host "  docker compose logs -f app"

# ---------------------------
# PASO 10: ADMINER
# ---------------------------
Write-Host "`nPASO 10: ADMINER"
Write-Host "Abrir navegador en http://localhost:9090"
Write-Host "Usuario: postgres, Password: secret, DB: pizzeria"

# ---------------------------
# ERRORES PROYECTO PERSONAL
# ---------------------------
Write-Host "`nERRORES PROYECTO PERSONAL (verificar manualmente)"
Write-Host "A) data.sql existe?"
Write-Host "B) Puerto distinto a 8081?"
Write-Host "C) MySQL en vez de PostgreSQL?"
Write-Host "D) data.sql compatible con PostgreSQL?"

Write-Host "`n====================================================================="
Write-Host "Checklist completo ejecutado."