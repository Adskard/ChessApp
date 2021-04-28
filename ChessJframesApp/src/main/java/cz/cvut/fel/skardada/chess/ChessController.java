/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.*;
/**
 *
 * @author Adam Škarda
 */
public class ChessController implements Runnable{

    private Game game;
    private MainView mainView;
    private BoardView boardView;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public ChessController(){
        
        logger.addHandler(new ConsoleHandler());
    }
    
    public static void main(String[] args){
        //initialize main controller thread
        ChessController control = new ChessController();
        Thread mainThread = new Thread(control, "main Thread");
        mainThread.start();
    }
    
    public void run(){
        //Initializing view for user options
        mainView = new MainView();
        mainView.setVisible(true);
  
        //Wait for to input options and start the game
        synchronized(this){
            while(!mainView.ready){
                try{
                  this.wait(100);  
                }
                catch(Exception E){
                    System.err.println(E.getMessage());
                }
            }
        }
        
        //Set up the game model
        this.setUpTheGame();
        
        //Options nolonger visible, but not terminated - can be used after game ends
        mainView.setVisible(false);
        
        //Initialize game board view
        this.boardView = new BoardView(this.game);
        
        //wait for the board to initialize
        synchronized(this){
            while(!boardView.ready){
                try{
                  this.wait(100);  
                }
                catch(Exception E){
                    System.err.println(E.getMessage());
                }
            }
        }
        
        //Start the chess game
        this.startTheGame();
    }
    
    private void setUpTheGame(){
        ChessStyle style = this.loadGameStyle(mainView.getStylePath());    
        ArrayList<Player> players = new ArrayList<>();
        
        
        
        //initialize clocks for players
        ChessClock p1Clock = new ChessClock(this.mainView.getP1time(),this.mainView.getP1incrementTime());
        ChessClock p2Clock = new ChessClock(this.mainView.getP2time(),this.mainView.getP2incrementTime());
        
        //initialize players for view
        Player p1;
        Player p2;
        switch(this.mainView.getP2Type()){
            case internet :
                //TODO internet opponents
            case computer:
                p2 = new Player_Computer(this.mainView.getP2name(), this.mainView.getP2color(), getPlayerPieces(PlayerColors.black, style), p2Clock);
                break;
            case local:
            default:
                p2 = new Player_Human(this.mainView.getP2name(), this.mainView.getP2color(), getPlayerPieces(PlayerColors.black, style), p2Clock);
                break;    
        }
        switch(this.mainView.getP1Type()){
            case internet :
                //TODO internet opponents
            case computer:
                p1 = new Player_Computer(this.mainView.getP1name(), this.mainView.getP1color(), getPlayerPieces(PlayerColors.white, style), p1Clock);
                break;
            case local:
            default:
                p1 = new Player_Human(this.mainView.getP1name(), this.mainView.getP1color(), getPlayerPieces(PlayerColors.white, style), p1Clock);
                break;    
        }
        
        //Create model game
        players.add(p1);
        players.add(p2);
        this.game = new Game(style, players);
    }
    
    private void startTheGame(){
        //game is played until winner is decided
        Player winner = null;
        ArrayList<Player> players = this.game.getPlayers();
        int currentPlayerIndex = 0;
        
        //get starting player - white starts
        for(int i = 0; i< players.size(); i++){
            if(players.get(i).getColor() == PlayerColors.white){
                currentPlayerIndex = i;
            }
        }
        
        //main game loop
        while(winner == null){
            
            Player currentPlayer = players.get(currentPlayerIndex);
            //diagnostic fucntions
            printBoard();
            System.out.println("Current player: " + currentPlayer.getColor().toString());
            
            //remove CheckMated player, decide winner of the game
            ArrayList<Player> playersToRemove = new ArrayList();
            for(Player p : players){
                p.updateAvailableMoves();
                System.out.println(p.getColor() + " " + p.getAvailableMoves() + " " + p.getOwnPieces());
                if(p.isMated()){
                    System.out.println(p.getColor() + " mated");
                    playersToRemove.add(p);
                }
                else{
                    if(p.getAvailableMoves().isEmpty()){
                        System.out.println("DRAW");
                        return; 
                    }
                }
            }
            players.removeAll(playersToRemove);
            if(players.size() == 1){
                winner = players.get(0);
                System.out.println("WINNER IS PLAYER "+ players.get(0).getColor());
                return;
            } 
            //TODO - add draw options
                
            //threefold repetiotion
            
            //no available moves 
            
            else{
                synchronized(this){
                    boardView.setCurrentPlayer(currentPlayer);
                    currentPlayer.getChessClock().clockStart();
                    currentPlayer.setFinishedTurn(false);
                    currentPlayer.makeMove(game.getGameBoard());
                    while(!currentPlayer.getFinishedTurn()){
                        this.boardView.updateClocks();
                        try{
                           this.wait(10); 
                            System.out.println("waiting for move input");
                        }
                        catch(Exception e){
                            System.err.println(e.getMessage() + " " + Thread.currentThread().getName());
                        }
                    } 
                    currentPlayer.getChessClock().clockStop();
                }
            }
            this.boardView.repaintFromModel();
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    } 
    
    private ArrayList getPlayerPieces(PlayerColors color, ChessStyle style){
        ArrayList<ChessPiece> pieces = new ArrayList();
        for(ChessPiece[] row : style.getBoardArrangement()){
            for(ChessPiece piece : row){
                if(piece != null && piece.getColor() == color){
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
    
    private void printBoard(){
        System.out.println("[");
        for(ChessPiece[] row : this.game.getGameBoard().getBoard()){
            System.out.print("[");
            for(ChessPiece piece : row){
                if(piece == null){
                   System.out.print("-1");
                    System.out.print(" | "); 
                    continue;
                }
                System.out.print(piece.toString());
                System.out.print(piece.getPosition().toString());
                System.out.print(" | ");
            }
            System.out.println("]");
        }
        System.out.println("]");
    }
    

    private ChessStyle loadGameStyle(String stylePath){
        //Get better pathing TODO
        stylePath = "D:\\Projects\\Java\\ChessJframesApp\\ChessJframesApp\\src\\main\\java\\cz\\cvut\\fel\\skardada\\chess\\ChessStyles\\style_standard.ser";
        ChessStyle style = null;
        try{
            FileInputStream file = new FileInputStream(stylePath);
            ObjectInputStream in = new ObjectInputStream(file);
            style = (ChessStyle) in.readObject();
            in.close();
            file.close();
        }
        catch(IOException ex){
            System.err.print("Could not read resource " + ex.getMessage());
            return null;
        }
        catch(ClassNotFoundException ex){
            System.err.print("Could not find class " + ex.getMessage());
            return null;
        }
        return style;
    }
}
