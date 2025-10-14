package solver;
public class Node {
    State state;
    String path;
    int gCost; //Actual cost so far (path length)
    int hCost; // Heuristic
    int tCost; // gCost + hCost

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
}
