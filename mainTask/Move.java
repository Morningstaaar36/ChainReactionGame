public class Move {
    private final int row;
    private final int col;
    
    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public boolean isValidFor(GameState state) {
        if (row < 0 || row >= GameConfig.ROWS || col < 0 || col >= GameConfig.COLS) {
            return false;
        }
        
        Cell cell = state.getCell(row, col);
        Player currentPlayer = state.getCurrentPlayer();
        
        return cell.isEmpty() || cell.isOwnedBy(currentPlayer);
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return row == move.row && col == move.col;
    }
    
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}