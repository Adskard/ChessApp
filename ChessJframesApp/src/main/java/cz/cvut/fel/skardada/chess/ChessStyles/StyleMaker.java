/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess.ChessStyles;

/**
 *
 * @author Adam Škarda
 */
import cz.cvut.fel.skardada.chess.ChessPiece;
import cz.cvut.fel.skardada.chess.ChessPieceImpl;
import cz.cvut.fel.skardada.chess.ChessPieceKing;
import cz.cvut.fel.skardada.chess.ChessPiecePawn;
import cz.cvut.fel.skardada.chess.ChessStyle;
import cz.cvut.fel.skardada.chess.Coordinates;
import cz.cvut.fel.skardada.chess.IncorrectLenghtException;
import cz.cvut.fel.skardada.chess.MoveSet;
import cz.cvut.fel.skardada.chess.PlayerColors;
import java.io.*;
import org.apache.commons.cli.*;
public class StyleMaker {

    public static void main(String[] args){
        ChessStyle style = null;
        String name;
        int size = 0;
        PlayerColors color;
        ChessPiece[][] board;
        
        // setting up options
        Options options = new Options();
        CommandLine cmd = null;
        CommandLineParser parser = new BasicParser();
        HelpFormatter help = new HelpFormatter();
        
        Option nameOption = new Option("n", "name", true, "set style name : string");
        nameOption.setRequired(true);
        options.addOption(nameOption);
        
        Option sizeOption = new Option("s", "size", true, "set board size for this style : int");
        sizeOption.setRequired(true);
        options.addOption(sizeOption);
        
        Option positioningOption = new Option("p", "position", true, "setting positions for individual pieces  from index 0 to sizeIndex - pieceName_numberOfPieces - blank is -1 * how many consecutive identical positions, max 128 positions");
        positioningOption.setRequired(true);
        positioningOption.setArgs(128);
        options.addOption(positioningOption);
        
        //interpreting input
        try{
            cmd = parser.parse(options, args);
        }
        
        catch(ParseException ex){
            System.err.println("Parse Error " + ex.getMessage());
            help.printHelp("StyleMaker", options);
            System.exit(-1);
        }
        
        name = cmd.getOptionValue(nameOption.getOpt());
        
        try{
            size = Integer.parseInt(cmd.getOptionValue(sizeOption.getOpt()));
        }
        catch(NumberFormatException ex){
            System.err.println("Size is not a number " + ex.getMessage());
            help.printHelp("StyleMaker", options);
            System.exit(-1);
        }
        board = new ChessPiece[size][size];
        
        //setting up chess pieces for usage
        String styleDirectory = ".\\src\\main\\java\\cz\\cvut\\fel\\skardada\\chess\\ChessStyles\\";
        String pieceDirectory = ".\\src\\main\\java\\cz\\cvut\\fel\\skardada\\chess\\ChessPieces\\";
        String piecePrefix = "piece_";
        String pieceSuffix = ".ser";
        String[] availablePieces = null;
        
        File[] availablePieceFiles = null;
        try{
            File piecesDir = new File(pieceDirectory);
            availablePieceFiles = piecesDir.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name)
                    {
                       return name.startsWith(piecePrefix) && name.endsWith(pieceSuffix);
                    }
            }); 
        }
        catch(IOError ex){
            System.err.println("Chess pieces directory error " + ex.getMessage());
            System.exit(-1);
        }
        
        //Filling the chess board
        String[] piecePositions = cmd.getOptionValues(positioningOption.getOpt());
        int colorOffset = 0;
        int pieceOffset = 1;
        int numberOfIdenticalPieces = 0;
        String currPos = null;
        String currPiece = null;
        String currMultiple = null;
        int currPosIndex = 0;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                
                if (numberOfIdenticalPieces <= 0) {
                    try{
                       currPos = piecePositions[currPosIndex++]; 
                    }
                    catch(ArrayIndexOutOfBoundsException ex){
                        System.err.println("Didnt fill the whole board " + ex.getMessage());
                        System.exit(-1);
                    }
                    
                    currPiece = currPos.split("\\*")[0];
                    currMultiple = currPos.split("\\*")[1];
                    try{
                        numberOfIdenticalPieces = Integer.parseInt(currMultiple);
                    }
                    catch(NumberFormatException ex){
                        System.err.println("Must multiply by an Integer " + ex.getMessage());
                        System.exit(-1);
                    }
                }
                if(currPos.substring(colorOffset,pieceOffset).equals("w")){
                    color = PlayerColors.white;
                }
                else{
                    color = PlayerColors.black;
                }
                String pieceName = currPiece.substring(pieceOffset);
                numberOfIdenticalPieces--;
                switch(pieceName){
                    case "King":
                        board[i][j]=new ChessPieceKing(new Coordinates(i,j),color);
                        break;
                    case "Pawn":
                        board[i][j]=new ChessPiecePawn(new Coordinates(i,j),color);
                        break;
                    case "-1":
                        board[i][j] = null;
                        break;
                    default:
                        for (File currFile : availablePieceFiles) {
                            String nameFromInput = currFile.getName().substring(
                                piecePrefix.length(), currFile.getName().length() - pieceSuffix.length());
                            if(pieceName.equals(nameFromInput)){
                                //deserialize
                                try{
                                    FileInputStream file = new FileInputStream(currFile.getAbsolutePath());
                                    ObjectInputStream in = new ObjectInputStream(file);
                                    board[i][j] = (ChessPiece) in.readObject();
                                    in.close();
                                    file.close();
                                }
                                catch(IOException ex){
                                    System.err.print("Could not read resource " + ex.getMessage());
                                    return;
                                }
                                catch(ClassNotFoundException ex){
                                    System.err.print("Could not find class " + ex.getMessage());
                                    return;
                                }
                            }
                        }
                        break;
                }
            }
        }
        try{
            style = new ChessStyle(size, board, name);
        }
        catch(IncorrectLenghtException ex){
            System.err.println("Could not create chess style " + ex.getMessage());
            System.exit(-1);
        }
        try{
            FileOutputStream styleSer = new FileOutputStream(styleDirectory + "style_" +name +  ".ser");
            ObjectOutputStream out = new ObjectOutputStream(styleSer);
            out.writeObject(style);
            out.close();
            styleSer.close();
            System.out.println("Data has been succesfully serialized");
        }
        catch(IOException ex){
            System.err.println("Could not serialize" + ex.getMessage());
            System.exit(-1);
        }
    }
    
}
