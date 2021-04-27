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
    private JPanel chessBoard;
    private JPanel history;
    private JPanel p1;
    private JPanel p2;
    private Player currentPlayer = null;
    private final ArrayList<JButton> availableSquares;
    private Coordinates chosenSquareCoords = null;
    private boolean redraw = false;
    public boolean ready = false;
    
    public BoardView(Board board) {
        this.modelBoard = board;
        this.boardSquares = new JButton[board.getSize()][board.getSize()];
        availableSquares = new ArrayList<JButton>();
        initComponents();
        this.ready = true;
        this.flipTheBoard();
    }
    
    private void moveChessPiece(Coordinates source, Coordinates dest){
        //model changes
        this.modelBoard.movePiece(source, dest);
        this.currentPlayer.setFinishedTurn(true);
        
        //view changes
        this.repaintFromModel();
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
    
    private void repaintFromModel(){
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
        //paint available squares
        if(redraw){
            for (JButton[] row : this.boardSquares) {
                for (JButton square : row) {
                    square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                }
            }
        }
        if(!availableSquares.isEmpty()){
            for (JButton square : availableSquares) {
                square.setBorder(BorderFactory.createLineBorder(Color.GREEN, 6));
            }
        }
    }
    
    private void initComponents(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(550,550);
        frame.setLayout(new GridLayout(1,2));
        
        chessBoard = new JPanel(new GridLayout(0,modelBoard.getSize()));
        frame.add(chessBoard);
               
        for (int i = 0; i < boardSquares.length; i++) {  
            for (int j = 0; j < boardSquares[i].length; j++) {
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
                
                boardSquares[i][j] = square;
                chessBoard.add(square);
            }
        }
        
        frame.setVisible(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
}
