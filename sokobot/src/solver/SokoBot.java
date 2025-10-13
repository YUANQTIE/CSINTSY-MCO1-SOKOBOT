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
    //BFS to get all player reachable positions
    public ArrayList<Move> getAllPossibleMoves(int width, Board board, int playerPosition, int[] boxPositions) {
        ArrayList<Move> moves = new ArrayList<>();
        int boardSize = board.getBoard().length;
        boolean[] visited = new boolean[boardSize];
        int[] directions = {-width, width, -1, 1};
        char[] directionChar = {'u', 'd', 'l', 'r'};
        //<tileNumber, pathToTile>
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
                //Out of bounds
                if(next < 0 || next >= boardSize) continue;

                //There is a wall or box blocking
                if(board.checkWall(next) || board.checkBox(next, boxPositions)) continue;

                if(!visited[next]) {
                    visited[next] = true;
                    paths.put(next, path + directionChar[i]);
                    queue.add(next);
                }
            }

        }
        for(int i=0; i<boardSize; i++) {
            //If the point or tile is not in the visited array, it means that it is not reachable
            if(!visited[i]) continue;

            //Path from player position to next to box
            String path = paths.get(i);
            //Checks if the player is next to a box
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
        for (int box : state.boxPositions) {
            if (!board.isInGoal(box)) {
                return false; // at least one box not on goal
            }
        }
        return true; // all boxes are on goals
    }

    public boolean isSimpleDeadlocked(Board board, int width, int[] boxPositions) {
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
    }

    public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
        Board board = new Board(width, height, mapData, itemsData);
        board.setBoard();
        Set<Long> visited = new HashSet<>();
        Zobrist zobrist = new Zobrist(width, height);
        Queue<Node> queue = new LinkedList<>();
        long startHash = zobrist.computeHash(board.getPlayerPosition(), board.getBoxPosition());
        State start = new State(board.getBoxPosition(), board.getPlayerPosition(), startHash);
        Node node = new Node(start, "");
        //Add initial state to visited queue
        visited.add(startHash);
        queue.add(node);
        System.out.print("Box positions: ");
        for(int i=0; i<board.getNumOfBoxes(); i++) {
            System.out.print(board.getBoxPosition()[i] + " ");
        }
        System.out.println();

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            if (isComplete(board, curr.state)) {
                return curr.getPath();
            }
            for (Move move : getAllPossibleMoves(width, board, curr.state.playerPosition, curr.state.boxPositions)) {
                State next = curr.state.apply(move);
                if (visited.contains(next.hash)) continue;
                if (isSimpleDeadlocked(board, width, next.boxPositions)) continue;
                visited.add(next.hash);
                node = new Node(next, curr.path + move.getPath());
                queue.add(node);
            }

        }
        System.out.print("New Box positions: ");
        for(int i=0; i<board.getNumOfBoxes(); i++) {
            System.out.print(node.state.boxPositions[i] + " ");
        }
        System.out.println();
        System.out.println("Path: " + node.getPath());
        System.out.println();
        return node.getPath();
    }

}
