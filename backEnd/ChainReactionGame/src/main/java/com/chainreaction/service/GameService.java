package com.chainreaction.service;

import com.chainreaction.model.GameState;
import com.chainreaction.model.Move;
import com.chainreaction.model.Player;
import com.chainreaction.exception.InvalidMoveException;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private GameState currentGame;

    public GameService() {
        this.currentGame = new GameState();
    }

    public GameState getCurrentGameState() {
        return currentGame;
    }

    public void startNewGame() {
        this.currentGame = new GameState();
    }

    public GameState makeHumanMove(int row, int col) {
        if (currentGame.getStatus() != GameState.GameStatus.ONGOING) {
            throw new InvalidMoveException("Game is already finished");
        }

        if (currentGame.getCurrentPlayer() != Player.RED) {
            throw new InvalidMoveException("It's not your turn");
        }

        Move move = new Move(row, col);
        if (!move.isValidFor(currentGame)) {
            throw new InvalidMoveException("Invalid move: position (" + row + "," + col + ")");
        }

        currentGame.makeMove(move);
        return currentGame;
    }

    public boolean isGameOver() {
        return currentGame.getStatus() != GameState.GameStatus.ONGOING;
    }

    public Player getWinner() {
        GameState.GameStatus status = currentGame.getStatus();
        if (status == GameState.GameStatus.RED_WINS) return Player.RED;
        if (status == GameState.GameStatus.BLUE_WINS) return Player.BLUE;
        return null;
    }
}