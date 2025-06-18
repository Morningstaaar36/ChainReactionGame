package com.chainreaction.service;

import com.chainreaction.ai.MinimaxAgent;
import com.chainreaction.model.GameState;
import com.chainreaction.model.Move;
import com.chainreaction.model.Player;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private final MinimaxAgent aiAgent;

    public AIService() {
        this.aiAgent = new MinimaxAgent(4); // Depth 4 for reasonable performance
    }

    public GameState makeAIMove(GameState gameState) {
        if (gameState.getStatus() != GameState.GameStatus.ONGOING) {
            return gameState;
        }

        if (gameState.getCurrentPlayer() != Player.BLUE) {
            return gameState;
        }

        Move aiMove = aiAgent.findBestMove(gameState);
        if (aiMove != null) {
            gameState.makeMove(aiMove);
        }

        return gameState;
    }
}