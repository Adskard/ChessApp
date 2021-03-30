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
public abstract class Player {
    private final PlayerColors color;
    private Timer timer;
    private boolean checked;
    private ChessPiece king;

    public Player(PlayerColors color) {
        this.color = color;
    }
    
    public Coordinates[] makeMove(Board currentBoard){
        Coordinates[] move = new Coordinates[2];
        //mb move to implementation
        //decisions
        //if after move player is checked, then player, CANNOT make the move
        return move;
    }
    
    public void isChecked(Board board){
        ChessPiece[][] pieces = board.getBoard();
        for(ChessPiece[] row : pieces){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(piece.getColor().equals(this.color)){
                    continue;
                }
                //enemy piece
                else{
                    for(Coordinates target : piece.getAvailableMoves()){
                        if(this.king.getPosition().equals(target)){
                            this.checked = true;
                        }
                    }
                }
            }
        }
        this.checked = false;
    }
    
    public boolean isMated(Board board){
        if(this.checked && this.king.getAvailableMoves() == null){
            return true;
        }
        return false;
    }

    public Timer getTimer() {
        return timer;
    }

    public PlayerColors getColor() {
        return color;
    }
    
    
    
}
