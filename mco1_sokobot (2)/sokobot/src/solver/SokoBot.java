package solver;

import java.util.*;

public class SokoBot {

    private final List<Integer> goalTiles = new ArrayList<>(); //locations of goal tiles
    public static int width, height; //dimensions of the map
    private int[] flatMap; // 1 = wall, 0 = free space
    private Zobrist zobristHash;
    private DeadlockFinder deadlockFinder;

    /*This function initializes the static maps in flatMap (map used in determining whether a tile is walkable or not) 
    and the goalTiles, which gets the integer value of each goal tile)*/
    public void initSpaces(char[][] mapData) {
        height = mapData.length;
        width = mapData[0].length;
        goalTiles.clear();

        int dim = width * height;

        flatMap = new int[dim];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int idx = y * width + x; //multiplies the width of the map with the levels down the y coordinate and adds them with the x coordinate to get the index of the 1D array

                if (mapData[y][x] == '#'){
                    flatMap[idx] = 1;
                } 
                else {
                    flatMap[idx] = 0;
                    if (mapData[y][x] == '.') goalTiles.add(idx);
                }
            }
        }
    }

    /*This function initializes the dynamic maps in playerPos (which stores the integer value of the player's position) 
    and boxes, which also stores the integer value of the boxes*/
    public State getInitialState(char[][] itemsData) {
        int playerPos = -1;
        List<Integer> boxes = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pos = y * width + x; //same concept as initSpaces
                if (itemsData[y][x] == '@'){
                    playerPos = pos;
                } 
                else if (itemsData[y][x] == '$'){
                    boxes.add(pos);
                } 
            }
        }

        int[] boxArray = boxes.stream().mapToInt(Integer::intValue).toArray();
        return new State(boxArray, playerPos, null, 0, 0, 0, ' ');
    }

    /* Checks if a state's box positions is already in all of the goal tiles's positions */
    public boolean isGoal(State s) {
        for (int box : s.getBoxPositions()) {
            if (!goalTiles.contains(box)){
                return false;
            } 
        }
        return true;
    }

    /* Gets the minimum heuristic value of a state through getting the manhattan distance of box to nearest goal.
       Basically the sum of every box's minimum distance to a goal tile and this becomes the heuristic of the entire state.
       FLAW 1: The sole focus of this heuristic is between box and goal. No correlation between this and the distance of the player and the box, so it can lead to more moves than necessary.
     */
    public int heuristic(State s) {
        boolean[] checked = new boolean[goalTiles.size()];
        int total = 0;
            for (int box : s.getBoxPositions()) {
            int min = Integer.MAX_VALUE; //just to make sure that any distance is smaller
            int chosenGoal = -1;

            for (int i = 0; i < goalTiles.size(); i++) {
                if (checked[i]){
                    continue; // skip already used goals
                }

                int goal = goalTiles.get(i);
                int dist = StaticHelpers.manhattanDistance(box, goal, width); //manhattan distance will serve as heuristic function

                if (dist < min) {
                    min = dist;
                    chosenGoal = i;
                }
            }

            if (chosenGoal != -1){
                checked[chosenGoal] = true;
            } 
            total += min; //adds the minimum distance of the box to the nearest goal to the total heuristic value
        }

        return total;
    }

    /* Returns the states containing the next valid moves to be checked. */
    public List<State> getNextStates(State cur) {
        List<State> nextStates = new ArrayList<>();
        int[] d = {-1, 1, -width, width};
        char[] m = {'l', 'r', 'u', 'd'};

        for (int i = 0; i < 4; i++) {
            int moveVal = d[i];
            char direction = m[i];
            int newPlayer = cur.getPlayerPosition() + moveVal;

            // Check if its outside bounds of the map or if player moves into a wall
            if (!isInside(newPlayer) || flatMap[newPlayer] == 1){
                continue;
            } 

            int[] newBoxes = Arrays.copyOf(cur.getBoxPositions(), cur.getBoxPositions().length);
            boolean blocked = StaticHelpers.isBlocked(moveVal, newPlayer, newBoxes, flatMap);

            if (blocked){ //skips the moves that are invalid
                continue;
            } 

            else if (!blocked || flatMap[newPlayer] == 0) { 
                State next = new State(newBoxes, newPlayer, cur, cur.getG() + 1, 0, 0, direction);
                nextStates.add(next);
            }
        }

        return nextStates;
    }

    /* just checks if positions are within the maps indices */
    private boolean isInside(int pos) {
        return pos >= 0 && pos < width * height;
    }

    /* Re-traces the path of all the nodes' directions*/
    public String findPath(State s) {
        StringBuilder sb = new StringBuilder();

        while (s.getParent() != null) {
            sb.append(s.getPrevDirection());
            s = s.getParent();
        }

        return sb.reverse().toString();
    }

    public String search(char[][] mapData, char[][] itemsData) {
        initSpaces(mapData);
        
        PriorityQueue<State> openList = new PriorityQueue<>(Comparator.comparingInt(State::getF)); //this makes it get so that the lowest F gets selected first
        //TO IMPLEMENT: DEADLOCKS SHOULD ALSO BE HASHED WITHIN THIS VISTED HASHSET (SIMPLE DEADLOCK)
        //TO IMPLEMENT: FREEZE DEADLOCK
        
        State start = getInitialState(itemsData); //initializes the parent state

        deadlockFinder = new DeadlockFinder(flatMap, width, height, zobristHash);
        
        openList.add(start);
        long startHash = Zobrist.hash(start); 
        zobristHash.getVisitedList().add(startHash);

        while (!openList.isEmpty()) {
            State current = openList.poll();

            if (isGoal(current)){ //checks if current state
                return findPath(current);
            } 

            List<State> nextStates = getNextStates(current);

            if (nextStates.isEmpty()){ //no new moves meaning player is stuck
                System.err.println("No solution found in this problem.");
                return null;
            } 
            else{
                for (State next : nextStates) {
                    long hash = Zobrist.hash(next); //gets the hash value of the state
                    if (zobristHash.getVisitedList().contains(hash)){
                        continue;
                    } 

                    // Check for deadlocks before processing the state
                    if (deadlockFinder.hasDeadlock(next)) {
                        deadlockFinder.addDeadlockedState(next);
                        continue; // Skip deadlocked states
                    }

                    int g = current.getG() + 1; //1 cost per depth
                    int nh = heuristic(next);
                    int f = g + nh;

                    State newState = new State(next.getBoxPositions(), next.getPlayerPosition(), current, g, nh, f, next.getPrevDirection());
                    zobristHash.getVisitedList().add(hash); //stores the explored state in the hash
                    openList.add(newState); //added to the next states to explore
                }
            }
        }

        return null;
    }

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        this.width = width;
        this.height = height;
        zobristHash = new Zobrist(width, height);

        return search(mapData, itemsData);
    }
}
