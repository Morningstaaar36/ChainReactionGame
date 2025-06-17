public class PerformanceAnalyzer {
    private long totalNodesExplored;
    private long totalTimeMs;
    private int totalGames;
    private int wins;
    private int losses;
    private int draws;
    
    public PerformanceAnalyzer() {
        this.totalNodesExplored = 0;
        this.totalTimeMs = 0;
        this.totalGames = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }
    
    public void recordGameResult(GameState.GameStatus status, Player aiPlayer) {
        totalGames++;
        if (status == GameState.GameStatus.RED_WINS && aiPlayer == Player.RED) {
            wins++;
        } else if (status == GameState.GameStatus.BLUE_WINS && aiPlayer == Player.BLUE) {
            wins++;
        } else if (status == GameState.GameStatus.ONGOING) {
            draws++;
        } else {
            losses++;
        }
    }
    
    public void recordSearchMetrics(long nodesExplored, long timeMs) {
        totalNodesExplored += nodesExplored;
        totalTimeMs += timeMs;
    }
    
    public void printReport() {
        System.out.println("Performance Report:");
        System.out.println("Total Games: " + totalGames);
        System.out.println("Wins: " + wins + " (" + (wins * 100.0 / totalGames) + "%)");
        System.out.println("Losses: " + losses + " (" + (losses * 100.0 / totalGames) + "%)");
        System.out.println("Draws: " + draws + " (" + (draws * 100.0 / totalGames) + "%)");
        if (totalGames > 0) {
            System.out.println("Average Nodes Explored per Game: " + (totalNodesExplored / (double) totalGames));
            System.out.println("Average Time per Game: " + (totalTimeMs / (double) totalGames) + "ms");
        }
    }
}