package solver;
import java.util.Random;

public class Zobrist {
    private static long[] playerHash;
    private static long[] boxHash;

    public Zobrist(int row, int cols) {
        Random rand = new Random();
        playerHash = new long[row*cols];
        boxHash = new long[row*cols];
        //PRNG (Pseudo Random Number Generator)
        for(int i=0; i<row*cols; i++) {
            playerHash[i] = rand.nextLong();
            boxHash[i] = rand.nextLong();
        }
    }

    public static long computeHash(int playerPosition, int[] boxPosition) {
        long hash = 0L;
        hash ^= playerHash[playerPosition];
        for(int i=0; i<boxPosition.length; i++) {
            hash ^= boxHash[boxPosition[i]];
        }
        return hash;
    }

    public static long computeHashForLargeMaps(int playerPosition, int[] boxPosition) {
        long hash = playerHash[playerPosition];
        hash ^= playerHash[playerPosition];
        for(int i=0; i<boxPosition.length; i++) {
            hash ^= boxHash[boxPosition[i]];
        }
        return hash;
    }



}
