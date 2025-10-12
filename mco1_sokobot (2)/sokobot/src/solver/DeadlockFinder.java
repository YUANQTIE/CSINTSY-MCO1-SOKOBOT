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

    private boolean isInCorner(int boxPos) {
        int x = boxPos % width;
        int y = boxPos / width;
        
        boolean hasWallLeft = (x > 0 && flatMap[boxPos - 1] == 1);
        boolean hasWallRight = (x < width - 1 && flatMap[boxPos + 1] == 1);
        boolean hasWallUp = (y > 0 && flatMap[boxPos - width] == 1);
        boolean hasWallDown = (y < height - 1 && flatMap[boxPos + width] == 1);

        return (hasWallLeft && hasWallUp) || 
               (hasWallLeft && hasWallDown) || 
               (hasWallRight && hasWallUp) || 
               (hasWallRight && hasWallDown);
    }
    

    private boolean isAgainstWall(int boxPos) {
        int x = boxPos % width;
        int y = boxPos / width;
        boolean againstWall = false;
        

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            againstWall = true;
        } else {

            boolean hasWallLeft = (x > 0 && flatMap[boxPos - 1] == 1);
            boolean hasWallRight = (x < width - 1 && flatMap[boxPos + 1] == 1);
            boolean hasWallUp = (y > 0 && flatMap[boxPos - width] == 1);
            boolean hasWallDown = (y < height - 1 && flatMap[boxPos + width] == 1);
            
            if (hasWallLeft && hasWallRight) {
                againstWall = true;
            }
            if (hasWallUp && hasWallDown) {
                againstWall = true;
            }
        }
        
        return againstWall;
    }

    public boolean hasSimpleDeadlock(State state) {
        boolean hasDeadLock = false;
        int[] boxPositions = state.getBoxPositions();
        int i = 0;
        
        while (i < boxPositions.length && !hasDeadLock) {
            int boxPos = boxPositions[i];
            if (isInCorner(boxPos) || isAgainstWall(boxPos)) {
                hasDeadLock = true;
            }
            i++;
        }
        return hasDeadLock;
    }

    public boolean hasFreezeDeadlock(State state){
        boolean hasFreezeDeadlock = false;
        int[] boxPositions = state.getBoxPositions();
        int i = 0;
        int j = 0;

        while(i < boxPositions.length && !hasFreezeDeadlock){
            int boxPos = boxPositions[i];

            if(isAgainstWall(boxPos)){
                int x = boxPos % width;
                int y = boxPos / width;

                boolean leftBox = false;
                boolean rightBox = false;
                boolean upBox = false;
                boolean downBox = false;

             while(j < boxPositions.length){
                 int otherBox = boxPositions[j];

                 if(otherBox == boxPos - 1){
                     leftBox = true
                 }

                 if(otherBox == boxPos + 1){
                     rightBox = true;
                 }

                 if(otherBox == boxPos - width){
                     upBox = true;
                 }

                 if(otherBox == boxPos + width){
                     downBox = true;
                 }

                 j++;
                 
             }    

                if((leftBox && rightBox) || (upBox && downBox)){
                    hasFreezeDeadlock = true;
                }
                
            }

                i++;
        } 

            return hasFreezeDeadlock;
    }

    public boolean hasDeadlock(State state) {
        boolean hasAnyDeadlock = hasSimpleDeadlock(state) || hasFreezeDeadlock(state);
        return hasAnyDeadlock;
    }
    
 
    public void addDeadlockedState(State state) {
        long hash = Zobrist.hash(state);
        deadlockedStates.add(hash);

        zobristHash.getVisitedList().add(hash);
    }
    
   
    public boolean isDeadlocked(State state) {
        long hash = Zobrist.hash(state);
        boolean isDeadlocked = deadlockedStates.contains(hash) || zobristHash.getVisitedList().contains(hash);
        return isDeadlocked;
    }
}
