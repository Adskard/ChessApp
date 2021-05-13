
package cz.cvut.fel.skardada.chess;

/**
 * ChessStyle a class that describes the variant of chess game. It is mainly about starting board arrangement.
 * @author Adam Škarda
 */
public class ChessStyle implements java.io.Serializable{
    private final int boardSize;
    private final ChessPiece[][] boardArrangement;
    private final String name;
    
    /**
     * @param boardSize size of the game board
     * @param boardArrangement arrangement of pieces on the board
     * @param name name of the style
     * @throws IncorrectLenghtException throws exceptions if boardArrangement does not suit the board size
     */
    public ChessStyle(int boardSize, ChessPiece[][] boardArrangement, String name) throws IncorrectLenghtException{
        if(boardArrangement.length != boardSize){
            throw new IncorrectLenghtException("Board Size: " + Integer.toString(boardSize) + 
                " does not equal boardArrangement lenght: " + Integer.toString(boardArrangement.length));
        }
        else{
            for (int i = 0; i < boardSize; i++) {
                if (boardArrangement[i].length != boardSize) {
                    throw new IncorrectLenghtException(
                        "Board Size: " + Integer.toString(boardSize) + 
                        " does not equal boardArrangement lenght: " + Integer.toString(boardArrangement[i].length));
                }
            }
        }
        this.boardArrangement = boardArrangement;
        this.boardSize = boardSize;
        this.name = name;
    }

    /**
     *
     * @return returns size of the board
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     *
     * @return returns arrangement of ppieces on board
     */
    public ChessPiece[][] getBoardArrangement() {
        return boardArrangement;
    }

    /**
     *
     * @return returns name of the style
     */
    public String getName() {
        return name;
    }
    
}
