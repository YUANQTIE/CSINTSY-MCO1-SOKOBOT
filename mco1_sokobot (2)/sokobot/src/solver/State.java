package solver;

public class State{
    private final int[] boxPositions; 
	private final int playerPosition; //for controls. Up: -width, Down: +width, Left: -1, Right: +1
    private final State parent;
    private final int g; // cost so far down the tree
    private final int h; // heuristic (distance estimate)
    private final int f; // f = g + h
    private final char prevDirection;

    public State(int[] boxPositions, int playerPosition, State parent, int g, int h, int f, char prevDirection) {
        this.boxPositions = boxPositions;
        this.playerPosition = playerPosition;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = f;
        this.prevDirection = prevDirection;
    }

    public int[] getBoxPositions() {
        return boxPositions;
    }
    public int getPlayerPosition() {
        return playerPosition;
    }
    public State getParent() {
        return parent;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    public char getPrevDirection() {
        return prevDirection;
    }

    //no need for setters
}
