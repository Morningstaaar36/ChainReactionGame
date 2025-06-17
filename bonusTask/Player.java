public enum Player {
    RED, BLUE, EMPTY;
    
    public Player getOpponent() {
        if (this == RED) return BLUE;
        if (this == BLUE) return RED;
        return EMPTY;
    }
    
    public char toChar() {
        if (this == RED) return 'R';
        if (this == BLUE) return 'B';
        return ' ';
    }
    
    public static Player fromChar(char c) {
        if (c == 'R') return RED;
        if (c == 'B') return BLUE;
        return EMPTY;
    }
}