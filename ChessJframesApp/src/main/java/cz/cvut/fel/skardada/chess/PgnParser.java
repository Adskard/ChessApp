
package cz.cvut.fel.skardada.chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

/**
 * PgnParser is a class responsible for parsing imported Pgn games, loaded games,
 * converting moved and model infomation to pgn notation for exporting and importing games.
 * @author Adam Škarda
 */
public class PgnParser {
    private static final Logger logger = Logger.getLogger(PgnParser.class.getName());
    
    /**
    * Parses player name from a given file
    * @param  color color of the player
    * @param  pathToSave path to file with the player
    * @return returns player name as string
    * @throws Exception exception if player of given color was not found in the file
    * @throws FileNotFoundException exception if file for a given path doesnt exist
    */
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
    
    /**
    * Function that parses time from a file on a given path
    * @param  playerName name of the player whose clock we are searching for
    * @param  pathToSave path to the saved game in pgn - should be a save from this app (tagg problems)
    * @return returns array of longs for clock initialization, array lenght is 2, at index 0 is remaining time and at index 1 is clock increment
    * @throws FileNotFoundException throws exception if a file was not found for a given path
    */
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
    
    /**
    * Function that parses individial moves from pgn notation
    * @param  pathToSave path to the saved pgn notation file
    * @return returns array of moves without turn notation and result
    * @throws Exception exception if the parse did not find move beggining in the pgn file - moves begin with "1."
    * @throws FileNotFoundException exception if the file for a give path does not exist
    */
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
    
    /**
     * Encodes move to pgn notation
     * @param movedPiece ChessPiece that was moved
     * @param dest Where the piece was moved
     * @param start Where the moved piece started
     * @param board Current state of the board - for preventing ambiguity errors
     * @param takes If a piece was taken of the board 
     * @param castleQ If the move was Queen side castle
     * @param castleK If the move was King side castle
     * @param promotion If the move resulted in a promotion
     * @return A string representation of a chess move
     */
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
    
    /**
     * Parses game result
     * @param pathToSave path to the pgn file 
     * @throws FileNotFoundException throws exception if a file was not found for a given path
     * @return returns string with the game result or null if the game result was not found
     */
    public static String parseGameResult(String pathToSave) throws FileNotFoundException{
        File savedGame = new File(pathToSave);
        Scanner input = new Scanner(savedGame);
        String result = "";
        while(input.hasNextLine()) {
            String line = input.nextLine();
            if(line.contains("Result")){
                return line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
            }
        }
        return null;
    }
    
    /**
     * Encodes a Game to a Pgn notation.
     * @param game Game to be encoded to Pgn.
     * @param winner Who won the encoded games
     * @return String representation of the encoded game.
     */
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
    
    /**
     * Converts saved game to pgn notation and adds tags for player time management
     * @param game game to be saved
     * @return notation of the saved game as a pseudo pgn file - time tags are not strict pgn
     */
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
            notation +="[" + p.getName() + " \"" +  p.getChessClock().getRemainingTime() + "\" " +"\"" + p.getChessClock().getIncrement() +"\"]" +System.lineSeparator();
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

    /**
     * Find out if Pgn move is a promotion
     * @param move pgn representation of the move
     * @return true if the move is a promotion - contains "=" token
     */
    public static String isThisMoveAPromotion(String move){
        if (move.contains("=")) {
            return move.substring(move.indexOf("=") + 1, move.indexOf("=") + 2);
        }
        return null;
    }
    
    /**
     * Find out if a pgn move is a queen side castle
     * @param move pgn representation of the move
     * @return true if the move is a queen side castle - the move is O-O-O
     */
    public static boolean isThisMoveAQueenCastle(String move){
        return move.equals("O-O-O");
    }
    
    /**
     * Find out if a pgn move is a king side castle
     * @param move pgn representation of the move
     * @return true if the move is a king side castle - the move is O-O
     */
    public static boolean isThisMoveAKingCastle(String move){
        return move.equals("O-O");
    }
    
    /**
     * Parses piece name for a given pgn move
     * @param move move in pgn notation
     * @return returns name of the moved piece as a string
     */
    public static String parseMovedPiece(String move){
        String pieceName = findPieceForPgnNotation(move);
        if(pieceName.equals("")){
            pieceName = "Pawn";
        }
        return pieceName;
    }
    
    /**
     * Gets destination for a given move in pgn format
     * @param move move in pgn format
     * @return Coordinates of the destination square
     * @throws Exception if the destination was not found for a given move
     */
    public static Coordinates getDestination(String move) throws Exception{
        for (int i = move.length() - 1; i > 0; i--) {
            if (Character.isDigit(move.charAt(i))) {
                return new Coordinates(move.substring(i-1,i+1));
            }
        }
        throw new Exception("Move parse error: destination not found for move: " + move);
    }
    
    /**
     * Saves a gaem to a file in a pgn notation, creates a new file for a given path
     * @param path path to the new file
     * @param game game to be saved 
     */
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
    
    /**
     * Exports a game to a file in a pgn notation, creates new file for a given path
     * @param path path to the new file
     * @param game game to be exported to pgn
     * @param winner winner of the exported game
     */
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
    
    /**
     * Finds pgn notation fo a given piece name
     * @param piece name of a chess piece
     * @return return english pgn piece notation, return "" if the notation is not found or is pawn
     */
    public static String findPgnNotationForPiece(String piece){
        String pieceName = piece.toLowerCase();
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
    
    /**
     * Gets piece name base on pgn notation
     * @param pgn pgn move notation
     * @return name of moved piece, return "" if the notation is not found or is pawn
     */
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
