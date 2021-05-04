/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;
import java.util.logging.*;
/**
 *
 * @author Adam Škarda
 */
public class Board {
    private final int size;
    private final ChessPiece[][] board; 
    private final TurnHistory history; 
    private final ArrayList<ChessPiece> uniquePieces;
    private static Logger logger = Logger.getLogger(Board.class.getName());

    public Board(int size, ChessPiece[][] arrangement) {
        this.size = size;
        this.board = arrangement;
        this.history = new TurnHistory();
        this.uniquePieces = this.findUniques(arrangement);
        this.distributeIds();
    }
    
    //copy constructor
    public Board(Board original){
        this.history = new TurnHistory(original.getHistory());
        this.size = original.getSize();
        this.board = new ChessPiece[size][size];
        this.uniquePieces = new ArrayList();
        uniquePieces.addAll(original.getUniquePieces());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (original.getBoard()[i][j] == null) {
                    this.board[i][j] = null;
                    continue;
                }
                
                if(original.getBoard()[i][j] instanceof ChessPiecePawn){
                    this.board[i][j] = new ChessPiecePawn(original.getBoard()[i][j]);
                    continue;
                }
                if(original.getBoard()[i][j] instanceof ChessPieceKing){
                    this.board[i][j] = new ChessPieceKing(original.getBoard()[i][j]);
                    continue;
                }

                else
                {
                    this.board[i][j] = new ChessPieceImpl(original.getBoard()[i][j]);
                } 
            }
        } 
    }
    
    private void distributeIds(){
        // each piece gets unique id
        int id = 0;
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if(piece != null){
                    piece.setId(id);
                    id++;
                }
            }
        }
    }
    
    public void updatePieces(){
        updateAvailableMoves();
        updateChecks();
        updateLegalMoves();
        
    }
    
    public void updateAvailableMoves(){
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){   
                if (piece != null) {
                    piece.setAvailableMoves(FutureStatesGen.calculateAvailableMoves(this, piece));
                }
            }
        }
    }
    
    public void updateLegalMoves(){
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if (piece != null) {
                    piece.setLegalMoves(FutureStatesGen.calculateLegalMoves(this, piece)); 
                } 
            }
        }
    }
    
    public void updateChecks(){
        // find the king
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(piece instanceof ChessPieceKing){
                    // Check if checked
                    ((ChessPieceKing) piece).setChecked(FutureStatesGen.isChecked(this, piece));
                }
            }
        }
    }
    
    //Do the moves, controll if they CAN be made somwhere else
    public void movePiece(Coordinates startPos, Coordinates dest){
        
        //help for notation
        boolean takes = false;
        boolean castleQ = false;
        boolean castleK = false;
        boolean promotion = false;
        
        //trying to move nonexistent piece
        if(this.getChessPieceAtCoordinate(startPos) == null){
            return;
        }
        ChessPiece movingPiece = this.getChessPieceAtCoordinate(startPos);
        
        //enpassant
        if (movingPiece instanceof ChessPiecePawn && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece) != null && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece).equals(dest)) {
            //remove enPassanted pawn
            Coordinates enemyPawnCoord = new Coordinates(dest.getX() - movingPiece.getMoveSet().getMoveVectors()[0].getX(),dest.getY());
            this.getChessPieceAtCoordinate(enemyPawnCoord).setPosition(new Coordinates(-1,-1));
            this.board[enemyPawnCoord.getX()][enemyPawnCoord.getY()] = null; 
            takes = true;
        }
        
        //move rook for castles
        if (movingPiece instanceof ChessPieceKing && !FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece).isEmpty()) {
            ArrayList<Coordinates> castleLocations = FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece);
            int distanceTraveledByKing = 2;
            
            //TODO - NAIVE IMPLEMENTATION REDO
            for(Coordinates newKingCoord : castleLocations){
                if(newKingCoord.equals(dest)){
                    
                    //king side
                    if(dest.getY() == movingPiece.getPosition().getY() + distanceTraveledByKing){
                        int rookY = 7;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() + distanceTraveledByKing - 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);
                        castleK = true;
                    }
                    
                    //Queen side
                    if(dest.getY() == movingPiece.getPosition().getY() - distanceTraveledByKing){
                        int rookY = 0;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() - distanceTraveledByKing + 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);
                        castleQ = true;
                    }
                }
            }  
        }
        //moving to an empty square
        
        if(this.getChessPieceAtCoordinate(dest) == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        
        //moving to an occupied square
        else{
            if (this.getChessPieceAtCoordinate(dest).getColor() != movingPiece.getColor()) {
                this.getChessPieceAtCoordinate(dest).setPosition(new Coordinates(-1,-1));
                this.board[dest.getX()][dest.getY()] = movingPiece;
                movingPiece.setPosition(dest);
                this.board[startPos.getX()][startPos.getY()] = null;
                takes = true;
            }
        }
        
        //Promotion
        if (movingPiece instanceof ChessPiecePawn && (movingPiece.getPosition().getX() == this.size -1 || movingPiece.getPosition().getX() == 0)) {
            ChessPiecePawn promotable = (ChessPiecePawn) movingPiece;
            promotable.setCanBePromoted(true);
            promotion = true;
        }
        //update history for enPassants and castle calculations
        history.addEntry(movingPiece, dest, startPos);
        updatePieces();
        
        //updatePgnNotation
        history.updatePgnNotation(this, takes, castleQ, castleK, promotion);
    }
    
    //for generating future board states and recursion control
    public void movePieceWithoutUpdatingLegalMoves(Coordinates startPos, Coordinates dest){

        //trying to move nonexistent piece
        if(this.getChessPieceAtCoordinate(startPos) == null){
            return;
        }
        ChessPiece movingPiece = this.getChessPieceAtCoordinate(startPos);
        
        //enpassant
        if (movingPiece instanceof ChessPiecePawn && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece) != null && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece).equals(dest)) {
            //remove enPassanted pawn
            Coordinates enemyPawnCoord = new Coordinates(dest.getX() - movingPiece.getMoveSet().getMoveVectors()[0].getX(),dest.getY());
            this.getChessPieceAtCoordinate(enemyPawnCoord).setPosition(new Coordinates(-1,-1));
            this.board[enemyPawnCoord.getX()][enemyPawnCoord.getY()] = null; 
        }
        
        //move rook for castles
        if (movingPiece instanceof ChessPieceKing && !FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece).isEmpty()) {
            ArrayList<Coordinates> castleLocations = FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece);
            int distanceTraveledByKing = 2;
            
            //TODO - NAIVE IMPLEMENTATION REDO
            for(Coordinates newKingCoord : castleLocations){
                if(newKingCoord.equals(dest)){
                    
                    //queen side
                    if(dest.getY() == movingPiece.getPosition().getY() + distanceTraveledByKing){
                        int rookY = 7;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() + distanceTraveledByKing - 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);

                    }
                    
                    //king side
                    if(dest.getY() == movingPiece.getPosition().getY() - distanceTraveledByKing){
                        int rookY = 0;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() - distanceTraveledByKing + 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);

                    }
                }
            }  
        }
        //moving to an empty square
        
        if(this.getChessPieceAtCoordinate(dest) == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        
        //moving to an occupied square
        else{
            if (this.getChessPieceAtCoordinate(dest).getColor() != movingPiece.getColor()) {
                this.getChessPieceAtCoordinate(dest).setPosition(new Coordinates(-1,-1));
                this.board[dest.getX()][dest.getY()] = movingPiece;
                movingPiece.setPosition(dest);
                this.board[startPos.getX()][startPos.getY()] = null;

            }
        }
        
        //Promotion
        if (movingPiece instanceof ChessPiecePawn && (movingPiece.getPosition().getX() == this.size -1 || movingPiece.getPosition().getX() == 0)) {
            ChessPiecePawn promotable = (ChessPiecePawn) movingPiece;
            promotable.setCanBePromoted(true);

        }
        //update history for enPassants and castle calculations
        history.addEntry(movingPiece, dest, startPos);
        
        updateAvailableMoves();
        updateChecks();
    }
    
    public ChessPiece getChessPieceAtPosition(int x, int y){
        //check index out of bounds
        if(x >= this.size || y >= this.size || y<0 || x<0){
            return null;
        }
        else{
            return this.board[x][y];
        }
    }
    
    public ChessPiece promotion(ChessPiece promoted, ChessPiece newRank){
        Coordinates coords = promoted.getPosition();
        int id = promoted.getId();
        ChessPiece promotedPiece = new ChessPieceImpl(newRank);
        promotedPiece.setId(id);
        promotedPiece.setPosition(coords);
        promotedPiece.setColor(promoted.getColor());
        this.getBoard()[coords.getX()][coords.getY()] = promotedPiece;
        
        promoted = promotedPiece;
        
        this.updatePieces();
        logger.log(Level.FINER, "PROMOTION TO {0} {1}", new Object[]{coords, promoted.getName()});
        //update history
        String movePgn = this.getHistory().getMovesInPgn().get(this.getHistory().getMovesInPgn().size() - 1);
        movePgn += PgnParser.findPgnNotationForPiece(promoted.getName());
        this.getHistory().getMovesInPgn().remove(this.getHistory().getMovesInPgn().size() - 1);
        this.getHistory().getMovesInPgn().add(movePgn);
        return promotedPiece;
    }
    
    private ArrayList<ChessPiece> findUniques(ChessPiece[][] board){
        ArrayList<ChessPiece> unique = new ArrayList<>();
        for(ChessPiece[] row : board){
            for(ChessPiece piece : row){
                if (piece == null) {
                    continue;
                }
                
                //search prior occurences
                boolean contains = true;
                for(ChessPiece uniquePiece : unique){
                    if(piece.getName().equals(uniquePiece.getName())){
                        contains = false;
                    }
                }
                //dont want kings or pawns
                if(contains && piece instanceof ChessPieceImpl){
                    unique.add(piece);
                }
            }
        }
        return unique;
    }
    
    public ChessPiece getChessPieceAtCoordinate(Coordinates coord){
        return this.getChessPieceAtPosition(coord.getX(), coord.getY());
    }
    
    public int getSize() {
        return size;
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public TurnHistory getHistory() {
        return history;
    }
    
    public ArrayList<ChessPiece> getUniquePieces() {
        return uniquePieces;
    }
    
}
