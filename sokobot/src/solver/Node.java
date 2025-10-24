package solver;
public class Node {
    private State state;
    private String path;
    private int gCost; // Actual cost so far (path length)
    private int hCost; // Heuristic
    private int tCost; // gCost + hCost

    public Node(State state, String path, int gCost, int hCost) {
        this.state = state;
        this.path = path;
        this.gCost = gCost;
        this.hCost = hCost;
        this.tCost = gCost + hCost;
    }

    /* Function that returns the path of the node.
        @return path
    */
    public String getPath() {
        return this.path;
    }

    /* Function that returns the total costs of the node.
        @return total cost
    */
    public int getTCost() {
        return tCost;
    }

    /* Function that returns the actual cost so far of the node.
        @return gcost
    */
    public int getGCost() {
        return gCost;
    }

    /* Function that returns the state of the node.
        @return state
    */
    public State getState() {
        return state;
    }
}
