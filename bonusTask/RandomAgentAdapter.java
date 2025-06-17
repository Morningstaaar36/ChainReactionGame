public class RandomAgentAdapter implements Agent {
    private RandomAgent agent;

    public RandomAgentAdapter(RandomAgent agent) {
        this.agent = agent;
    }

    @Override
    public Move getMove(GameState state) {
        return agent.findRandomMove(state);
    }
}