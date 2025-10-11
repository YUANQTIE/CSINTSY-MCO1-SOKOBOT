package solver;

public class StaticHelpers{

    public static int manhattanDistance(int box, int goal, int width){
        //compute x-component and y-component of the manhattan distance
        int xComponent = Math.abs((box % width) - (goal % width));
        int yComponenent = Math.abs((box / width) - (goal / width)); //we only need to add kasi the number of tiles is exactly the distance

        return xComponent + yComponenent;
    }

    public static boolean isInside(int object) {
        return object >= 0 && object < SokoBot.width * SokoBot.height;
    }
    
    public static boolean isBlocked(int moveVal, int newPlayer, int[] newBoxes, int[] flatMap) {
        boolean blocked = false;

        for (int j = 0; j < newBoxes.length; j++) { //box move checker
            if (newBoxes[j] == newPlayer) { //checks which box the player is trying to push
                int newBoxPos = newBoxes[j] + moveVal; //the new position of the box if pushed
                if (!isInside(newBoxPos) || flatMap[newBoxPos] == 1) { //validats if the boxes new position is valid (outside or hits a wall)
                    blocked = true;
                    break;
                }
                for (int bx : newBoxes) {
                    if (bx == newBoxPos) { //checks if a box a player is trying to push is already occupied by another box
                        blocked = true;
                        break;
                    }
                }
                if (!blocked) { //once a box' new position is validated, the new position is assigned to the box
                    newBoxes[j] = newBoxPos;
                }
                break;
            }
        }

        return blocked;
    }
}