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
public abstract class Player {
    private final PlayerColors color;
    private boolean checked;
    private final ArrayList<ChessPiece> ownPieces;
    private ArrayList<Coordinates> availableMoves;
    private ChessClock clock;
    private boolean currentlyPlaying;
    private boolean finishedTurn;
    private String name;
    
    public Player(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        this.color = color;
        this.ownPieces = ownPieces;
        this.availableMoves = new ArrayList<>();
        this.clock = clock;
        this.currentlyPlaying = false;
        this.finishedTurn = false;
        this.name = name;
    }
    
    public abstract void makeMove(Board currentBoard);
    
    public boolean isMated(){
        if((this.isChecked() && this.availableMoves.isEmpty()) || this.clock.getRemainingTime() <= 0){
            return true;
        }
        return false;
    }
    
    public void updateAvailableMoves(){
        this.availableMoves.clear();
        for(ChessPiece piece : this.ownPieces){
            this.availableMoves.addAll(piece.getLegalMoves());
        }
    }
    
    public ArrayList<ChessPiece> getKnockedOutPieces(){
        ArrayList<ChessPiece> knockedOutPieces = new ArrayList<>();
        for(ChessPiece piece : this.ownPieces){
            if(piece.getPosition().getX() == -1){
                knockedOutPieces.add(piece);
            }
        }
        return knockedOutPieces;
    }
    
    public boolean isChecked(){ 
        //find king chessPiece - ask it if its in check 
        for (ChessPiece piece : this.ownPieces) {
            if(piece instanceof ChessPieceKing){
                ChessPieceKing king = (ChessPieceKing) piece;
                if(king.isChecked()){
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean getFinishedTurn() {
        return finishedTurn;
    }

    public void setFinishedTurn(boolean madeAMove) {
        this.finishedTurn = madeAMove;
    }

    public PlayerColors getColor() {
        return color;
    }
    public boolean isCurrentlyPlaying() {
        return currentlyPlaying;
    }

    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

}
