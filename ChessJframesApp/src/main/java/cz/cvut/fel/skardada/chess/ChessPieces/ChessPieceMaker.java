/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess.ChessPieces;

import cz.cvut.fel.skardada.chess.ChessPieceImpl;
import cz.cvut.fel.skardada.chess.Coordinates;
import cz.cvut.fel.skardada.chess.MoveSet;
import cz.cvut.fel.skardada.chess.PlayerColors;
import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.commons.cli.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
/**
 *
 * @author Adam Škarda
 */
public class ChessPieceMaker {
    public static void main(String[] args){
        ChessPieceImpl piece;
        String pieceName;
        int maximumVectors = 20;
        int moveSetDistance = -1;
        Coordinates[] moveSetVectors = null;
        
        Options options = new Options();
        CommandLineParser parser = new BasicParser();
        HelpFormatter help = new HelpFormatter();
        CommandLine cmd = null;
        
        Option name = new Option("n","name", true, "name of created piece : string");
        name.setRequired(true);
        options.addOption(name);
        
        Option icon = new Option("i", "image", true, "path to chesspiece icon");
        icon.setRequired(true);
        options.addOption(icon);
        
        Option moveVectors = new Option("v", "vectors", true, "vectors of movement : int,int");
        moveVectors.setRequired(true);
        moveVectors.setArgs(maximumVectors);
        options.addOption(moveVectors);
        
        Option moveDistance = new Option("d", "distance", true, "distance that a piece can travel : int");
        moveDistance.setRequired(true);
        options.addOption(moveDistance);
        
        try{
            cmd = parser.parse(options, args);
        }
        
        catch(ParseException ex){
            System.err.println("Parse Error " + ex.getMessage());
            help.printHelp("ChessPieceMaker", options);
            System.exit(-1);
        }
        
        pieceName = cmd.getOptionValue(name.getOpt());
        String colorInput = pieceName.substring(0, 5); //white and black
        PlayerColors color = PlayerColors.white;
        if(colorInput.equals("white")){
            color = PlayerColors.white;
        }
        if(colorInput.equals("black")){
            color = PlayerColors.black;
        }
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
        
        piece = new ChessPieceImpl(pieceName, new Coordinates(-1,-1), color, new MoveSet(moveSetVectors, moveSetDistance), imagePath);
        
        try{
            FileOutputStream chessSer = new FileOutputStream(".\\src\\main\\java\\cz\\cvut\\fel\\skardada\\chess\\ChessPieces\\piece_" + pieceName +  ".ser");
            ObjectOutputStream out = new ObjectOutputStream(chessSer);
            out.writeObject(piece);
            out.close();
            chessSer.close();
            System.out.println("Data has been succesfully serialized");
        }
        
        catch(IOException ex){
            System.err.println("Could not serialize" + ex.getMessage());
            System.exit(-1);
        }
    }
    
}
