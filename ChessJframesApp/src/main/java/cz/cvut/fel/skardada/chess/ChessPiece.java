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
    private ArrayList<Coordinates> legalMoves;
    private int id;
    
    public ChessPiece(String name,Coordinates pos, PlayerColors color, MoveSet mov) {
        this.position = pos;
        this.name = name;
        this.color = color;
        this.moveSet = mov;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChessPiece(MoveSet moveSet, String name) {
        this.moveSet = moveSet;
        this.name = name;
    }   
    
    public boolean canGetTo(Coordinates dest){
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

    public void setAvailableMoves(ArrayList<Coordinates> availableMoves) {
        this.availableMoves = availableMoves;
    }

    public ArrayList<Coordinates> getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(ArrayList<Coordinates> legalMoves) {
        this.legalMoves = legalMoves;
    }
    
}
