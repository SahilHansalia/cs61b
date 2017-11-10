package qirkat;
import static qirkat.PieceColor.*;

/** A Player that computes its own moves.
 *  @author sahil h
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 6;
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
        int bestScore;
        Move best = null;
        if (depth == 0 || board.gameOver()) {
            return staticScore(board);
        }
        if (sense == 1) {
            bestScore = -1 * INFTY;
            for (Move testMove : board.getMoves()) {
                Board board1 = new Board();
                board1.copy(board);
                board1.makeMove(testMove);
                int response = findMove(board1, depth - 1,
                        saveMove, -1, alpha, beta);
                if (response >= bestScore) {
                    best = testMove;
                    bestScore = response;
                    alpha = Math.max(alpha, response);
                    if (beta <= alpha) {
                        break;
                    }
                }

            }
        } else {
            bestScore = INFTY;
            for (Move testMove2 : board.getMoves()) {
                Board board2 = new Board();
                board2.copy(board);
                board2.makeMove(testMove2);
                int response2 = findMove(board2, depth - 1,
                        saveMove, 1, alpha, beta);
                if (response2 <= bestScore) {
                    best = testMove2;
                    bestScore = response2;
                    beta = Math.min(beta, response2);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;

    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int whiteCount = board.whitePieces();
        int blackCount = board.blackPieces();
        if (board.gameOver() && board.whoseMove() == BLACK) {
            return WINNING_VALUE * -1;
        }
        if (board.gameOver() && board.whoseMove() == WHITE) {
            return WINNING_VALUE;
        }

        return (whiteCount - blackCount);
    }
}
