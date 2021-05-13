
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
/**
 * PlayerComputer is an implementation of abstract class Player and is the main class for this computer algorithm decision making.
 * For now decisions are pseudo-random.
 * @author Adam Škarda
 */
public class Player_Computer extends Player{
    
    /**
     *
     * @param name Computer player name
     * @param color Computer player color
     * @param ownPieces Computer player pieces, ChessPieces of the same color - can be moved by this Player
     * @param clock Computer player time controll
     */
    public Player_Computer(String name, PlayerColors color, ArrayList<ChessPiece> ownPieces, ChessClock clock) {
        super(name, color, ownPieces, clock);
    }

    /**
     * Uses Board.movePiece() method to make direct changes to the game board.
     * Change being this player move.
     * @see Board#movePiece(cz.cvut.fel.skardada.chess.Coordinates, cz.cvut.fel.skardada.chess.Coordinates)
     * @param currentBoard current state of the game board
     * 
     */
    @Override
    public void makeMove(Board currentBoard){
        System.out.println("making move");
        //no available pieces to play
        if(this.getOwnPieces().isEmpty()){
            return;
        }
        //Choose piece to play
        ChessPiece piece = getRandomPiece();
        while(piece.getLegalMoves().isEmpty()){
            System.out.println("wrong piece" +piece.getName());
            piece = getRandomPiece();
        }
        System.out.println("piece chosen");
        
        //get source and destination for the move
        Coordinates source = piece.getPosition();
        Coordinates dest = getRandomDest(piece);
        
        System.out.println(piece.getName() + source  + " " + dest);
        
        //move the piece
        currentBoard.movePiece(source, dest);
        
        //check promotion
        if(piece instanceof ChessPiecePawn){
            ChessPiecePawn pawn = (ChessPiecePawn) piece;
            if(pawn.isCanBePromoted()){
                ChessPiece newRank = getRandomUnique(currentBoard);
                while(newRank.getColor() != pawn.getColor()){
                    newRank = getRandomUnique(currentBoard);
                }
                ChessPiece promoted = currentBoard.promotion(pawn, newRank);
                this.getOwnPieces().remove(pawn);
                this.getOwnPieces().add(promoted);
            }
        }
        
        //indicate end of turn to the controller
        this.setFinishedTurn(true);
    }
    /**
     * Method for choosing a random ChessPiece
     * @return random ChessPiece from OwnPieces
     */
    private ChessPiece getRandomPiece(){
        return this.getOwnPieces().get(ThreadLocalRandom.current().nextInt(0, this.getOwnPieces().size()));
    }
    
    /**
     * Method for choosing a random unique ChessPiece for promotion
     * @param board current board with list of unique ChessPieces
     * @return random ChessPiece from Unique pieces of Board class
     */
    private ChessPiece getRandomUnique(Board board){
        return board.getUniquePieces().get(ThreadLocalRandom.current().nextInt(0, board.getUniquePieces().size())); 
    }
    
    /**
     * Method for choosing random destination for a given piece
     * @param piece A chess piece that is to be moved
     * @return random Coordinate of 
     */
    private Coordinates getRandomDest(ChessPiece piece){
        return piece.getLegalMoves().get(ThreadLocalRandom.current().nextInt(0, piece.getLegalMoves().size()));
    }

}
