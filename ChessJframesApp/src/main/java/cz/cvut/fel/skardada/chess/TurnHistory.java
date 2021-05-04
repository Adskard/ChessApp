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
    private final ArrayList<Coordinates> startPos;
    private final ArrayList<String> movesInPgn;
    private int turn;
    
    public TurnHistory(){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
        this.startPos = new ArrayList();
        this.movesInPgn = new ArrayList<>();
        this.turn = 0;
    }
    
    public TurnHistory(TurnHistory original){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
        this.startPos = new ArrayList();
        this.movesInPgn = new ArrayList<>();
        this.destinations.addAll(original.getDestinations());
        this.piecesMoved.addAll(original.getPiecesMoved());
        this.turn = original.getTurn();
        this.movesInPgn.addAll(original.getMovesInPgn());
        this.startPos.addAll(original.getStartPos());
    }
    
    public void addEntry(ChessPiece piece, Coordinates dest, Coordinates start){
        this.piecesMoved.add(piece);
        this.destinations.add(dest);
        this.startPos.add(start);
        this.turn += piecesMoved.size() % 2;
        
    }
    
    public void updatePgnNotation(Board board, boolean takes, boolean castleQ, boolean castleK, boolean promo){
        ChessPiece piece = this.piecesMoved.get(piecesMoved.size() - 1);
        Coordinates dest = this.destinations.get(this.destinations.size() - 1);
        Coordinates start = this.startPos.get(this.startPos.size() - 1);
        
        this.movesInPgn.add(PgnParser.enocodeMoveToPgn(piece, dest, start, board, takes, castleQ, castleK, promo));
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

    public ArrayList<String> getMovesInPgn() {
        return movesInPgn;
    }
    
    public String getWholeGamePgn(){
        String game = "";
        for (int i = 0; i < movesInPgn.size(); i++) {
            if(i % 2 == 0){
                game += i/2 + 1;
                game +=". ";
            }
            game += movesInPgn.get(i) + " ";
        }
        return game;
    }

    public ArrayList<Coordinates> getStartPos() {
        return startPos;
    }

    public int getTurn() {
        return turn;
    }
}
