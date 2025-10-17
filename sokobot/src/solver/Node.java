package solver;
public class Node {
    private State state;
    private String path;
    private int gCost; //Actual cost so far (path length)
    private int hCost; // Heuristic
    private int tCost; // gCost + hCost

    public Node(State state, String path, int gCost, int hCost) {
        this.state = state;
        this.path = path;
        this.gCost = gCost;
        this.hCost = hCost;
        this.tCost = gCost + hCost;
    }

    public String getPath() {
        return this.path;
    }

    public int getTCost() {
        return tCost;
    }

    public int getGCost() {
        return gCost;
    }

    public State getState() {
        return state;
    }
}
