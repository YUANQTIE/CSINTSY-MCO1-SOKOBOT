package debug;

import gui.GamePanel;

import java.awt.*;

public class Debug {
    private GamePanel gamePanel;
    private char[][] map, items;
    private int textCount = 0;
    private int playerRow;
    private int playerCol;

    public Debug(GamePanel gamePanel, char[][] map, char[][] items, int playerRow, int playerCol) {
        this.gamePanel = gamePanel;
        this.map = map;
        this.items = items;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
    }

    public void showDebugText(Graphics g) {
        textCount = 0;
        g.setColor(Color.WHITE);
        drawDebugText("width: " + map[0].length, g);
        drawDebugText("height: " + map.length, g);
        drawDebugText("player: (" + playerCol + ", " + playerRow + ")", g);
    }

    public void update() {
        gamePanel.repaint();
    }

    private int[] drawDebugText(String text, Graphics g) {
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        g.drawString(text, gamePanel.getWidth() - textWidth - 6, textHeight * (textCount + 1));
        textCount++;
        return new int[]{textWidth, textHeight};
    }

    private void displayBoxCoords(Graphics g, int hPad) {

    }

    private void displayNearestBoxFromPlayerCoords(Graphics g, int hPad) {

    }

    private void displayGoalCoords(Graphics g, int hPad) {

    }

    private void displayNearestGoalFromPlayerCoords(Graphics g, int hPad) {

    }

    public void setPlayerCol(int playerCol) {
        this.playerCol = playerCol;
    }

    public void setPlayerRow(int playerRow) {
        this.playerRow = playerRow;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    public void setItems(char[][] items) {
        this.items = items;
    }
}
