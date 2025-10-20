package solver;
import java.util.Random;

public class Zobrist {
    private static long[] playerHash;
    private static long[] boxHash;

    public static void initialize(int rows, int cols) {
        Random rand = new Random();
        playerHash = new long[rows*cols];
        boxHash = new long[rows*cols];

        Random rand = new Random(0); 
        //PRNG (Pseudo Random Number Generator)
        for(int i=0; i<rows*cols; i++) {
            playerHash[i] = rand.nextLong();
            boxHash[i] = rand.nextLong();
        }
    }

    public static long computeSingleBoxHash(int boxPosition) {
        long hash = 0L;
        hash ^= boxHash[boxPosition];

        return hash;
    }

    public static long computeHash(int playerPosition, int[] boxPosition) {
        long hash = 0L;
        hash ^= playerHash[playerPosition];
        for (int j : boxPosition) {
            hash ^= boxHash[j];
        }
        return hash;
    }

    public static long computeHashForLargeMaps(int playerPosition, int[] boxPosition) {
        long hash = playerHash[playerPosition];
        hash ^= playerHash[playerPosition];
        for (int j : boxPosition) {
            hash ^= boxHash[j];
        }
        return hash;
    }
}
