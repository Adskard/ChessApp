
package cz.cvut.fel.skardada.chess;

/**
 * ChessPieceImpl is a generic implementation of abstract class ChessPiece.
 * @author Adam Škarda
 */
public class ChessPieceImpl extends ChessPiece{

    /**
     * Copy constructor
     * @param piece original piece
     */
    public ChessPieceImpl(ChessPiece piece) {
        super(piece);
    }

    /**
     * Ye Olde Constructor
     * @param name name of created piece
     * @param pos position of this piece
     * @param color piece color
     * @param mov piece moveSet
     * @param imagePath path to image that represents this piece - should be relative and in resources of this project
     */
    public ChessPieceImpl(String name, Coordinates pos, PlayerColors color, MoveSet mov, String imagePath) {
        super(name, pos, color, mov, imagePath);
    }
    
}
