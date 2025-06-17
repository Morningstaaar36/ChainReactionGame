public class GameConfig {
    public static final int ROWS = 9;
    public static final int COLS = 6;
    
    public static final String GAME_STATE_FILE = "gamestate.txt";
    public static final String HUMAN_MOVE_HEADER = "Human Move:";
    public static final String AI_MOVE_HEADER = "AI Move:";
    
    public static final int DEFAULT_DEPTH = 3;
    public static final long TIME_LIMIT_MS = 3000; 
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
}