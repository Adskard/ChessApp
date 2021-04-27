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
public class Player_Computer extends Player{

    public Player_Computer(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        super(name, color, ownPieces, clock);
    }

  
    public void makeMove(Board currentBoard){
        Coordinates[] move = new Coordinates[2];
        //mb move to implementation
        //decisions
        //if after move player is checked, then player, CANNOT make the move
        return;
    }


    
}
