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
public class CoordinatesTest {
    

    /**
     * Test of toString method, of class Coordinates.
     */
    @Test
    public void testToString_x0y3_1d() {
        //arrange
        Coordinates instance = new Coordinates(0,3);
        String expResult = "4a";
        //act
        String result = instance.toString();
        //assert
        assertEquals(expResult, result);
    }
    
}
