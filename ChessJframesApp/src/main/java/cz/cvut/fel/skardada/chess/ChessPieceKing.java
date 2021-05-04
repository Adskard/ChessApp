/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

/**
 *
 * @author Adam Škarda
 */
public class ChessPieceKing extends ChessPiece {
    private boolean checked;

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

    public ChessPieceKing(ChessPiece piece) {
        super(piece);
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
}
