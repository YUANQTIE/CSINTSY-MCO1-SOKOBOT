package solver;

import java.util.ArrayList;

public class Node {
    private int gCost, hCost;
    private int width, height;
    private Node parent;
    private char move;
    private char[][] mapData;
    private char[][] itemsData;
    private int[] playerCoords;
    private boolean walkable = true;
    private boolean goal = false;
    private boolean box = false;
    private int col;
    private int row;

    public Node(int col, int row, char[][] mapData, char[][] itemsData) {
        this.col = col;
        this.row = row;
        this.width = mapData.length;
        this.height = mapData[0].length;
        this.mapData = mapData;
        this.itemsData = itemsData;
    }

    public int[] getPlayerCoords() {
        return findPlayerCoords(width, height, itemsData);
    }


    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }


    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    public int[] findPlayerCoords(int width, int height, char[][] data) {
        return findPosition(width, height, data, '@');
    }

    public ArrayList<int[]> findBoxCoords(int width, int height, char[][] data) {
        return findPositions(width, height, data, '$');
    }

    public ArrayList<int[]> findGoalCoords(int width, int height, char[][] data) {
        return findPositions(width, height, data, '.');
    }

    public int[] findPosition(int width, int height, char[][] data, char target) {
        int[] position = new int[2];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] == target) {
                    position[0] = x;
                    position[1] = y;
                }
            }
        }
        return position;
    }

    public ArrayList<int[]> findPositions(int width, int height, char[][] data, char target) {
        ArrayList<int[]> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x] == target)
                    positions.add(new int[]{x, y});
            }
        }
        return positions;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isGoal() {
        return goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
