package solver;
public class State {
    private int[] boxPositions;
    private int playerPosition;
    private long hash;
    private State linkToState;
    private int newBoxPosition;

    public State(int[] boxPositions, int playerPosition, long hash) {
        this.boxPositions = boxPositions;
        this.playerPosition = playerPosition;
        this.hash = hash;
    }

    public State(int[] boxPositions, int playerPosition, long hash, State previousState, int newBoxPosition) {
        this.boxPositions = boxPositions;
        this.playerPosition = playerPosition;
        this.hash = hash;
        this.linkToState = previousState;
        this.newBoxPosition = newBoxPosition;
    }

    public int[] updateBoxPosition(int[] boxPositions, int oldBoxPosition, int newBoxPosition) {
        for(int i=0; i<boxPositions.length; i++) {
            if(boxPositions[i] == oldBoxPosition) {
                boxPositions[i] = newBoxPosition;
            }
        }
        return boxPositions;
    }

    public State apply(Move move, int numOfBoxes) {
        int newPlayerPosition = move.getNewPlayerPosition();
        int[] newBoxPosition = this.boxPositions.clone();
        long newHash = this.hash;

        if(move.isPushedBox()) {
           newBoxPosition = this.updateBoxPosition(newBoxPosition, move.getOldBoxPosition(), move.getNewBoxPosition());
        }
        if(numOfBoxes > 5) {
            newHash = Zobrist.computeHashForLargeMaps(newPlayerPosition, newBoxPosition);
        }
        else
            newHash = Zobrist.computeHash(newPlayerPosition, newBoxPosition);

        return new State(newBoxPosition, newPlayerPosition, newHash);
    }

    public int getNewBoxPosition() {
        return newBoxPosition;
    }

    public void setNewBoxPosition(int newBoxPosition) {
        this.newBoxPosition = newBoxPosition;
    }

    public State getLinkToState() {
        return linkToState;
    }

    public void setLinkToState(State linkToState) {
        this.linkToState = linkToState;
    }

    public long getHash() {
        return hash;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public int[] getBoxPositions() {
        return boxPositions;
    }

    public void setBoxPositions(int[] boxPositions) {
        this.boxPositions = boxPositions;
    }
}
