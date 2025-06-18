package com.chainreaction.dto;

import com.chainreaction.model.GameState;
import com.chainreaction.model.Player;
import com.chainreaction.model.Cell;

public class GameStateResponse {
    private CellData[][] board;
    private Player currentPlayer;
    private GameState.GameStatus status;
    private int redOrbCount;
    private int blueOrbCount;
    private boolean redHasPlayed;
    private boolean blueHasPlayed;
    
    public GameStateResponse(GameState gameState) {
        this.board = new CellData[9][6];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                Cell cell = gameState.getCell(i, j);
                this.board[i][j] = new CellData(
                    cell.getOrbCount(),
                    cell.getOwner(),
                    cell.getCriticalMass()
                );
            }
        }
        this.currentPlayer = gameState.getCurrentPlayer();
        this.status = gameState.getStatus();
        this.redOrbCount = gameState.getOrbCount(Player.RED);
        this.blueOrbCount = gameState.getOrbCount(Player.BLUE);
        this.redHasPlayed = gameState.hasPlayerPlayed(Player.RED);
        this.blueHasPlayed = gameState.hasPlayerPlayed(Player.BLUE);
    }
    
    public static class CellData {
        private int orbCount;
        private Player owner;
        private int criticalMass;
        
        public CellData(int orbCount, Player owner, int criticalMass) {
            this.orbCount = orbCount;
            this.owner = owner;
            this.criticalMass = criticalMass;
        }
        
        public int getOrbCount() { return orbCount; }
        public Player getOwner() { return owner; }
        public int getCriticalMass() { return criticalMass; }
    }
    
    // Getters
    public CellData[][] getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public GameState.GameStatus getStatus() { return status; }
    public int getRedOrbCount() { return redOrbCount; }
    public int getBlueOrbCount() { return blueOrbCount; }
    public boolean isRedHasPlayed() { return redHasPlayed; }
    public boolean isBlueHasPlayed() { return blueHasPlayed; }
}