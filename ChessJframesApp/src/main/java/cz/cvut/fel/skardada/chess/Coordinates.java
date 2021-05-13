
package cz.cvut.fel.skardada.chess;

/**
 *
 * @author Adam Škarda
 */
public class Coordinates implements java.io.Serializable{
    private final int x;
    private final int y;
    private final int offset;
    /**
     * Creates a serializable object that stores coordinates
     * 
     * @param x Row/rank number starting from 0
     * @param y Column/file number starting from 0
     */
    public Coordinates(int x,int y) {
        this.x = x; //row
        this.y = y; //column
        this.offset = 97; //char offset
    }
    /**
     * Creates a serializable object that stores coordinates
     * 
     * @param pgn Pgn notation of coordinate (eg. d2)
     * @throws Exception if the supplied pgn isnt of proper lenght (2)
     */
    public Coordinates(String pgn) throws Exception{
        this.offset = 97;
        if(pgn.length() != 2){
            throw new Exception("Incorrect coordinate format");
        }
        else{
            this.x = Integer.parseInt(pgn.substring(1)) - 1;
            this.y = (int) pgn.charAt(0) - offset;
        }
    }

    /**
     *
     * @return String representation of Coordinates as file + rank 
     */
    @Override
    public String toString(){
        String str;
        str =  (char)(offset + this.y) + Integer.toString((this.x + 1));
        return str;
    }

    /**
     *
     * @return Unique hash for these Coordinates
     */
    @Override
    public int hashCode(){
        return (this.x * 321654 + this.y * 1234566 )% 12582917;
    }

    /**
     *
     * @param o Compared object
     * @return Returns true if the coordinates are identical, else false
     */
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(!(o instanceof Coordinates)){
            return false;
        }
        Coordinates c = (Coordinates) o;
        if(c.getX() == this.getX() && c.getY()== this.getY()){
            return true;
        }
        return false;
    }
    
    /**
     *
     * @return String representation of coordinate rank
     */
    public String getRank(){
        return Integer.toString(this.x+1);
    }
    
    /**
     *
     * @return String representation of coordinate file
     */
    public String getFile(){
        return Character.toString((char)(offset + this.y));
    }

    /**
     *
     * @param added Coordinate object to be added
     * @return New Coordinate object that is sum of this and added Coordinates
     */
    public Coordinates getSum(Coordinates added){
        Coordinates result = new Coordinates(this.x+added.getX(),this.y+added.getY());
        return result;
    }
    
    /**
     *
     * @param number Coordinate multiplier
     * @return New Coordinate object that is a product of original * number
     */
    public Coordinates getProduct(int number){
        return new Coordinates(this.x*number,this.y*number);
    }

    /**
     *
     * @param boardSize Size of game board
     * @return Coordinate integer representation for coordinates
     */
    public int getIntRepr(int boardSize){
        return this.x + this.y * boardSize;
    }

    /**
     *
     * @return X coordinate - represented by integer
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return Y coordinate - represented by integer
     */
    public int getY() {
        return y;
    }
    
}
