public class MinimaxAgentAdapter implements Agent {
    private MinimaxAgent agent;
    private int lastNodesExplored;

    public MinimaxAgentAdapter(MinimaxAgent agent) {
        this.agent = agent;
        this.lastNodesExplored = 0;
    }

    @Override
    public Move getMove(GameState state) {
        MinimaxAgent.MoveEvaluation result = agent.findBestMove(state);
        this.lastNodesExplored = agent.getLastNodesExplored(); // Assuming this method is added
        return result.getMove();
    }

    public int getLastNodesExplored() {
        return lastNodesExplored;
    }
}