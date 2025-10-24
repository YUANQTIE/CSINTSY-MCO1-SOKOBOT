package solver;

public class Move {
    private int oldPlayerPosition;
    private int newPlayerPosition;
    private int oldBoxPosition;
    private int newBoxPosition;
    private boolean pushedBox;
    private String path;

    public Move(int oldPlayerPosition, int newPlayerPosition, int oldBoxPosition, int newBoxPosition, String path) {
        this.oldPlayerPosition = oldPlayerPosition;
        this.newPlayerPosition = newPlayerPosition;
        this.oldBoxPosition = oldBoxPosition;
        this.newBoxPosition = newBoxPosition;
        this.pushedBox = true;
        this.path = path;
    }

    /* Function that returns the new player position of the move.
        @return new player position
    */
    public int getNewPlayerPosition() {
        return this.newPlayerPosition;
    }

    /* Function that returns the old box position of the move.
        @return old box position
    */
    public int getOldBoxPosition() {
        return this.oldBoxPosition;
    }

    /* Function that returns the new box position of the move.
        @return new box position
    */
    public int getNewBoxPosition() {
        return this.newBoxPosition;
    }

    /* Function that returns the path of the move.
        @return path
    */
    public String getPath() {
        return this.path;
    }

    /* Function that returns whether a box was pushed in the move.
        @return true if a box was pushed, otherwise false
    */
    public boolean isPushedBox() {
        return pushedBox;
    }

}
