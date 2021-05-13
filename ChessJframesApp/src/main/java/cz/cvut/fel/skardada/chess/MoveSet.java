
package cz.cvut.fel.skardada.chess;

/**
 * MoveSet is a class that describes the way a chess piece can move on the board (barring any special moves, such as casteling).
 * It comprises of two parts: a set of vectors which give a direction of the movement 
 * and distance which describes how many squares in a given direction can the piece move - it is an upper limit.
 * @author Adam Škarda
 */
public class MoveSet implements java.io.Serializable{
    private final Coordinates[] moveVectors;
    private final int moveDistance;
    
    /**
     *
     * @param vectors vectors along which a chess piece can move
     * @param distance how many places can a chess piece move, -1 for unlimited
     */
    public MoveSet(Coordinates[] vectors, int distance) {
        this.moveVectors = vectors;
        this.moveDistance = distance;
    }

    /**
     *
     * @return returns move vectors as array of Coordinates
     */
    public Coordinates[] getMoveVectors() {
        return moveVectors;
    }

    /**
     * 
     * @return returns moveDistance as integer
     */
    public int getMoveDistance() {
        return moveDistance;
    }
    
}
