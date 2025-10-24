package solver;
import java.util.Random;

public class Zobrist {
    private static long[] playerHash;
    private static long[] boxHash;

    /* Function that initializes random numbers for each tile of the map for both player and box.
        @rows - the number of rows in the map
        @cols - the number of columns in the map
        @return none
    */
    public static void initialize(int rows, int cols) {
        Random rand = new Random(0); 
        playerHash = new long[rows * cols];
        boxHash = new long[rows * cols];
        
        //PRNG (Pseudo Random Number Generator)
        for (int i = 0; i < rows * cols; i++) {
            playerHash[i] = rand.nextLong();
            boxHash[i] = rand.nextLong();
        }
    }

    /* Function that computes the hash of a single box.
        @boxPosition - the position of the bpx
        @return hash of box
    */
    public static long computeSingleBoxHash(int boxPosition) {
        long hash = 0L;
        hash ^= boxHash[boxPosition];
        return hash;
    }

    /* Function that computes the hash of a state based on the player position and box positions.
        @param playerPosition - the position of the player
        @param boxPosition - the positions of the boxes
        @return hash of the state
    */
    public static long computeHash(int playerPosition, int[] boxPosition) {
        long hash = 0L;
        hash ^= playerHash[playerPosition];
        for (int j : boxPosition) hash ^= boxHash[j];
        return hash;
    }

    /* Function that computes the hash of a state based on the player position and box positions for larger maps.
        @param boxPosition - the positions of the boxes
        @return hash of the state
    */
    public static long computeHashForLargeMaps(int[] boxPosition) {
        long hash = 0L;
        for (int j : boxPosition) hash ^= boxHash[j];
        return hash;
    }
}
