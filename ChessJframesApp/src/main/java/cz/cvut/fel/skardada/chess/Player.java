
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 * Player is an abstract class that defines a generic player of the game.
 * That means their color, clock, availablePieces. Its main task is to take a turn (make a decision on what to do on a given turn).
 * @author Adam Škarda
 */
public abstract class Player {
    private final PlayerColors color;
    private final ArrayList<ChessPiece> ownPieces;
    private final ArrayList<Coordinates> availableMoves;
    private final ChessClock clock;
    private boolean currentlyPlaying;
    private boolean finishedTurn;
    private final String name;
    
    /**
     *
     * @param name Player name 
     * @param color Player color
     * @param ownPieces Pieces with the same color as the player, are owned by the player
     * @param clock Player time control
     */
    public Player(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        this.color = color;
        this.ownPieces = ownPieces;
        this.availableMoves = new ArrayList<>();
        this.clock = clock;
        this.currentlyPlaying = false;
        this.finishedTurn = false;
        this.name = name;
    }
    
    /**
     * Player move on the board
     * @param currentBoard current state of the board
     */
    public abstract void makeMove(Board currentBoard);
    
    /**
     * Checking if the player lost/ is checkmated
     * @return true if player is checked and has no available moves or is out of time
     */
    public boolean isMated(){
        if((this.isChecked() && this.availableMoves.isEmpty()) || this.clock.getRemainingTime() <= 0){
            return true;
        }
        return false;
    }
    
    /**
     * Checking conditions for a draw
     * @return true if player has insufficient material for a checkmate
     */
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
    
    /**
     * Updates this Available moves by going through player owned pieces and agregating their legal moves
     */
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
    
    /**
     * Find checked state of players king
     * @return true if player owns a king that is checked
     */
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

    /**
     *
     * @return boolean for finished turn
     */
    public boolean getFinishedTurn() {
        return finishedTurn;
    }

    /**
     * Set player made move
     * @param madeAMove new finishedTurn value
     */
    public void setFinishedTurn(boolean madeAMove) {
        this.finishedTurn = madeAMove;
    }

    /**
     *
     * @return gets this Player color
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     *
     * @return gets this player currentlyPlaying status
     */
    public boolean isCurrentlyPlaying() {
        return currentlyPlaying;
    }

    /**
     * Set this player currentlyPlaying
     * @param currentlyPlaying new currentlyPlaying value
     */
    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        this.currentlyPlaying = currentlyPlaying;
    }

    /**
     *
     * @return get this player clock
     */
    public ChessClock getChessClock(){
        return clock;
    }

    /**
     *
     * @return get this player pieces
     */
    public ArrayList<ChessPiece> getOwnPieces() {
        return ownPieces;
    }

    /**
     *
     * @return gets this player name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return gets ArrayList of this Player availableMoves
     */
    public ArrayList<Coordinates> getAvailableMoves() {
        return availableMoves;
    }
}
