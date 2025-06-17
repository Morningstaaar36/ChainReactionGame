public class GameEngine {
    private int depth;
    private HeuristicEvaluator evaluator;
    private FileManager fileManager;

    public GameEngine(int depth, int heuristicType, FileManager fileManager) {
        this.depth = depth;
        this.evaluator = new HeuristicEvaluator(heuristicType);
        this.fileManager = fileManager;
    }

    public void computeAiMove() {
        FileManager.GameStateInfo info = fileManager.readGameState();
        if (info == null) {
            System.err.println("Failed to read game state for AI move");
            return;
        }
        GameState state = info.getState();

        if (state.getStatus() != GameState.GameStatus.ONGOING) {
            return;
        }

        MinimaxAgent agent = new MinimaxAgent(depth, evaluator);
        MinimaxAgent.MoveEvaluation result = agent.findBestMove(state);
        Move aiMove = result.getMove();

        if (aiMove != null) {
            System.out.println("AI choosing move: " + aiMove);
            state.makeMove(aiMove);
            fileManager.writeGameState(state, false);
        } else {
            System.out.println("AI could not find a valid move.");
        }
    }
}