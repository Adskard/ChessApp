/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

/**
 *
 * @author Adam Škarda
 */
public class Coordinates implements java.io.Serializable{
    private final int x;
    private final int y;

    /**
     *
     * @param x
     * @param y
     */
    public Coordinates(int x,int y) {
        this.x = x; //row
        this.y = y; //column
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){
        String str;
        int offset = 97;
        str =  (char)(offset + this.y) + Integer.toString((this.x + 1));
        return str;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode(){
        return (this.x * 321654 + this.y * 1234566 )% 12582917;
    }

    /**
     *
     * @param o
     * @return
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
     * @param added coordinate object to be added
     * @return new Coordinate objact that is sum of this and added Coordinates
     */
    public Coordinates getSum(Coordinates added){
        Coordinates result = new Coordinates(this.x+added.getX(),this.y+added.getY());
        return result;
    }
    
    /**
     *
     * @param number coordinate multiplier
     * @return new Coordinate object that is a product of original * number
     */
    public Coordinates getProduct(int number){
        return new Coordinates(this.x*number,this.y*number);
    }

    /**
     *
     * @param boardSize size of game board
     * @return coordinate integer representation
     */
    public int getIntRepr(int boardSize){
        return this.x + this.y * boardSize;
    }

    /**
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
    
}