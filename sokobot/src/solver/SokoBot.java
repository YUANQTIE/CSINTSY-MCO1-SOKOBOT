package solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SokoBot {
  private void configureMap(int width, int height, char[][] mapData, char[][] itemsData) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (mapData[y][x] == ' ')
          mapData[y][x] = itemsData[y][x];
//        System.out.print(mapData[y][x]);
      }
//      System.out.println();
    }
  }

  private void search(int width, int height, char[][] mapData, char[][] itemsData) {
    System.out.println("Searching...");
    PriorityQueue<Node> openQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getFCost).thenComparing(Node::getHCost));
    HashSet<Node> closedSet = new HashSet<>();
    Node[][] nodeMap = new Node[height][width];
    Node playerNode = null, goalNode;
    ArrayList<Node> boxNodes = new ArrayList<>();
    ArrayList<Node> goalNodes = new ArrayList<>();


    configureMap(width, height, mapData, itemsData);
    for (int y = 0; y < mapData.length; y++) {
      for (int x = 0; x < mapData[0].length; x++) {
        Node node = new Node(x, y, mapData, itemsData);
        // Set infinite G cost to all nodes
        node.setGCost(Integer.MAX_VALUE);
        nodeMap[y][x] = node;
        switch (mapData[y][x]) {
          case '#' -> node.setWalkable(false);
          case '@' -> playerNode = node;
          case '.' -> goalNodes.add(node);
          case '$' -> boxNodes.add(node);
        }
      }
    }
    System.out.println("G cost for all nodes set to infinite");

    // Set 0 G cost for start node
    if (playerNode != null) playerNode.setGCost(0);
    System.out.println("G cost for start node set to 0");

    System.out.println("Map initialized...");

    // For debugging
    goalNode = nodeMap[6][7];
    goalNode.setGoal(true);

    int[][] directions = {
            {1, 0}, // Top
            {-1, 0}, // Bottom
            {0, -1}, // Left
            {0, 1} // Right
    };

    int count = 1;

    openQueue.add(playerNode);
    while (!openQueue.isEmpty()) {
      System.out.println("\n\n========= Running loop: " + count++ + " =========");

      Node currentNode = openQueue.poll();
      System.out.println("Polled currentNode: " + debugCoords(currentNode) + " with updated values " + openQueue.stream().map((Function<Node, Object>) this::debugCoords).toList());

      if (closedSet.contains(currentNode)) continue;
      closedSet.add(currentNode);
      System.out.println("Closed set: " + closedSet.stream().map((Function<Node, Object>) this::debugCoords).collect(Collectors.toSet()));
      System.out.println("Executed lazy insertion");

      if (currentNode == goalNode) {
        trackPath(playerNode, goalNode);
        return;
      }
      System.out.println("Goal node checked: " + debugCoords(goalNode));

      System.out.println("Exploring neighbors...");
      boolean neighborExplored = false;

      System.out.println("currentNode (col, row): " + currentNode.getCol() + ", " + currentNode.getRow());
      // Explore neighbors (row, col)
      for (int[] d : directions) {
        int dRow = currentNode.getRow() + d[0];
        int dCol = currentNode.getCol() + d[1];

        if (dRow < 0 || dRow >= height || dCol < 0 || dCol >= width) continue;

        Node neighbor = nodeMap[dRow][dCol];
        System.out.println("Neighbor (col, row): " + neighbor.getCol() + ", " + neighbor.getRow());
        if (!neighbor.isWalkable() || closedSet.contains(neighbor)) continue;

        System.out.println("Calculating Costs...");
        int newGCost = currentNode.getGCost() + 1;
        System.out.println("Current Node GCost: " + newGCost);
        System.out.println("Neighbor GCost: " + neighbor.getGCost());
        if (newGCost < neighbor.getGCost()) {
            neighbor.setGCost(newGCost);
            int h = heuristic(neighbor.getCol(), goalNode.getCol(), neighbor.getRow(), goalNode.getRow());
            neighbor.setHCost(h);
            neighbor.setParent(currentNode);
            System.out.println("Heuristic: " + h);
            System.out.println("Parent node: " + debugCoords(currentNode));
            openQueue.add(neighbor);
            System.out.println("Open Queue: " + openQueue.stream().map(this::debugCoords).toList());
            System.out.println("Explored neighbor node: " + debugCoords(neighbor));
            neighborExplored = true;
        }
      }

      if (neighborExplored) System.out.println("Neighbors explored");
      else System.out.println("Neighbor failed to explore!");
    }
  }


  private String debugCoords(Node node) {
    return "(" + node.getCol() + ", " + node.getRow() + ")";
  }

  private void trackPath(Node startNode, Node goalNode) {
    ArrayList<Node> paths = new ArrayList<>();
    paths.add(goalNode);

    Node currentNode = goalNode.getParent();
    while (currentNode != startNode) {
      paths.add(currentNode);
      currentNode = currentNode.getParent();
    }
    paths.add(startNode);
    System.out.println("\n\nPATH TRACKED: " + paths.reversed().stream().map(this::debugCoords).toList());
  }

  public int heuristic(int x1, int x2, int y1, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
    search(width, height, mapData, itemsData);

    return "";
  }

}
