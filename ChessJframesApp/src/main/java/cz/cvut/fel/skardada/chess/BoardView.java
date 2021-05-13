
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * BoardView is a class of view and is a java.swing window application.
 * It displays the game board and is responsible for displaying the current state of the game and listening for user input.
 * @author Adam Škarda
 */
public class BoardView{
    
    private JFrame frame;
    private ChessController controller;
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

    /**
     * flag for when boardView is ready visible
     */
    public boolean ready = false;
    private JFileChooser saveDialog;
    private boolean manual = false;
    private JButton manualSetUpDoneButton;
    private JPanel optionsPanel;
    private static final Logger logger = Logger.getLogger(BoardView.class.getName());
    
    /**
     * Constructor used for viewing Pgn games 
     * @param game game for the view to display
     * @param control controller connection
     */
    public BoardView(Game game, ChessController control) {
        this.modelGame = game;
        this.modelBoard = game.getGameBoard();
        this.boardSquares = new JButton[modelBoard.getSize()][modelBoard.getSize()];
        availableSquares = new ArrayList<JButton>();
        this.saveDialog = new JFileChooser();
        this.controller = control;
        initPgnViewing();
        this.ready = true;
    }
    
    /**
     * Constructor for playing games
     * @param game game for the view to display
     */
    public BoardView(Game game) {
        this.modelGame = game;
        this.modelBoard = game.getGameBoard();
        this.boardSquares = new JButton[modelBoard.getSize()][modelBoard.getSize()];
        availableSquares = new ArrayList<JButton>();
        this.saveDialog = new JFileChooser();
        initComponents();
        this.ready = true;
    }
    
    
    /**
     * initializes frame and frame componetns for pgn viewing
     */
    private void initPgnViewing(){
        //create main frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setLayout(new BorderLayout(40, 20));
        
        optionsPanel = new JPanel(new BorderLayout(30, 60));
        
        JButton saveButton = new JButton("Exit to main menu");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                controller.exitBoardToMain();
            }
        });
        optionsPanel.add(saveButton, BorderLayout.PAGE_START);
        
        history = new JList(modelGame.getGameBoard().getHistory().getMovesInPgn().toArray());
        history.setLayoutOrientation(JList.VERTICAL);
        history.setVisibleRowCount(10);
        history.setSize(200, 500);
        history.setFixedCellHeight(20);
        history.setFixedCellWidth(100);
        history.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener historyListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = history.getSelectedIndex();
                String[] moves = new String[selectedIndex + 1];
                for (int i = 0; i < moves.length; i++) {
                    moves[i] = (String) history.getModel().getElementAt(i);
                }
                try {
                    displayBoard(controller.createFakeGameStateForView(moves).getGameBoard().getBoard());
                } catch (Exception ex) {
                    Logger.getLogger(BoardView.class.getName()).log(Level.SEVERE, "Cannot simulate this move", ex);
                }
            }
        };
        history.addListSelectionListener(historyListener);
        optionsPanel.add(new JScrollPane(history), BorderLayout.CENTER);
        
        p1 = new JPanel(new FlowLayout(0, 100, 20)); // panel with player name, time, pieces lost
        p2 = new JPanel(new FlowLayout(0, 100, 20)); // panel with player name, time, pieces lost
        
        JLabel p1Name = new JLabel(modelGame.getPlayers().get(1).getName() + "(" + modelGame.getPlayers().get(1).getColor() + ")");
        JLabel p2Name = new JLabel(modelGame.getPlayers().get(0).getName() + "(" + modelGame.getPlayers().get(0).getColor() + ")");
        p1.add(p1Name);
        p1.add(p2Name);
        
        initBoardSquares();
        frame.add(p1, BorderLayout.PAGE_START);
        frame.add(chessBoard, BorderLayout.CENTER);
        frame.add(optionsPanel, BorderLayout.LINE_END);
        frame.setVisible(true);
    }
    
    /**
     * @param result result text to be displayed
     */
    public void addResultText(String result){
        this.p1.add(new JLabel(result));
    }
    
    /**
     * @param modelBoard board to be displayed
     */
    private void displayBoard(ChessPiece[][] modelBoard){
        for (int i = 0; i < boardSquares.length; i++) {  
            for (int j = 0; j < boardSquares[i].length; j++) {
                JButton square = this.boardSquares[i][j];
                if(modelBoard[i][j] != null){
                    String imagePath = modelBoard[i][j].getImagePath();
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
                    catch(IOException e){
                        logger.log(Level.SEVERE, "Could not laod Chess piece image {0}", e.getMessage());
                    }  
                }
                else{
                    square.setIcon(null);
                }
            }
        }
    }
    
    /**
     * moves the piece in model based on user input (clicked squares)
     * @param source source square of the move (first click)
     * @param dest destination square of the move (second click)
     */
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
    
    /**
     * @param source source square of the move (first click)
     * @param dest destination square of the move (second click)
     */
    private void manualMoveChessPiece(Coordinates source, Coordinates dest){
        this.modelBoard.movePieceAnywhere(source, dest);
        repaintFromModel();
    }
    
    /**
     * displays pop-up window for promotion choosing
     * @return returns chosen promotion
     */
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
            if(manual){
                if(chosenSquareCoords == null){ 
                    int x = Integer.parseInt(square.getName().substring(0, 1));
                    int y = Integer.parseInt(square.getName().substring(1, 2));
                    if(modelBoard.getBoard()[x][y] == null){
                        return;
                    }
                    chosenSquareCoords = new Coordinates(Integer.parseInt(square.getName().substring(0,1)),Integer.parseInt(square.getName().substring(1,2)));
                    return;
                }
                else {
                    manualMoveChessPiece(chosenSquareCoords, squareCoordinates);
                    chosenSquareCoords = null;
                    return;
                }
            }
            
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
            if((currentPlayer != null) && chosenSquareCoords != null && modelBoard.getBoard()[chosenSquareCoords.getX()][chosenSquareCoords.getY()].getColor() == currentPlayer.getColor() && currentPlayer.isCurrentlyPlaying()){
                //second click to move
                if(availableSquares.contains(square)){
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
    
    /**
     * updates chess clock display for players
     */
    public void updateClocks(){
        P1Time.setText(modelGame.getPlayers().get(0).getChessClock().getRemainingSeconds());
        P2Time.setText(modelGame.getPlayers().get(1).getChessClock().getRemainingSeconds());
    }
    
    /**
     * updates history display for game 
     */
    private void updateHistory(){
        history.setListData(modelGame.getGameBoard().getHistory().getMovesInPgn().toArray());
    }
    
    /**
     * repaints board from model
     */
    public void repaintFromModel(){
        if (manual) {
            if (manualSetUpDoneButton == null) {
                manualSetUpDoneButton = new JButton("Done Setting Up");
                manualSetUpDoneButton.setBackground(Color.red);
                this.p1.add(manualSetUpDoneButton);
                manualSetUpDoneButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manual = false;
                        frame.dispose();
                    }
                });
            }
        }
        else{
           updateHistory(); 
        }
        
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
                    catch(IOException e){
                        logger.log(Level.SEVERE, "Could not laod Chess piece image {0}", e.getMessage());
                    }  
                }
                else{
                    square.setIcon(null);
                }
            }
        }
    }
    
    /**
     * Updates square highlights of available moves
     */
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
    
    /**
     * initializes boardView components based on information form model
     */
    private void initComponents(){
        //create main frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setLayout(new BorderLayout(40, 20));
        
        //components of options and history part - left side
        
        optionsPanel = new JPanel(new BorderLayout(30, 60));
        
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
        
        JLabel p1Name = new JLabel(modelGame.getPlayers().get(1).getName() + "(" + modelGame.getPlayers().get(1).getColor() + ")");
        JLabel p2Name = new JLabel(modelGame.getPlayers().get(0).getName() + "(" + modelGame.getPlayers().get(0).getColor() + ")") ;
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

    /**
     *
     * @return returns current player who can move pieces
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player who can move pieces
     * @param currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    /**
     * Initializes game board (squares) base on information from modelBoard
     */
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
                        logger.log(Level.SEVERE, "Error finding path to piece image: ", ex);
                    }
                    try{
                      BufferedImage image = ImageIO.read(new File(fullImagePath));  
                      square.setIcon(new ImageIcon(image));
                    }
                    
                    catch(IOException e){
                       logger.log(Level.SEVERE, "Error loading chesspiece image: {0}", e);
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
    
    /**
     * Shows winner pop-up window for options
     * @param resultMsg end of game message
     * @return returns booleans for export game dialog
     */
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
    
    /**
     * 
     * @return returns result of saveDialog - that is path to save file
     */
    public String showSaveDialog(){
        int k = saveDialog.showSaveDialog(frame);
        if (k != JFileChooser.APPROVE_OPTION) {
            return "";
        }
        String path = saveDialog.getSelectedFile().getAbsolutePath();
        return path;
    }

    /**
     *
     * @return returns manual flag
     */
    public boolean isManual() {
        return manual;
    }

    /**
     * Sets manual flag
     * @param manual
     */
    public void setManual(boolean manual) {
        this.manual = manual;
    }

    /**
     * 
     * @return returns controller of this view
     */
    public ChessController getController() {
        return controller;
    }

    /**
     * Sets controller of this class
     * @param controller 
     */
    public void setController(ChessController controller) {
        this.controller = controller;
    }
    

}
