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
        switch(style){
            case standard:
            default:
                int pawnBlack = 6;
                int specialBlack = 7;
                int pawnWhite = 1;
                int specialWhite = 0;
                for (int i = 0; i < this.size; i++) {
                    this.board[i][pawnWhite] = new ChessPiece_Pawn(new int[]{i,pawnWhite}, PlayerColors.white);
                    this.board[i][pawnBlack] = new ChessPiece_Pawn(new int[]{i,pawnBlack}, PlayerColors.black);
                }
                break;
        }
    }
    public int getSize() {
        return size;
    }
    
}
