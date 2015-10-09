/*
 * 
 *
 * 
 * 
 */
package com.fluxchess.pulse;

final class MoveType {

  static final int MASK = 0x7;

  static final int NORMAL = 0;
  static final int PAWNDOUBLE = 1;
  static final int PAWNPROMOTION = 2;
  static final int ENPASSANT = 3;
  static final int CASTLING = 4;

  static final int NOMOVETYPE = 5;

  static final int NULLMOVE = 6;
  
  private MoveType() {
  }

  static boolean isValid(int type) {
    switch (type) {
      case NORMAL:
      case PAWNDOUBLE:
      case PAWNPROMOTION:
      case ENPASSANT:
      case CASTLING:
      case NULLMOVE:
        return true;
      default:
        return false;
    }
  }

}
