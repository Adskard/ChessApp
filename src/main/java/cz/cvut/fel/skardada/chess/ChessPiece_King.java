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
public class ChessPiece_King extends ChessPiece {

    public ChessPiece_King(int[] pos, PlayerColors color) {
        super(pos, color, new MoveSet(new int[][]{{1,1},{-1,1},{-1,-1},{1,-1},{1,0},{-1,0},{0,1},{0,-1}}, 1));
    }
    public void Castle(){
        
    }
}
