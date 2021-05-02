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
        //change this to change chessStyle
        String stylePath = ChessController.class.getResource("/style_standard.ser").toExternalForm().substring(6);
        ChessStyle style = this.loadGameStyle(stylePath);  
        
        ArrayList<Player> players = new ArrayList<>();
        
        //TODO - change initialization for n - players
        
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
                p2 = new Player_Computer(this.mainView.getP2name(), this.mainView.getP2color(), getPlayerPieces(this.mainView.getP2color(), style), p2Clock);
                break;
            case local:
            default:
                p2 = new Player_Human(this.mainView.getP2name(), this.mainView.getP2color(), getPlayerPieces(this.mainView.getP2color(), style), p2Clock);
                break;    
        }
        switch(this.mainView.getP1Type()){
            case internet :
                //TODO internet opponents
            case computer:
                p1 = new Player_Computer(this.mainView.getP1name(), this.mainView.getP1color(), getPlayerPieces(this.mainView.getP1color(), style), p1Clock);
                break;
            case local:
            default:
                p1 = new Player_Human(this.mainView.getP1name(), this.mainView.getP1color(), getPlayerPieces(this.mainView.getP1color(), style), p1Clock);
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
        boolean noMovesDraw = false;
        boolean insufficientMatDraw;
        
        //get starting player - white starts
        for(int i = 0; i< players.size(); i++){
            if(players.get(i).getColor() == PlayerColors.white){
                currentPlayerIndex = i;
            }
        }
        
        //main game loop
        while(winner == null){
            
            Player currentPlayer = players.get(currentPlayerIndex);
            insufficientMatDraw = true;
            //diagnostic fucntions
            printBoard();
            System.out.println("Current player: " + currentPlayer.getColor().toString());
            
            //remove CheckMated player, decide winner of the game
            ArrayList<Player> playersToRemove = new ArrayList();
            for(Player p : players){
                p.updateAvailableMoves();
                
                //player is mated
                if(p.isMated()){
                    System.out.println(p.getColor() + " mated");
                    playersToRemove.add(p);
                }
                
                
                else{
                    
                    //no available moves DRAW
                    if(p.getAvailableMoves().isEmpty()){
                        noMovesDraw = true;
                    }
                    
                    //insuficient material DRAW
                    if(!p.hasInsufficientMaterial()){
                        insufficientMatDraw = false;
                    }
                }
            }
            players.removeAll(playersToRemove);
            
            //last player standing wins
            if(players.size() == 1){
                winner = players.get(0);
                System.out.println("WINNER IS PLAYER "+ winner.getColor());
                return;
            } 
            
            // DRAW - THREE fold repetition isnt automatic, but agreed uppon by players - its not part of this project
            if(noMovesDraw || insufficientMatDraw){
                System.out.println("DRAW");
                return;
            }
            
            //take a turn
            synchronized(this){
                boardView.setCurrentPlayer(currentPlayer);
                currentPlayer.getChessClock().clockStart();
                currentPlayer.setFinishedTurn(false);
                currentPlayer.makeMove(game.getGameBoard());
                while(!currentPlayer.getFinishedTurn()){
                    this.boardView.updateClocks();
                    try{
                       this.wait(10); 
                    }
                    catch(Exception e){
                        System.err.println(e.getMessage() + " " + Thread.currentThread().getName());
                    }
                } 
                currentPlayer.getChessClock().clockStop();
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            this.boardView.repaintFromModel();
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
    
    private void manualSetUp(){
        //TODO
    }
    
    private void importPgnGame(){
        //TODO
    }
    
    private void exportPgnGame(){
        //TODO
    }
    
    private void saveGame(){
        //TODO
    }
    
    private void loadGame(){
        //TODO
    }
    
    private void announceWinner(){
        //TODO
    }
}
