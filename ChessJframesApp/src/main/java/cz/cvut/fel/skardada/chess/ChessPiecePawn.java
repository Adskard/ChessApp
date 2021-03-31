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
public class ChessPiecePawn extends ChessPiece{
    
    private boolean canPromote;
    private boolean canDoublePawn;
    private boolean canEnPassant;
    private boolean canPawnTake;

    public ChessPiecePawn(Coordinates pos, PlayerColors color) {
        super("Pawn",pos, color, new MoveSet((color==PlayerColors.white ?
            new Coordinates[]{new Coordinates(0,1)} :
            new Coordinates[]{new Coordinates(0,-1)}), 1));
    }   
}
