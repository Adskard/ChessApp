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
    ChessStyle style;

    public Game(int boardSize) {
        this.style = ChessStyle.standard;
        this.board = new Board(this.style);
        this.players = new Player[2];
        this.timer = new Timer();
    }
    
    
}
