package solver;
import java.util.*;

public class SokoBot {

    /* Function that finds and marks all the deadlock tiles in the board.
        @param board - the 1-D board/map
        @return boolean array containing all the marked tiles
    */
    private boolean[] getSimpleDeadlockTiles(Board board) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] reachable = new boolean[Board.getWidth() * Board.getHeight()];
        int boardSize = Board.getWidth() * Board.getHeight();
        boolean[] visited = new boolean[boardSize];
        int[] directions = {-Board.getWidth(), Board.getWidth(), -1, 1};

        for (int i = 0; i < board.getNumOfBoxes(); i++) {
            int goal = board.getGoalPosition()[i];
            reachable[goal] = true;
            queue.add(goal);
        }

        while (!queue.isEmpty()) {
            int goal = queue.poll();
            for (int i = 0; i < 4; i++) {
                int box = goal + directions[i];
                int player = box + directions[i];

                if (box < 0 || box >= boardSize || player < 0 || player >= boardSize) continue;
                if (board.checkWall(player) || board.checkWall(box)) continue;
                if (board.isInGoal(box)) continue;
                
                if (!visited[box]) {
                    visited[box] = true;
                    reachable[box] = true;
                    queue.add(box);
                }
            }
        }

        boolean[] deadlock = new boolean[boardSize];
        for (int i = 0; i < boardSize; i++)
            if (!board.checkWall(i) && !reachable[i] && !board.isInGoal(i))
                deadlock[i] = true;

        return deadlock;
    }

    /* Function that hashes all the deadlock tiles in the board.
        @param board - the 1-D board/map
        @return hashmap of all the deadlock tiles
    */

    private Set<Long> hashSimpleDeadlockedTiles(Board board){
        Set<Long> unvisited = new HashSet<>();
        int boardSize = Board.getHeight() * Board.getWidth();
        boolean[] tileOccupied = getSimpleDeadlockTiles(board);

        for (int i = 0; i < boardSize; i++) {
            if (tileOccupied[i]){
                System.out.println("Deadlocked tile: " +  i);
                unvisited.add(Zobrist.computeSingleBoxHash(i));
            }
        }
        return unvisited;
    }


    /* Function that checks if any of the boxes in a state contains the same hash as a deadlocked spot.
        @param state - the state to be checked
        @param deadlocked - the hashmap containing all deadlocked tiles
        @return true if any box is deadlocked, otherwise false
    */

    private boolean isSimpleDeadlocked(State state, Set<Long> deadlocked){
        for (int box : state.getBoxPositions())
            if (deadlocked.contains(Zobrist.computeSingleBoxHash(box)))
                return true; //a box is in a deadlocked spot
        return false; 
    }

    /* Function that finds all the possible moves a player can make to reach and push every box.
        @param width - the width of the map
        @param board - the 1-D board/map
        @param playerPosition - the position of the player
        @param boxPositions - the positions of the boxes
        @return all the possible moves
    */
    
    public ArrayList<Move> getAllPossibleMoves(int width, int playerPosition, Board board, int[] boxPositions) {
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
            for(int i = 0; i < 4; i++) {
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

        for(int i = 0; i < boardSize; i++) {
            // If the point or tile is not in the visited array, it means that it is not reachable
            if(!visited[i]) continue;

            // Path from player position to next to box
            String path = paths.get(i);

            // Checks if the player is next to a box
            boolean boxIsUp = board.isPlayerNextToBox("Up", width, boxPositions, i);
            boolean boxIsDown = board.isPlayerNextToBox("Down", width, boxPositions, i);
            boolean boxIsLeft = board.isPlayerNextToBox("Left", width, boxPositions, i);
            boolean boxIsRight = board.isPlayerNextToBox("Right", width, boxPositions, i);

            if (boxIsUp && (i - 2 * width) >= 0)
                if (board.canMoveBox("Up", i - width, width, boxPositions))
                    moves.add(new Move(i, i - width, i - width, i - width - width, path + "u"));

            if (boxIsDown && (i + 2 * width) < boardSize)
                if (board.canMoveBox("Down", i + width, width, boxPositions))
                    moves.add(new Move(i, i + width, i + width, i + width + width, path + "d"));

            if (boxIsLeft && i % width > 1)
                if (board.canMoveBox("Left", i - 1, width, boxPositions))
                    moves.add(new Move(i, i - 1, i - 1, i - 1 - 1, path + "l"));

            if (boxIsRight && i % width < width - 2)
                if (board.canMoveBox("Right", i + 1, width, boxPositions))
                    moves.add(new Move(i, i + 1, i + 1, i+ 1 + 1, path + "r"));
        }
        return moves;
    }

    /* Function that determines if the puzzle is solved based on the state.
        @param board - the 1-D board/map
        @param state - the state of the map
        @return true if all boxes are in goals, otherwise false
    */

    public boolean isComplete(Board board, State state) {
        for (int box : state.getBoxPositions())
            if (!board.isInGoal(box))
                return false; // at least one box not on goal
        return true; // all boxes are on goals
    }

    /* Function that computes the manhattan distance of a box and goal.
        @param box - the position of the box
        @param goal - the position of the goal
        @param width - the width of the map
        @return manhattan distance of box and goal
    */

    public int manhattanDistance(int box, int goal, int width){
        //compute x-component and y-component of the manhattan distance
        int xComponent = Math.abs((box % width) - (goal % width));
        int yComponent = Math.abs((box / width) - (goal / width));
        return xComponent + yComponent;
    }

    /* Function that determines the heuristic value based on the distance of every box to every goal.
        @param board - the 1-D board/map
        @param boxPositions - the positions of the boxes
        @param width - width of the map
        @return heuristic value
    */

    public int getHeuristic(int width, Board board, int[] boxPositions) {
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
            int boxPos = boxPositions[i];
            if(!matchedBoxes.contains(i)) {
                int min = Integer.MAX_VALUE;
                for(int j=0; j<board.getNumOfBoxes(); j++) {
                    int goal = board.getGoalPosition()[j];
                    int d = manhattanDistance(boxPos, goal, width);
                    if(d < min) {
                        min = d;
                    }
                }
                h += min;
            }
        }

        return h;
    }


    /* Function that solves the puzzle using A* search and returns the path.
        @param width - the width of the map
        @param height - the height of the map
        @param mapData - contains the wall and goal details of the map
        @param itemsData - contains the player and box details of the map
        @return the path
    */

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        Board board = new Board(width, height, mapData, itemsData);
        board.setBoard();

        Zobrist.initialize(width, height);

        Set<Long> visited = new HashSet<>();
        Set<Long> deadlocked = hashSimpleDeadlockedTiles(board);
        
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getTCost));
        long startHash = Zobrist.computeHash(board.getPlayerPosition(), board.getBoxPosition());
        State start = new State(board.getBoxPosition(), board.getPlayerPosition(), startHash);
        int h = getHeuristic(width, board, start.getBoxPositions());
        Node node = new Node(start, "", 0, h);

        //Add initial state to visited queue
        visited.add(startHash);
        queue.add(node);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (isComplete(board, curr.getState())) return curr.getPath();

            for (Move move : getAllPossibleMoves(width, curr.getState().getPlayerPosition(), board, curr.getState().getBoxPositions())) {
                State next = curr.getState().apply(move);

                if (visited.contains(next.getHash())) continue;
                if (isSimpleDeadlocked(next, deadlocked)) continue;

                int g = curr.getGCost() + 1;
                h = getHeuristic(width, board, next.getBoxPositions());
                visited.add(next.getHash());
                node = new Node(next, curr.getPath() + move.getPath(), g, h);
                queue.add(node);
            }
        }
        return node.getPath();
    }
}