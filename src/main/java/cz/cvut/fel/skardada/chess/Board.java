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
    private final int size;
    private ChessPiece[][] board;
    ChessPiece[] piecesOnBoard;
    ChessStyle style;   
    public Board(int Size){
        this.size = Size;
        this.board = new ChessPiece[this.size][this.size];
    }

    public Board(ChessStyle style) {
        this.size = 8;
        this.style = style;
        this.board = new ChessPiece[this.size][this.size];
        this.setUpPieces(this.style);
    }
    private void setUpPieces(ChessStyle style){
    }
    public int getSize() {
        return size;
    }
    
}
