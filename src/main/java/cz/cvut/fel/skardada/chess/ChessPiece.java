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
public abstract class ChessPiece {
    int[] position;
    String[] moveSet;
    public ChessPiece(int[] pos) {
        position = pos;
    }
    
}
