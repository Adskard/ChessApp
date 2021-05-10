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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.*;
public class PgnParser {
    private static final Logger logger = Logger.getLogger(PgnParser.class.getName());
    
    
    public static String parsePlayerName(PlayerColors color, String pathToSave) throws FileNotFoundException, Exception{
        File savedGame = new File(pathToSave);
        Scanner input = new Scanner(savedGame);
        //2 for now can be changed in future iterations based on style
        String searchedColor = color.toString().substring(0,1).toUpperCase() + color.toString().substring(1);
        while(input.hasNextLine()){
            String line = input.nextLine();
            
            //searching throught tags - tags always start with char "["
            if (line.contains(searchedColor)) {
                int offset = line.indexOf("\"") + 1;
                int endOffset = line.lastIndexOf("\"");
                return line.substring(offset, endOffset);
            }
        }
        throw new Exception("Color: " + color + " not present in this save:" + pathToSave);
    }
    
    public static long[] parseTime(String playerName, String pathToSave) throws FileNotFoundException{
        File savedGame = new File(pathToSave);
        Scanner input = new Scanner(savedGame);
        long[] timeParts = new long[2];
        while(input.hasNextLine()){
            String line = input.nextLine();
            
            //searching throught tags - tags always start with char "["
            if (line.startsWith("[" + playerName)) {
                String[] parts = line.split("\"");
                timeParts[0] = Long.parseLong(parts[1].strip());
                timeParts[1] = Long.parseLong(parts[3].strip());
                return timeParts;
            }
        }
        // if time is not specified get unlimited time!
        return new long[] {Long.MAX_VALUE, Long.MAX_VALUE};
    } 
    
    
    public static String[] getIndividualMoves(String pathToSave) throws FileNotFoundException, Exception{
        File savedGame = new File(pathToSave);
        Scanner input = new Scanner(savedGame);
        String gameNotation = "";
        boolean startFound = false;
        boolean ignoreComment = false;
        while(input.hasNext()){
            String token = input.next();
            if (token.startsWith("{") || ignoreComment) {
                ignoreComment = true;
                if(token.endsWith("}")){
                    ignoreComment = false;
                }
                continue;
            }
            if(token.equals("1-0") || token.equals("0-1") || token.equals("1/2-1/2")){
                continue;
            }
            if(token.startsWith("1.") || startFound){
                startFound = true;
                gameNotation += token;
                gameNotation += " ";
            }
        }
        if (!startFound) {
            throw new Exception("Pgn parse Error: first turn not found");
        }
        
        //delete unnecessary 1. and 2. at the move beginning
        String[] movesWithTurns = gameNotation.split(" ");
        ArrayList<String> movesWithoutTurns = new ArrayList<>();
        for (int i = 0; i < movesWithTurns.length; i++) {
            if((i % 2 == 1)){
                movesWithoutTurns.add(movesWithTurns[i]);
            }
            else{
                movesWithoutTurns.add(movesWithTurns[i].substring(movesWithTurns[i].indexOf(".") + 1 ));
            }
        }
        String[] output = new String[movesWithoutTurns.size()];
        output = movesWithoutTurns.toArray(output);
        return output;
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
                notation += (i/2 + 1) + ".";
            }
            notation += gameNotation.get(i);
            notation += " ";
        }
        
        return notation;
    }
    
    public static String saveGameInProgress(Game game){
        String notation = "";
        //notation for custom game loading
        notation += "[Style \"" + game.getStyle().getName() + "\"]" + System.lineSeparator();
        
        //saving players
        notation += "[Round \"" + game.getGameBoard().getHistory().getTurn() + "\"]" + System.lineSeparator();
        for (Player p : game.getPlayers()) {
            notation +="[" + p.getColor().toString().substring(0,1).toUpperCase() + p.getColor().toString().substring(1) + " \"" + p.getName() +  "\"]" + System.lineSeparator();
        }
        
        //saving times for players
        for (Player p : game.getPlayers()) {
            notation +="[" + p.getName() + " \"" +  p.getClock().getRemainingTime() + "\" " +"\"" + p.getClock().getIncrement() +"\"]" +System.lineSeparator();
        }
        
        //game
        notation += System.lineSeparator();
        ArrayList<String> gameNotation = game.getGameBoard().getHistory().getMovesInPgn();
        for (int i = 0; i < gameNotation.size(); i++) {
            if(i % 2 == 0){
                notation += (i/2 + 1) + ".";
            }
            notation += gameNotation.get(i);
            notation += " ";
        }
        
        return notation;
    }

    public static String isThisMoveAPromotion(String move){
        if (move.contains("=")) {
            return move.substring(move.indexOf("=") + 1, move.indexOf("=") + 2);
        }
        return null;
    }
    
    public static boolean isThisMoveAQueenCastle(String move){
        return move.equals("O-O-O");
    }
    
    public static boolean isThisMoveAKingCastle(String move){
        return move.equals("O-O");
    }
    
    public static String parseMovedPiece(String move){
        String pieceName = findPieceForPgnNotation(move);
        if(pieceName.equals("")){
            pieceName = "Pawn";
        }
        return pieceName;
    }
    
    public static Coordinates getDestination(String move) throws Exception{
        for (int i = move.length() - 1; i > 0; i--) {
            if (Character.isDigit(move.charAt(i))) {
                return new Coordinates(move.substring(i-1,i+1));
            }
        }
        throw new Exception("Move parse error: destination not found for move: " + move);
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
    
    public static String findPgnNotationForPiece(String move){
        String pieceName = move.toLowerCase();
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
    
    public static String findPieceForPgnNotation(String pgn){
        if(pgn.startsWith("B")){
            return "Bishop";
        }
        if(pgn.startsWith("N")){
            return "Knight";
        }
        if(pgn.startsWith("R")){
            return "Rook";
        }
        if(pgn.startsWith("Q")){
            return "Queen";
        }
        if(pgn.startsWith("K")){
            return "King";
        }
        return "";
    }
        
}
