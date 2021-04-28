/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.util.ArrayList;
/**
 *
 * @author Adam Škarda
 */
public class BoardView{
    
    private JFrame frame;
    private final Board modelBoard;
    private final JButton[][] boardSquares;
    private final Game modelGame;
    private JPanel chessBoard;
    private JPanel p1;
    private JPanel p2;
    JLabel P1Time;
    JLabel P2Time;
    JList history;
    private Player currentPlayer = null;
    private final ArrayList<JButton> availableSquares;
    private Coordinates chosenSquareCoords = null;
    private boolean redraw = false;
    public boolean ready = false;
    
    public BoardView(Game game) {
        this.modelGame = game;
        this.modelBoard = game.getGameBoard();
        this.boardSquares = new JButton[modelBoard.getSize()][modelBoard.getSize()];
        availableSquares = new ArrayList<JButton>();
        initComponents();
        this.ready = true;
    }
    
    private void moveChessPiece(Coordinates source, Coordinates dest){
        //model changes
        this.modelBoard.movePiece(source, dest);
        this.currentPlayer.setFinishedTurn(true);
    }
    
    private class userMoveInputHandler implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e){
            JButton square = (JButton)e.getSource();
            Coordinates squareCoordinates = new Coordinates(Integer.parseInt(square.getName().substring(0,1)),Integer.parseInt(square.getName().substring(1,2)));
            
            //second click on wrong squares - not available squares
            if(chosenSquareCoords != null && !availableSquares.contains(square)){
                availableSquares.clear();
                chosenSquareCoords = null;
                redraw = true;
            }
            
            //first click
            if(chosenSquareCoords == null){ 
                int x = Integer.parseInt(square.getName().substring(0, 1));
                int y = Integer.parseInt(square.getName().substring(1, 2));
                if(modelBoard.getBoard()[x][y] == null){
                    return;
                }
                for (Coordinates coord : modelBoard.getBoard()[x][y].getLegalMoves()) {
                    availableSquares.add(boardSquares[coord.getX()][coord.getY()]);
                }
                chosenSquareCoords = new Coordinates(Integer.parseInt(square.getName().substring(0,1)),Integer.parseInt(square.getName().substring(1,2)));
            }
            
           //Check who clicked and if he can move the clicked piece
            if(chosenSquareCoords != null && modelBoard.getBoard()[chosenSquareCoords.getX()][chosenSquareCoords.getY()].getColor() == currentPlayer.getColor() && currentPlayer.isCurrentlyPlaying()){
                //second click to move
                if((currentPlayer != null) && availableSquares.contains(square)){
                    availableSquares.clear();
                    moveChessPiece(chosenSquareCoords, squareCoordinates);
                    chosenSquareCoords = null;
                    redraw = true;
                }
            }
            
            //for now update view 
            updateAvailableMovesView();
        }
    }
    
    public void updateClocks(){
        P1Time.setText(modelGame.getPlayers().get(0).getChessClock().getRemainingSeconds());
        P2Time.setText(modelGame.getPlayers().get(1).getChessClock().getRemainingSeconds());
    }
    
    private void updateLostPieces(){
        
    }
    
    private void updateHistory(){
        history.setListData(modelGame.getGameBoard().getHistory().getDestinations().toArray());
    }
    
    public void repaintFromModel(){
        
        updateClocks(); // migrate to per second thread 
        updateHistory();
        
        for (int i = 0; i < boardSquares.length; i++) {  
            for (int j = 0; j < boardSquares[i].length; j++) {
                JButton square = this.boardSquares[i][j];
                if(modelBoard.getBoard()[i][j] != null){
                    String imagePath = modelBoard.getBoard()[i][j].getImagePath();
                    try{
                      BufferedImage image = ImageIO.read(new File(imagePath));  
                      square.setIcon(new ImageIcon(image));
                    }
                    catch(Exception e){
                        System.err.println("Could not load Chess piece image "+e.getMessage());
                    }  
                }
                else{
                    square.setIcon(null);
                }
            }
        }
    }
    
    private void flipTheBoard(){

    }
    
    private void updateAvailableMovesView(){
        //repaint squares bach to original
        if(redraw){
            for (JButton[] row : this.boardSquares) {
                for (JButton square : row) {
                    square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                }
            }
        }
        
        //paint available squares green
        if(!availableSquares.isEmpty()){
            for (JButton square : availableSquares) {
                square.setBorder(BorderFactory.createLineBorder(Color.GREEN, 6));
            }
        }
    }
    
    private void initComponents(){
        //create main frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(550,550);
        frame.setLayout(new GridLayout(0,2));
        
        //components of main frame
        JPanel optionsAndHistory = new JPanel(new GridBagLayout());
        JPanel boardFrame = new JPanel(new GridLayout(3, 0));
        
        //components of options and history part - left side
        
        JPanel optionsPanel = new JPanel();
        
        history = new JList(modelGame.getGameBoard().getHistory().getDestinations().toArray());
        
        optionsAndHistory.add(optionsPanel);
        optionsAndHistory.add(history);
        
        //components of board part - right side
        
        p1 = new JPanel(); // panel with player name, time, pieces lost
        p2 = new JPanel(); // panel with player name, time, pieces lost
        
        JLabel p1Name = new JLabel(modelGame.getPlayers().get(0).getName());
        JLabel p2Name = new JLabel(modelGame.getPlayers().get(1).getName());
        p1.add(p1Name);
        p2.add(p2Name);
        
        P1Time = new JLabel(modelGame.getPlayers().get(0).getChessClock().getRemainingSeconds());
        P2Time = new JLabel(modelGame.getPlayers().get(1).getChessClock().getRemainingSeconds());
        p1.add(P1Time);
        p2.add(P2Time);
        
        JLabel p1Pieces = new JLabel("Pieces");
        JLabel p2Pieces = new JLabel("Pieces");
        p1.add(p1Pieces);
        p2.add(p2Pieces);
        
        chessBoard = new JPanel(new GridLayout(0,modelBoard.getSize() + 1));
        
        boardFrame.add(p1);
        boardFrame.add(chessBoard);
        boardFrame.add(p2);
              
        //alloting squares to chessBoard, where board squares are JButtons and coordinates are lables      
        for (int i = 0; i < boardSquares.length; i++) { 
            //add coordinates to first row of chess board
            if (i == 0) {
                for (int j = 0; j < boardSquares[i].length + 1; j++) {
                    
                    //first label in corner is blank
                    if(j == 0){
                        JLabel coordsLabel = new JLabel();
                        chessBoard.add(coordsLabel);
                        continue;
                    }
                    JLabel coordsLabel = new JLabel(Character.toString((char) 96 + j), SwingConstants.CENTER);
                    chessBoard.add(coordsLabel);
                }
            }
            for (int j = 0; j < boardSquares[i].length; j++) {
                //add coordinates to first column of chess board
                if (j == 0) {
                    JLabel coordsLabel = new JLabel(Integer.toString(i+1), 0);
                    chessBoard.add(coordsLabel);
                }
                
                //set square properties
                JButton square = new JButton();
                square.addActionListener(new userMoveInputHandler());
                square.setName(Integer.toString(i) + Integer.toString(j));
                square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                
                //getting squre content
                if(modelBoard.getBoard()[i][j] != null){
                    
                    String imagePath = modelBoard.getBoard()[i][j].getImagePath();
                    try{
                      BufferedImage image = ImageIO.read(new File(imagePath));  
                      square.setIcon(new ImageIcon(image));
                    }
                    catch(Exception e){
                        System.err.println("Could not load Chess piece image "+e.getMessage());
                    }  
                }
                
                //coloring squares
                if((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)){
                 square.setBackground(Color.WHITE);
                 }
                 else{
                     square.setBackground(new Color(45, 83, 108));
                 } 
                
                //adding to collections
                boardSquares[i][j] = square;
                chessBoard.add(square);
            }
        }
        
        //finalize computing and set visible
        frame.add(optionsAndHistory);
        frame.add(boardFrame);
        frame.setVisible(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
}
