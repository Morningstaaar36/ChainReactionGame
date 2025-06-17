import java.util.List;

public class MinimaxAgent {
    private int depthLimit;
    private HeuristicEvaluator evaluator;
    private long timeLimit;
    private long startTime;
    private int nodesExplored;
    private Player aiPlayer;

    public MinimaxAgent(int depthLimit, HeuristicEvaluator evaluator) {
        this.depthLimit = depthLimit;
        this.evaluator = evaluator;
        this.timeLimit = GameConfig.TIME_LIMIT_MS;
        this.nodesExplored = 0;
    }

    public MoveEvaluation findBestMove(GameState state) {
        startTime = System.currentTimeMillis();
        nodesExplored = 0;
        aiPlayer = state.getCurrentPlayer();

        MoveEvaluation result = maximizer(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("Nodes explored: " + nodesExplored);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + "ms");
        return result;
    }

    private MoveEvaluation maximizer(GameState state, int depth, int alpha, int beta) {
        nodesExplored++;
        if (state.getStatus() != GameState.GameStatus.ONGOING || depth >= depthLimit ||
            System.currentTimeMillis() - startTime > timeLimit) {
            return new MoveEvaluation(null, evaluator.evaluate(state, aiPlayer));
        }

        List<Move> legalMoves = state.getLegalMoves();
        if (legalMoves.isEmpty()) {
            return new MoveEvaluation(null, evaluator.evaluate(state, aiPlayer));
        }

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Move move : legalMoves) {
            GameState nextState = new GameState(state);
            nextState.makeMove(move);
            int value = minimizer(nextState, depth + 1, alpha, beta).getValue();

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
            alpha = Math.max(alpha, value);
            if (beta <= alpha) break;
        }
        return new MoveEvaluation(bestMove, bestValue);
    }

    private MoveEvaluation minimizer(GameState state, int depth, int alpha, int beta) {
        nodesExplored++;
        if (state.getStatus() != GameState.GameStatus.ONGOING || depth >= depthLimit ||
            System.currentTimeMillis() - startTime > timeLimit) {
            return new MoveEvaluation(null, evaluator.evaluate(state, aiPlayer));
        }

        List<Move> legalMoves = state.getLegalMoves();
        if (legalMoves.isEmpty()) {
            return new MoveEvaluation(null, evaluator.evaluate(state, aiPlayer));
        }

        Move bestMove = null;
        int bestValue = Integer.MAX_VALUE;

        for (Move move : legalMoves) {
            GameState nextState = new GameState(state);
            nextState.makeMove(move);
            int value = maximizer(nextState, depth + 1, alpha, beta).getValue();

            if (value < bestValue) {
                bestValue = value;
                bestMove = move;
            }
            beta = Math.min(beta, value);
        }
        return new MoveEvaluation(bestMove, bestValue);
    }

    public int getLastNodesExplored() {
        return nodesExplored;
    }

    public static class MoveEvaluation {
        private final Move move;
        private final int value;
        public MoveEvaluation(Move move, int value) {
            this.move = move;
            this.value = value;
        }
        public Move getMove() { return move; }
        public int getValue() { return value; }
    }
}