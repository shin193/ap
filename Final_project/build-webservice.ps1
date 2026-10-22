param([switch]$SkipTests)
$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $PSScriptRoot
$mavenCommand = Get-Command mvn.cmd -ErrorAction SilentlyContinue
if ($mavenCommand) {
    $mavenPath = $mavenCommand.Source
} else {
    $bundledMaven = Get-ChildItem "$env:ProgramFiles/JetBrains/IntelliJ IDEA*/plugins/maven/lib/maven3/bin/mvn.cmd" -ErrorAction SilentlyContinue |
        Sort-Object FullName -Descending | Select-Object -First 1
    $mavenPath = if ($bundledMaven) { $bundledMaven.FullName } else { Join-Path $PSScriptRoot 'mvnw.cmd' }
}
$buildArguments = @('-f', (Join-Path $PSScriptRoot 'pom.xml'), "-Dmaven.repo.local=$repoRoot/.maven-cache", 'verify')
if ($SkipTests) { $buildArguments += '-DskipTests' }
& $mavenPath @buildArguments
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Host 'Build complete. Select Library Web Service in IntelliJ, or run:'
Write-Host "java -jar $PSScriptRoot/webservice/target/library-webservice-1.0.0.jar"
