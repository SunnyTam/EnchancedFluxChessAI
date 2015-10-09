package com.fluxchess.pulse;

import static com.fluxchess.pulse.Bitboard.next;
import static com.fluxchess.pulse.Bitboard.remainder;
import static com.fluxchess.pulse.Piece.NOPIECE;
import static com.fluxchess.pulse.PieceType.PAWN;
import static com.fluxchess.pulse.PieceType.BISHOP;
import static com.fluxchess.pulse.PieceType.KNIGHT;
import static com.fluxchess.pulse.PieceType.QUEEN;
import static com.fluxchess.pulse.PieceType.ROOK;
import static com.fluxchess.pulse.PieceType.KING;
import static com.fluxchess.pulse.PieceType.isSliding;
import static com.fluxchess.pulse.Square.bishopDirections;
import static com.fluxchess.pulse.Square.knightDirections;
import static com.fluxchess.pulse.Square.queenDirections;
import static com.fluxchess.pulse.Square.rookDirections;

import org.jetbrains.annotations.NotNull;

public class PiecePostionEvaluator {

	// It is for black side!!
	
	  //Piece position value
	  static final int[] PAWN_POSITION_VALUE = {
		  0,  0,  0,  0,  0,  0,  0,  0,
		  50, 50, 50, 50, 50, 50, 50, 50,
		  10, 10, 20, 30, 30, 20, 10, 10,
		   5,  5, 10, 25, 25, 10,  5,  5,
		   0,  0,  0, 20, 20,  0,  0,  0,
		   5, -5,-10,  0,  0,-10, -5,  5,
		   5, 10, 10,-20,-20, 10, 10,  5,
		   0,  0,  0,  0,  0,  0,  0,  0
	  };
	  
	  //Knight position value
	  static final int[] KNIGHT_POSITION_VALUE = {
		  -50,-40,-30,-30,-30,-30,-40,-50,
		  -40,-20,  0,  0,  0,  0,-20,-40,
		  -30,  0, 10, 15, 15, 10,  0,-30,
		  -30,  5, 15, 20, 20, 15,  5,-30,
		  -30,  0, 15, 20, 20, 15,  0,-30,
		  -30,  5, 10, 15, 15, 10,  5,-30,
		  -40,-20,  0,  5,  5,  0,-20,-40,
		  -50,-40,-30,-30,-30,-30,-40,-50
	  };
	  
	  //Bishop position value
	  static final int[] BISHOP_POSITION_VALUE = {
		  -20,-10,-10,-10,-10,-10,-10,-20,
		  -10,  0,  0,  0,  0,  0,  0,-10,
		  -10,  0,  5, 10, 10,  5,  0,-10,
		  -10,  5,  5, 10, 10,  5,  5,-10,
		  -10,  0, 10, 10, 10, 10,  0,-10,
		  -10, 10, 10, 10, 10, 10, 10,-10,
		  -10,  5,  0,  0,  0,  0,  5,-10,
		  -20,-10,-10,-10,-10,-10,-10,-20,
	  };
	  
	  //Rook position value
	  static final int[] ROOK_POSITION_VALUE = {
		  0,  0,  0,  0,  0,  0,  0,  0,
		  5, 10, 10, 10, 10, 10, 10,  5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		  0,  0,  0,  5,  5,  0,  0,  0
	  };
	  
	  //Queen position value
	  static final int[] QUEEN_POSITION_VALUE = {
		  -20,-10,-10, -5, -5,-10,-10,-20,
		  -10,  0,  0,  0,  0,  0,  0,-10,
		  -10,  0,  5,  5,  5,  5,  0,-10,
		   -5,  0,  5,  5,  5,  5,  0, -5,
		    0,  0,  5,  5,  5,  5,  0, -5,
		  -10,  5,  5,  5,  5,  5,  0,-10,
		  -10,  0,  5,  0,  0,  0,  0,-10,
		  -20,-10,-10, -5, -5,-10,-10,-20
	  };
	  
