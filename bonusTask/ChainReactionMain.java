import java.util.Scanner;
import java.util.List;

public class ChainReactionMain {

    public static void main(String[] args) {
        int depth = GameConfig.DEFAULT_DEPTH;
        int heuristicType = HeuristicEvaluator.HEURISTIC_EXPLOSION_CASCADE;
        boolean aiVsAi = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--depth") && i + 1 < args.length) {
                depth = Integer.parseInt(args[i + 1]);
                i++;
            } else if (args[i].equals("--heuristic") && i + 1 < args.length) {
                heuristicType = Integer.parseInt(args[i + 1]);
                i++;
            } else if (args[i].equals("--ai-vs-ai")) {
                aiVsAi = true;
            }
        }

        System.out.println("Starting Chain Reaction with depth=" + depth +
                ", heuristic=" + heuristicType);

        if (aiVsAi) {
            startAiVsAiMode(depth, heuristicType);
        } else {
            startHumanVsAiMode(depth, heuristicType);
        }
    }

    private static void startHumanVsAiMode(int depth, int heuristicType) {
        for (int d : new int[]{1, 4, 6}) {
            for (int h = 0; h <= 5; h++) {
                System.out.println("Running with depth=" + d + ", heuristic=" + h);
                FileManager fileManager = new FileManager(GameConfig.GAME_STATE_FILE);
                GameState uiState = new GameState();
                fileManager.writeGameState(uiState, true);

                ChainReactionUI ui = new ChainReactionUI(fileManager);
                GameEngine engine = new GameEngine(d, h, fileManager);

                while (uiState.getStatus() == GameState.GameStatus.ONGOING) {
                    ui.displayBoard(uiState);
                    if (uiState.getCurrentPlayer() == Player.RED) {
                        List<Move> possibleMoves = uiState.getLegalMoves();
                        if (!possibleMoves.isEmpty()) {
                            Move randomMove = possibleMoves.get((int) (Math.random() * possibleMoves.size()));
                            System.out.println("RandomAgent chooses move: " + randomMove);
                            uiState.makeMove(randomMove);
                            fileManager.writeGameState(uiState, true);
                        }
                    } else {
                        engine.computeAiMove();
                        FileManager.GameStateInfo info = fileManager.readGameState();
                        if (info != null) {
                            uiState = info.getState();
                        }
                    }
                }

                ui.displayGameResult(uiState);

                String winner = uiState.getStatus() == GameState.GameStatus.RED_WINS ? "RED" :
                                uiState.getStatus() == GameState.GameStatus.BLUE_WINS ? "BLUE" : "DRAW";
                String outcome = String.format("depth=%d, heuristic=%d, winner=%s%n", d, h, winner);
                try (java.io.FileWriter fw = new java.io.FileWriter("outcome.txt", true)) {
                    fw.write(outcome);
                } catch (java.io.IOException e) {
                    System.err.println("Failed to write outcome: " + e.getMessage());
                }
            }
        }
    }

    private static void startAiVsAiMode(int depth, int heuristicType) {
        System.out.println("Starting AI vs AI mode");
        GameExperiment.runExperiments();
    }
}