package solver;
/*
0 - empty space
1 - player
2 - box
3 - goal
4 - wall
5 - box on goal
6 - player on goal
*/

public class Board {
    private int[] board;
    public static int width, height;
    private char[][] mapData;
    private char[][] itemsData;
    private int numOfBoxes;
    private int[] initialBoxPosition;
    private int playerPosition;
    public int[] goalPosition;

    public Board(int width, int height, char[][] mapData, char[][] itemsData) {
        this.board = new int[width * height];
        this.width = width;
        this.height = height;
        this.mapData = mapData;
        this.itemsData = itemsData;
        this.goalPosition = new int[20];
    }

    public void setBoard() {
        int ctr = 0;
        int box_ctr = 0;
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                if(mapData[i][j] == ' ') {
                    board[ctr] = 0;
                }
                else if(mapData[i][j] == '.') {
                    board[ctr] = 3;
                    numOfBoxes++;
                    goalPosition[box_ctr] = ctr;
                    box_ctr++;
                }

                else if(mapData[i][j] == '#') {
                    board[ctr] = 4;
                }
                ctr++;
            }
        }
        initialBoxPosition = new int[numOfBoxes];

        ctr = 0;
        box_ctr = 0;
        int player = 0;

        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                if (board[ctr] == 0) {
                    if(itemsData[i][j] == '@') {
                        player = ctr;
                    }
                    else if(itemsData[i][j] == '$') {
                        initialBoxPosition[box_ctr] = ctr;
                        box_ctr++;
                    }
                }
                else if (board[ctr] == 3 && itemsData[i][j] == '$') {
                    initialBoxPosition[box_ctr] = ctr; //* means box is in the goal
                    box_ctr++;
                }
                else if(board[ctr] == 3 && itemsData[i][j] == '@') {
                    player = ctr;
                }
                ctr++;
            }
        }
        playerPosition = player;
    }

    public boolean isFloor(int row, int col) {
        if (row < 0 || row >= height || col < 0 || col >= width)
            return false;

        char c = mapData[row][col];
        return c == ' ' || c == '.';
    }



    public int[] getBoard() {
        return this.board;
    }
    
    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    public int getNumOfBoxes() {
        return this.numOfBoxes;
    }

    public int getPlayerPosition() {
        return this.playerPosition;
    }

    public int[] getBoxPosition() {
        return this.initialBoxPosition;
    }

    public boolean canMoveBox(String direction, int boxPosition, int width, int[] boxPositions) {
        boolean canMove = false;
        if(direction.equalsIgnoreCase("Up")) {
            if(!checkWall(boxPosition-width) && !checkBox(boxPosition-width, boxPositions)) {
                canMove = true;
            }
        }
        else if(direction.equalsIgnoreCase("Down")) {
            if(!checkWall(boxPosition+width) && !checkBox(boxPosition+width, boxPositions)) {
                canMove = true;
            }
        }
        else if(direction.equalsIgnoreCase("Left")) {
            if(!checkWall(boxPosition-1) && !checkBox(boxPosition-1, boxPositions)) {
                canMove = true;
            }
        }
        else if(direction.equalsIgnoreCase("Right")) {
            if(!checkWall(boxPosition+1) && !checkBox(boxPosition+1, boxPositions)) {
                canMove = true;
            }
        }
        return canMove;
    }

    public boolean isPlayerNextToBox(String direction, int width, int[] boxPosition, int playerPosition) {
        boolean checker = false;
        if(direction.equalsIgnoreCase("Up")) {
            for(int i=0; i<boxPosition.length; i++) {
                if(boxPosition[i] == playerPosition-width) {
                    checker = true;
                }
            }
        }
        else if(direction.equalsIgnoreCase("Down")) {
            for(int i=0; i<boxPosition.length; i++) {
                if(boxPosition[i] == playerPosition+width) {
                    checker = true;
                }
            }
        }
        else if(direction.equalsIgnoreCase("Left")) {
            for(int i=0; i<boxPosition.length; i++) {
                if(boxPosition[i] == playerPosition-1) {
                    checker = true;
                }
            }
        }
        else if(direction.equalsIgnoreCase("Right")) {
            for(int i=0; i<boxPosition.length; i++) {
                if(boxPosition[i] == playerPosition+1) {
                    checker = true;
                }
            }
        }
        return checker;

    }

    public boolean checkWall(int tile) {
        if(board[tile] == 4) {
            return true;
        }
        else return false;
    }

    public boolean checkBox(int tile, int[] boxPosition) {
        for(int i=0; i<numOfBoxes; i++) {
            if(boxPosition[i] == tile) {
                return true;
            }
        }
        return false;
    }

    public boolean isInGoal(int box) {
        for(int i=0; i<numOfBoxes; i++) {
            if(goalPosition[i] == box) {
                return true;
            }
        }
        return false;
    }

    public int[] getGoalPosition() {
        return this.goalPosition;
    }

    public boolean isFloor(int tileIndex) {
        if (tileIndex < 0 || tileIndex >= board.length)
            return false;

        return board[tileIndex] != 4;
    }

}