
package cz.cvut.fel.skardada.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.util.ArrayList;
import org.mockito.MockedStatic;
/**
 * Test Class Board
 * @author Adam Škarda
 */
public class BoardTest {
    Board emptyBoard;
    ChessPiece[] emptyRow;
    ChessPiece[][] emptyArrangement;
    int boardSize;
    
    /**
     * Set up Empty board before each test
     */
    @BeforeEach
    public void setUp(){
        boardSize = 8;
        emptyRow = new ChessPiece[boardSize];
        emptyArrangement = new ChessPiece[boardSize][boardSize];
        this.emptyBoard = new Board(boardSize, emptyArrangement);
    }
    
    /**
     * Tests findUniques().
     * Test scenario - methods searches for unique ChessPieces on empty board.
     * Expected result - method returns empty arraylist - there no (unique) pieces.
     */
    
    @Test
    public void findUniques_findUniqueChessPiecesOnEmptyBoard_returnsEmptyArrayList(){
        //arrange
        boolean expectedResult = true;
        //act
        ArrayList pieces = emptyBoard.findUniques(emptyBoard.getBoard());
        boolean piecesIsEmpty = pieces.isEmpty();
        //assert
        assertEquals(expectedResult, piecesIsEmpty);
    }
    
    /**
     * Tests findUniques().
     * Test scenario - method searches for unique ChessPieces on board with:
     * 2 black kings, 1 white pawn, 2 black rooks, 1 white rook, 1 white bishop
     * Expected result - method returns ArrayList of 1 black rook, 1 white rook, 1 white bishop
     */
    @Test
    public void findUniques_findUniqueChessPieces2BlackKings1WhitePawn2BlackRooks1WhiteRook1whiteBishop_ArrayListOf1BlackRook1WhiteRook1WhiteBishop(){
        //arrange
        ChessPieceImpl blackRook = Mockito.mock(ChessPieceImpl.class);
        Mockito.when(blackRook.getName()).thenReturn("blackRook");
        ChessPieceImpl whiteRook = Mockito.mock(ChessPieceImpl.class);
        Mockito.when(whiteRook.getName()).thenReturn("whiteRook");
        ChessPieceImpl whiteBishop = Mockito.mock(ChessPieceImpl.class);
        Mockito.when(whiteBishop.getName()).thenReturn("whiteBishop");
        
        ArrayList<ChessPiece> expResult = new ArrayList<>();
        expResult.add(blackRook);
        expResult.add(whiteBishop);
        expResult.add(whiteRook);
        
        ChessPiece[] row1 = new ChessPiece[]{new ChessPieceKing(null, PlayerColors.black), blackRook, null};
        ChessPiece[] row2 = new ChessPiece[]{new ChessPiecePawn(null, PlayerColors.white), whiteBishop, blackRook};
        ChessPiece[] row3 = new ChessPiece[]{null, new ChessPieceKing(null, PlayerColors.black), whiteRook};
        Board customBoard = new Board(3, new ChessPiece[][]{
            row1, row2, row3
        });
        //act
        ArrayList uniquePieces = customBoard.findUniques(customBoard.getBoard());
        //assert
        assertArrayEquals(expResult.toArray(), uniquePieces.toArray());
    }
    
    /**
     * Tests movePiece(Coordinates, Coordinates).
     * Test scenario - method moves king on 43 to empty square on 33
     * Expected result - king has coordinates 33, board square 43 is empty and 33 has king on it
     */
    @Test
    public void movePiece_MoveChessPieceKingOn43ToAnEmptySquare33_33IsOccupiedByKing43IsEmptyAndKingHasPosition33(){
        //arrange
        Coordinates start = new Coordinates(4,3);
        Coordinates dest = new Coordinates(3,3);
        ChessPieceKing king = new ChessPieceKing(start, PlayerColors.white);
        emptyArrangement[start.getX()][start.getY()] = king;
        Board board = new Board(boardSize, emptyArrangement);
        
        //act
        board.movePiece(start, dest);
        
        //assert
        assertEquals(king, board.getChessPieceAtCoordinate(dest));
        assertEquals(null, board.getChessPieceAtCoordinate(start));
        assertEquals(dest, king.getPosition());
    }
    
    /**
     * Tests movePiece(Coordinates, Coordinates).
     * Test scenario - method moves black king on 64 square to 63, which is occupied by white pawn
     * Expected result - king has position 63, 64 is empty, pawn is not on the board
     */
    @Test
    public void movePiece_MoveChessPieceKingOn64ToASquare63OccupiedByChessPiecePawn_KingHasPosition63PawnIsNotOnTheBoard(){
        //arrange
        Coordinates start = new Coordinates(6,4);
        Coordinates dest = new Coordinates(6,3);
        ChessPieceKing king = new ChessPieceKing(start, PlayerColors.black);
        ChessPiecePawn pawn = new ChessPiecePawn(dest, PlayerColors.white);
        
        emptyArrangement[start.getX()][start.getY()] = king;
        emptyArrangement[dest.getX()][dest.getX()] = pawn;
        Board board = new Board(boardSize, emptyArrangement);
        
        //act
        board.movePiece(start, dest);
        //assert
        
        assertEquals(king, board.getChessPieceAtCoordinate(dest));
        assertEquals(null, board.getChessPieceAtCoordinate(start));
        assertEquals(dest, king.getPosition());
    }
    

}
