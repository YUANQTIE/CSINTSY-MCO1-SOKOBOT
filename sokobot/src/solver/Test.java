public class Test{
    
    private boolean[] getUnoccupiedTiles(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        int boardSize = width * height;
        boolean[] reachable = new boolean[boardSize];
        int[] directions = {-width, width, -1, 1};

        for (int goal : board.getGoalPosition()) {
            Queue<State> queue = new LinkedList<>();

            int[] initialBox = {goal};
            queue.add(new State(initialBox, goal, 0L));

            while (!queue.isEmpty()) {
                State current = queue.poll();
                int boxPos = current.getBoxPositions()[0];
                reachable[boxPos] = true;

                for (int dir : directions) {
                    
                }
            }
        }

        return reachable;
    }

}