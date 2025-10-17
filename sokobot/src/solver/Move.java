package solver;
public class Move {
    private int oldPlayerPosition;
    private int newPlayerPosition;
    private int oldBoxPosition;
    private int newBoxPosition;
    private boolean pushedBox;
    private String path;

    public Move(int oldPlayerPosition, int newPlayerPosition, String path) {
        this.oldPlayerPosition = oldPlayerPosition;
        this.newPlayerPosition = newPlayerPosition;
        this.path = path;
        this.pushedBox = false;
    }

    public Move(int oldPlayerPosition, int newPlayerPosition, int oldBoxPosition, int newBoxPosition, String path) {
        this.oldPlayerPosition = oldPlayerPosition;
        this.newPlayerPosition = newPlayerPosition;
        this.oldBoxPosition = oldBoxPosition;
        this.newBoxPosition = newBoxPosition;
        this.pushedBox = true;
        this.path = path;
    }

    public int getOldPlayerPosition() {
        return this.oldPlayerPosition;
    }

    public int getNewPlayerPosition() {
        return this.newPlayerPosition;
    }

    public int getOldBoxPosition() {
        return this.oldBoxPosition;
    }

    public int getNewBoxPosition() {
        return this.newBoxPosition;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isPushedBox() {
        return pushedBox;
    }

    public void setNewPlayerPosition(int newPlayerPosition) {
        this.newPlayerPosition = newPlayerPosition;
    }

}
