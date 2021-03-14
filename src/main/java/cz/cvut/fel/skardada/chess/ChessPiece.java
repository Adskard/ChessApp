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
public abstract class ChessPiece {
    private int[] position;
    private final MoveSet moveSet;
    private final PlayerColors color;
    public ChessPiece(int[] pos, PlayerColors color, MoveSet mov) {
        this.position = pos;
        this.color = color;
        this.moveSet = mov;
    }
    
}
