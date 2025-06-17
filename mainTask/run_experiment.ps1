# Configurable Parameters
$outputFile = "output.txt"
$reportFile = "report.txt"

# Specify the heuristic manually (e.g., 0, 2, or 4)
$heuristic = 0 # Change this to the desired heuristic (0, 1, 2, 3, 4, or 5)

# Run only for depths 2 and 4
$depths = 6

# Loop through the specified depths
foreach ($depth in $depths) {
    Write-Host "`nRunning experiment for Heuristic $heuristic | Depth $depth"

    # Run the game and capture output
    java ChainReactionMain --depth $depth --heuristic $heuristic 2>&1 | Tee-Object -FilePath $outputFile

    # Read and parse output
    $output = Get-Content $outputFile

    $nodeLine = $output | Where-Object { $_ -match "Nodes explored" }
    $timeLine = $output | Where-Object { $_ -match "Time taken" }

    $nodes = 0
    $time = 0

    if ($nodeLine -match "Nodes explored: (\d+)") {
        $nodes = [int]$matches[1]
    }

    if ($timeLine -match "Time taken: (\d+)ms") {
        $time = [int]$matches[1]
    }

    # Format and append to report
    $summary = "Heuristic: $heuristic | Depth: $depth | Nodes: $nodes | Time: ${time}ms"
    Add-Content $reportFile $summary
    Write-Host "  ➤ $summary"
}

Write-Host "`nAll experiments completed. Results saved in $reportFile."
