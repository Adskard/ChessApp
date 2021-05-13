
package cz.cvut.fel.skardada.chess;

/**
 * ChessPieceKing is special piece that needs to exist for the game to work. It can also castle and records if it is in check.
 * @author Adam Škarda
 */
public class ChessPieceKing extends ChessPiece {
    private boolean checked;

    /**
     * Creates king with proper moveset
     * sets checked as false
     * @param pos position of the king
     * @param color king color
     */
    public ChessPieceKing(Coordinates pos, PlayerColors color){
        super(color + "King",pos, color, new MoveSet(new Coordinates[]{
            new Coordinates(1,1),
            new Coordinates(-1,-1),
            new Coordinates(-1,1),
            new Coordinates(1,-1),
            new Coordinates(1,0),
            new Coordinates(0,1),
            new Coordinates(0,-1),
            new Coordinates(-1,0)
        }, 1),

            "/"+color.toString()+"King.png"
                );
        this.checked = false;
    }

    /**
     * Creates king based on original piece
     * @param piece
     */
    public ChessPieceKing(ChessPiece piece) {
        super(piece);
        this.checked = false;
    }

    /**
     *
     * @return returns checked status of this king
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * sets this king checked status
     * @param checked 
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
}
