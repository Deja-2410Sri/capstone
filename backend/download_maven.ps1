$url = 'https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip'
$out = 'apache-maven-3.9.6-bin.zip'
Write-Host 'Downloading Maven...'
Invoke-WebRequest -Uri $url -OutFile $out
Write-Host 'Extracting...'
Expand-Archive -Force -Path $out -DestinationPath '.'
Write-Host 'Done'