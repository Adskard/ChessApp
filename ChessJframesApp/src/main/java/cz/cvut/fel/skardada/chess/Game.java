
package cz.cvut.fel.skardada.chess;

import java.util.*;
/**
 * Game is a class that describes the state of the game - board state and players.
 * @author Adam Škarda
 */
public class Game {
    private final Board gameBoard;
    private final ArrayList<Player> players;
    private final ChessStyle style;

    /**
     * Constructor
     * @param style style in which the game is played dictates the board
     * @param players players who play the game
     */
    public Game(ChessStyle style, ArrayList<Player> players) {
        this.style = style;
        this.gameBoard = new Board(this.style.getBoardSize(),this.style.getBoardArrangement());
        this.gameBoard.updatePieces();
        this.players = players;
    }

    
    /**
     * 
     * @return returns game Board
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     *
     * @return returns games players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     *
     * @return returns game style
     */
    public ChessStyle getStyle() {
        return style;
    }

    
}
