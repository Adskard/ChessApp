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
public class Player_Human extends Player {
    

    public Player_Human(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        super(name, color, ownPieces, clock);
    }
    public void makeMove(Board currentBoard){
        this.setCurrentlyPlaying(true);
    }

    
}
