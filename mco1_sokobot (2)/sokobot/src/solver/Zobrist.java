package solver;

import java.util.HashSet;
import java.util.Random;

public class Zobrist {
    private static long[][] zobristTable;
    private final int width;
    private final int height;
    private static HashSet visitedList;
    private static final int PLAYER = 0;
    private static final int BOX = 1;

    //constructor for the zobrist table

    /* Zobrist hashing tldr: hashing sa zobrist usually means na there r random numbers per tile. Think of it na parang addition,
    so like it aadd nya lang mga location nung items and if nakita niyang may ganong similar sum na, edi irereject siya ng hash. If the hash
    does not contain that number, it gets stored in the hash for future use.*/

    public Zobrist(int width, int height) {
        this.width = width;
        this.height = height;
        zobristTable = new long[width * height][2]; 
        visitedList = new HashSet<>();

        Random rand = new Random(12345); 
        for (int i = 0; i < width * height; i++) {
            zobristTable[i][PLAYER] = rand.nextLong(); //per tile in the board, this generates a number lang on the table.
            zobristTable[i][BOX] = rand.nextLong();
        }
    }

     //generates long int that uniquely idenfifies the state based on locations of the non-static items on the map

    public static long hash(State s) {
        long h = 0;
        h ^= zobristTable[s.getPlayerPosition()][PLAYER];
        for (int pos : s.getBoxPositions()) {
            h ^= zobristTable[pos][BOX]; //XORS the items to get the numbers
        }
        return h;
    }

    public HashSet getVisitedList(){
        return visitedList;
    }
}
