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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private JFileChooser saveDialog;
    
    public BoardView(Game game) {
        this.modelGame = game;
        this.modelBoard = game.getGameBoard();
        this.boardSquares = new JButton[modelBoard.getSize()][modelBoard.getSize()];
        availableSquares = new ArrayList<JButton>();
        this.saveDialog = new JFileChooser();
        initComponents();
        this.ready = true;
    }
    
    private void moveChessPiece(Coordinates source, Coordinates dest){
        //model changes
        this.modelBoard.movePiece(source, dest);
        
        //Check if promotion is needed/available
        ChessPiece piece = modelBoard.getChessPieceAtCoordinate(dest);
        if(piece instanceof ChessPiecePawn){
            ChessPiecePawn pawn = (ChessPiecePawn) piece;
            if(pawn.isCanBePromoted()){
                ChessPiece newRank = promotionPopUp();
                ChessPiece promoted = modelBoard.promotion(pawn, newRank);
                currentPlayer.getOwnPieces().remove(pawn);
                currentPlayer.getOwnPieces().add(promoted);
            }
        }
        this.currentPlayer.setFinishedTurn(true);

    }
    
    private ChessPiece promotionPopUp(){
        //get options for promotions
        ArrayList<ChessPiece> options = new ArrayList();
        for(ChessPiece piece : modelBoard.getUniquePieces()){
            if(piece.getColor().equals(currentPlayer.getColor())){
                options.add(piece);
            }
        }
        Object[] possibilities = options.toArray();
        
        //create popUp
        ChessPiece answer = (ChessPiece)JOptionPane.showInputDialog(frame,"Quick!" + System.lineSeparator() + "Choose piece to promote to!","Promotion",
            JOptionPane.PLAIN_MESSAGE,null,possibilities,possibilities[0]);
        return answer;
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
    
    private void updateHistory(){
        history.setListData(modelGame.getGameBoard().getHistory().getMovesInPgn().toArray());
    }
    
    public void repaintFromModel(){
        
        updateHistory();
        
        //synchronizes squares from model and squares from view view
        for (int i = 0; i < boardSquares.length; i++) {  
            for (int j = 0; j < boardSquares[i].length; j++) {
                JButton square = this.boardSquares[i][j];
                if(modelBoard.getBoard()[i][j] != null){
                    String imagePath = modelBoard.getBoard()[i][j].getImagePath();
                    String fullImagePath = null;
                    try {
                        fullImagePath = BoardView.class.getResource(imagePath).toURI().getPath();
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try{
                      BufferedImage image = ImageIO.read(new File(fullImagePath));  
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
        frame.setSize(800,800);
        frame.setLayout(new BorderLayout(40, 20));
        
        //components of options and history part - left side
        
        JPanel optionsPanel = new JPanel(new BorderLayout(30, 60));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = showSaveDialog();

                PgnParser.savePgnGameToFile(path, modelGame);

            }
        });
        optionsPanel.add(saveButton, BorderLayout.PAGE_START);
        
        history = new JList(modelGame.getGameBoard().getHistory().getMovesInPgn().toArray());
        history.setLayoutOrientation(JList.VERTICAL);
        history.setVisibleRowCount(10);
        history.setSize(200, 500);
        history.setFixedCellHeight(20);
        history.setFixedCellWidth(100);
        optionsPanel.add(new JScrollPane(history), BorderLayout.CENTER);
        
        
        //components of board part - right side
        
        p1 = new JPanel(new FlowLayout(0, 100, 20)); // panel with player name, time, pieces lost
        p2 = new JPanel(new FlowLayout(0, 100, 20)); // panel with player name, time, pieces lost
        
        JLabel p1Name = new JLabel(modelGame.getPlayers().get(0).getName());
        JLabel p2Name = new JLabel(modelGame.getPlayers().get(1).getName());
        p1.add(p1Name);
        p2.add(p2Name);
        
        P1Time = new JLabel(modelGame.getPlayers().get(0).getChessClock().getRemainingSeconds());
        P2Time = new JLabel(modelGame.getPlayers().get(1).getChessClock().getRemainingSeconds());
        p1.add(P1Time);
        p2.add(P2Time);

        initBoardSquares();
        frame.add(p1, BorderLayout.PAGE_START);
        frame.add(chessBoard, BorderLayout.CENTER);
        frame.add(p2, BorderLayout.PAGE_END);
        frame.add(optionsPanel, BorderLayout.LINE_END);
        frame.setVisible(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    private void initBoardSquares(){
        chessBoard = new JPanel(new GridLayout(0,modelBoard.getSize() + 1));
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
                    String fullImagePath = null;
                    try {
                        fullImagePath = BoardView.class.getResource(imagePath).toURI().getPath();
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try{
                      BufferedImage image = ImageIO.read(new File(fullImagePath));  
                      square.setIcon(new ImageIcon(image));
                    }
                    catch(Exception e){
                        System.err.println("Could not load Chess piece image "+e.getMessage());
                    }  
                }
                
                //coloring squares
                if((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)){
                 square.setBackground(new Color(45, 83, 108));
                 }
                 else{
                     square.setBackground(Color.WHITE);
                 } 
                
                //adding to collections
                boardSquares[i][j] = square;
                chessBoard.add(square);
            }
        }
    }
    
    public boolean showWinnerWindow(String resultMsg){
        //two options to choose from
        String[] options = new String[2];
        options[0] = "Export";
        options[1] = "To Main Menu";
        
        //show endGame dialog window with export option
        int n = JOptionPane.showOptionDialog(frame, resultMsg,"Who won?",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        
        //export the game
        if(n == JOptionPane.YES_OPTION){
            this.frame.setVisible(false);
            this.frame.dispose();
            return true;
        }
        
        //return to main menu without exporting
        if(n == JOptionPane.NO_OPTION || n == JOptionPane.CLOSED_OPTION){
            this.frame.setVisible(false);
            this.frame.dispose();
        }
        return false;
    }
    
    public String showSaveDialog(){
        int k = saveDialog.showSaveDialog(frame);
        if (k != JFileChooser.APPROVE_OPTION) {
            return "";
        }
        String path = saveDialog.getSelectedFile().getAbsolutePath();
        return path;
    }

}
