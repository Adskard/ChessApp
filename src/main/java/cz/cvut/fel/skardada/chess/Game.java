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
    private final Player[] players;
    private final ChessStyle style;
    private ArrayList<String> turnHistory;
    private int currPlayerIndex;

    public Game(ChessStyle style) {
        this.style = style;
        this.gameBoard = new Board(this.style.getBoardSize(),this.style.getBoardArrangement());
        this.players = new Player[2];
        this.turnHistory = new ArrayList<String>();
        this.currPlayerIndex = 0;
    }
    
    public void nextTurn(){
        Player currPlayer = players[currPlayerIndex++ % players.length];
        currPlayer.getTimer().start();
        Coordinates[] move = currPlayer.makeMove(gameBoard);
        this.gameBoard.movePiece(move[0], move[1]);
        currPlayer.getTimer().stop();
    }
    
    public void startGame(){
        Player winner = null;
        while(winner == null){
            this.nextTurn();
        }
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public Player[] getPlayers() {
        return players;
    }

    public ChessStyle getStyle() {
        return style;
    }
    
}
