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
public class ChessPiecePawn extends ChessPiece{

    public ChessPiecePawn(Coordinates pos, PlayerColors color) {
        super("Pawn",pos, color, new MoveSet((color==PlayerColors.white ?
            new Coordinates[]{new Coordinates(0,1)} :
            new Coordinates[]{new Coordinates(0,-1)}), 1));
    }
    public void doublePass(){
        
    }
    public void enPassant(){
        
    }
    public void promotion(){
        
    }
    
}
