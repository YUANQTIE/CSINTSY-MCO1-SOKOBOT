package solver;
public class Node {
    State state;
    String path;

    public Node(State state, String path) {
        this.state = state;
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
