/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;
/**
 *
 * @author Adam Škarda
 */
public class ChessController implements Runnable{

    private Game game;
    private MainView mainView;
    private BoardView boardView;
    private static final Logger logger = Logger.getLogger(ChessController.class.getName());
    private PlayerColors startingPlayer;
    private String loadedPgnPath;
    
    public ChessController(){
        logger.addHandler(new ConsoleHandler());
    }
    
    public static void main(String[] args){
        //initialize main controller thread
        ChessController control = new ChessController();
        Thread mainThread = new Thread(control, "main Thread");
        mainThread.start();
    }
    
    @Override
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
                try {
                    loadGameStart(path);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Saved game was not found ", ex);
                } catch (Exception ex) {
                    Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Game could not be loaded ", ex);
                    this.game = null;
                }
            }
            if(mainView.isPgnView()){
                mainView.setPgnView(false);
                String path = mainView.showOpenDialog();
                try {
                    loadedPgnPath = path;
                    viewPgn(path);
                } catch (Exception ex) {
                    Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Could not view this file", ex);
                    this.game = null;
                }
            }

        }
    }
    
    private Game createFakeGameForView(String pgnPath) throws Exception{
        String stylePath = "";
        //change this to change chessStyle, may not work, not fully implemented
        try {
            stylePath = ChessController.class.getResource("/style_standard.ser").toURI().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Could not find game style file", ex);
            System.exit(-1);
        }
        ChessStyle style = this.loadGameStyle(stylePath); 
        
        //TODO init for n players
        
        String blackPlayerName = PgnParser.parsePlayerName(PlayerColors.black, pgnPath);
        String whitePlayerName = PgnParser.parsePlayerName(PlayerColors.white, pgnPath);
        
        
        ArrayList<Player> players = new ArrayList<>();
        
        //TODO future switch case for loading in computer and internet players
        Player blackPlayer = new Player_Human(blackPlayerName, PlayerColors.black, getPlayerPieces(PlayerColors.black, style), null);
        players.add(blackPlayer);
        Player whitePlayer = new Player_Human(whitePlayerName, PlayerColors.white, getPlayerPieces(PlayerColors.white, style), null);
        players.add(whitePlayer);
        
        startingPlayer = PlayerColors.white;
        
        return new Game(style, players);
    }
    
    private void loadGameStart(String pathToSave) throws FileNotFoundException, Exception{
        String stylePath = "";
        //change this to change chessStyle, may not work, not fully implemented
        try {
            stylePath = ChessController.class.getResource("/style_standard.ser").toURI().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Could not find game style file", ex);
            System.exit(-1);
        }
        ChessStyle style = this.loadGameStyle(stylePath); 
        
        //TODO init for n players
        
        String blackPlayerName = PgnParser.parsePlayerName(PlayerColors.black, pathToSave);
        String whitePlayerName = PgnParser.parsePlayerName(PlayerColors.white, pathToSave);
        
        ChessClock blackClock = new ChessClock(PgnParser.parseTime(blackPlayerName, pathToSave)[0], PgnParser.parseTime(blackPlayerName, pathToSave)[1]);
        ChessClock whiteClock = new ChessClock(PgnParser.parseTime(whitePlayerName, pathToSave)[0], PgnParser.parseTime(whitePlayerName, pathToSave)[1]);
        
        ArrayList<Player> players = new ArrayList<>();
        
        //TODO future switch case for loading in computer and internet players
        Player blackPlayer = new Player_Human(blackPlayerName, PlayerColors.black, getPlayerPieces(PlayerColors.black, style), blackClock);
        players.add(blackPlayer);
        Player whitePlayer = new Player_Human(whitePlayerName, PlayerColors.white, getPlayerPieces(PlayerColors.white, style), whiteClock);
        players.add(whitePlayer);
        
        startingPlayer = PlayerColors.white;
        
        this.game = new Game(style, players);
        String [] turnsTaken = PgnParser.getIndividualMoves(pathToSave);
        takeTurns(turnsTaken, this.game);
        this.normalStart();
    }
    
    public Game createFakeGameStateForView(String[] turnsTaken) throws Exception{
        Game fake = createFakeGameForView(loadedPgnPath);
        takeTurns(turnsTaken, fake);
        return fake;
    }
    
    private void viewPgn(String pgnPath) throws Exception{
        this.game = createFakeGameForView(pgnPath);
        String [] turnsTaken = PgnParser.getIndividualMoves(pgnPath);
        takeTurns(turnsTaken, this.game);
        this.boardView = new BoardView(game, this);
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
    }
    
    
    public void takeTurns(String[] turns, Game playedGame) throws Exception{
        Board board = playedGame.getGameBoard();
        int currentPlayerIndex = 0;
        //set starting palyer
        for(int i = 0; i< playedGame.getPlayers().size(); i++){
            if(playedGame.getPlayers().get(i).getColor() == startingPlayer){
                currentPlayerIndex = i;
                break;
            }
        }
        //game cycle
        for (int i = 0; i < turns.length; i++) {
            Player currentPlayer = playedGame.getPlayers().get(currentPlayerIndex % 2);
            board.movePiece(turns[i], currentPlayer);
            currentPlayer.updateAvailableMoves();
            currentPlayerIndex++;
        }
        startingPlayer = playedGame.getPlayers().get(currentPlayerIndex % 2).getColor();
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
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, "Could not find game style file", ex);
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
        
        this.startingPlayer = PlayerColors.white;
        this.game = new Game(style, players);
    }
    
    private void startTheGame(){
        //game is played until winner is decided
        Player winner = null;
        ArrayList<Player> players = this.game.getPlayers();
        int currentPlayerIndex = 0;
        boolean noMovesDraw = false;
        boolean insufficientMatDraw;
        
        //get starting player - white starts normally - can be changed when game is loaded 
        for(int i = 0; i< players.size(); i++){
            if(players.get(i).getColor() == startingPlayer){
                currentPlayerIndex = i;
                break;
            }
        }
        
        //main game loop
        while(winner == null){
            
            Player currentPlayer = players.get(currentPlayerIndex);
            insufficientMatDraw = true;
            //diagnostic fucntions
            logger.log(Level.INFO, printBoard());
            
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
    
    private String printBoard(){
        String gameState = "";
        gameState += "[" + System.lineSeparator();
        for(ChessPiece[] row : this.game.getGameBoard().getBoard()){
            gameState += "[";
            for(ChessPiece piece : row){
                if(piece == null){
                    gameState += "-1";
                    gameState += " | "; 
                    continue;
                }
                gameState += piece.toString();
                gameState += piece.getPosition().toString();
                gameState += " | ";
            }
            gameState += "[" + System.lineSeparator();
        }
        gameState += "[" + System.lineSeparator();
        return gameState;
    }
    

    private ChessStyle loadGameStyle(String stylePath){
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
    
    
    private void exportPgnGame(PlayerColors winner){
        String path = boardView.showSaveDialog();
        PgnParser.exportPgnGameToFile(path, game, winner);
    }
    
    
    public void exitBoardToMain(){
        this.mainView.setVisible(true);
        this.game = null;
        this.boardView = null;
    }
}
