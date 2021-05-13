/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Adam Škarda
 */
public class CoordinatesTest {
    

    /**
     * Test of toString method, of class Coordinates.
     */
    @Test
    public void testToString_Coordinates03_StringReslut1d() {
        //arrange
        Coordinates instance = new Coordinates(0,3);
        String expResult = "d1";
        //act
        String result = instance.toString();
        //assert
        assertEquals(expResult, result);
    }
    
    @Test
    public void testConstructor_Coordinatesb5_CoordinatesX6Y1(){
        //arrange
        String coord = "b5";
        int expectedX = 4;
        int expectedY = 1;
        Coordinates instance = null;
        //act
        try {
            instance = new Coordinates(coord);
        } catch (Exception ex) {
            fail(coord + " " + ex.getMessage());
        }
        //assert   
        assertEquals(expectedX, instance.getX());
        assertEquals(expectedY, instance.getY());
    }
    
    @Test
    public void testConstructor_IncorrectCoordinatesBbb1_throwsException(){
        //arrange 
        String move = "Bbb1";
        
        //act & assert
        assertThrows(Exception.class, () -> new Coordinates(move));
    }
}
