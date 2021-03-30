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
public class MoveSet implements java.io.Serializable{
    private final Coordinates[] moveVectors;
    private final int moveDistance;
    
    /**
     *
     * @param vectors vectors along which can chess piece move
     * @param distance how many places can a chess piece move
     */
    public MoveSet(Coordinates[] vectors, int distance) {
        this.moveVectors = vectors;
        this.moveDistance = distance;
    }

    public Coordinates[] getMoveVectors() {
        return moveVectors;
    }

    public int getMoveDistance() {
        return moveDistance;
    }
    
}
