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
public class TurnHistory {
    
    //could add Board atribute in the future for past viewing
    private final ArrayList<ChessPiece> piecesMoved;
    private final ArrayList<Coordinates> destinations;
    
    public TurnHistory(){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
    }
    
    public TurnHistory(TurnHistory original){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
        this.destinations.addAll(original.getDestinations());
        this.piecesMoved.addAll(original.getPiecesMoved());
    }
    
    public void addEntry(ChessPiece piece, Coordinates dest){
        this.piecesMoved.add(piece);
        this.destinations.add(dest);
    }
    
    @Override
    public String toString(){
        String repr = "[";
        //piecesMoved has always the same size as destinations
        for (int i = 0; i < piecesMoved.size(); i++) {
            repr = repr + piecesMoved.get(i).toString().substring(0,2);
            repr = repr + destinations.get(i).toString();
            if(i +1 == piecesMoved.size()) {
                break;
            }
            repr = repr + ", ";
        }
        return repr + "]";
    }
    
    public ArrayList<ChessPiece> getPiecesMoved(){
        return this.piecesMoved;
    }
    public ArrayList<Coordinates> getDestinations(){
        return this.destinations;
    }
}
