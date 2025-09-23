Write-Host "Checking if anything is running on port 15400..."

$portInUse = netstat -ano | Select-String ":15400"

if ($null -eq $portInUse) {
    Write-Host "Nothing is running on port 15400. Starting the process..."
    java -jar "..\File-Integrity-Scanner\File-Integrity-Scanner\target\File-Integrity-Scanner-1.2.jar" & 
    Write-Host "File-Integrity-Scanner started."
    java -jar .\target\file_integrity_gui-0.3.1-jar-with-dependencies.jar
} else {
    Write-Host "File-Integrity-Scanner is already running on port 15400."
    java -jar .\target\file_integrity_gui-0.3.1-jar-with-dependencies.jar
}