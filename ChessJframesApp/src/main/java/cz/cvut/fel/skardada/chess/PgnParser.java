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

import java.util.logging.*;
public class PgnParser {
    private static Logger logger = Logger.getLogger(PgnParser.class.getName());
    
    private Game parse(){
        return null;
    }
    
    public Game parseFromFile(String path){
        
        return parse();
    }
    
    public void saveGameAsPgn(String path){
        
    }
    
    public static String enocodeMoveToPgn(ChessPiece movedPiece, Coordinates dest, Coordinates start, Board board, boolean takes, boolean castleQ, boolean castleK, boolean promotion){
        String pgnNotation = "";
        String pieceName = movedPiece.getName().toLowerCase();
        String pieceNotation = findPgnNotationForPiece(pieceName);
        if (castleK) {
            return "O-O";
        }
        if(castleQ){
            return "O-O-O";
        }
        if(promotion){
            String promotedTo = findPgnNotationForPiece(board.getChessPieceAtCoordinate(dest).getName().toLowerCase());
            logger.log(Level.FINEST, "Pawn promoted to {0}", board.getChessPieceAtCoordinate(dest).getName());
            return dest.toString() + "=";
        }
        
        pgnNotation += pieceNotation;
        
        if(pieceName.contains("pawn") && takes){
            pgnNotation += start.getFile();
        }
        
        //ambiguity elimination
        boolean ambiguityPrevented = false;
        for(ChessPiece[] row : board.getBoard()){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(!piece.canGetTo(dest) || ambiguityPrevented){
                    continue;
                }
                logger.log(Level.FINEST, "abiguity prevention - comparedpiece: {0}, {1}, {2} -  ids equal:{4}", new Object[] {piece.getName(), piece.getId(), piece.getPosition(), piece.equals(movedPiece)});
                if(((piece.getName().toLowerCase()).equals(pieceName)) && !piece.equals(movedPiece)){
                    logger.log(Level.FINER, "ambiguity prevented");
                    if(pgnNotation.length()<2 && !(piece instanceof ChessPiecePawn)){
                        pgnNotation += start.getFile();
                    }
                    if(piece.getPosition().getY() == start.getY()){
                        pgnNotation += start.getRank();
                    }
                    
                }
            }
        }
        
        //piece taken notation
        if(takes){
            pgnNotation += "x";
        }
        
        //destination square notation
        
        pgnNotation += dest.toString();
        
        //checks
        for (ChessPiece[] row : board.getBoard()) {
            for(ChessPiece piece : row){
                if(piece instanceof ChessPieceKing){
                    ChessPieceKing king = (ChessPieceKing) piece;
                    if(king.isChecked()){
                        pgnNotation += "+";
                    }
                }
            }
        }
        return pgnNotation;
    }
    
    private String encodeGameToPgn(Game game){
        if(!game.getStyle().getName().equals("standard")){
            return null;
        }
        return "";
    }
    
    public static String findPgnNotationForPiece(String pieceNameOrg){
        String pieceName = pieceNameOrg.toLowerCase();
        if(pieceName.contains("bishop")){
            return "B";
        }
        if(pieceName.contains("knight")){
            return "N";
        }
        if(pieceName.contains("rook")){
            return "R";
        }
        if(pieceName.contains("queen")){
            return "Q";
        }
        if(pieceName.contains("king")){
            return "K";
        }
        return "";
    }
}
