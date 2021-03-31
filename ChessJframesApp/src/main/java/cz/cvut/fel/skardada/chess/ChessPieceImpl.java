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
public class ChessPieceImpl extends ChessPiece{

    public ChessPieceImpl(String name, Coordinates pos, PlayerColors color, MoveSet mov) {
        super(name, pos, color, mov);
    }

    public ChessPieceImpl(MoveSet moveSet, String name) {
        super(moveSet, name);
    }
    
}
