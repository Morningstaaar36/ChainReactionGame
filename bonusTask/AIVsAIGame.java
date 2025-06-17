import java.util.List;

public class AIVsAIGame {
    private Agent redAgent;
    private Agent blueAgent;

    public AIVsAIGame(Agent redAgent, Agent blueAgent) {
        this.redAgent = redAgent;
        this.blueAgent = blueAgent;
    }

    public GameResult playGame() {
        GameState state = new GameState();
        long startTime = System.currentTimeMillis();
        long redNodes = 0;
        long blueNodes = 0;

        while (state.getStatus() == GameState.GameStatus.ONGOING) {
            Player current = state.getCurrentPlayer();
            Agent agent = (current == Player.RED) ? redAgent : blueAgent;
            Move move = agent.getMove(state);
            state.makeMove(move);

            if (agent instanceof MinimaxAgentAdapter) {
                MinimaxAgentAdapter adapter = (MinimaxAgentAdapter) agent;
                int nodes = adapter.getLastNodesExplored();
                System.out.println("Player " + current + " explored " + nodes + " nodes for move " + move);
                if (current == Player.RED) {
                    redNodes += nodes;
                } else {
                    blueNodes += nodes;
                }
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;
        GameState.GameStatus status = state.getStatus();
        Player winner = (status == GameState.GameStatus.RED_WINS) ? Player.RED : 
                        (status == GameState.GameStatus.BLUE_WINS) ? Player.BLUE : null;

        return new GameResult(winner, totalTime, redNodes, blueNodes);
    }
}