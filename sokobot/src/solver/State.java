package solver;
public class State {
    private int[] boxPositions;
    private int playerPosition;
    private long hash;

    public State(int[] boxPositions, int playerPosition, long hash) {
        this.boxPositions = boxPositions;
        this.playerPosition = playerPosition;
        this.hash = hash;
    }

    /* Function that updates the box positions in a state.
        @param oldBoxPosition - the position of the box to be moved
        @param newBoxPosition - the new position of the box
        @return array containing updated box positions
    */
    public int[] updateBoxPosition(int oldBoxPosition, int newBoxPosition, int[] boxPositions) {
        for (int i = 0; i < boxPositions.length; i++)
            if (boxPositions[i] == oldBoxPosition)
                boxPositions[i] = newBoxPosition;
        return boxPositions;
    }

    /* Function that updates a state based on the move.
        @param move - contains the changes to the state
        @return updated state
    */
    public State apply(Move move) {
        int newPlayerPosition = move.getNewPlayerPosition();
        int[] newBoxPosition = this.boxPositions.clone();
        long newHash;

        if (move.isPushedBox())
           newBoxPosition = this.updateBoxPosition(move.getOldBoxPosition(), move.getNewBoxPosition(), newBoxPosition);

        newHash = Board.getWidth() * Board.getHeight() > 156 ?
                Zobrist.computeHashForLargeMaps(newBoxPosition) :
                Zobrist.computeHash(newPlayerPosition, newBoxPosition);

        return new State(newBoxPosition, newPlayerPosition, newHash);
    }

    /* Function that returns the state's hash.
        @return hash of the state
    */
    public long getHash() {
        return hash;
    }

    /* Function that returns the player position in the state.
        @return player position
    */
    public int getPlayerPosition() {
        return playerPosition;
    }

    /* Function that returns the box positions in the state.
        @return box positions
    */
    public int[] getBoxPositions() {
        return boxPositions;
    }
}
