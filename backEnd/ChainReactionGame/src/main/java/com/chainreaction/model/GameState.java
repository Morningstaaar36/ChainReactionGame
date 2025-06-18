package com.chainreaction.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class GameState {
    private Cell[][] board;
    private Player currentPlayer;
    private GameStatus status;
    private boolean redHasPlayed = false;
    private boolean blueHasPlayed = false;

    public enum GameStatus {
        ONGOING, RED_WINS, BLUE_WINS
    }

    public GameState() {
        this.board = new Cell[9][6];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                this.board[i][j] = new Cell(i, j);
            }
        }
        this.currentPlayer = Player.RED;
        this.status = GameStatus.ONGOING;
        this.redHasPlayed = false;
        this.blueHasPlayed = false;
    }

    public GameState(GameState other) {
        this.board = new Cell[9][6];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                this.board[i][j] = new Cell(other.board[i][j]);
            }
        }
        this.currentPlayer = other.currentPlayer;
        this.status = other.status;
        this.redHasPlayed = other.redHasPlayed;
        this.blueHasPlayed = other.blueHasPlayed;
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<Move> getLegalMoves() {
        List<Move> legalMoves = new ArrayList<>();

        if (status != GameStatus.ONGOING) {
            return legalMoves;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                Cell cell = board[i][j];
                if (cell.isEmpty() || cell.isOwnedBy(currentPlayer)) {
                    legalMoves.add(new Move(i, j));
                }
            }
        }

        return legalMoves;
    }

    public void makeMove(Move move) {
        if (status != GameStatus.ONGOING) {
            throw new IllegalStateException("Game is already over");
        }

        if (!move.isValidFor(this)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }

        Cell cell = board[move.getRow()][move.getCol()];
        cell.addOrb(currentPlayer);

        if (currentPlayer == Player.RED) {
            redHasPlayed = true;
        } else if (currentPlayer == Player.BLUE) {
            blueHasPlayed = true;
        }

        handleExplosions();
        checkGameStatus();

        if (status == GameStatus.ONGOING) {
            currentPlayer = currentPlayer.getOpponent();
        }
    }

    private void handleExplosions() {
        boolean explosionOccurred;

        do {
            explosionOccurred = false;
            Queue<Cell> unstableCells = new LinkedList<>();

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 6; j++) {
                    if (board[i][j].isUnstable()) {
                        unstableCells.add(board[i][j]);
                    }
                }
            }

            while (!unstableCells.isEmpty()) {
                Cell cell = unstableCells.poll();

                if (cell.isUnstable()) {
                    explosionOccurred = true;
                    Player owner = cell.getOwner();
                    int row = cell.getRow();
                    int col = cell.getCol();

                    cell.reset();

                    distributeOrb(row - 1, col, owner);
                    distributeOrb(row + 1, col, owner);
                    distributeOrb(row, col - 1, owner);
                    distributeOrb(row, col + 1, owner);
                }
            }

            boolean allSameColor = true;
            Player firstColor = null;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 6; j++) {
                    Cell c = board[i][j];
                    if (!c.isEmpty()) {
                        if (firstColor == null) {
                            firstColor = c.getOwner();
                        } else if (c.getOwner() != firstColor) {
                            allSameColor = false;
                            break;
                        }
                    }
                }
                if (!allSameColor) break;
            }
            if (allSameColor && firstColor != null) {
                break;
            }

        } while (explosionOccurred);
    }

    private void distributeOrb(int row, int col, Player owner) {
        if (row >= 0 && row < 9 && col >= 0 && col < 6) {
            Cell cell = board[row][col];

            if (!cell.isEmpty() && !cell.isOwnedBy(owner)) {
                cell.convert(owner);
            }

            if (cell.isEmpty()) {
                cell.addOrb(owner);
            } else {
                cell.addOrb(owner);
            }
        }
    }

    private void checkGameStatus() {
        boolean redExists = false;
        boolean blueExists = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                Cell cell = board[i][j];
                if (!cell.isEmpty()) {
                    if (cell.isOwnedBy(Player.RED)) {
                        redExists = true;
                    } else if (cell.isOwnedBy(Player.BLUE)) {
                        blueExists = true;
                    }
                }
            }
        }

        if (redExists && !blueExists) {
            if (blueHasPlayed) {
                status = GameStatus.RED_WINS;
            }
        } else if (!redExists && blueExists) {
            if (redHasPlayed) {
                status = GameStatus.BLUE_WINS;
            }
        }
    }

    public int getOrbCount(Player player) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                Cell cell = board[i][j];
                if (cell.isOwnedBy(player)) {
                    count += cell.getOrbCount();
                }
            }
        }
        return count;
    }

    public boolean hasPlayerPlayed(Player player) {
        if (player == Player.RED) {
            return redHasPlayed;
        } else if (player == Player.BLUE) {
            return blueHasPlayed;
        }
        return false;
    }

    public Cell[][] getBoard() {
        return board;
    }
}