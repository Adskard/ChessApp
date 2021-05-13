
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 * TurnHistory is a class that records move history, that means movement of the pieces.
 * Where they were moved from, where they ended up and Pgn notation of the move.
 * @author Adam Škarda
 */
public class TurnHistory {
    
    //could add Board atribute in the future for past viewing
    private final ArrayList<ChessPiece> piecesMoved;
    private final ArrayList<Coordinates> destinations;
    private final ArrayList<Coordinates> startPos;
    private final ArrayList<String> movesInPgn;
    private int turn;
    
    /**
     * Class constructor
     */
    public TurnHistory(){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
        this.startPos = new ArrayList();
        this.movesInPgn = new ArrayList<>();
        this.turn = 0;
    }
    
    /**
     * Class copy constructor creates a new object and copies atribute values
     * @param original TurnHistory object to be copied from
     */
    public TurnHistory(TurnHistory original){
        this.piecesMoved = new ArrayList();
        this.destinations = new ArrayList();
        this.startPos = new ArrayList();
        this.movesInPgn = new ArrayList<>();
        this.destinations.addAll(original.getDestinations());
        this.piecesMoved.addAll(original.getPiecesMoved());
        this.turn = original.getTurn();
        this.movesInPgn.addAll(original.getMovesInPgn());
        this.startPos.addAll(original.getStartPos());
    }
    
    /**
     * Adds adds a move as an entry into TurnHistory
     * @param piece ChessPiece that was moved
     * @param dest Coordinates where the ChessPiece was moved
     * @param start ChessPiece starting coordintes
     */
    public void addEntry(ChessPiece piece, Coordinates dest, Coordinates start){
        this.piecesMoved.add(piece);
        this.destinations.add(dest);
        this.startPos.add(start);
        this.turn += piecesMoved.size() % 2;  
    }
    
    /**
     * Updates TurnHistory Pgn notation for easy game saving.
     * Uses PgnParser to encode the last move made from this TurnHistory into pgn.
     * Should be called from outside after the noted move is made and added to this TurnHistory.
     * 
     * @see PgnParser#encodeMoveToPgn()
     * @param board Current boarded state. Is used as a parameter for PgnParser. 
     * @param takes True if a piece was removed during the move.
     * @param castleQ True if the king has casteled queen side
     * @param castleK True if the king has casteled king side - should not be true if castleQ is true
     * @param promo True if a piece was promoted during the recorded move
     */
    public void updatePgnNotation(Board board, boolean takes, boolean castleQ, boolean castleK, boolean promo){
        ChessPiece piece = this.piecesMoved.get(piecesMoved.size() - 1);
        Coordinates dest = this.destinations.get(this.destinations.size() - 1);
        Coordinates start = this.startPos.get(this.startPos.size() - 1);
        
        this.movesInPgn.add(PgnParser.encodeMoveToPgn(piece, dest, start, board, takes, castleQ, castleK, promo));
    }
    
    /**
     *
     * @return String representation of this TurnHistory, based on destinations and piecesMoved
     */
    @Override
    public String toString(){
        String repr = "[";
        //piecesMoved has always the same size as destinations
        for (int i = 0; i < piecesMoved.size(); i++) {
            repr = repr + piecesMoved.get(i).toString().substring(0,2);
            repr = repr + destinations.get(i).toString();
            if(i +1 == piecesMoved.size()) {
                break;
            }
            repr = repr + ", ";
        }
        return repr + "]";
    }
    
    /**
     *
     * @return ArrayList of pieces moved
     */
    public ArrayList<ChessPiece> getPiecesMoved(){
        return this.piecesMoved;
    }

    /**
     *
     * @return ArrayList of destinations
     */
    public ArrayList<Coordinates> getDestinations(){
        return this.destinations;
    }

    /**
     *
     * @return ArrayList of Pgn encoded moves
     */
    public ArrayList<String> getMovesInPgn() {
        return movesInPgn;
    }
    
    /**
     *
     * @return ArrayList of start positions
     */
    public ArrayList<Coordinates> getStartPos() {
        return startPos;
    }

    /**
     *
     * @return Current turn in this TurnHistory
     */
    public int getTurn() {
        return turn;
    }
}
