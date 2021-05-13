
package cz.cvut.fel.skardada.chess;

/**
 * ChessPiecePawn, while not essential to some chess variants it is still important and special enough to have its own class.
 * It can be promoted and has a variable that describes if if can be promoted.
 * @author Adam Škarda
 */
public class ChessPiecePawn extends ChessPiece{
    
    private boolean canBePromoted;

    /**
     * Creates a pawn with proper moveSet for a given color
     * @param pos pawns position
     * @param color pawn color
     */
    public ChessPiecePawn(Coordinates pos, PlayerColors color) {
        super(color + "Pawn",pos, color, new MoveSet((color==PlayerColors.white ?
            new Coordinates[]{new Coordinates(1,0)} :
            new Coordinates[]{new Coordinates(-1,0)}), 1),
        "/"+color.toString()+"Pawn.png"
        );
        canBePromoted = false;
    }   

    /**
     * Creates pawn based on original
     * @param piece original piece to be coppied
     */
    public ChessPiecePawn(ChessPiece piece) {
        super(piece);
        canBePromoted = false;
    }

    /**
     *
     * @return returns canBePromoted flag
     */
    public boolean isCanBePromoted() {
        return canBePromoted;
    }

    /**
     * Sets promotion status for this pawn
     * @param canBePromoted
     */
    public void setCanBePromoted(boolean canBePromoted) {
        this.canBePromoted = canBePromoted;
    }
    
}
