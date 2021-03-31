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
        this.turnHistory = new ArrayList<>();
        this.currPlayerIndex = 0;
        this.setUpPlayers();
    }
    
    private void setUpPlayers(){
        //TODO PROPER CHOICE OF COLORS
        int pIndex = 0;
        for(PlayerColors color : PlayerColors.values()){
            ArrayList<ChessPiece> ownPieces = new ArrayList<>(); 
            for(ChessPiece[] row : this.gameBoard.getBoard()){
                for(ChessPiece piece : row){
                    if(piece.getColor() == color){
                        ownPieces.add(piece);
                    }
                }
            }
            Player player = new Player_Human(color, ownPieces);
            this.players[pIndex] = player;
            pIndex++;
        }
    }
    
    public void nextTurn(){
        Player currPlayer = players[currPlayerIndex++ % players.length];
        currPlayer.getTimer().start();
        Coordinates[] move = currPlayer.makeMove(gameBoard);
        ChessPiece piece = this.gameBoard.getBoard()[move[0].getX()][move[0].getY()];
        this.turnHistory.add(piece.toString().substring(0, 1) + move[1].toString());
        this.gameBoard.movePiece(move[0], move[1]);
        currPlayer.getTimer().stop();
    }
    
    public void startGame(){
        Player winner = null;
        while(winner == null){
            if(players[currPlayerIndex % players.length].isMated(gameBoard)){
                winner = players[currPlayerIndex+1 % players.length];
                break;
            }
            //draw by no moves
            //draw by threefold rep
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
