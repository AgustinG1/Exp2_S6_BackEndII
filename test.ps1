$uri = "http://localhost:8080/api/auth/login"
$body = @{
    username = "admin"
    password = "12345"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri $uri -Method Post -Body $body -ContentType "application/json"
    Write-Host "Success! Token:"
    Write-Host $response
} catch {
    Write-Host "Error details:"
    Write-Host $_.Exception.Response.StatusCode.value__
    Write-Host $_.Exception.Message
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $responseBody = $reader.ReadToEnd()
    Write-Host $responseBody
}
