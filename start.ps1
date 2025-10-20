Write-Host "Checking if anything is running on port 15400..."

$portInUse = netstat -ano | Select-String ":15400"

if (-not (Test-Path -Path "..\artifacts")) {
    New-Item -Path "..\artifacts" -ItemType Directory
}

if ($null -eq $portInUse) {
    Write-Host "Nothing is running on port 15400. Starting the process..."
    java -jar "..\File-Integrity-Scanner\File-Integrity-Scanner\target\File-Integrity-Scanner-1.5.jar" & 
    Write-Host "File-Integrity-Scanner started."
    cp .\target\integrity_hash-1.0-jar-with-dependencies.jar ..\artifacts
    java -jar ..\artifacts\integrity_hash-1.0-jar-with-dependencies.jar
    exit
} else {
    Write-Host "File-Integrity-Scanner is already running on port 15400."
    cp .\target\integrity_hash-1.0-jar-with-dependencies.jar ..\artifacts
    java -jar ..\artifacts\integrity_hash-1.0-jar-with-dependencies.jar
    exit
}
