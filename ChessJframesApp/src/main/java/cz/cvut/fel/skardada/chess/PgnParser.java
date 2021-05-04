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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
public class PgnParser {
    private static final Logger logger = Logger.getLogger(PgnParser.class.getName());
    
    private Game parse(){
        return null;
    }
    
    public Game parseFromFile(String path){
        
        return parse();
    }
    
    public void saveGameAsPgn(String path){
        
    }
    
    public static String encodeMoveToPgn(ChessPiece movedPiece, Coordinates dest, Coordinates start, Board board, boolean takes, boolean castleQ, boolean castleK, boolean promotion){
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
            return dest.toString() + "=";
            //rest of the promotion is done during the promotion in Board:promotion()
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
    
    public static String encodeGameToPgn(Game game, PlayerColors winner){   
        //tags
        String result = "1/2-1/2";
        if (winner == PlayerColors.black) {
            result = "0-1";
        }
        if (winner == PlayerColors.white){
            result = "1-0";
        }
        String notation = "";
        notation += "[Round \"" + game.getGameBoard().getHistory().getTurn() + "\"]" + System.lineSeparator();
        for (Player p : game.getPlayers()) {
            notation +="[" +p.getColor().toString().substring(0,1).toUpperCase() + p.getColor().toString().substring(1) + " \"" + p.getName() +  "\"]" + System.lineSeparator();
        }
        notation += "[Result \"" + result +"\"]" + System.lineSeparator();
        
        //game
        notation += System.lineSeparator();
        ArrayList<String> gameNotation = game.getGameBoard().getHistory().getMovesInPgn();
        for (int i = 0; i < gameNotation.size(); i++) {
            if(i % 2 == 0){
                notation += (i/2 + 1) + ". ";
            }
            notation += gameNotation.get(i);
            notation += " ";
        }
        
        return notation;
    }
    
    public static String saveGameInProgress(Game game){
        String notation = "";
        for (Player p : game.getPlayers()) {
            notation +="[" + p.getName() + " \"" +  p.getClock().getRemainingTime() + "\" " +"\"" + p.getClock().getIncrement() +"\"]" +System.lineSeparator();
        }
        notation += "[Round \"" + game.getGameBoard().getHistory().getTurn() + "\"]" + System.lineSeparator();
        for (Player p : game.getPlayers()) {
            notation +="[" + p.getColor().toString().substring(0,1).toUpperCase() + p.getColor().toString().substring(1) + " \"" + p.getName() +  "\"]" + System.lineSeparator();
        }
        
        //game
        notation += System.lineSeparator();
        ArrayList<String> gameNotation = game.getGameBoard().getHistory().getMovesInPgn();
        for (int i = 0; i < gameNotation.size(); i++) {
            if(i % 2 == 0){
                notation += (i/2 + 1) + ". ";
            }
            notation += gameNotation.get(i);
            notation += " ";
        }
        
        return notation;
    }
    

    
    private static TurnHistory convertPgnToTurnHistory(String pgn){
        TurnHistory history = new TurnHistory();
        
        return history;
    }
    
    public static void savePgnGameToFile(String path, Game game){
        try {
            File output = new File(path);
            output.createNewFile();
            FileWriter writer = new FileWriter(output);
            writer.write(saveGameInProgress(game));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PgnParser.class.getName()).log(Level.SEVERE, "Could not save this game to file: {0}", path);
        }
    }
    
    public static void exportPgnGameToFile(String path, Game game, PlayerColors winner){
        try {
            File output = new File(path);
            output.createNewFile();
            FileWriter writer = new FileWriter(output);
            writer.write(encodeGameToPgn(game, winner));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PgnParser.class.getName()).log(Level.SEVERE, "Could not save this game to file: {0}", path);
        }
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
