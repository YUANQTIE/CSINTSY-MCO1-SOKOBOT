package gui;

import debug.Debug;
import solver.SokoBot;

public class BotThread extends Thread {
  private SokoBot sokoBot;
  private int width;
  private int height;
  private char[][] mapData;
  private char[][] itemsData;

  private String solution = null;

  private Debug debug;

  public BotThread(int width, int height, char[][] mapData, char[][] itemsData, Debug debug) {
    sokoBot = new SokoBot();
    this.width = width;
    this.height = height;
    this.mapData = mapData;
    this.itemsData = itemsData;
    this.debug = debug;
  }

  @Override
  public void run() {
    solution = sokoBot.solveSokobanPuzzle(width, height, mapData, itemsData, debug);
  }

  public String getSolution() {
    return solution;
  }
}
