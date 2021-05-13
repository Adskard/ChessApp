/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

/**
 * exception for incorrect board lenght
 * @author Adam Škarda
 */
public class IncorrectLenghtException extends Exception{

    /**
     *
     * @param message message to be displayed
     */
    public IncorrectLenghtException(String message) {
        super(message);
    }
    
}
