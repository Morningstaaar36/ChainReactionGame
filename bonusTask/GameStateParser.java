import java.util.List;
import java.util.ArrayList;

public class GameStateParser {
    
    public static String gameStateToString(GameState state, boolean isHumanMove) {
        StringBuilder sb = new StringBuilder();
        
        if (isHumanMove) {
            sb.append(GameConfig.HUMAN_MOVE_HEADER).append("\n");
        } else {
            sb.append(GameConfig.AI_MOVE_HEADER).append("\n");
        }
        
        sb.append(state.toString());
        
        return sb.toString();
    }
    
    public static GameState stringToGameState(List<String> lines) {
        if (lines.size() < GameConfig.ROWS + 1) {
            throw new IllegalArgumentException("Invalid game state format");
        }
        
        GameState state = new GameState();
        
        for (int i = 1; i <= GameConfig.ROWS; i++) {
            String[] cells = lines.get(i).split(" ");
            
            for (int j = 0; j < Math.min(cells.length, GameConfig.COLS); j++) {
                String cellStr = cells[j];
                
                if (cellStr.equals("0")) {
                    continue;
                } else {
                    int orbCount = Integer.parseInt(cellStr.substring(0, cellStr.length() - 1));
                    char colorChar = cellStr.charAt(cellStr.length() - 1);
                    Player owner = Player.fromChar(colorChar);
                    
                    Cell cell = state.getCell(i - 1, j);
                    for (int k = 0; k < orbCount; k++) {
                        cell.addOrb(owner);
                    }
                }
            }
        }
        
        return state;
    }
    
    public static boolean isHumanMove(String header) {
        return header.equals(GameConfig.HUMAN_MOVE_HEADER);
    }
    
    public static boolean isAiMove(String header) {
        return header.equals(GameConfig.AI_MOVE_HEADER);
    }
    
    public static Move parseMove(String moveStr) {
        moveStr = moveStr.replace("(", "").replace(")", "").trim();
        String[] parts = moveStr.split(",");
        
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid move format: " + moveStr);
        }
        
        int row = Integer.parseInt(parts[0].trim());
        int col = Integer.parseInt(parts[1].trim());
        
        return new Move(row, col);
    }
    
    public static String moveToString(Move move) {
        return "(" + move.getRow() + "," + move.getCol() + ")";
    }
}