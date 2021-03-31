/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 *
 * @author Adam Škarda
 */
public class FutureStatesGen {
 
    // calculate isChecked for every King on the board, 1 arg given - suround with for cycle
    public boolean isChecked(Board board, ChessPiece king){
        ChessPiece[][] pieces = board.getBoard();
        for(ChessPiece[] row : pieces){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(piece.getColor().equals(king.getColor())){
                    continue;
                }
                //enemy piece
                else{
                    for(Coordinates target : piece.getAvailableMoves()){
                        if(king.getPosition().equals(target)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static void calculateLegalMoves(Board board){
        //filter moves that escape check and add special moves eg. Castles
        ChessPiece[][] pieces = board.getBoard();
        for(ChessPiece[] row : pieces){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(piece.getName().equals("Pawn")){
                    
                }
                if(piece.getName().equals("King")){
                    
                }
                        
            }
        }
    }
    
    public boolean checkEnPassant(){
        return false;
    }
    
    public boolean checkCastle(){
        return false;
    }
    
    public boolean checkPromotion(){
        return false;
    }
    
    public boolean checkDoublePawn(){
        return false;
    }
    
    public boolean checkPawnTakes(){
        return false;
    }
    
    public void calculateAvailableMoves(Board board){
        for(ChessPiece[] row : board.getBoard()){
            for(ChessPiece piece : row){
                ArrayList<Coordinates> availableMoves = piece.getAvailableMoves();
                availableMoves.clear();
                MoveSet moves= piece.getMoveSet();
                int distance = moves.getMoveDistance();
                if(distance == Integer.MAX_VALUE){
                    distance = board.getSize();
                }
                for(Coordinates vector : moves.getMoveVectors()){
                    for (int i = 1; i <= distance; i++) {
                        Coordinates dest = piece.getPosition().getSum(vector.getProduct(i));
                        //board boundry
                        if(dest.getX()<0 || dest.getX()>=board.getSize() || dest.getY()<0 || dest.getY()>=board.getSize()){
                            break;
                        }
                        availableMoves.add(piece.getPosition().getSum(vector.getProduct(i)));
                        // cant move past pieces
                        if(board.getBoard()[dest.getX()][dest.getY()] != null){
                            break;
                        }
                    }
                }
                piece.setAvailableMoves(availableMoves);
            }
        } 
    }
}
