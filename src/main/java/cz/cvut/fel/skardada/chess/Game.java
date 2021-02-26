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
    Board board;
    Player[] players;
    Timer timer;
    public enum playerColors {
        white,
        black
    }
    public Game(int boardSize) {
        this.board = new Board(boardSize);
        
    }

    public Game(int boardSize, int numberOfPlayers) {
        this.board = new Board(boardSize);
        this.players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            this.players[i] = new Player( playerColors.values()[i].toString());
        }
        this.timer = new Timer();
    }
    
    
}
