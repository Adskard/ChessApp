
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 * ChessPiece is an abstract class that stores informetion about a given chess piece. Mainly how they can move (MoveSet), where they are (Coordinates),
 * where they can go and where they cant go - legal (moves that dont result in check on king of same color) and available moves.
 * @author Adam Škarda
 */
public abstract class ChessPiece implements java.io.Serializable{
    private final MoveSet moveSet;
    private final String name;
    private Coordinates position;
    private PlayerColors color;
    private ArrayList<Coordinates> availableMoves;
    private ArrayList<Coordinates> legalMoves;
    private int id;
    private final String imagePath;
    
    /**
     * Standard constructor
     * @param name piece names
     * @param pos position of the piece on the board
     * @param color piece color
     * @param mov move set of this piece
     * @param imagePath path to image that represents this piece, should be relative and
     * target resources of this project
     */
    public ChessPiece(String name,Coordinates pos, PlayerColors color, MoveSet mov, String imagePath) {
        this.position = pos;
        this.name = name;
        this.color = color;
        this.moveSet = mov;
        this.availableMoves = new ArrayList();
        this.legalMoves = new ArrayList();
        this.imagePath = imagePath;
    }
    

    /**
     * Copy constructor
     * @param piece original piece to be copied
     */
    public ChessPiece(ChessPiece piece){
        this.moveSet = piece.getMoveSet();
        this.name = piece.getName();
        this.id = piece.getId();
        this.imagePath = piece.imagePath;
        this.color = piece.getColor();
        this.position = piece.getPosition();
        this.availableMoves = piece.availableMoves;
        this.legalMoves = new ArrayList();
    }

    /**
     *
     * @return returns piece id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Two ChessPieces are equal if they have the same id
     * @param o compared object
     * @return returns true if o is both ChessPiece and has the same id
     */
    @Override
    public boolean equals(Object o ){
        if(o == null){
            return false;
        }
        if(! (o instanceof ChessPiece)){
        return false;
        }
        ChessPiece p = (ChessPiece) o;
        return p.getId() == this.getId();
    }

    /**
     * hash based on id
     * @return returns this object hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        return hash;
    }

    /**
     * Sets id of this piece
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Checks if piece can get to a Coordinate
     * @param dest destination coordinate of possible piece move
     * @return returns true if destination is contained in available moves of this piece
     */
    public boolean canGetTo(Coordinates dest){
        for (Coordinates pos : this.availableMoves) {
            if(dest.equals(pos)){
                return true;
            }
        }
        return false;
    }
    
    /**
     *
     * @return returns pieces position
     */
    public Coordinates getPosition() {
        return position;
    }
    
    /**
     *
     * @return returs string representation of this piece
     */
    @Override
    public String toString(){
        return this.name;
    }
    
    /**
     * Sets positon of this piece to given coordinates
     * @param position
     */
    public void setPosition(Coordinates position) {
        this.position = position;
    }

    /**
     *
     * @return returns this pieces moveSet
     */
    public MoveSet getMoveSet() {
        return moveSet;
    }

    /**
     *
     * @return returns color of this piece
     */
    public PlayerColors getColor() {
        return color;
    }

    /**
     *
     * @return returns this piece name (functionally same as toString)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets color of this piece
     * @param color 
     */
    public void setColor(PlayerColors color) {
        this.color = color;
    }

    /**
     *
     * @return returns moves available to this piece
     */
    public ArrayList<Coordinates> getAvailableMoves() {
        return availableMoves;
    }

    /**
     * sets Available moves of this piece
     * @param availableMoves
     */
    public void setAvailableMoves(ArrayList<Coordinates> availableMoves) {
        this.availableMoves = availableMoves;
    }

    /**
     *
     * @return returns ArrayList of legal moves that are available to this piece
     */
    public ArrayList<Coordinates> getLegalMoves() {
        return legalMoves;
    }

    /**
     * Sets legal moves for this piece
     * @param legalMoves
     */
    public void setLegalMoves(ArrayList<Coordinates> legalMoves) {
        this.legalMoves = legalMoves;
    }

    /**
     *
     * @return returns path to this piece image
     */
    public String getImagePath() {
        return imagePath;
    }
}
