/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.*;
import org.apache.commons.io.IOUtils;
/**
 *
 * @author Adam Škarda
 */
public class ChessController implements Runnable{

    private Game game;
    private MainView mainView;
    private BoardView boardView;
    private static Logger logger = Logger.getLogger(ChessController.class.getName());
    
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
        //Escape option of this while loop is closing the view window
        while(true){
            
            //Initializing view for user options
            if(mainView == null){
                mainView = new MainView();
            }
            mainView.setVisible(true);

            //Wait for to input options and start the game
            synchronized(this){
                while(!mainView.isReady()){
                    try{
                      this.wait(100);  
                    }
                    catch(Exception E){
                        System.err.println(E.getMessage());
                    }
                }
            }
            
            //for the next game
            mainView.setReady(false);
            if(mainView.isManualSetup()){
                mainView.setManualSetup(false);
                this.manualSetUp();
                logger.log(Level.INFO, "Manual set up selected");
            }
            
            if(mainView.isNormalStart()){
                mainView.setNormalStart(false);
                this.normalStart();
                logger.log(Level.INFO, "Normal start selected");
            }
            if (mainView.isLoadGame()) {
                mainView.setLoadGame(false);
                String path = mainView.showOpenDialog();
            }
            if(mainView.isPgnView()){
                mainView.setPgnView(false);
                String path = mainView.showOpenDialog();
            }

        }
        
    }
    
    private void manualSetUp(){
        this.mainView.setEnabled(false);
        normalGameSetup();
        this.boardView = new BoardView(this.game);

        //wait for the board to initialize
        synchronized(this){
            while(!boardView.ready){
                try{
                  Thread.sleep(10);  
                }
                catch(Exception E){
                    System.err.println(E.getMessage());
                }
            }
        }
        boardView.setManual(true);
        while(boardView.isManual()){
            try{
                Thread.sleep(10);  
            }
            catch(Exception E){
                System.err.println(E.getMessage());
            }
        }
        this.boardView = null;
        this.game.getGameBoard().updatePieces();
        this.mainView.setEnabled(true);
    }
    
    private void normalStart(){
        //Set up the game model
        if (this.game == null) {
           this.normalGameSetup();  
        }
        

        //Options nolonger visible, but not terminated - can be used after game ends
        mainView.setVisible(false);

        //Initialize game board view
        this.boardView = new BoardView(this.game);

        //wait for the board to initialize
        synchronized(this){
            while(!boardView.ready){
                try{
                  this.wait(10);  
                }
                catch(Exception E){
                    System.err.println(E.getMessage());
                }
            }
        }

        //Start the chess game
        this.startTheGame(); 
    }
    
    private void normalGameSetup(){
        String stylePath = "";
        //change this to change chessStyle
        try {
            stylePath = ChessController.class.getResource("/style_standard.ser").toURI().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
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
                    logger.log(Level.INFO, "{0} mated", p.getColor());
                    playersToRemove.add(p);
                    break;
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
                boolean export = boardView.showWinnerWindow("Winner is " + winner.getName() + " with " + winner.getColor() + " pieces!");
                players.addAll(playersToRemove);
                if(export){
                    this.exportPgnGame(winner.getColor() );
                }
                exitBoardToMain();
                return;
            } 
            
            // DRAW - THREE fold repetition isnt automatic, but agreed uppon by players - its not part of this project
            if(noMovesDraw || insufficientMatDraw){
                boolean export = boardView.showWinnerWindow("This Game is a draw!");
                if(export){
                    this.exportPgnGame(null);
                }
                exitBoardToMain();
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
        System.out.println(style.getName());
        return style;
    }
    
    
    private void importPgnGame(){
        //TODO
    }
    
    private void exportPgnGame(PlayerColors winner){
        String path = boardView.showSaveDialog();
        PgnParser.exportPgnGameToFile(path, game, winner);
    }
    
    private void saveGame(){
        //TODO
    }
    
    private void loadGame(){
        //TODO
    }
    
    public void exitBoardToMain(){
        this.mainView.setVisible(true);
        this.game = null;
        this.boardView = null;
    }
}
