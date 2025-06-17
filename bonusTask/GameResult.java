public class GameResult {
    public final Player winner;
    public final long totalTime;
    public final long redNodes;
    public final long blueNodes;

    public GameResult(Player winner, long totalTime, long redNodes, long blueNodes) {
        this.winner = winner;
        this.totalTime = totalTime;
        this.redNodes = redNodes;
        this.blueNodes = blueNodes;
    }
}