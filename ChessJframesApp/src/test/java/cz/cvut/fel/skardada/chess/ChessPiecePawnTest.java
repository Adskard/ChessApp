
package cz.cvut.fel.skardada.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test contructors of ChessPiecePawn class
 * @author Adam Škarda
 */
public class ChessPiecePawnTest {

    /**
     * Test of constructor creating white pawn
     */
    @Test
    public void testConstructor_whitePawn_vector10Distance1(){
        //arrange
        Coordinates pos = new Coordinates(1,1);
        Coordinates[] expVectors = new Coordinates[]{new Coordinates(1,0)};
        int expDistance = 1;
        PlayerColors color = PlayerColors.white;
        
        //act
        ChessPiecePawn pawn = new ChessPiecePawn(pos,color);
        
        //assert
        assertArrayEquals(expVectors, pawn.getMoveSet().getMoveVectors());
        assertEquals(expDistance, pawn.getMoveSet().getMoveDistance());
    }
    /**
     * Test of constructor creating black pawn
     */
     @Test
    public void testConstructor_blackPawn_vectorNeg10Distnace1(){
        //arrange
        Coordinates pos = new Coordinates(1,1);
        PlayerColors color = PlayerColors.black;
        Coordinates[] expVectors = new Coordinates[]{new Coordinates(-1,0)};
        int expDistance = 1;
        
        //act
        ChessPiecePawn pawn = new ChessPiecePawn(pos,color);
        
        //assert
        assertArrayEquals(expVectors, pawn.getMoveSet().getMoveVectors() );
        assertEquals(expDistance, pawn.getMoveSet().getMoveDistance());
    }
}
