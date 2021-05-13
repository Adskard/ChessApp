
package cz.cvut.fel.skardada.chess.ChessPieces;

import cz.cvut.fel.skardada.chess.ChessPieceImpl;
import cz.cvut.fel.skardada.chess.Coordinates;
import cz.cvut.fel.skardada.chess.MoveSet;
import cz.cvut.fel.skardada.chess.PlayerColors;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.cli.*;

/**
 * Serializes ChessPieceImpl for given inputs and saves them into resources
 * @author Adam Škarda
 */
public class ChessPieceMaker {

    /**
     *
     * @param args
     */
    public static void main(String[] args){
        //variables for chess piece creation
        ChessPieceImpl piece;
        String pieceName;
        int maximumVectors = 20;
        int moveSetDistance = -1;
        Coordinates[] moveSetVectors = null;
        
        //create command line options
        Options options = new Options();
        CommandLineParser parser = new BasicParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd = null;
        
        //name of the piece - should start with black or white
        Option name = new Option("n","name", true, "name of created piece, must strat with black or white : string");
        name.setRequired(true);
        options.addOption(name);
        
        //path to piece icon img file
        Option icon = new Option("i", "image", true, "path to chesspiece icon, it is recommended to use classpath to resources folder");
        icon.setRequired(true);
        options.addOption(icon);
        
        //movement vectors for piece
        Option moveVectors = new Option("v", "vectors", true, "vectors of movement : int,int");
        moveVectors.setRequired(true);
        moveVectors.setArgs(maximumVectors);
        options.addOption(moveVectors);
        
        //move distance of piece
        Option moveDistance = new Option("d", "distance", true, "distance that a piece can travel : int");
        moveDistance.setRequired(true);
        options.addOption(moveDistance);
        
        //parse commandline 
        try{
            cmd = parser.parse(options, args);
        }
        
        catch(ParseException ex){
            System.err.println("Parse Error " + ex.getMessage());
            help.printHelp("ChessPieceMaker", options);
            System.exit(-1);
        }
        
        //get name
        pieceName = cmd.getOptionValue(name.getOpt());
        
        //get color
        String colorInput = pieceName.substring(0, 5); //white and black
        PlayerColors color = PlayerColors.white;
        if(colorInput.equals("white")){
            color = PlayerColors.white;
        }
        if(colorInput.equals("black")){
            color = PlayerColors.black;
        }
        
        // get vectors and distance
        String[] argMoves = cmd.getOptionValues(moveVectors.getOpt());
        try{
            moveSetDistance = Integer.parseInt(cmd.getOptionValue(moveDistance.getOpt()));
            if(moveSetDistance == -1){
                moveSetDistance = Integer.MAX_VALUE;
            }
        }
        
        catch(NumberFormatException ex){
            System.err.println("distance is not a number " + ex.getMessage());
            help.printHelp("ChessPieceMaker", options);
        }
        
        try{
            moveSetVectors = new Coordinates[argMoves.length];
            for (int i = 0; i < argMoves.length; i++) {
                String[] parts = argMoves[i].split(",");
                moveSetVectors[i] = new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
        }
        
        catch(IndexOutOfBoundsException ex){
            System.err.println("wrong number of vector coordinates " + ex.getMessage());
            help.printHelp("ChessPieceMaker", options);
        }
        
        catch(NumberFormatException ex){
            System.err.println("Vector is not a number " + ex.getMessage());
            help.printHelp("ChessPieceMaker", options);
        }   
        String imagePath = cmd.getOptionValue(icon.getOpt());
        
        //create the piece
        piece = new ChessPieceImpl(pieceName, new Coordinates(-1,-1), color, new MoveSet(moveSetVectors, moveSetDistance), imagePath);
        
        //save path - default resources folder - SUBSTITUTE FOR YOUR OWN PATH if there is nullPointerError while serializing
        String savedPiecesPath = ".\\src\\main\\resources\\";
        
        
        //serialize created piece
        try{
            FileOutputStream chessSer = new FileOutputStream(savedPiecesPath + "piece_" + pieceName +  ".ser");
            ObjectOutputStream out = new ObjectOutputStream(chessSer);
            out.writeObject(piece);
            out.close();
            chessSer.close();
            System.out.println("Data has been succesfully serialized");
        }
        
        catch(Exception ex){
            System.err.println("Could not serialize" + ex.getMessage());
            System.exit(-1);
        }
    }
    
}
