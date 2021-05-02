/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.skardada.chess;

import java.util.ArrayList;

/**
 *
 * @author Adam Škarda
 */
public class FutureStatesGen {
 
    public static boolean isChecked(Board board, ChessPiece king){
        ChessPiece[][] pieces = board.getBoard();
        for(ChessPiece[] row : pieces){
            for(ChessPiece piece : row){
                //skip empty squares
                if(piece == null){
                    continue;
                }
                
                //same color pieces cant check king
                if(piece.getColor().equals(king.getColor())){
                    continue;
                }
                
                //echeck if enemy pieces can get to king
                else{ 
                    if(piece.canGetTo(king.getPosition())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static ArrayList<Coordinates> calculateLegalMoves(Board board, ChessPiece piece){
        ArrayList<Coordinates> legalMoves = new ArrayList();
        
        //skip emty squares
        if(piece == null){
            return null;
        }
        
        //start with all available moves, then remove illegal ones
        legalMoves.addAll(piece.getAvailableMoves());

        //remove moves on pieces with the same color
        ArrayList<Coordinates> destinationsToRemove = new ArrayList();
        for(Coordinates coords : legalMoves){
            ChessPiece dest = board.getBoard()[coords.getX()][coords.getY()];
            if(dest == null){
                continue;
            }
            if(dest.getColor() == piece.getColor()){
                destinationsToRemove.add(coords);
            }
        }    

        //Remove moves that endanger same colored king
        //1. get future board state for every available move
        for(Coordinates dest : legalMoves){
            Coordinates source = piece.getPosition();
            Board futureBoard = getFutureBoardState(board, source, dest);

            //2. find same color king
            ChessPiece[][] futurePieces = futureBoard.getBoard();
            ChessPieceKing sameColorKing = null;
            for(ChessPiece[] kingRow : futurePieces){
                for(ChessPiece king : kingRow){
                    if(king instanceof ChessPieceKing && king.getColor() == piece.getColor()){
                        sameColorKing = (ChessPieceKing) king;
                    }
                }
            }
            //3. remove the move if king in danger (after the move is played)
            if(sameColorKing != null && sameColorKing.isChecked()){
                destinationsToRemove.add(dest);
            }
        }
        
        legalMoves.removeAll(destinationsToRemove); 
        
        return legalMoves;
    }
    
    public static ArrayList<Coordinates> calculateAvailableMoves(Board board, ChessPiece piece){
        ArrayList<Coordinates> availableMoves = new ArrayList();
        
        MoveSet moves= piece.getMoveSet();
        int distance = moves.getMoveDistance();
        
        //set maximum distance for some long-range pieces - eg.Bishop
        if(distance == Integer.MAX_VALUE){
            distance = board.getSize();
        }       
        //add regular moves from move set - for every direction
        for(Coordinates vector : moves.getMoveVectors()){
            
            //for every square to a set distance
            for (int i = 1; i <= distance; i++) {
                Coordinates dest = piece.getPosition().getSum(vector.getProduct(i));

                //stop at board boundry
                if(dest.getX()<0 || dest.getX()>=board.getSize() || dest.getY()<0 || dest.getY()>=board.getSize()){
                    break;
                }
                availableMoves.add(dest);

                // cant move past pieces
                if(board.getBoard()[dest.getX()][dest.getY()] != null){
                    break;
                }
            }
        }
        
        //add special moves
        if(piece instanceof ChessPiecePawn){
            ChessPiecePawn pawn = (ChessPiecePawn) piece;
            int moveForwardIndex = 0;
            //Remove taking by going forward - get at 0 index because sofar pawn has only one move: forward
            if(!availableMoves.isEmpty() && board.getChessPieceAtCoordinate(availableMoves.get(moveForwardIndex)) != null){
                availableMoves.remove(moveForwardIndex);
            }
            
            Coordinates dest = checkEnPassant(board, pawn);
            if (dest != null) {
                availableMoves.add(dest);
            }
            
            dest = checkDoublePawn(board, pawn);
            if (dest != null) {
                availableMoves.add(dest);
            }
            availableMoves.addAll(checkPawnTakes(board, pawn));
        }
        if(piece instanceof ChessPieceKing){
            ChessPieceKing king = (ChessPieceKing) piece;
            availableMoves.addAll(checkCastle(board, king));
        }  
        return availableMoves;
    }
    
    
    public static Coordinates checkEnPassant(Board board, ChessPiecePawn pawn){
        
        ChessPiece lastPlayedPawn;
        ArrayList piecesHistory = board.getHistory().getPiecesMoved();
        
        //first move cannot be enPassant
        if(piecesHistory.isEmpty()){
            return null;
        }
        
        //Check if pawn was played last
        if(piecesHistory.get(piecesHistory.size() - 1) instanceof ChessPiecePawn){
            lastPlayedPawn = (ChessPiece) piecesHistory.get(piecesHistory.size() - 1);
            
            //Check if lastPlayedPawn was moved only once 
            if(piecesHistory.indexOf(lastPlayedPawn) != piecesHistory.lastIndexOf(lastPlayedPawn) || lastPlayedPawn.getColor().equals(pawn.getColor())){
                return null;
            }
            
            //Check proper position for enPassant - same rank, column one removed
            if((lastPlayedPawn.getPosition().getY() == pawn.getPosition().getY() - 1 || lastPlayedPawn.getPosition().getY() == pawn.getPosition().getY() + 1) && lastPlayedPawn.getPosition().getX() == pawn.getPosition().getX() ){
                
                //destination square is move from pawn moves set plus proper vector to get into target pawn column
                Coordinates dest = pawn.getPosition().getSum(pawn.getMoveSet().getMoveVectors()[0].getSum(new Coordinates(0,lastPlayedPawn.getPosition().getY() - pawn.getPosition().getY())));
                return dest;
            }
        }
        return null;
    }
    
    public static ArrayList<Coordinates> checkCastle(Board board, ChessPieceKing king){
        //STUPID GENERAL IMPLEMENTATION RETURN ARRAYLIST<COORDINATES>
        ArrayList<Coordinates> destinationSquares = new ArrayList();
        
        //if king has been moved
        if(board.getHistory().getPiecesMoved().contains(king)){
            return destinationSquares;
        }
        
        //find same colored rooks
        ArrayList<ChessPiece> sameColorRooks = new ArrayList();
        for (ChessPiece[] row : board.getBoard()) {
            for(ChessPiece piece : row){
                if(piece != null && piece.getColor() == king.getColor() && piece.getName().contains("Rook")){
                    sameColorRooks.add(piece);
                }
            }
        }
        
        //check if rooks were moved
        for(ChessPiece movedPiece : board.getHistory().getPiecesMoved()){
            if(sameColorRooks.contains(movedPiece)){
                sameColorRooks.remove(movedPiece);
            }
        }
        if(sameColorRooks.isEmpty()){
            return destinationSquares;
        }
        
        int distanceTraveledByKing = 2;
        ArrayList<ChessPiece> rooksToRemove = new ArrayList();
        
        // check squares between rook and king if they are empty and not checked (two closest to king cant be under attack)
        for(ChessPiece rook : sameColorRooks){
            
            //check if rook and king are in the same row - in some opening setups can be false
            if(rook.getPosition().getX() != king.getPosition().getX()){
                continue;
            }
            Coordinates rookCoords = rook.getPosition();
            int biggerY = rookCoords.getY() > king.getPosition().getY() ? rookCoords.getY() : king.getPosition().getY();
            int smallerY = rookCoords.getY() < king.getPosition().getY() ? rookCoords.getY() : king.getPosition().getY();
            
            //Check the squeres between rook and king, if they are not empty rook cant castle
            for (int i = smallerY + 1; i < biggerY; i++) {
                Coordinates inBetweenSquare = new Coordinates(king.getPosition().getX(), i);
                
                if(board.getChessPieceAtCoordinate(inBetweenSquare) != null){
                    rooksToRemove.add(rook);
                }
                else{
                    //Check two squares traveld by king for checks, if there are attacks by opponent on these squares, cant castle that side
                    if(i< smallerY + 1 + distanceTraveledByKing){
                        for(ChessPiece[] row : board.getBoard()){
                            for(ChessPiece piece :row){

                                if(piece != null && piece.getColor() != king.getColor() && piece.canGetTo(inBetweenSquare)){
                                    rooksToRemove.add(rook);
                                }
                            }
                        }
                    } 
                }
                
            }
            
        
        }
        
        sameColorRooks.removeAll(rooksToRemove);
        for(ChessPiece rook : sameColorRooks){
            if(rook.getPosition().getY() > king.getPosition().getY()){
                destinationSquares.add(new Coordinates(king.getPosition().getX(), king.getPosition().getY()+distanceTraveledByKing));
            }
            if(rook.getPosition().getY() < king.getPosition().getY()){
                destinationSquares.add(new Coordinates(king.getPosition().getX(), king.getPosition().getY()-distanceTraveledByKing));
            }
        }
        return destinationSquares;
    }
    
    public static boolean checkPromotion(Board board, ChessPiecePawn pawn){
        if(pawn.getPosition().getX() == board.getSize() || pawn.getPosition().getX() == 0){
            return true;
        }
        return false;
    }
    
    private static Coordinates checkDoublePawn(Board board, ChessPiecePawn pawn){
        //The pawn has moved - ergo cant do this special movement
        if(board.getHistory().getPiecesMoved().contains(pawn)){
            return null;
        }
        
        else{
            Coordinates start = pawn.getPosition();
            // normal move +1 distance
            Coordinates normalMove = pawn.getMoveSet().getMoveVectors()[0].getProduct(pawn.getMoveSet().getMoveDistance());
            Coordinates doubleMove = pawn.getMoveSet().getMoveVectors()[0].getProduct(pawn.getMoveSet().getMoveDistance()+1);
            Coordinates dest = start.getSum(doubleMove);
            Coordinates inBetween = start.getSum(normalMove);
            //destination square is empty
            if(board.getBoard()[dest.getX()][dest.getY()] == null && board.getBoard()[inBetween.getX()][inBetween.getY()] == null){
                return dest;
            }
        }
        return null;
    }
    
    private static ArrayList<Coordinates> checkPawnTakes(Board board, ChessPiecePawn pawn){
        ArrayList<Coordinates> result = new ArrayList();
        Coordinates start = pawn.getPosition();
        
        //pawn has only one vector - calculate it
        Coordinates normalMove = pawn.getMoveSet().getMoveVectors()[0].getProduct(pawn.getMoveSet().getMoveDistance());
        
        
        //Add taking by going diagonally
        Coordinates[] vectors = new Coordinates[2];
        vectors[0] = normalMove.getSum(new Coordinates(0,1));
        vectors[1] = normalMove.getSum(new Coordinates(0,-1));
        for (int i = 0; i < vectors.length; i++) {
            Coordinates dest = start.getSum(vectors[i]);
            
            //check if destination square is occupied
            if(board.getChessPieceAtCoordinate(dest) != null){
                result.add(dest);
            }
        }
        return result;
    }
    
    private static Board getFutureBoardState(Board board, Coordinates source, Coordinates dest){
        //clone the board
        Board futureState = new Board(board);
        //move the piece
        futureState.movePieceWithoutUpdatingLegalMoves(source, dest);
        return futureState;
    }
    
}
