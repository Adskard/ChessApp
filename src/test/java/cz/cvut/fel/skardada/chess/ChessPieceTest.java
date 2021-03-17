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
        Coordinates pos = new Coordinates(4,4);
        ChessPiece instance = new ChessPieceKing(new Coordinates(3,3),PlayerColors.black);
        boolean expResult = true;
        boolean result = instance.canGetTo(pos);
        assertEquals(expResult, result);
    }
    @Test
    public void testCanGetTo_King33to55_false() {
        Coordinates pos = new Coordinates(5,5);
        ChessPiece instance = new ChessPieceKing(new Coordinates(3,3),PlayerColors.black);
        boolean expResult = false;
        boolean result = instance.canGetTo(pos);
        assertEquals(expResult, result);
    }
    @Test
    public void testAvailableMoves_pawn33_34(){
        ChessPiecePawn instance = new ChessPiecePawn(new Coordinates(3,3), PlayerColors.white);
        int boardSize = 8;
        Coordinates[] expResult = new Coordinates[boardSize*boardSize];
        expResult[0] = new Coordinates(3,4);
        Coordinates[] moves = instance.availableMoves(boardSize);
        assertArrayEquals(expResult, moves);
    }
}
