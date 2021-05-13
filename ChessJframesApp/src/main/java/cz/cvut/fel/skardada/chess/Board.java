
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;
import java.util.logging.*;
/**
 * Board is class that describes current state of the chess board. Its main purpose is to store information about the pieces in play and their position.
 * Its methods are used to change the state of the board.
 * That means moving the pieces already on the board, promoting them and implementing special movements (castles, en passant).
 * These changes to the board state are recorded in TurnHistory in this class.
 * @author Adam Škarda
 */
public class Board {
    private final int size;
    private final ChessPiece[][] board; 
    private final TurnHistory history; 
    private final ArrayList<ChessPiece> uniquePieces;
    private static final Logger logger = Logger.getLogger(Board.class.getName());
    private ArrayList<Coordinates> allCoordinates;

    /**
     * Constructor
     * @param size size of the square board
     * @param arrangement arrangement of pieces on the board
     */
    public Board(int size, ChessPiece[][] arrangement) {
        this.size = size;
        this.board = arrangement;
        this.history = new TurnHistory();
        this.uniquePieces = this.findUniques(arrangement);
        this.distributeIds();
    }
    

    /**
     * Copy constructor
     * @param original original board to be copied
     */
    public Board(Board original){
        this.history = new TurnHistory(original.getHistory());
        this.size = original.getSize();
        this.board = new ChessPiece[size][size];
        this.uniquePieces = new ArrayList();
        uniquePieces.addAll(original.getUniquePieces());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (original.getBoard()[i][j] == null) {
                    this.board[i][j] = null;
                    continue;
                }
                
                if(original.getBoard()[i][j] instanceof ChessPiecePawn){
                    this.board[i][j] = new ChessPiecePawn(original.getBoard()[i][j]);
                    continue;
                }
                if(original.getBoard()[i][j] instanceof ChessPieceKing){
                    this.board[i][j] = new ChessPieceKing(original.getBoard()[i][j]);
                }

                else
                {
                    this.board[i][j] = new ChessPieceImpl(original.getBoard()[i][j]);
                } 
            }
        } 
    }
    
    /**
     * Distributes unique ids to pieces on board
     * Should be called only once during initialization
     */
    private void distributeIds(){
        // each piece gets unique id
        int id = 0;
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if(piece != null){
                    piece.setId(id);
                    id++;
                }
            }
        }
    }
    
    /**
     * Updates available moves, checks and legal moves of pieces on the board
     */
    public void updatePieces(){
        updateAvailableMoves();
        updateChecks();
        updateLegalMoves();
    }
    
    /**
     * Updates available moves for every ChessPiece on the board
     */
    public void updateAvailableMoves(){
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){   
                if (piece != null) {
                    piece.setAvailableMoves(FutureStatesGen.calculateAvailableMoves(this, piece));
                }
            }
        }
    }
    
    /**
     * Updates legal moves for every ChessPiece on the board
     */
    public void updateLegalMoves(){
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if (piece != null) {
                    piece.setLegalMoves(FutureStatesGen.calculateLegalMoves(this, piece)); 
                } 
            }
        }
    }
    
    /**
     * Updates checks for every king on the board
     */
    public void updateChecks(){
        // find the king
        for(ChessPiece[] row : this.board){
            for(ChessPiece piece : row){
                if(piece == null){
                    continue;
                }
                if(piece instanceof ChessPieceKing){
                    // Check if checked
                    ((ChessPieceKing) piece).setChecked(FutureStatesGen.isChecked(this, piece));
                }
            }
        }
    }
    
    
    /**
     * Method for moving pieces on the board
     * @param pgnMove move encoded in pgn 
     * @param currentPlayer player who played the move
     * @throws Exception bubbled up from parsing with PgnParser
     */
    public void movePiece(String pgnMove, Player currentPlayer) throws Exception{
        this.updatePieces();
        ArrayList<ChessPiece> availablePieces = currentPlayer.getOwnPieces();
        Coordinates dest;
        Coordinates start;
        String pieceName;
        
        //solve castles
        if(PgnParser.isThisMoveAKingCastle(pgnMove) || PgnParser.isThisMoveAQueenCastle(pgnMove)){
            pieceName = "King";
            int rank = currentPlayer.getColor().equals(PlayerColors.black) ? 7 : 0;
            int file = 4;
            if (PgnParser.isThisMoveAKingCastle(pgnMove)) {
                dest = new Coordinates(rank, file + 2);
            }
            else{
                dest = new Coordinates(rank, file - 2);
            }
        }
        else{
            pieceName = PgnParser.parseMovedPiece(pgnMove);
            dest = PgnParser.getDestination(pgnMove);
        }
        
        //select all possible pieces with this destination
        ArrayList<ChessPiece> candidatePieces = new ArrayList<>();
        for(ChessPiece piece : availablePieces){
            if (piece.getName().toLowerCase().contains(pieceName.toLowerCase()) && piece.canGetTo(dest)) {
                candidatePieces.add(piece);
            }
        }
        
        //solve ambiguity
        ArrayList<ChessPiece> ambiguousPieces = new ArrayList<>();
        if(candidatePieces.size() > 1){  
            for (ChessPiece piece : candidatePieces) {
                int startCoordsOffset = piece.getName().toLowerCase().contains("pawn") ? 0 : 1;
                String firstAmbiguity = pgnMove.substring(startCoordsOffset,2 - (1 -startCoordsOffset));
                if (piece.getPosition().getFile().equals(firstAmbiguity)) {
                    continue;
                }
                else{
                    if(piece.getPosition().getRank().equals(firstAmbiguity)){
                        continue;
                    } 
                    else{
                        ambiguousPieces.add(piece);
                    }
                }
                
            }
        }
        
        candidatePieces.removeAll(ambiguousPieces);
        ambiguousPieces.clear();
        
        //for really ambiguous moves
        if(candidatePieces.size() > 1){
            String startCoords = pgnMove.substring(1, 3);
            for (ChessPiece piece : candidatePieces) {
                if (piece.getPosition().toString().equals(startCoords)) {
                    continue;
                }
                else{
                    ambiguousPieces.add(piece);
                }
            }
        }
        
        //remove ambiguous pieces
        candidatePieces.removeAll(ambiguousPieces);
        
        //check and move piece if ambiguity is solved
        if (candidatePieces.size() == 1) {
            this.movePiece(candidatePieces.get(0).getPosition(), dest);
        }
        
        //else there is a problem with pgn
        else{
            logger.log(Level.INFO, "{0} {1} {2}", new Object[]{pgnMove, pieceName, dest});
            throw new Exception("Pgn parsing ambiguity error: " + candidatePieces.toString());
        }
        
        //check promotions
        String promotion = PgnParser.isThisMoveAPromotion(pgnMove);
        if (promotion != null) {
            for(ChessPiece newRank : this.uniquePieces){
                if(newRank.getName().toLowerCase().contains(PgnParser.findPieceForPgnNotation(promotion).toLowerCase())){
                   this.promotion(candidatePieces.get(0), newRank); 
                }
            }
        }
    }
 
    /**
     * Move piece anywhere on the board - for manual set up
     * @param startPos initial position of moving piece
     * @param dest destination of moving piece
     */
    public void movePieceAnywhere(Coordinates startPos, Coordinates dest){
        
        if(this.getChessPieceAtCoordinate(startPos) == null){
            return;
        }
        ChessPiece movingPiece = this.getChessPieceAtCoordinate(startPos);
        if(this.getChessPieceAtCoordinate(dest) == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        
        //moving to an occupied square
        else{
            this.getChessPieceAtCoordinate(dest).setPosition(new Coordinates(-1,-1));
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
    }
    
    /**
     * Move the piece on the board according to chess rules
     * @param startPos initial position of the moving piece
     * @param dest destination of the moving piece
     */
    public void movePiece(Coordinates startPos, Coordinates dest){
        
        //help for notation
        boolean takes = false;
        boolean castleQ = false;
        boolean castleK = false;
        boolean promotion = false;
        
        //trying to move nonexistent piece
        if(this.getChessPieceAtCoordinate(startPos) == null){
            return;
        }
        ChessPiece movingPiece = this.getChessPieceAtCoordinate(startPos);
        
        //enpassant
        if (movingPiece instanceof ChessPiecePawn && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece) != null && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece).equals(dest)) {
            //remove enPassanted pawn
            Coordinates enemyPawnCoord = new Coordinates(dest.getX() - movingPiece.getMoveSet().getMoveVectors()[0].getX(),dest.getY());
            this.getChessPieceAtCoordinate(enemyPawnCoord).setPosition(new Coordinates(-1,-1));
            this.board[enemyPawnCoord.getX()][enemyPawnCoord.getY()] = null; 
            takes = true;
        }
        
        //move rook for castles
        if (movingPiece instanceof ChessPieceKing && !FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece).isEmpty()) {
            ArrayList<Coordinates> castleLocations = FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece);
            int distanceTraveledByKing = 2;
            
            //TODO - NAIVE IMPLEMENTATION - make it beter next time (generalize)
            for(Coordinates newKingCoord : castleLocations){
                if(newKingCoord.equals(dest)){
                    int rookY = 7;
                    ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                    //king side
                    if(rook != null && dest.getY() == movingPiece.getPosition().getY() + distanceTraveledByKing){
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() + distanceTraveledByKing - 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);
                        castleK = true;
                    }
                    rookY = 0;
                    rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                    //Queen side
                    if(rook != null && dest.getY() == movingPiece.getPosition().getY() - distanceTraveledByKing){
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() - distanceTraveledByKing + 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);
                        castleQ = true;
                    }
                }
            }  
        }
        //moving to an empty square
        
        if(this.getChessPieceAtCoordinate(dest) == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        
        //moving to an occupied square
        else{
            if (this.getChessPieceAtCoordinate(dest).getColor() != movingPiece.getColor()) {
                this.getChessPieceAtCoordinate(dest).setPosition(new Coordinates(-1,-1));
                this.board[dest.getX()][dest.getY()] = movingPiece;
                movingPiece.setPosition(dest);
                this.board[startPos.getX()][startPos.getY()] = null;
                takes = true;
            }
        }
        
        //Promotion
        if (movingPiece instanceof ChessPiecePawn && (movingPiece.getPosition().getX() == this.size -1 || movingPiece.getPosition().getX() == 0)) {
            ChessPiecePawn promotable = (ChessPiecePawn) movingPiece;
            promotable.setCanBePromoted(true);
            promotion = true;
        }
        //update history for enPassants and castle calculations
        history.addEntry(movingPiece, dest, startPos);
        updatePieces();
        
        //updatePgnNotation
        history.updatePgnNotation(this, takes, castleQ, castleK, promotion);
    }
    
    /**
     * Help method for generating future states
     * @param startPos initial position of the moving piece
     * @param dest destination of the moving piece
     */
    public void movePieceWithoutUpdatingLegalMoves(Coordinates startPos, Coordinates dest){

        //trying to move nonexistent piece
        if(this.getChessPieceAtCoordinate(startPos) == null){
            return;
        }
        ChessPiece movingPiece = this.getChessPieceAtCoordinate(startPos);
        
        //enpassant
        if (movingPiece instanceof ChessPiecePawn && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece) != null && FutureStatesGen.checkEnPassant(this, (ChessPiecePawn)movingPiece).equals(dest)) {
            //remove enPassanted pawn
            Coordinates enemyPawnCoord = new Coordinates(dest.getX() - movingPiece.getMoveSet().getMoveVectors()[0].getX(),dest.getY());
            this.getChessPieceAtCoordinate(enemyPawnCoord).setPosition(new Coordinates(-1,-1));
            this.board[enemyPawnCoord.getX()][enemyPawnCoord.getY()] = null; 
        }
        
        //move rook for castles
        if (movingPiece instanceof ChessPieceKing && !FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece).isEmpty()) {
            ArrayList<Coordinates> castleLocations = FutureStatesGen.checkCastle(this, (ChessPieceKing)movingPiece);
            int distanceTraveledByKing = 2;
            
            //TODO - NAIVE IMPLEMENTATION REDO
            for(Coordinates newKingCoord : castleLocations){
                if(newKingCoord.equals(dest)){
                    
                    //queen side
                    if(dest.getY() == movingPiece.getPosition().getY() + distanceTraveledByKing){
                        int rookY = 7;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() + distanceTraveledByKing - 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);

                    }
                    
                    //king side
                    if(dest.getY() == movingPiece.getPosition().getY() - distanceTraveledByKing){
                        int rookY = 0;
                        ChessPiece rook = this.getChessPieceAtCoordinate(new Coordinates(newKingCoord.getX(), rookY));
                        this.board[rook.getPosition().getX()][rook.getPosition().getY()] = null;
                        Coordinates rookDest = new Coordinates(newKingCoord.getX(), movingPiece.getPosition().getY() - distanceTraveledByKing + 1);
                        this.board[rookDest.getX()][rookDest.getY()] = rook;
                        rook.setPosition(rookDest);

                    }
                }
            }  
        }
        //moving to an empty square
        
        if(this.getChessPieceAtCoordinate(dest) == null){
            this.board[dest.getX()][dest.getY()] = movingPiece;
            movingPiece.setPosition(dest);
            this.board[startPos.getX()][startPos.getY()] = null;
        }
        
        //moving to an occupied square
        else{
            if (this.getChessPieceAtCoordinate(dest).getColor() != movingPiece.getColor()) {
                this.getChessPieceAtCoordinate(dest).setPosition(new Coordinates(-1,-1));
                this.board[dest.getX()][dest.getY()] = movingPiece;
                movingPiece.setPosition(dest);
                this.board[startPos.getX()][startPos.getY()] = null;

            }
        }
        
        //Promotion
        if (movingPiece instanceof ChessPiecePawn && (movingPiece.getPosition().getX() == this.size -1 || movingPiece.getPosition().getX() == 0)) {
            ChessPiecePawn promotable = (ChessPiecePawn) movingPiece;
            promotable.setCanBePromoted(true);

        }
        //update history for enPassants and castle calculations
        history.addEntry(movingPiece, dest, startPos);
        
        updateAvailableMoves();
        updateChecks();
    }
    
    /**
     * 
     * @param x rank coordinate
     * @param y file coordinate
     * @return returns chessPiece at given position returns null if postion is out of bounds
     */
    public ChessPiece getChessPieceAtPosition(int x, int y){
        //check index out of bounds
        if(x >= this.size || y >= this.size || y<0 || x<0){
            return null;
        }
        else{
            return this.board[x][y];
        }
    }
    
    /**
     * Method used for pawn promotion
     * @param promoted piece that is to be promoted
     * @param newRank rank the piece will be promoted to
     * @return
     */
    public ChessPiece promotion(ChessPiece promoted, ChessPiece newRank){
        Coordinates coords = promoted.getPosition();
        int id = promoted.getId();
        ChessPiece promotedPiece = new ChessPieceImpl(newRank);
        promotedPiece.setId(id);
        promotedPiece.setPosition(coords);
        promotedPiece.setColor(promoted.getColor());
        this.getBoard()[coords.getX()][coords.getY()] = promotedPiece;
        
        promoted = promotedPiece;
        
        this.updatePieces();
        logger.log(Level.FINER, "PROMOTION TO {0} {1}", new Object[]{coords, promoted.getName()});
        //update history
        String movePgn = this.getHistory().getMovesInPgn().get(this.getHistory().getMovesInPgn().size() - 1);
        movePgn += PgnParser.findPgnNotationForPiece(promoted.getName());
        this.getHistory().getMovesInPgn().remove(this.getHistory().getMovesInPgn().size() - 1);
        this.getHistory().getMovesInPgn().add(movePgn);
        return promotedPiece;
    }
    
    /**
     * find all unique chessPieces on the board
     * @param board current state of the board
     * @return  returns ArrayList of unique ChessPieces
     */
    
    public ArrayList<ChessPiece> findUniques(ChessPiece[][] board){
        ArrayList<ChessPiece> unique = new ArrayList<>();
        for(ChessPiece[] row : board){
            for(ChessPiece piece : row){
                if (piece == null) {
                    continue;
                }
                
                //search prior occurences
                boolean contains = true;
                for(ChessPiece uniquePiece : unique){
                    if(piece.getName().equals(uniquePiece.getName())){
                        contains = false;
                    }
                }
                //dont want kings or pawns
                if(contains && piece instanceof ChessPieceImpl){
                    unique.add(piece);
                }
            }
        }
        return unique;
    }
    
    /**
     *
     * @param coord coordinates on the board
     * @return returns chessPiece at given coordinates
     */
    public ChessPiece getChessPieceAtCoordinate(Coordinates coord){
        return this.getChessPieceAtPosition(coord.getX(), coord.getY());
    }
    
    /**
     *
     * @return returns board size
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return returns board piece arrangement
     */
    public ChessPiece[][] getBoard() {
        return board;
    }

    /**
     *
     * @return returns board move history
     */
    public TurnHistory getHistory() {
        return history;
    }
    
    /**
     *
     * @return returns unique pieces present on the board
     */
    public ArrayList<ChessPiece> getUniquePieces() {
        return uniquePieces;
    }
    
}
