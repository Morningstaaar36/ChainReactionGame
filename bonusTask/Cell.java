public class Cell {
    private int orbCount;
    private Player owner;
    private final int row;
    private final int col;
    private final int criticalMass;
    
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.orbCount = 0;
        this.owner = Player.EMPTY;
        
        if ((row == 0 || row == GameConfig.ROWS - 1) && (col == 0 || col == GameConfig.COLS - 1)) {
            this.criticalMass = 2;
        } else if (row == 0 || row == GameConfig.ROWS - 1 || col == 0 || col == GameConfig.COLS - 1) {
            this.criticalMass = 3;
        } else {
            this.criticalMass = 4;
        }
    }
    
    public Cell(Cell other) {
        this.row = other.row;
        this.col = other.col;
        this.orbCount = other.orbCount;
        this.owner = other.owner;
        this.criticalMass = other.criticalMass;
    }
    
    public boolean isEmpty() {
        return owner == Player.EMPTY;
    }
    
    public boolean isOwnedBy(Player player) {
        return owner == player;
    }
    
    public void addOrb(Player player) {
        if (isEmpty()) {
            owner = player;
            orbCount = 1;
        } else if (isOwnedBy(player)) {
            orbCount++;
        } else {
            throw new IllegalStateException("Cannot add orb to cell owned by opponent");
        }
    }
    
    public boolean isUnstable() {
        return orbCount >= criticalMass;
    }
    
    public void reset() {
        orbCount = 0;
        owner = Player.EMPTY;
    }
    
    public void convert(Player player) {
        owner = player;
    }
    
    public int getOrbCount() {
        return orbCount;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public int getCriticalMass() {
        return criticalMass;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "0";
        } else {
            return Integer.toString(orbCount) + owner.toChar();
        }
    }
}