package solver;
public class State {
    int[] boxPositions;
    int playerPosition;
    long hash;
    State linkToState;
    int newBoxPosition;

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

    public State apply(Move move) {
        int newPlayerPosition = move.newPlayerPosition;
        int[] newBoxPosition = this.boxPositions.clone();
        long newHash = this.hash;

        if(move.pushedBox) {
           newBoxPosition = this.updateBoxPosition(newBoxPosition, move.getOldBoxPosition(), move.getNewBoxPosition());
        }

        newHash = Zobrist.computeHash(newPlayerPosition, newBoxPosition);
        return new State(newBoxPosition, newPlayerPosition, newHash);
    }


}
