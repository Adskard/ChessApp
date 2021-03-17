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
public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x,int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString(){
        String str = new String();
        int offset = 97;
        str = Integer.toString((this.y + 1)) +  (char)(this.x + offset);
        return str;
    }
    @Override
    public int hashCode(){
        return (this.x * 321654 + this.y * 1234566 )% 12582917;
    }
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
    public Coordinates getSum(Coordinates added){
        Coordinates result = new Coordinates(this.x+added.getX(),this.y+added.getY());
        return result;
    }
    
    public Coordinates getProduct(int number){
        return new Coordinates(this.x*number,this.y*number);
    }
    public int getIntRepr(int boardSize){
        return this.x + this.y * boardSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
}
