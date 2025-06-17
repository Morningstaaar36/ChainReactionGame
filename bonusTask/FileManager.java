import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {
    private String filePath;

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    // public void writeGameState(GameState state, boolean isHumanMove) {
    // try {
    // FileWriter writer = new FileWriter(filePath);

    // if (isHumanMove) {
    // writer.write(GameConfig.HUMAN_MOVE_HEADER + "\n");
    // } else {
    // writer.write(GameConfig.AI_MOVE_HEADER + "\n");
    // }

    // writer.write("Current Player: " + state.getCurrentPlayer().toString() +
    // "\n");
    // writer.write(state.toString());

    // writer.close();
    // } catch (IOException e) {
    // System.err.println("Error writing to file: " + e.getMessage());
    // }
    // }
    // public void writeGameState(GameState state, boolean isHumanMove) {
    //     try {
    //         FileWriter writer = new FileWriter(filePath);

    //         if (isHumanMove) {
    //             writer.write(GameConfig.HUMAN_MOVE_HEADER + "\n");
    //         } else {
    //             writer.write(GameConfig.AI_MOVE_HEADER + "\n");
    //         }

    //         writer.write("Current Player: " + state.getCurrentPlayer().toString() + "\n");
    //         writer.write("Red Has Played: " + state.hasPlayerPlayed(Player.RED) + "\n");
    //         writer.write("Blue Has Played: " + state.hasPlayerPlayed(Player.BLUE) + "\n");
    //         writer.write(state.toString());

    //         writer.close();
    //     } catch (IOException e) {
    //         System.err.println("Error writing to file: " + e.getMessage());
    //     }
    // }
public void writeGameState(GameState state, boolean isHumanMove) {
    try {
        FileWriter writer = new FileWriter(filePath);

        if (isHumanMove) {
            writer.write(GameConfig.HUMAN_MOVE_HEADER + "\n");
        } else {
            writer.write(GameConfig.AI_MOVE_HEADER + "\n");
        }

        writer.write("Status: " + state.getStatus().toString() + "\n");
        writer.write("Current Player: " + state.getCurrentPlayer().toString() + "\n");
        writer.write("Red Has Played: " + state.hasPlayerPlayed(Player.RED) + "\n");
        writer.write("Blue Has Played: " + state.hasPlayerPlayed(Player.BLUE) + "\n");
        writer.write(state.toString());

        writer.close();
    } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
    }
}
    public GameStateInfo readGameState() {
        try {
            File file = new File(filePath);
            while (!file.exists()) {
                Thread.sleep(100); 
            }

            List<String> lines = Files.readAllLines(Paths.get(filePath));

            if (lines.size() < 2) {
                throw new IOException("Invalid game state format");
            }

            String header = lines.get(0);
            boolean isHumanMove = header.equals(GameConfig.HUMAN_MOVE_HEADER);

            GameState state = parseGameState(lines);

            return new GameStateInfo(state, isHumanMove);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    // private GameState parseGameState(List<String> lines) {
    // if (lines.size() < 2) {
    // throw new IllegalArgumentException("Invalid game state format");
    // }

    // String currentPlayerLine = lines.get(1);
    // if (!currentPlayerLine.startsWith("Current Player: ")) {
    // throw new IllegalArgumentException("Invalid current player line");
    // }
    // String playerStr = currentPlayerLine.substring("Current Player:
    // ".length()).trim();
    // Player currentPlayer = Player.valueOf(playerStr);

    // GameState state = new GameState();
    // state.currentPlayer = currentPlayer;

    // for (int i = 2; i < Math.min(lines.size(), 2 + GameConfig.ROWS); i++) {
    // String[] cells = lines.get(i).split(" ");

    // for (int j = 0; j < Math.min(cells.length, GameConfig.COLS); j++) {
    // String cellStr = cells[j];

    // if (cellStr.equals("0")) {
    // continue;
    // } else {
    // int orbCount = Integer.parseInt(cellStr.substring(0, cellStr.length() - 1));
    // char colorChar = cellStr.charAt(cellStr.length() - 1);
    // Player owner = Player.fromChar(colorChar);

    // Cell cell = state.getCell(i - 2, j);
    // for (int k = 0; k < orbCount; k++) {
    // cell.addOrb(owner);
    // }
    // }
    // }
    // }

    // return state;
    // }
    // private GameState parseGameState(List<String> lines) {
    //     if (lines.size() < 4) { // Need at least 4 lines: header, current player, red has played, blue has
    //                             // played
    //         throw new IllegalArgumentException("Invalid game state format");
    //     }

    //     // Parse current player
    //     String currentPlayerLine = lines.get(1);
    //     if (!currentPlayerLine.startsWith("Current Player: ")) {
    //         throw new IllegalArgumentException("Invalid current player line");
    //     }
    //     String playerStr = currentPlayerLine.substring("Current Player: ".length()).trim();
    //     Player currentPlayer = Player.valueOf(playerStr);

    //     // Parse redHasPlayed
    //     String redHasPlayedLine = lines.get(2);
    //     if (!redHasPlayedLine.startsWith("Red Has Played: ")) {
    //         throw new IllegalArgumentException("Invalid red has played line");
    //     }
    //     boolean redHasPlayed = Boolean.parseBoolean(redHasPlayedLine.substring("Red Has Played: ".length()).trim());

    //     // Parse blueHasPlayed
    //     String blueHasPlayedLine = lines.get(3);
    //     if (!blueHasPlayedLine.startsWith("Blue Has Played: ")) {
    //         throw new IllegalArgumentException("Invalid blue has played line");
    //     }
    //     boolean blueHasPlayed = Boolean.parseBoolean(blueHasPlayedLine.substring("Blue Has Played: ".length()).trim());

    //     // Create GameState and set parsed values
    //     GameState state = new GameState();
    //     state.currentPlayer = currentPlayer;
    //     state.redHasPlayed = redHasPlayed;
    //     state.blueHasPlayed = blueHasPlayed;

    //     // Parse the board (starting from line 4)
    //     for (int i = 4; i < Math.min(lines.size(), 4 + GameConfig.ROWS); i++) {
    //         String[] cells = lines.get(i).split(" ");

    //         for (int j = 0; j < Math.min(cells.length, GameConfig.COLS); j++) {
    //             String cellStr = cells[j];

    //             if (cellStr.equals("0")) {
    //                 continue;
    //             } else {
    //                 int orbCount = Integer.parseInt(cellStr.substring(0, cellStr.length() - 1));
    //                 char colorChar = cellStr.charAt(cellStr.length() - 1);
    //                 Player owner = Player.fromChar(colorChar);

    //                 Cell cell = state.getCell(i - 4, j);
    //                 for (int k = 0; k < orbCount; k++) {
    //                     cell.addOrb(owner);
    //                 }
    //             }
    //         }
    //     }

    //     return state;
    // }
    private GameState parseGameState(List<String> lines) {
    if (lines.size() < 5) { 
        throw new IllegalArgumentException("Invalid game state format");
    }

    String statusLine = lines.get(1);
    if (!statusLine.startsWith("Status: ")) {
        throw new IllegalArgumentException("Invalid status line");
    }
    String statusStr = statusLine.substring("Status: ".length()).trim();
    GameState.GameStatus status = GameState.GameStatus.valueOf(statusStr);

    String currentPlayerLine = lines.get(2);
    if (!currentPlayerLine.startsWith("Current Player: ")) {
        throw new IllegalArgumentException("Invalid current player line");
    }
    String playerStr = currentPlayerLine.substring("Current Player: ".length()).trim();
    Player currentPlayer = Player.valueOf(playerStr);

    String redHasPlayedLine = lines.get(3);
    if (!redHasPlayedLine.startsWith("Red Has Played: ")) {
        throw new IllegalArgumentException("Invalid red has played line");
    }
    boolean redHasPlayed = Boolean.parseBoolean(redHasPlayedLine.substring("Red Has Played: ".length()).trim());

    String blueHasPlayedLine = lines.get(4);
    if (!blueHasPlayedLine.startsWith("Blue Has Played: ")) {
        throw new IllegalArgumentException("Invalid blue has played line");
    }
    boolean blueHasPlayed = Boolean.parseBoolean(blueHasPlayedLine.substring("Blue Has Played: ".length()).trim());

    GameState state = new GameState();
    state.status = status;
    state.currentPlayer = currentPlayer;
    state.redHasPlayed = redHasPlayed;
    state.blueHasPlayed = blueHasPlayed;

    for (int i = 5; i < Math.min(lines.size(), 5 + GameConfig.ROWS); i++) {
        String[] cells = lines.get(i).split(" ");

        for (int j = 0; j < Math.min(cells.length, GameConfig.COLS); j++) {
            String cellStr = cells[j];

            if (cellStr.equals("0")) {
                continue;
            } else {
                int orbCount = Integer.parseInt(cellStr.substring(0, cellStr.length() - 1));
                char colorChar = cellStr.charAt(cellStr.length() - 1);
                Player owner = Player.fromChar(colorChar);

                Cell cell = state.getCell(i - 5, j);
                for (int k = 0; k < orbCount; k++) {
                    cell.addOrb(owner);
                }
            }
        }
    }

    return state;
}

    public static class GameStateInfo {
        private final GameState state;
        private final boolean isHumanMove;

        public GameStateInfo(GameState state, boolean isHumanMove) {
            this.state = state;
            this.isHumanMove = isHumanMove;
        }

        public GameState getState() {
            return state;
        }

        public boolean isHumanMove() {
            return isHumanMove;
        }
    }
}