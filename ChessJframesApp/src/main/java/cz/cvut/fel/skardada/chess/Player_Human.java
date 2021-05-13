/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 * PlayerHuman is an implementation of abstract class Player and deals with correctly interpreting human player input through the view of this application.
 * @author Adam Škarda
 */
public class Player_Human extends Player {
    
    /**
     *
     * @param name Player name 
     * @param color Player color
     * @param ownPieces Player pieces pieces that have the same color as the player, Player can move them
     * @param clock Player clock, for measuring turn time
     */
    public Player_Human(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        super(name, color, ownPieces, clock);
    }

    /**
     * Signifies that the human player is making a move on the board.
     * The move is realized in the view of this application.
     * @param currentBoard Current state of the board
     */
    @Override
    public void makeMove(Board currentBoard){
        this.setCurrentlyPlaying(true);
    }

    
}
