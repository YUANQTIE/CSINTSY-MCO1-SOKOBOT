package solver;
import java.util.*;

/*
Movement:
- width: up
+ width: down
- 1: left
+ 1: right
 */
public class SokoBot {

    // BFS to get all player reachable positions

    private boolean[] getUnoccupiedTiles(Board board) {
        boolean[] reachable = new boolean[board.width*board.height];
        Queue<Integer> queue = new LinkedList<>();
        int boardSize = board.width*board.height;
        boolean[] visited = new boolean[boardSize];
        int[] directions = {-board.width, board.width, -1, 1};

        for (int i = 0; i < board.getNumOfBoxes(); i++){
            int goal = board.getGoalPosition()[i];
            reachable[goal] = true;
            queue.add(goal);
        }

        while(!queue.isEmpty()){
            int goal = queue.poll();
            for (int i = 0; i < 4; i++){
                int box = goal + directions[i];
                int player = box + directions[i];

                if (box < 0 || box >= boardSize || player < 0 || player >= boardSize)
                    continue;
                
                if (board.checkWall(player) || board.checkWall(box))
                    continue;

                if (board.isInGoal(box))
                    continue;
                
                if (!visited[box]){
                    visited[box] = true;
                    reachable[box] = true;
                    queue.add(box);
                }
            }
        }

        boolean[] deadlock = new boolean[boardSize];
        for (int i = 0; i < boardSize; i++){
            if (!board.checkWall(i) && !reachable[i] && !board.isInGoal(i)){
                deadlock[i] = true;
            }
        }

        return deadlock;
    }

    private Set<Long> getSimpleDeadlockedTiles(Board board){
        Set<Long> unvisited = new HashSet<>();
        int boardSize = board.getHeight() * board.getWidth();
        boolean[] tileOccupied = getUnoccupiedTiles(board);

        for (int i = 0; i < boardSize; i++){
            if (tileOccupied[i]){
                System.out.println("Deadlocked tile: " +  i);
                unvisited.add(Zobrist.computeSingleBoxHash(i));
            }
        }

        return unvisited;
    }


    //checks if one box in a current state contains the same hash as a deadlocked spot
    private boolean isolateBox(Board board, State state, Set<Long> deadlocked){
        for (int box : state.getBoxPositions()) {
            if (deadlocked.contains(Zobrist.computeSingleBoxHash(box))) {
                return true; //a box is in a deadlocked spot
            }
        }
        return false; 
    }

    /*public boolean isSimpleDeadlocked(Board board, int width, int[] boxPositions) {
        for (int box : boxPositions) {
            // Skip if the box is on a goal
            if (board.isInGoal(box)) continue;

            // Check if box is in a corner (two adjacent walls)
            boolean up = board.checkWall(box - width);
            boolean down = board.checkWall(box + width);
            boolean left = board.checkWall(box - 1);
            boolean right = board.checkWall(box + 1);

            // If it's stuck in a corner and not on goal
            if ((up && left) || (up && right) || (down && left) || (down && right)) {
                return true;
            }
        }
        return false;
    }*/
    
