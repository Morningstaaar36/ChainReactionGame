package com.chainreaction.controller;

import com.chainreaction.dto.MoveRequest;
import com.chainreaction.dto.MoveResponse;
import com.chainreaction.dto.GameStateResponse;
import com.chainreaction.exception.InvalidMoveException;
import com.chainreaction.model.GameState;
import com.chainreaction.service.GameService;
import com.chainreaction.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private AIService aiService;

    @GetMapping("/state")
    public ResponseEntity<GameStateResponse> getGameState() {
        GameState currentState = gameService.getCurrentGameState();
        return ResponseEntity.ok(new GameStateResponse(currentState));
    }

    @PostMapping("/new")
    public ResponseEntity<GameStateResponse> startNewGame() {
        gameService.startNewGame();
        GameState newGame = gameService.getCurrentGameState();
        return ResponseEntity.ok(new GameStateResponse(newGame));
    }

    @PostMapping("/move")
    public ResponseEntity<MoveResponse> makeMove(@Valid @RequestBody MoveRequest moveRequest) {
        try {
            // Make human move
            GameState afterHumanMove = gameService.makeHumanMove(moveRequest.getRow(), moveRequest.getCol());
            
            // Check if game is over after human move
            if (gameService.isGameOver()) {
                return ResponseEntity.ok(new MoveResponse(
                    "Game finished! Winner: " + gameService.getWinner(),
                    true,
                    new GameStateResponse(afterHumanMove)
                ));
            }

            // Make AI move
            GameState afterAIMove = aiService.makeAIMove(afterHumanMove);
            
            // Check if game is over after AI move
            if (gameService.isGameOver()) {
                return ResponseEntity.ok(new MoveResponse(
                    "Game finished! Winner: " + gameService.getWinner(),
                    true,
                    new GameStateResponse(afterAIMove)
                ));
            }

            return ResponseEntity.ok(new MoveResponse(
                "Move successful",
                true,
                new GameStateResponse(afterAIMove)
            ));

        } catch (InvalidMoveException e) {
            return ResponseEntity.badRequest().body(new MoveResponse(
                e.getMessage(),
                false,
                new GameStateResponse(gameService.getCurrentGameState())
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MoveResponse(
                "Internal server error: " + e.getMessage(),
                false,
                new GameStateResponse(gameService.getCurrentGameState())
            ));
        }
    }

    @GetMapping("/legal-moves")
    public ResponseEntity<?> getLegalMoves() {
        GameState currentState = gameService.getCurrentGameState();
        return ResponseEntity.ok(currentState.getLegalMoves());
    }
}