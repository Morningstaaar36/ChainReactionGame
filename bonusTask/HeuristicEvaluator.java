import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class HeuristicEvaluator {

    public static final int HEURISTIC_EXPLOSION_CASCADE = 0;
    public static final int HEURISTIC_STRATEGIC_POSITIONING = 1;
    public static final int HEURISTIC_DEFENSIVE_STABILITY = 2;
    public static final int HEURISTIC_CHAIN_AMPLIFICATION = 3;
    public static final int HEURISTIC_TERRITORIAL_DOMINANCE = 4;
    public static final int HEURISTIC_CRITICAL_MASS_CONTROL = 5;

    private int selectedHeuristic;

    public HeuristicEvaluator(int heuristicType) {
        this.selectedHeuristic = heuristicType;
    }

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

        switch (selectedHeuristic) {
            case HEURISTIC_EXPLOSION_CASCADE:
                return explosionCascade(state, player, opponent);
            case HEURISTIC_STRATEGIC_POSITIONING:
                return strategicPositioning(state, player, opponent);
            case HEURISTIC_DEFENSIVE_STABILITY:
                return defensiveStability(state, player, opponent);
            case HEURISTIC_CHAIN_AMPLIFICATION:
                return chainAmplification(state, player, opponent);
            case HEURISTIC_TERRITORIAL_DOMINANCE:
                return territorialDominance(state, player, opponent);
            case HEURISTIC_CRITICAL_MASS_CONTROL:
                return criticalMassControl(state, player, opponent);
            default:
                return 0;
        }
    }

  
    private int explosionCascade(GameState state, Player player, Player opponent) {
        int playerCascadeScore = 0;
        int opponentCascadeScore = 0;

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
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

   
    private int strategicPositioning(GameState state, Player player, Player opponent) {
        int playerScore = 0;
        int opponentScore = 0;

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int positionValue = calculatePositionValue(i, j, cell.getOrbCount());
                    int influenceRadius = calculateInfluenceRadius(state, i, j, cell.getOwner());

                    int totalValue = positionValue + influenceRadius;

                    if (cell.isOwnedBy(player)) {
                        playerScore += totalValue;
                    } else {
                        opponentScore += totalValue;
                    }
                }
            }
        }

        return playerScore - opponentScore;
    }

   
    private int defensiveStability(GameState state, Player player, Player opponent) {
        int playerStability = 0;
        int opponentStability = 0;

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int stability = calculateCellStability(state, i, j);
                    int defenseValue = calculateDefenseValue(state, i, j, cell.getOwner());
                    int threatLevel = calculateThreatLevel(state, i, j, cell.getOwner());

                    int totalStability = stability + defenseValue - threatLevel;

                    if (cell.isOwnedBy(player)) {
                        playerStability += totalStability;
                    } else {
                        opponentStability += totalStability;
                    }
                }
            }
        }

        return playerStability - opponentStability;
    }

    
    private int chainAmplification(GameState state, Player player, Player opponent) {
        int playerAmplification = 0;
        int opponentAmplification = 0;

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int chainPotential = calculateChainPotential(state, i, j);
                    int amplificationFactor = calculateAmplificationFactor(state, i, j, cell.getOwner());

                    int totalAmplification = chainPotential * amplificationFactor;

                    if (cell.isOwnedBy(player)) {
                        playerAmplification += totalAmplification;
                    } else {
                        opponentAmplification += totalAmplification;
                    }
                }
            }
        }

        return playerAmplification - opponentAmplification;
    }

    
    private int territorialDominance(GameState state, Player player, Player opponent) {
        int playerDominance = 0;
        int opponentDominance = 0;

        int[][] territoryMap = calculateTerritoryMap(state);

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int territoryValue = territoryMap[i][j];
                    int expansionPotential = calculateExpansionPotential(state, i, j);
                    int connectivity = calculateConnectivity(state, i, j, cell.getOwner());

                    int totalDominance = territoryValue + expansionPotential + connectivity;

                    if (cell.isOwnedBy(player)) {
                        playerDominance += totalDominance;
                    } else {
                        opponentDominance += totalDominance;
                    }
                }
            }
        }

        return playerDominance - opponentDominance;
    }

   
    private int criticalMassControl(GameState state, Player player, Player opponent) {
        int playerControl = 0;
        int opponentControl = 0;

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    int criticalMassValue = calculateCriticalMassValue(state, i, j);
                    int timingAdvantage = calculateTimingAdvantage(state, i, j);
                    int controlPotential = calculateControlPotential(state, i, j, cell.getOwner());

                    int totalControl = criticalMassValue + timingAdvantage + controlPotential;

                    if (cell.isOwnedBy(player)) {
                        playerControl += totalControl;
                    } else {
                        opponentControl += totalControl;
                    }
                }
            }
        }

        return playerControl - opponentControl;
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

    private int calculatePositionValue(int row, int col, int orbCount) {
        if ((row == 0 || row == GameConfig.ROWS - 1) && (col == 0 || col == GameConfig.COLS - 1)) {
            return 15 * orbCount;
        }
        else if (row == 0 || row == GameConfig.ROWS - 1 || col == 0 || col == GameConfig.COLS - 1) {
            return 10 * orbCount;
        }
        else {
            return 8 * orbCount;
        }
    }

    private int calculateInfluenceRadius(GameState state, int row, int col, Player owner) {
        int influence = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (adj.isEmpty()) {
                influence += 2;
            } else if (adj.isOwnedBy(owner)) {
                influence += 1; 
            } else {
                influence -= 1; 
            }
        }

        return influence;
    }

    private int calculateCellStability(GameState state, int row, int col) {
        Cell cell = state.getCell(row, col);
        int stability = cell.getCriticalMass() - cell.getOrbCount();

        if (stability > 1) {
            stability *= 2;
        }

        return stability;
    }

    private int calculateDefenseValue(GameState state, int row, int col, Player owner) {
        int defense = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (adj.isOwnedBy(owner)) {
                defense += 3; 
            } else if (!adj.isEmpty()) {
                if (adj.getOrbCount() >= adj.getCriticalMass() - 1) {
                    defense -= 5; 
                }
            }
        }

        return defense;
    }

    private int calculateThreatLevel(GameState state, int row, int col, Player owner) {
        int threat = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (!adj.isEmpty() && !adj.isOwnedBy(owner)) {
                int orbsToExplosion = adj.getCriticalMass() - adj.getOrbCount();
                if (orbsToExplosion <= 1) {
                    threat += 10; 
                } else if (orbsToExplosion <= 2) {
                    threat += 5; 
                }
            }
        }

        return threat;
    }

    private int calculateChainPotential(GameState state, int row, int col) {
        int potential = 0;
        Cell cell = state.getCell(row, col);

        if (cell.getOrbCount() >= cell.getCriticalMass() - 1) {
            List<Cell> adjacent = getAdjacentCells(state, row, col);
            for (Cell adj : adjacent) {
                if (!adj.isEmpty()) {
                    potential += adj.getOrbCount(); 
                }
            }
        }

        return potential;
    }

    private int calculateAmplificationFactor(GameState state, int row, int col, Player owner) {
        int factor = 1;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (adj.isOwnedBy(owner) && adj.getOrbCount() >= adj.getCriticalMass() - 2) {
                factor += 2; 
            }
        }

        return factor;
    }

    private int[][] calculateTerritoryMap(GameState state) {
        int[][] territory = new int[GameConfig.ROWS][GameConfig.COLS];

        for (int i = 0; i < GameConfig.ROWS; i++) {
            for (int j = 0; j < GameConfig.COLS; j++) {
                Cell cell = state.getCell(i, j);
                if (!cell.isEmpty()) {
                    Player owner = cell.getOwner();
                    int influence = cell.getOrbCount();

                    List<Cell> adjacent = getAdjacentCells(state, i, j);
                    for (Cell adj : adjacent) {
                        int adjRow = adj.getRow();
                        int adjCol = adj.getCol();
                        if (adj.isEmpty() || adj.isOwnedBy(owner)) {
                            territory[adjRow][adjCol] += influence / 2;
                        }
                    }

                    territory[i][j] += influence;
                }
            }
        }

        return territory;
    }

    private int calculateExpansionPotential(GameState state, int row, int col) {
        int potential = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (adj.isEmpty()) {
                potential += 3; 
            }
        }

        return potential;
    }

    private int calculateConnectivity(GameState state, int row, int col, Player owner) {
        int connectivity = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (adj.isOwnedBy(owner)) {
                connectivity += 2; 
            }
        }

        return connectivity;
    }

    private int calculateCriticalMassValue(GameState state, int row, int col) {
        Cell cell = state.getCell(row, col);
        int orbsToExplosion = cell.getCriticalMass() - cell.getOrbCount();

        if (orbsToExplosion == 1) {
            return 20; 
        } else if (orbsToExplosion == 2) {
            return 10; 
        } else {
            return cell.getOrbCount();
        }
    }

    private int calculateTimingAdvantage(GameState state, int row, int col) {
        Cell cell = state.getCell(row, col);
        int advantage = 0;

        int orbsToExplosion = cell.getCriticalMass() - cell.getOrbCount();
        if (orbsToExplosion <= 2) {
            advantage = 10 - orbsToExplosion * 3;
        }

        return advantage;
    }

    private int calculateControlPotential(GameState state, int row, int col, Player owner) {
        int control = 0;
        List<Cell> adjacent = getAdjacentCells(state, row, col);

        for (Cell adj : adjacent) {
            if (!adj.isEmpty() && !adj.isOwnedBy(owner)) {
                control += adj.getOrbCount();
            }
        }

        return control;
    }

    private List<Cell> getAdjacentCells(GameState state, int row, int col) {
        List<Cell> adjacent = new ArrayList<>();
        if (row > 0)
            adjacent.add(state.getCell(row - 1, col));
        if (row < GameConfig.ROWS - 1)
            adjacent.add(state.getCell(row + 1, col));
        if (col > 0)
            adjacent.add(state.getCell(row, col - 1));
        if (col < GameConfig.COLS - 1)
            adjacent.add(state.getCell(row, col + 1));
        return adjacent;
    }
}