	  static final int[] KING_EARLY_POSITION_VALUE = {
		  -30,-40,-40,-50,-50,-40,-40,-30,
		  -30,-40,-40,-50,-50,-40,-40,-30,
		  -30,-40,-40,-50,-50,-40,-40,-30,
		  -30,-40,-40,-50,-50,-40,-40,-30,
		  -20,-30,-30,-40,-40,-30,-30,-20,
		  -10,-20,-20,-20,-20,-20,-20,-10,
		   20, 20,  0,  0,  0,  0, 20, 20,
		   20, 30, 10,  0,  0, 10, 30, 20
	  };
	  
	  static final int[] KING_LATE_POSITION_VALUE = {
		  -50,-40,-30,-20,-20,-30,-40,-50,
		  -30,-20,-10,  0,  0,-10,-20,-30,
		  -30,-10, 20, 30, 30, 20,-10,-30,
		  -30,-10, 30, 40, 40, 30,-10,-30,
		  -30,-10, 30, 40, 40, 30,-10,-30,
		  -30,-10, 20, 30, 30, 20,-10,-30,
		  -30,-30,  0,  0,  0,  0,-30,-30,
		  -50,-30,-30,-30,-30,-30,-30,-50
	  };
	
	static int evaluatePosition( int color, @NotNull Position position) {
		    assert Color.isValid(color);
		    
		    int PiecePositioning = 0;
		    for (long squares = position.pieces[color][PAWN].squares; squares != 0; squares = remainder(squares)) {
		      int square = next(squares);
		      PiecePositioning += evaluatePosition(color, position, square,PAWN);
		    }

		    int knightPositioning = 0;
		    for (long squares = position.pieces[color][KNIGHT].squares; squares != 0; squares = remainder(squares)) {
		      int square = next(squares);
		      knightPositioning += evaluatePosition(color, position, square,KNIGHT);
		    }

		    int bishopPositioning = 0;
		    for (long squares = position.pieces[color][BISHOP].squares; squares != 0; squares = remainder(squares)) {
		      int square = next(squares);
		      bishopPositioning += evaluatePosition(color, position, square,BISHOP);
		    }

		    int rookPositioning = 0;
		    for (long squares = position.pieces[color][ROOK].squares; squares != 0; squares = remainder(squares)) {
		      int square = next(squares);
		      rookPositioning += evaluatePosition(color, position, square,ROOK);
		    }

		    int queenPositioning = 0;
		    for (long squares = position.pieces[color][QUEEN].squares; squares != 0; squares = remainder(squares)) {
		      int square = next(squares);
		      queenPositioning += evaluatePosition(color, position, square,QUEEN);
		    }

		    return PiecePositioning 
		    	+ knightPositioning
		        + bishopPositioning 
		        + rookPositioning
		        + queenPositioning;
		  }

	  private static int evaluatePosition(int color, @NotNull Position position, int square,int PieceType) {
		    assert Color.isValid(color);
		    assert Piece.isValid(position.board[square]);

		    int positionValue = 0;
		    
		    int[] scoreBoard  = null;
		    
		    int gamePhrase = position.taperedEval();
		    //turn into correct array index
		    switch(PieceType){
		    case PAWN:
		    	scoreBoard = PAWN_POSITION_VALUE;
		    	break;
		    case KNIGHT:
		    	scoreBoard = KNIGHT_POSITION_VALUE;
		    	break;
		    case ROOK:
		    	scoreBoard = ROOK_POSITION_VALUE;
		    	break;
		    case BISHOP:
		    	scoreBoard = BISHOP_POSITION_VALUE;
		    	break;
		    case QUEEN:
		    	scoreBoard = QUEEN_POSITION_VALUE;
		    	break;
		    case KING:
		    	if(gamePhrase > 250) // ending
		    		scoreBoard = KING_LATE_POSITION_VALUE;
		    	else
		    		scoreBoard = KING_EARLY_POSITION_VALUE;
		    	break;
		    default:
		    	return 0;
		    }
		    
		    int file = Square.getFile(square);
		    int rank = Square.getRank(square);
		    
		    if(Color.WHITE == color){
		    	rank = 7 - rank;
		    }

		    positionValue = scoreBoard[(rank) * 8 + file];
		    return positionValue;
		  }
		  
		  
}
