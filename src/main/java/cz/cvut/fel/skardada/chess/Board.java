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

    public Board(int size, ChessPiece[][] arrangement) {
        this.size = size;
        this.board = arrangement;
    }
    
    public void update(){
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece :row){
                piece.calculateAvailableMoves(this);
            }
        }
    }
    
    public void movePiece(Coordinates startPos, Coordinates dest){
        if(this.board[startPos.getX()][startPos.getY()] == null){
            return;
        }
        ChessPiece movingPiece = this.board[startPos.getX()][startPos.getY()];
        if(this.board[dest.getX()][dest.getY()] == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        else{
            if (this.board[dest.getX()][dest.getY()].getColor() != movingPiece.getColor()) {
                this.board[dest.getX()][dest.getY()].setPosition(new Coordinates(-1,-1));
                this.board[dest.getX()][dest.getY()] = movingPiece;
                movingPiece.setPosition(dest);
                this.board[startPos.getX()][startPos.getY()] = null;
            }
        }
    }
    public int getSize() {
        return size;
    }

    public ChessPiece[][] getBoard() {
        return board;
    }
    
}
