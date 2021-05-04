/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Adam Škarda
 */
public class ChessPieceTest {
    

    /**
     * Test of canGetTo method, of class ChessPiece.
     */
    @Test
    public void testCanGetTo_King33to44_true() {
        //arrange
        Coordinates pos = new Coordinates(4,4);
        ChessPiece instance = new ChessPieceKing(new Coordinates(3,3),PlayerColors.black);
        boolean expResult = true;
        
        //act
        boolean result = instance.canGetTo(pos);
        
        //assert
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCanGetTo_King33to55_false() {
        //arrange
        Coordinates pos = new Coordinates(5,5);
        ChessPiece instance = new ChessPieceKing(new Coordinates(3,3),PlayerColors.black);
        boolean expResult = false;
        
        //act
        boolean result = instance.canGetTo(pos);
        
        //assert
        assertEquals(expResult, result);
    }
}