    public ArrayList<Move> getAllPossibleMoves(int width, Board board, int playerPosition, int[] boxPositions) {
        ArrayList<Move> moves = new ArrayList<>();
        int boardSize = board.getBoard().length;
        boolean[] visited = new boolean[boardSize];
        int[] directions = {-width, width, -1, 1};
        char[] directionChar = {'u', 'd', 'l', 'r'};

        // <tileNumber, pathToTile>
        Map<Integer, String> paths = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(playerPosition);
        visited[playerPosition] = true;
        paths.put(playerPosition, "");

        while (!queue.isEmpty()) {
            int position = queue.poll();
            String path = paths.get(position);
            for(int i=0; i<4; i++) {
                int next = position + directions[i];
                // Out of bounds
                if(next < 0 || next >= boardSize) continue;

                // There is a wall or box blocking
                if(board.checkWall(next) || board.checkBox(next, boxPositions)) continue;

                if(!visited[next]) {
                    visited[next] = true;
                    paths.put(next, path + directionChar[i]);
                    queue.add(next);
                }
            }

        }
        for(int i=0; i<boardSize; i++) {
            // If the point or tile is not in the visited array, it means that it is not reachable
            if(!visited[i]) continue;

            // Path from player position to next to box
            String path = paths.get(i);
            // Checks if the player is next to a box
            boolean boxIsUp = board.isPlayerNextToBox("Up", width, boxPositions, i);
            boolean boxIsDown = board.isPlayerNextToBox("Down", width, boxPositions, i);
            boolean boxIsLeft = board.isPlayerNextToBox("Left", width, boxPositions, i);
            boolean boxIsRight = board.isPlayerNextToBox("Right", width, boxPositions, i);
            if (boxIsUp && (i - 2*width) >= 0) {
                if (board.canMoveBox("Up", i - width, width, boxPositions)) {
                    moves.add(new Move(i, i - width, i - width, i - width - width, path + "u"));
                }
            }
            if (boxIsDown && (i + 2*width) < boardSize) {
                if (board.canMoveBox("Down", i + width, width, boxPositions)) {
                    moves.add(new Move(i, i + width, i + width, i + width + width, path + "d"));
                }
            }
            if (boxIsLeft && i % width > 1) {
                if (board.canMoveBox("Left", i - 1, width, boxPositions)) {
                    moves.add(new Move(i, i - 1, i - 1, i - 1 - 1, path + "l"));
                }
            }
            if (boxIsRight && i % width < width - 2) {
                if (board.canMoveBox("Right", i + 1, width, boxPositions)) {
                    moves.add(new Move(i, i + 1, i + 1, i+ 1 + 1, path + "r"));
                }
            }
        }
        return moves;
    }

    public boolean isComplete(Board board, State state) {
        for (int box : state.getBoxPositions()) {
            if (!board.isInGoal(box)) {
                return false; // at least one box not on goal
            }
        }
        return true; // all boxes are on goals
    }

    public boolean isFreezeDeadlock(Board board, int width, int[] boxPositions) {
        // Check if any box is frozen (cannot be pushed in any direction)
        for (int i = 0; i < boxPositions.length; i++) {
            int box = boxPositions[i];
            if (board.isInGoal(box)) continue; // Skip boxes on goals
            
            // Check if this box is frozen (blocked on both axes)
            if (isBoxFrozen(board, width, box, boxPositions, i, new HashSet<>())) {
                return true; // Found a frozen box not on goal = freeze deadlock
            }
        }
        return false; // No frozen boxes found
    }

    /**
     * Check if a box is frozen (blocked on both vertical and horizontal axes)
     */
    private boolean isBoxFrozen(Board board, int width, int box, int[] boxPositions, int currentBoxIndex, Set<Integer> checkedBoxes) {
        // Avoid circular checks - if we've already checked this box, treat it as blocked
        if (checkedBoxes.contains(currentBoxIndex)) {
            return true; // Already checked this box, treat as blocked
        }
        checkedBoxes.add(currentBoxIndex);

        // Check if box is blocked vertically
        boolean blockedVertically = isBoxBlockedOnAxis(board, width, box, boxPositions, currentBoxIndex, true, checkedBoxes);
        
        // Check if box is blocked horizontally  
        boolean blockedHorizontally = isBoxBlockedOnAxis(board, width, box, boxPositions, currentBoxIndex, false, checkedBoxes);

        // Box is frozen if blocked on both axes
        return blockedVertically && blockedHorizontally;
    }

    /**
     * Check if a box is blocked on a specific axis (vertical or horizontal)
     */
    private boolean isBoxBlockedOnAxis(Board board, int width, int box, int[] boxPositions, int currentBoxIndex, boolean isVertical, Set<Integer> checkedBoxes) {
        int boardSize = board.getBoard().length;
        
        if (isVertical) {
            // Check vertical axis (up and down)
            boolean upBlocked = isPositionBlocked(board, width, box - width, boxPositions, currentBoxIndex, checkedBoxes);
            boolean downBlocked = isPositionBlocked(board, width, box + width, boxPositions, currentBoxIndex, checkedBoxes);
            return upBlocked && downBlocked;
        } else {
            // Check horizontal axis (left and right)
            boolean leftBlocked = isPositionBlocked(board, width, box - 1, boxPositions, currentBoxIndex, checkedBoxes);
            boolean rightBlocked = isPositionBlocked(board, width, box + 1, boxPositions, currentBoxIndex, checkedBoxes);
            return leftBlocked && rightBlocked;
        }
    }

