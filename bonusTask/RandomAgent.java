import java.util.List;
import java.util.Random;

public class RandomAgent {
    private Random random;
    
    public RandomAgent() {
        this.random = new Random();
    }
    
    public Move findRandomMove(GameState state) {
        List<Move> legalMoves = state.getLegalMoves();
        
        if (legalMoves.isEmpty()) {
            return null;
        }
        
        int randomIndex = random.nextInt(legalMoves.size());
        return legalMoves.get(randomIndex);
    }
}