package solver;
public class Board {
    private final int[] board;
    private final char[][] mapData;
    private final char[][] itemsData;
    private int numOfBoxes;
    private int[] initialBoxPosition;
    private int playerPosition;
    private static int width, height;
    private int[] goalPosition;

    public Board(int width, int height, char[][] mapData, char[][] itemsData) {
        this.board = new int[width * height];
        Board.width = width;
        Board.height = height;
        this.mapData = mapData;
        this.itemsData = itemsData;
        this.goalPosition = new int[20];
    }

    /* Function that converts the 2D map into a 1D array.
        @return none
    */
    public void setBoard() {
        int ctr = 0;
        int box_ctr = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(mapData[i][j] == ' ') board[ctr] = 0;
                else if(mapData[i][j] == '.') {
                    board[ctr] = 3;
                    numOfBoxes++;
                    goalPosition[box_ctr] = ctr;
                    box_ctr++;
                } else if(mapData[i][j] == '#') board[ctr] = 4;

                ctr++;
            }
        }

        initialBoxPosition = new int[numOfBoxes];

        ctr = 0;
        box_ctr = 0;
        int player = 0;

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if (board[ctr] == 0) {
                    if(itemsData[i][j] == '@') player = ctr;
                    else if(itemsData[i][j] == '$') {
                        initialBoxPosition[box_ctr] = ctr;
                        box_ctr++;
                    }
                }
                else if (board[ctr] == 3) {
                    if (itemsData[i][j] == '$') {
                        initialBoxPosition[box_ctr] = ctr; //* means box is in the goal
                        box_ctr++;
                    }
                    else if(itemsData[i][j] == '@') player = ctr;
                }
                ctr++;
            }
        }
        playerPosition = player;
    }

    /* Function that determines whether a box can be moved given a specified direction.
        @param direction - direction to be checked
        @param boxPosition - the position of the box
        @param width - the width of the map
        @param boxPositions - the position of the boxes
        @return true if the box can be moved, otherwise false
    */
    public boolean canMoveBox(String direction, int boxPosition, int width, int[] boxPositions) {
        boolean canMove = false;
        if (direction.equalsIgnoreCase("Up")) {
            if(!checkWall(boxPosition-width) && !checkBox(boxPosition-width, boxPositions)) {
                canMove = true;
            }
        }
        else if (direction.equalsIgnoreCase("Down")) {
            if(!checkWall(boxPosition+width) && !checkBox(boxPosition+width, boxPositions)) {
                canMove = true;
            }
        }
        else if (direction.equalsIgnoreCase("Left")) {
            if(!checkWall(boxPosition-1) && !checkBox(boxPosition-1, boxPositions)) {
                canMove = true;
            }
        }
        else if (direction.equalsIgnoreCase("Right")) {
            if(!checkWall(boxPosition+1) && !checkBox(boxPosition+1, boxPositions)) {
                canMove = true;
            }
        }
        return canMove;
    }

    /* Function that returns whether a player is next to a box.
        @param direction - direction to be checked
        @param width - the width of the map
        @param boxPosition - the position of the boxes
        @param playerPosition - the position of the player
        @return true if player is next to box, otherwise false
    */
    public boolean isPlayerNextToBox(String direction, int width, int[] boxPosition, int playerPosition) {
        boolean checker = false;
        if (direction.equalsIgnoreCase("Up")) {
            for (int j : boxPosition) {
                if (j == playerPosition - width) {
                    checker = true;
                    break;
                }
            }
        }
        else if (direction.equalsIgnoreCase("Down")) {
            for (int j : boxPosition) {
                if (j == playerPosition + width) {
                    checker = true;
                    break;
                }
            }
        }
        else if (direction.equalsIgnoreCase("Left")) {
            for (int j : boxPosition) {
                if (j == playerPosition - 1) {
                    checker = true;
                    break;
                }
            }
        }
        else if (direction.equalsIgnoreCase("Right")) {
            for (int j : boxPosition) {
                if (j == playerPosition + 1) {
                    checker = true;
                    break;
                }
            }
        }
        return checker;
    }

    /* Function that checks if the given position is a wall.
        @tile - the position to be checked
        @return true if it's a wall, otherwise false
    */
    public boolean checkWall(int tile) {
        return board[tile] == 4;
    }

    /* Function that checks if the given position is a box.
        @param tile - the position to be checked
        @param boxPosition -  the position of the boxes
        @return box true if it is a box, otherwise false
    */
    public boolean checkBox(int tile, int[] boxPosition) {
        for (int i = 0; i < numOfBoxes; i++)
            if (boxPosition[i] == tile)
                return true;
        return false;
    }

    /* Function that checks if a box is in a goal.
        @param box - the box to be checked
        @return true if box is in goal, otherwise false
    */
    public boolean isInGoal(int box) {
        for (int i = 0; i < numOfBoxes; i++)
            if (goalPosition[i] == box)
                return true;
        return false;
    }

    /* Function that returns the goal positions of the board.
        @return goal positions
    */
    public int[] getGoalPosition() {
        return this.goalPosition;
    }

    /* Function that returns the board/1D array.
        @return board
    */
    public int[] getBoard() {
        return this.board;
    }

    /* Function that returns the width of the map.
        @return width
    */
    public static int getWidth(){
        return width;
    }

    /* Function that returns the height of the map.
        @return height
    */
    public static int getHeight(){
        return height;
    }

    /* Function that returns the number of boxes.
        @return number of boxes
    */
    public int getNumOfBoxes() {
        return this.numOfBoxes;
    }

    /* Function that returns the player position.
        @return player position
    */
    public int getPlayerPosition() {
        return this.playerPosition;
    }

    /* Function that returns the box positions.
        @return box positions
    */
    public int[] getBoxPosition() {
        return this.initialBoxPosition;
    }
}