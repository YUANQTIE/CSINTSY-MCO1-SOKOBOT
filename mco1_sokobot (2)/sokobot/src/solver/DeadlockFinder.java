package solver;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class DeadlockFinder{
    //TO IMPLEMENT: Simple Deadlock Finder, Freeze Deadlock Finder. these functions should utilize the zobrist hashing to store the deadlocked states there.
    private int[] flatMap;
    private final int width;
    private final int height;
    private HashSet<Long> deadlockedStates;
    private Zobrist zobristHash;
    
    public DeadlockFinder(int[] flatMap, int width, int height, Zobrist zobristHash) {
        this.flatMap = flatMap;
        this.width = width;
        this.height = height;
        this.zobristHash = zobristHash;
        this.deadlockedStates = new HashSet<>();
    }

    public HashSet<Long> getDeadlockedStates() {
        return deadlockedStates;
    }
}
