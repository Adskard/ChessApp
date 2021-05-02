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
    
    public boolean hasInsufficientMaterial(){
        //only king remains 
        if (this.ownPieces.size() == 1) {
            return true;
        }
        //king and bishop or knight remains
        if(this.ownPieces.size() == 2 && (this.ownPieces.get(0).getName().contains("Knight") || this.ownPieces.get(0).getName().contains("Bishop") || this.ownPieces.get(1).getName().contains("Knight") || this.ownPieces.get(1).getName().contains("Bishop"))){
            return true;
        }
        return false;
    }
    
    public void updateAvailableMoves(){
        //go through all owned pieces, removed destoryed pieces and get legal moves of still alive pieces
        this.availableMoves.clear();
        ArrayList<ChessPiece> removedPieces = new ArrayList();
        for(ChessPiece piece : this.ownPieces){
            if(piece.getPosition().getX() == -1){
                removedPieces.add(piece);
                continue;
            }
            this.availableMoves.addAll(piece.getLegalMoves());
        }
        this.ownPieces.removeAll(removedPieces);
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

    public ChessClock getChessClock(){
        return clock;
    }

    public ArrayList<ChessPiece> getOwnPieces() {
        return ownPieces;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Coordinates> getAvailableMoves() {
        return availableMoves;
    }

    public ChessClock getClock() {
        return clock;
    }
}