    /**
     * Check if a position is blocked (wall, out of bounds, or blocked box)
     */
    private boolean isPositionBlocked(Board board, int width, int position, int[] boxPositions, int currentBoxIndex, Set<Integer> checkedBoxes) {
        int boardSize = board.getBoard().length;
        
        // Check 1: Out of bounds = blocked
        if (position < 0 || position >= boardSize) {
            return true;
        }
        
        // Check 2: Wall = blocked
        if (board.checkWall(position)) {
            return true;
        }

        
        // Check 4: Box at position - check if that box is blocked
        for (int i = 0; i < boxPositions.length; i++) {
            if (i != currentBoxIndex && boxPositions[i] == position) {
                // Found a box at this position - check if it's blocked
                return isBoxFrozen(board, width, position, boxPositions, i, checkedBoxes);
            }
        }
        
        return false; // Position is not blocked
    }

    public int manhattanDistance(int box, int goal, int width){
        //compute x-component and y-component of the manhattan distance
        int xComponent = Math.abs((box % width) - (goal % width));
        int yComponenent = Math.abs((box / width) - (goal / width)); //we only need to add kasi the number of tiles is exactly the distance

        return xComponent + yComponenent;
    }

    public int getHeuristic(Board board, int[] boxPositions, int width) {
        List<int[]> list = new ArrayList<>(); //each int[] contains {distance, box, goal}
        int h = 0;

        //Computes the distance of every box to every goal
        for(int i=0; i< board.getNumOfBoxes(); i++) {
            for(int j=0; j<board.getNumOfBoxes(); j++) {
                int dist = manhattanDistance(boxPositions[i], board.getGoalPosition()[j], width);
                list.add(new int[]{dist, i, j});
            }
        }

        //Sort by ascending distance
        list.sort(Comparator.comparingInt(dist -> dist[0]));

        ArrayList<Integer> matchedBoxes = new ArrayList<>();
        ArrayList<Integer> matchedGoals = new ArrayList<>();

        for(int[] d: list) {
            int dist = d[0];
            int box = d[1];
            int goal = d[2];
            if(!matchedBoxes.contains(box) && !matchedGoals.contains(goal)) {
                matchedBoxes.add(box);
                matchedGoals.add(goal);
                h += dist;
            }
        }

        //For unmatched boxes, assign them to nearest goal
        for(int i=0; i<board.getNumOfBoxes(); i++) {
            if(!matchedBoxes.contains(i)) {
                int min = Integer.MAX_VALUE;
                for(int j=0; i<board.getNumOfBoxes(); j++) {
                    int goal = board.getGoalPosition()[j];
                    int d = manhattanDistance(i, goal, width);
                    if(d < min) {
                        min = d;
                    }
                }
                h += min;
            }
        }

        return h;
    }

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        Board board = new Board(width, height, mapData, itemsData);
        board.setBoard();

        Zobrist.initialize(width, height);

        Set<Long> visited = new HashSet<>();
        Set<Long> deadlocked = getSimpleDeadlockedTiles(board);
        
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getTCost));
        long startHash = Zobrist.computeHash(board.getPlayerPosition(), board.getBoxPosition());
        State start = new State(board.getBoxPosition(), board.getPlayerPosition(), startHash);
        int h = getHeuristic(board, start.getBoxPositions(), width);
        Node node = new Node(start, "", 0, h);

        //Add initial state to visited queue
        visited.add(startHash);
        queue.add(node);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (isComplete(board, curr.getState())) {
                return curr.getPath();
            }
            for (Move move : getAllPossibleMoves(width, board, curr.getState().getPlayerPosition(), curr.getState().getBoxPositions())) {
                State next = curr.getState().apply(move, board.getNumOfBoxes());
                if (visited.contains(next.getHash())) continue;

                if (isolateBox(board, next, deadlocked)){
                    continue;
                }

                else if (isFreezeDeadlock(board, width, next.getBoxPositions())) {
                    deadlocked.add(next.getHash());
                    continue;
                }

                int g = curr.getGCost() + 1;
                h = getHeuristic(board, next.getBoxPositions(), width);
                visited.add(next.getHash());
                node = new Node(next, curr.getPath() + move.getPath(), g, h);
                queue.add(node);
            }

        }
        System.out.print("New Box positions: ");
        for(int i=0; i<board.getNumOfBoxes(); i++) {
            System.out.print(node.getState().getBoxPositions()[i] + " ");
        }
        System.out.println();
        System.out.println("Path: " + node.getPath());
        System.out.println();
        return node.getPath();
    }

}

/*
*/