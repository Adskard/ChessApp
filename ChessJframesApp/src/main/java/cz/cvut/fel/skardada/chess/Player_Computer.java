/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Adam Škarda
 */
public class Player_Computer extends Player{
    

    public Player_Computer(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        super(name, color, ownPieces, clock);
    }

  
    @Override
    public void makeMove(Board currentBoard){
        System.out.println("making move");
        //no available pieces to play
        if(this.getOwnPieces().isEmpty()){
            return;
        }
        //Choose piece to play
        ChessPiece piece = getRandomPiece();
        while(piece.getLegalMoves().isEmpty()){
            System.out.println("wrong piece" +piece.getName());
            piece = getRandomPiece();
        }
        System.out.println("piece chosen");
        
        //get source and destination for the move
        Coordinates source = piece.getPosition();
        Coordinates dest = getRandomDest(piece);
        
        System.out.println(piece.getName() + source  + " " + dest);
        
        //move the piece
        currentBoard.movePiece(source, dest);
        
        //check promotion
        if(piece instanceof ChessPiecePawn){
            ChessPiecePawn pawn = (ChessPiecePawn) piece;
            if(pawn.isCanBePromoted()){
                ChessPiece newRank = getRandomUnique(currentBoard);
                while(newRank.getColor() != pawn.getColor()){
                    newRank = getRandomUnique(currentBoard);
                }
                ChessPiece promoted = currentBoard.promotion(pawn, newRank);
                this.getOwnPieces().remove(pawn);
                this.getOwnPieces().add(promoted);
            }
        }
        
        //indicate end of turn to the controller
        this.setFinishedTurn(true);
    }
    
    private ChessPiece getRandomPiece(){
        return this.getOwnPieces().get(ThreadLocalRandom.current().nextInt(0, this.getOwnPieces().size()));
    }
    
    private ChessPiece getRandomUnique(Board board){
        return board.getUniquePieces().get(ThreadLocalRandom.current().nextInt(0, board.getUniquePieces().size())); 
    }
    
    private Coordinates getRandomDest(ChessPiece piece){
        return piece.getLegalMoves().get(ThreadLocalRandom.current().nextInt(0, piece.getLegalMoves().size()));
    }

}
