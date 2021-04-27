/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import junit.framework.TestCase;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

/**
 *
 * @author Adam Škarda
 */
public class FutureStatesGenTest extends TestCase {
    
    public FutureStatesGenTest(String testName) {
        super(testName);
    }

    public void testIsChecked() {
    }

    public void testCalculateLegalMoves() {
    }

    public void testCheckEnPassant() {
    }

    public void testCheckCastle() {
    }

    public void testCheckPromotion() {
    }

    public void testCheckDoublePawn() {
    }

    public void testCheckPawnTakes() {
    }

    public void testCalculateAvailableMoves() {
        FutureStatesGen gen = new FutureStatesGen();
        Board mockBoard = Mockito.mock(Board.class);
        ChessPiece[][] board = new ChessPiece[4][4];
        ChessPiece p = new ChessPieceKing(new Coordinates(0,0),PlayerColors.black);
        board[0][0] = p;
        Mockito.when(mockBoard.getBoard()).thenReturn(board);
        Mockito.when(mockBoard.getSize()).thenReturn(4);
        gen.calculateAvailableMoves(mockBoard);
        System.out.println(p.getAvailableMoves().toString());
    }
    
}
