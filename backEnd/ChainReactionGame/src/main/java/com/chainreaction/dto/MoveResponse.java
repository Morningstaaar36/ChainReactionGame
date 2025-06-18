package com.chainreaction.dto;

public class MoveResponse {
    private String message;
    private boolean success;
    private GameStateResponse gameState;
    
    public MoveResponse(String message, boolean success, GameStateResponse gameState) {
        this.message = message;
        this.success = success;
        this.gameState = gameState;
    }
    
    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
    public GameStateResponse getGameState() { return gameState; }
}