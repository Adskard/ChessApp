/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

/**
 *
 * @author Adam Škarda
 */
import java.util.*;

public class Game {
    private final Board gameBoard;
    private final ArrayList<Player> players;
    private final ChessStyle style;

    public Game(ChessStyle style, ArrayList<Player> players) {
        this.style = style;
        this.gameBoard = new Board(this.style.getBoardSize(),this.style.getBoardArrangement());
        this.gameBoard.updatePieces();
        this.players = players;
    }
    
    public Game(Board board, ArrayList<Player> players){
        this.style = null;
        this.gameBoard = board;
        this.gameBoard.updatePieces();
        this.players = players;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ChessStyle getStyle() {
        return style;
    }

    
}
