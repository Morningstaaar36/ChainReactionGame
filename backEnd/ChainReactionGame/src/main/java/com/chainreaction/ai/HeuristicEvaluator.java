package com.chainreaction.ai;

import com.chainreaction.model.GameState;
import com.chainreaction.model.Player;
import com.chainreaction.model.Cell;
import java.util.ArrayList;
import java.util.List;

public class HeuristicEvaluator {
    
    public int evaluate(GameState state, Player player) {
        GameState.GameStatus status = state.getStatus();
        if (status != GameState.GameStatus.ONGOING) {
            if ((status == GameState.GameStatus.RED_WINS && player == Player.RED) ||
                    (status == GameState.GameStatus.BLUE_WINS && player == Player.BLUE)) {
                return 1000000;
            } else {
                return -1000000;
            }
        }

        Player opponent = player.getOpponent();
        return explosionCascade(state, player, opponent);
    }

    private int explosionCascade(GameState state, Player player, Player opponent) {
        int playerCascadeScore = 0;
        int opponentCascadeScore = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int cascadePotential = calculateCascadePotential(state, i, j);
                    int proximityToExplosion = cell.getCriticalMass() - cell.getOrbCount();

                    int weight = (proximityToExplosion <= 1) ? 15 : (5 - proximityToExplosion) * 3;
                    int cellScore = cascadePotential * weight * cell.getOrbCount();

                    if (cell.isOwnedBy(player)) {
                        playerCascadeScore += cellScore;
                    } else {
                        opponentCascadeScore += cellScore;
                    }
                }
            }
        }

        return playerCascadeScore - (int) (opponentCascadeScore * 1.5);
    }

    private int calculateCascadePotential(GameState state, int row, int col) {
        int potential = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (!adj.isEmpty()) {
                int orbsToExplosion = adj.getCriticalMass() - adj.getOrbCount();
                if (orbsToExplosion <= 2) {
                    potential += (3 - orbsToExplosion) * adj.getOrbCount();
                }
            }
        }

        return potential;
    }

    private List<Cell> getAdjacentCells(GameState state, int row, int col) {
        List<Cell> adjacent = new ArrayList<>();
        if (row > 0) adjacent.add(state.getCell(row - 1, col));
        if (row < 8) adjacent.add(state.getCell(row + 1, col));
        if (col > 0) adjacent.add(state.getCell(row, col - 1));
        if (col < 5) adjacent.add(state.getCell(row, col + 1));
        return adjacent;
    }
}