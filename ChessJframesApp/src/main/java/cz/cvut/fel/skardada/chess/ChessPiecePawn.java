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
public class ChessPiecePawn extends ChessPiece{
    
    private boolean canBePromoted;

    public ChessPiecePawn(Coordinates pos, PlayerColors color)  throws Exception {
        super(color + "Pawn",pos, color, new MoveSet((color==PlayerColors.white ?
            new Coordinates[]{new Coordinates(1,0)} :
            new Coordinates[]{new Coordinates(-1,0)}), 1),
        "D:\\Projects\\Java\\ChessJframesApp\\ChessJframesApp\\src\\main\\java\\resources\\"+color.toString()+"Pawn.png"
        );
        canBePromoted = false;
    }   

    public ChessPiecePawn(ChessPiece piece) {
        super(piece);
        canBePromoted = false;
    }

    public boolean isCanBePromoted() {
        return canBePromoted;
    }

    public void setCanBePromoted(boolean canBePromoted) {
        this.canBePromoted = canBePromoted;
    }
    
}
