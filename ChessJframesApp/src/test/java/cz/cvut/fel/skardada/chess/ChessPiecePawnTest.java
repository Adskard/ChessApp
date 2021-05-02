/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Adam Škarda
 */
public class ChessPiecePawnTest {

    /**
     * Test of doublePass method, of class ChessPiecePawn.
     */
    @Test
    public void testConstructor_white_vector01(){
        //arrange
        Coordinates pos = new Coordinates(1,1);
        Coordinates[] expRes = new Coordinates[]{new Coordinates(1,0)};
        PlayerColors color = PlayerColors.white;
        //act
        ChessPiecePawn pawn = new ChessPiecePawn(pos,color);
        //assert
        assertArrayEquals(expRes, pawn.getMoveSet().getMoveVectors() );
    }
    /**
     * Test of doublePass method, of class ChessPiecePawn.
     */
     @Test
    public void testConstructor_black_vector0neg1(){
        //arrange
        Coordinates pos = new Coordinates(1,1);
        PlayerColors color = PlayerColors.black;
        Coordinates[] expRes = new Coordinates[]{new Coordinates(-1,0)};
        //act
        ChessPiecePawn pawn = new ChessPiecePawn(pos,color);
        //assert
        assertArrayEquals(expRes, pawn.getMoveSet().getMoveVectors() );
    }
    
    
    @Test
    public void testConstructor_coorectFilePathToImg_imgInResources(){
        //arrange
        ProcessBuilder pb = new ProcessBuilder("resources/piece_blackPawn.ser");
        Class clas = ChessPiecePawn.class;
        URL path = ChessPiecePawn.class.getResource("/piece_blackRook.ser");
        System.out.println(path);
        //act
        //ChessPiecePawn pawn = new ChessPiecePawn(pos,color);
        //assert
        //assertEquals(expRes, pawn.getMoveSet().getMoveVectors() );
    }
}
