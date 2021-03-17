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
    private Coordinates position;
    private final MoveSet moveSet;
    private final PlayerColors color;
    public ChessPiece(Coordinates pos, PlayerColors color, MoveSet mov) {
        this.position = pos;
        this.color = color;
        this.moveSet = mov;
    }
    public Coordinates[] availableMoves(int boardSize){
        Coordinates[] result = new Coordinates[boardSize*boardSize];
        int offset = 0;
        for(Coordinates vector : this.moveSet.getMoveVectors()){
            for (int i = 1; i <= this.moveSet.getMoveDistance(); i++) {
                result[offset] = this.position.getSum(vector.getProduct(i));
                offset++;
            }
        }
        return result;
    }
    public boolean canGetTo(Coordinates pos){
        Coordinates[] vectors = this.moveSet.getMoveVectors();
        for (Coordinates vector : vectors) {
            if (this.position.getSum(vector).equals(pos)) {
                return true;
            }
        }
        return false;
    }
    public Coordinates getPosition() {
        return position;
    }

    public MoveSet getMoveSet() {
        return moveSet;
    }

    public PlayerColors getColor() {
        return color;
    }
    
}
