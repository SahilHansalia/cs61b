package qirkat;

import graph.B;

import static qirkat.PieceColor.*;

/** A Player that computes its own moves.
 *  @author
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 8;
    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        String col = "";
        if (myColor() == WHITE) {
            col = "White";
        }
        if (myColor() == BLACK) {
            col = "Black";
        }
        System.out.println(col + " moves " + move.toString() + ".");

        // FIXME //fixed?
        return move;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findMove(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            findMove(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        // mind simple max if depth = 0 or gameover(board)

        ///
        Move best;
        best = null;

        // FIXME

        if (saveMove) {
            _lastFoundMove = best;
        }

        return 0; // FIXME
    }





    private int simpleMax(Board board, int alpha, int beta) {
        if (board.gameOver() && board.whoseMove() == WHITE) {

        }
        return 0;
    }












    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int whiteCount = 0;
        int blackCount = 0;
        for (int i = 0; i < 25; i++) {
            if (board.board[i] == WHITE) {
                whiteCount ++;
            }
            if (board.board[i] == BLACK) {
                blackCount ++;
            }
        }
//        if (myColor() == WHITE) {
//            return (whiteCount - blackCount);
//        }
//        if (myColor() == BLACK) {
//            return (blackCount - whiteCount);
//        }
        return whiteCount - blackCount; // FIXME //fixed?
    }
}
