import java.io.FileWriter;
import java.io.IOException;

public class GameExperiment {
    public static void runExperiments() {
        try (FileWriter fw = new FileWriter("report.txt", false)) { // Overwrite file
            fw.write("Chain Reaction AI vs AI Experiment Results\n\n");

            int[] depths = {1, 2, 3};
            int[] heuristics = {0, 1, 2, 3, 4, 5};
            int numGames = 10; 
            fw.write("Random vs. Minimax Experiments\n");
            for (int depth : depths) {
                for (int heuristic : heuristics) {
                    HeuristicEvaluator evaluator = new HeuristicEvaluator(heuristic);
                    Agent minimaxAgent = new MinimaxAgentAdapter(new MinimaxAgent(depth, evaluator));
                    Agent randomAgent = new RandomAgentAdapter(new RandomAgent());
                    int minimaxWins = 0;
                    long totalTime = 0;
                    long totalMinimaxNodes = 0;

                    for (int i = 0; i < numGames; i++) {
                        AIVsAIGame game = new AIVsAIGame(randomAgent, minimaxAgent); // Random (Red) vs. Minimax (Blue)
                        GameResult result = game.playGame();
                        if (result.winner == Player.BLUE) {
                            minimaxWins++;
                        }
                        totalTime += result.totalTime;
                        totalMinimaxNodes += result.blueNodes;
                    }

                    double winRate = (double) minimaxWins / numGames;
                    double avgTime = (double) totalTime / numGames;
                    double avgNodes = (double) totalMinimaxNodes / numGames;

                    String report = String.format(
                        "Experiment: Random (Red) vs. Minimax (Blue, depth=%d, heuristic=%d), %d games\n" +
                        "Minimax Win Rate: %.2f%%\n" +
                        "Average Time per Game: %.2f ms\n" +
                        "Average Nodes Explored by Minimax: %.2f\n\n",
                        depth, heuristic, numGames, winRate * 100, avgTime, avgNodes);
                    fw.write(report);
                }
            }

            int fixedDepth = 2;
            fw.write("Minimax vs. Minimax Experiments\n");
            int[][] winCounts = new int[heuristics.length][heuristics.length]; 
            for (int h1 = 0; h1 < heuristics.length; h1++) {
                for (int h2 = h1 + 1; h2 < heuristics.length; h2++) {
                    HeuristicEvaluator evaluator1 = new HeuristicEvaluator(h1);
                    HeuristicEvaluator evaluator2 = new HeuristicEvaluator(h2);
                    Agent agent1 = new MinimaxAgentAdapter(new MinimaxAgent(fixedDepth, evaluator1));
                    Agent agent2 = new MinimaxAgentAdapter(new MinimaxAgent(fixedDepth, evaluator2));

                    AIVsAIGame game1 = new AIVsAIGame(agent1, agent2);
                    GameResult result1 = game1.playGame();
                    if (result1.winner == Player.RED) {
                        winCounts[h1][h2]++;
                    } else if (result1.winner == Player.BLUE) {
                        winCounts[h2][h1]++;
                    }
                    String report1 = String.format(
                        "Experiment: Minimax (heuristic=%d, Red) vs. Minimax (heuristic=%d, Blue), depth=%d\n" +
                        "Winner: %s\n" +
                        "Total Time: %d ms\n" +
                        "Nodes Explored by Red: %d\n" +
                        "Nodes Explored by Blue: %d\n\n",
                        h1, h2, fixedDepth, result1.winner, result1.totalTime, result1.redNodes, result1.blueNodes);
                    fw.write(report1);

                    AIVsAIGame game2 = new AIVsAIGame(agent2, agent1);
                    GameResult result2 = game2.playGame();
                    if (result2.winner == Player.RED) {
                        winCounts[h2][h1]++;
                    } else if (result2.winner == Player.BLUE) {
                        winCounts[h1][h2]++;
                    }
                    String report2 = String.format(
                        "Experiment: Minimax (heuristic=%d, Red) vs. Minimax (heuristic=%d, Blue), depth=%d\n" +
                        "Winner: %s\n" +
                        "Total Time: %d ms\n" +
                        "Nodes Explored by Red: %d\n" +
                        "Nodes Explored by Blue: %d\n\n",
                        h2, h1, fixedDepth, result2.winner, result2.totalTime, result2.redNodes, result2.blueNodes);
                    fw.write(report2);
                }
            }

            fw.write("Minimax vs. Minimax Win Summary (Wins out of 2 games)\n");
            fw.write("Heuristic | " + String.join(" | ", "0", "1", "2", "3", "4", "5") + "\n");
            fw.write("----------|" + "-|".repeat(heuristics.length) + "\n");
            for (int h1 = 0; h1 < heuristics.length; h1++) {
                StringBuilder row = new StringBuilder(String.format("%d         |", h1));
                for (int h2 = 0; h2 < heuristics.length; h2++) {
                    if (h1 == h2) {
                        row.append(" - |");
                    } else {
                        row.append(String.format(" %d |", winCounts[h1][h2]));
                    }
                }
                fw.write(row.toString() + "\n");
            }

        } catch (IOException e) {
            System.err.println("Failed to write report: " + e.getMessage());
        }
    }
}