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
public class Board {
    private int size;
    private ChessPiece[][] board;
    ChessPiece[] piecesOnBoard;
    public Board( int Size){
        this.size = Size;
        this.board = new ChessPiece[this.size][this.size];
    }

    public Board() {
        this.size = 8;
        this.board = new ChessPiece[this.size][this.size];
    }
    public int getSize() {
        return size;
    }
    
}
