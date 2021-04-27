/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import javax.imageio.*;
import java.io.File;
/**
 *
 * @author Adam Škarda
 */
public class ChessPieceKing extends ChessPiece {
    private boolean checked;

    public ChessPieceKing(Coordinates pos, PlayerColors color)  throws Exception{
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

            "D:\\Projects\\Java\\ChessJframesApp\\ChessJframesApp\\src\\main\\java\\resources\\"+color.toString()+"King.png"
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
