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
public abstract class ChessPiece implements java.io.Serializable{
    private final MoveSet moveSet;
    private final String name;
    private Coordinates position;
    private PlayerColors color;
    private ArrayList<Coordinates> availableMoves;
    
    public ChessPiece(String name,Coordinates pos, PlayerColors color, MoveSet mov) {
        this.position = pos;
        this.name = name;
        this.color = color;
        this.moveSet = mov;
    }

    public ChessPiece(MoveSet moveSet, String name) {
        this.moveSet = moveSet;
        this.name = name;
    }   
    
    public void calculateAvailableMoves(Board board){
        this.availableMoves.clear();
        Coordinates[] result = new Coordinates[board.getSize()*board.getSize()];
        int offset = 0;
        int distance = this.moveSet.getMoveDistance();
        if(distance == Integer.MAX_VALUE){
            distance = board.getSize();
        }
        for(Coordinates vector : this.moveSet.getMoveVectors()){
            for (int i = 1; i <= distance; i++) {
                Coordinates dest = this.position.getSum(vector.getProduct(i));
                //board boundry
                if(dest.getX()<0 || dest.getX()>=board.getSize() || dest.getY()<0 || dest.getY()>=board.getSize()){
                    offset++;
                    break;
                }
                result[offset] = this.position.getSum(vector.getProduct(i));
                offset++;
                // cant move past pieces
                if(board.getBoard()[dest.getX()][dest.getY()] != null){
                    break;
                }
            }
        }
        //updates available moves
        for(Coordinates coord : result){
            if(coord != null){
                this.availableMoves.add(coord);
            }
        }
    }
    
    public boolean canGetTo(Coordinates dest, Board board){
        for (Coordinates pos : this.availableMoves) {
            if(dest.equals(pos)){
                return true;
            }
        }
        return false;
    }
    
    public Coordinates getPosition() {
        return position;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public MoveSet getMoveSet() {
        return moveSet;
    }

    public PlayerColors getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setColor(PlayerColors color) {
        this.color = color;
    }

    public ArrayList<Coordinates> getAvailableMoves() {
        return availableMoves;
    }
    
}
