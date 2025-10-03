package solver;
public class Node implements Comparable<Node>{
    private Point point; // Position of bot
    private char[][] mapData; 
    private Node parent;
    private int sCost; // Cost from start node
    private int hCost; // Cost to goal (heuristic)
    private int tCost; // Total Cost (sCost + hCost)
    private String path;
    private /* something */ cratesPositions;
    private /* something */ goalPositions;
    private char move;
    
    public Node(Point point, char[][] mapData, Node parent, int sCost, int hCost){
        this.point = point;
        this.mapData = mapData;
        this.parent = parent;
        this.sCost = sCost;
        this.hCost = hCost; // heuristic(Node)
        this.tCost = gCost + hCost;
        this.path = (parent == null ? "" : parent.path + move);

        this.cratesPositions = new ArrayList<>(cratesPositions);
        this.goalPositions = new ArrayList<>(goalPositions);
        this.move = move;
    }

    public Point getPoint(){
        return this.point;
    }
    public char[][] getMapData(){
        return this.mapData;
    }

    public Node getparent(){
        return this.parent;
    }

    public int getSCost(){
        return this.sCost;
    }

    public int getHCost(){
        return this.hCost;
    }

    public int getTCost(){
        return this.tCost;
    }

    public String path(){
        return this.path;
    }

    public /* something */ getCratesPositions(){
        return this.cratesPositions;
    }

    public /* something */ getGoalPositions(){
        return this.goalPositions;
    }

    public char getMove(){
        return this.move;
    }
    

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.tCost, other.tCost);
    }

}
