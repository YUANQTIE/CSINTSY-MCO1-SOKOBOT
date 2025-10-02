package solver;
import java.util.PriorityQueue;
public class Queue {
    private PriorityQueue<Node> q = new PriorityQueue<>();

    public void enqueue(Node node) {
        q.add(node);
    }

    public Node dequeue() {
        return q.poll();
    }

    public boolean isEmpty() {
        return q.isEmpty();
    }
    
    public int size() {
        return q.size();
    }

    public void clear(){
        q.clear();
    }
}
