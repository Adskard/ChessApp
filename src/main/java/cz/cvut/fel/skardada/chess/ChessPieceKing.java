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

    public ChessPieceKing(Coordinates pos, PlayerColors color) {
        super(pos, color, new MoveSet(new Coordinates[]{
            new Coordinates(1,1),
            new Coordinates(-1,-1),
            new Coordinates(-1,1),
            new Coordinates(1,-1),
            new Coordinates(1,0),
            new Coordinates(0,1),
            new Coordinates(0,-1),
            new Coordinates(-1,0)
        }, 1));
    }
    public void Castle(){
        
    }
}
