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
public class ChessStyle implements java.io.Serializable{
    private final int boardSize;
    private final ChessPiece[][] boardArrangement;
    private final String name;
    
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

    public int getBoardSize() {
        return boardSize;
    }

    public ChessPiece[][] getBoardArrangement() {
        return boardArrangement;
    }

    public String getName() {
        return name;
    }
    
}
