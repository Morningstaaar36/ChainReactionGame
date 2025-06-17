import java.util.Scanner;

public class ChainReactionUI {
    private FileManager fileManager;
    private Scanner scanner;

    public ChainReactionUI(FileManager fileManager) {
        this.fileManager = fileManager;
        this.scanner = new Scanner(System.in);
    }

    public Move getHumanMove() {
        while (true) {
            System.out.print("Enter your move (row,col): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Try again.");
                continue;
            }

            try {
                Move move = GameStateParser.parseMove(input);
                return move;
            } catch (Exception e) {
                System.out.println("Invalid input format. Use 'row,col' (e.g., '0,0')");
            }
        }
    }

    public void displayBoard(GameState state) {

        System.out.println("Red Orbs: " + state.getRedOrbCount() +
            " | Blue Orbs: " + state.getBlueOrbCount());
        System.out.println("Red has played: " + state.hasPlayerPlayed(Player.RED) +
            " | Blue has played: " + state.hasPlayerPlayed(Player.BLUE));
  


            
        System.out.println("\nCurrent Board:");
        
        System.out.println("   0  1  2  3  4  5");
        System.out.println("  ------------------");

        for (int i = 0; i < GameConfig.ROWS; i++) {
            System.out.print(i + " |");

            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);

                if (cell.isEmpty()) {
                    System.out.print(" . ");
                } else {
                    String color = cell.isOwnedBy(Player.RED) ? GameConfig.ANSI_RED : GameConfig.ANSI_BLUE;
                    System.out.print(color + " " + cell.getOrbCount() + " " + GameConfig.ANSI_RESET);
                }
            }

            System.out.println("|");
        }

        System.out.println("  ------------------");
        System.out.println("Current player: "
                + (state.getCurrentPlayer() == Player.RED ? GameConfig.ANSI_RED + "RED" + GameConfig.ANSI_RESET
                        : GameConfig.ANSI_BLUE + "BLUE" + GameConfig.ANSI_RESET));
    }

    public void displayGameResult(GameState state) {
        displayBoard(state); 
        System.out.println("\nGame Over!");
        if (state.getStatus() == GameState.GameStatus.RED_WINS) {
            System.out.println(GameConfig.ANSI_RED + "RED WINS!" + GameConfig.ANSI_RESET);
        } else if (state.getStatus() == GameState.GameStatus.BLUE_WINS) {
            System.out.println(GameConfig.ANSI_BLUE + "BLUE WINS!" + GameConfig.ANSI_RESET);
        }
    }
}