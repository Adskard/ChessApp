/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.awt.image.BufferedImage;
/**
 *
 * @author Adam Škarda
 */
public class ChessPieceImpl extends ChessPiece{

    public ChessPieceImpl(ChessPiece piece) {
        super(piece);
    }

    public ChessPieceImpl(String name, Coordinates pos, PlayerColors color, MoveSet mov, String imagePath) {
        super(name, pos, color, mov, imagePath);
    }
    
}
