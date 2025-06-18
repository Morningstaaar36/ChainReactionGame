# Get all files in the current folder and subfolders
Get-ChildItem -Recurse -File -ErrorAction SilentlyContinue | ForEach-Object {
    try {
        # Prepare the output for this file
        $filePath = $_.FullName
        $separator = "===== File: $filePath ====="
        $endSeparator = "===== End of File: $filePath =====`n`n"

        # Check if the file is likely a text file based on extension
        $textExtensions = @('.txt', '.java', '.xml', '.properties', '.json', '.yaml', '.yml', '.md', '.ps1', '.sh', '.bat', '.cmd')
        if ($textExtensions -contains $_.Extension.ToLower()) {
            # Read file content
            $content = Get-Content -Path $filePath -Raw -ErrorAction Stop

            # Combine path, separator, content, and end separator
            $output = "$separator`n$content`n$endSeparator"

            # Append to code.txt with UTF-8 encoding using Add-Content
            Add-Content -Path "code.txt" -Value $output -Encoding UTF8
        } else {
            # For non-text files, only append the path and a note
            $output = "$separator`n[Non-text file, content omitted]`n$endSeparator"
            Add-Content -Path "code.txt" -Value $output -Encoding UTF8
        }
    } catch {
        # Log error for inaccessible or problematic files
        $errorMessage = "===== Error processing file: $filePath =====`nError: $($_.Exception.Message)`n===== End of Error =====`n`n"
        Add-Content -Path "code.txt" -Value $errorMessage -Encoding UTF8
    }
}
