package qirkat;

import java.util.Observable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observer;

import static qirkat.PieceColor.*;
import static qirkat.Move.*;

/** A Qirkat board.   The squares are labeled by column (a char value between
 *  'a' and 'e') and row (a char value between '1' and '5'.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (with row 0 being the bottom row)
 *  counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author sahil
 */


class Board extends Observable {

    /** A new, cleared board at the start of the game. */
    Board() {
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        internalCopy(b);
    }

    /** Return a constant view of me (allows any access method, but no
     *  method that modifies it). */
    Board constantView() {
        return this.new ConstantBoard();
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    void clear() {
        _whoseMove = WHITE;
        _gameOver = false;
        for (int i = 0; i < 10; i++) {
            board[i] = WHITE;
        }
        board[13] = WHITE;
        board[14] = WHITE;
        for (int j = 15; j < (Move.SIDE * Move.SIDE); j++) {
            board[j] = BLACK;
        }
        board[10] = BLACK;
        board[11] = BLACK;
        board[12] = EMPTY;
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            memory[i] = -1;
        }
        setChanged();
        notifyObservers();
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        _gameOver = b._gameOver;
        _whoseMove = b._whoseMove;
        board = b.board.clone();
        undoMoves = b.undoMoves;
        memory = b.memory.clone();
    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY | nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }
        _gameOver = false;
        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
            case '-':
                set(k, EMPTY);
                break;
            case 'b': case 'B':
                set(k, BLACK);
                break;
            case 'w': case 'W':
                set(k, WHITE);
                break;
            default:
                break;
            }
        }
        _whoseMove = nextMove;
        boolean pieces = false;
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            if (board[i].equals(whoseMove())) {
                pieces = true;
                break;
            }
        }
        if (!isMove() || !pieces) {
            _gameOver = true;
        }
        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if the current player has
     *  no moves. */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current contents of square C R, where 'a' <= C <= 'e',
     *  and '1' <= R <= '5'.  */
    PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return get(index(c, r));
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return board[k];
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        board[k] = v;
    }

    /** Return true iff MOV is legal on the current board. */
    boolean legalMove(Move mov) {
        if (!validSquare(mov.col0(), mov.row0())
                || !validSquare(mov.col1(), mov.row1())) {
            return false;
        }
        if (board[index(mov.col1(), mov.row1())] != EMPTY) {
            return false;
        }
        if (whoseMove() == WHITE) {
            if (get(mov.col0(), mov.row0()) != WHITE
                    || ((mov.row1() < mov.row0() && !mov.isJump()))) {
                return false;
            }
            if (Character.getNumericValue(mov.row0()) == 5 && !mov.isJump()) {
                return false;
            }
        }

        if (whoseMove() == BLACK) {
            if (((mov.row1() > mov.row0()) && !mov.isJump())
                    || get(mov.col0(), mov.row0()) != BLACK) {
                return false;
            }
            if (Character.getNumericValue(mov.row0()) == 1 && !mov.isJump()) {
                return false;
            }
        }
        int to = index(mov.col1(), mov.row1());
        int from = index(mov.col0(), mov.row0());
        if (memory[from] != -1 | memory[to] != -1) {
            if (memory[from].equals(to)) {
                return false;
            }
        }
        return true;
    }


    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    private void getMoves(ArrayList<Move> moves) {
        if (gameOver()) {
            return;
        }
        if (jumpPossible()) {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getJumps(moves, k);
            }
        } else {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getMoves(moves, k);
            }
        }
    }

    /** Add all legal non-capturing moves from the position
     *  with linearized index K to MOVES. */
    private void getMoves(ArrayList<Move> moves, int k) {
        if (k % 2 == 0) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    try {
                        Move currentMove = move(Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        if (legalMove(currentMove)) {
                            moves.add(currentMove);
                        }
                    } catch (AssertionError
                        | StringIndexOutOfBoundsException s) {
                        String a;
                    }
                }
            }
        }
        if (k % 2 == 1) {
            for (int i = -1; i < 2; i++) {
                if (i == 0) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k), Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (legalMove(current)) {
                        moves.add(current);
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }


                try {
                    Move current2 = move(Move.col(k), Move.row(k),
                            Move.col(k), Move.rowConverter(k , i));
                    if (legalMove(current2)) {
                        moves.add(current2);
                    }
                } catch (AssertionError
                        | StringIndexOutOfBoundsException s) {
                    String a;
                }
            }
        }
    }

    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        if (k % 2 == 0) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if ((i == 0 && j == 0) | Math.abs(i) == 1
                            | Math.abs(j) == 1) {
                        continue;
                    }
                    try {
                        Move currentMove = move(Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        if (checkJump(currentMove, false)) {
                            jumpHelper(currentMove, moves,
                                    index(colConverter(k, i),
                                            rowConverter(k, j)));
                        }
                    } catch (AssertionError
                            | StringIndexOutOfBoundsException s) {
                        String a;
                    }
                }

            }
        }
        if (k % 2 == 1) {
            for (int i = -2; i < 3; i++) {
                if (i == 0 | Math.abs(i) == 1) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k), Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (checkJump(current, false)) {
                        jumpHelper(current, moves, index(colConverter(k, i),
                                Move.row(k)));
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }

                try {
                    Move current2 = move(Move.col(k), Move.row(k),
                            Move.col(k), rowConverter(k, i));
                    if (checkJump(current2, false)) {
                        jumpHelper(current2, moves, index(Move.col(k),
                               rowConverter(k, i)));
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }
            }
        }
    }

    /** Helper to getting jumps.
     * @param z is Move to make
     * @param moves is the list to possible moves
     * @param k is the index to check moves at*/
    private void jumpHelper(Move z, ArrayList<Move> moves, int k) {
        boolean test = false;
        if (k % 2 == 0) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if ((i == 0 && j == 0) | Math.abs(i) == 1
                            | Math.abs(j) == 1) {
                        continue;
                    }
                    try {
                        Move currentMove = move(Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        Move a = move(z, currentMove);
                        if (checkJump(a, true)) {
                            test = true;
                            jumpHelper(a, moves, index(colConverter(k, i),
                                    rowConverter(k, j)));
                        }
                    } catch (AssertionError
                            | StringIndexOutOfBoundsException s) {
                        String a;
                    }
                }
            }
        }
        if (k % 2 == 1) {
            for (int i = -2; i < 3; i++) {
                if (i == 0 | Math.abs(i) == 1) {
                    continue;
                }
                try {
                    Move currentMove = move(Move.col(k), Move.row(k),
                            colConverter(k, i), Move.row(k));
                    Move b = move(z, currentMove);
                    if (checkJump(b, true)) {
                        test = true;
                        jumpHelper(b, moves, index(colConverter(k, i),
                                Move.row(k)));
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }
                try {
                    Move currentMove = move(Move.col(k), Move.row(k),
                            Move.col(k), rowConverter(k, i));
                    Move c = move(z, currentMove);
                    if (checkJump(c, true)) {
                        test = true;
                        jumpHelper(c, moves, index(Move.col(k),
                                rowConverter(k, i)));
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }
            }
        }
        if (!test) {
            moves.add(z);
        }
    }




    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        Board a = new Board(this);
        return checkJumpHelper(mov, allowPartial, a);
    }

    /** helper to check jumps.
     * @param mov = move to check
     * @param  allowPartial to allow partial jumps
     * @param a is board to check moves on
     * @return boolean*/
    private boolean checkJumpHelper(Move mov, boolean allowPartial, Board a) {
        if (!validSquare(mov.col0(), mov.row0())
                | !validSquare(mov.col1(), mov.row1())) {
            return false;
        }
        if (a.whoseMove() == WHITE) {
            if (a.get(mov.col0(), mov.row0()) != WHITE
                    | ((mov.row1() < mov.row0() && !mov.isJump()))) {
                return false;
            }
        }

        if (a.whoseMove() == BLACK) {
            if (((mov.row1() > mov.row0()) && !mov.isJump())
                    | a.get(mov.col0(), mov.row0()) != BLACK) {
                return false;
            }
        }
        if (!mov.isJump()) {
            return false;
        }
        if (a.board[mov.jumpedIndex()] != whoseMove().opposite()) {
            return false;
        }
        if (a.board[index(mov.col1(), mov.row1())] != EMPTY) {
            return false;
        }
        a.board[mov.jumpedIndex()] = EMPTY;
        a.board[index(mov.col1(), mov.row1())] = a.whoseMove();
        a.board[index(mov.col0(), mov.row0())] = EMPTY;
        if (allowPartial) {
            if (mov.jumpTail() != null) {
                return checkJumpHelper(mov.jumpTail(), allowPartial, a);
            }
        }
        return true;
    }

    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {
        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        if (k % 2 == 0) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if ((i == 0 && j == 0) | Math.abs(i) == 1
                            | Math.abs(j) == 1) {
                        continue;
                    }
                    try {
                        Move currentMove = move(Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        if (checkJump(currentMove, false)) {
                            return true;
                        }
                    } catch (AssertionError
                            | StringIndexOutOfBoundsException s) {
                        String a;
                    }
                }

            }
        }
        if (k % 2 == 1) {
            for (int i = -2; i < 3; i++) {
                if (i == 0 | Math.abs(i) == 1) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k), Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (checkJump(current, false)) {
                        return true;
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    String a;
                }

                try {
                    Move current2 = move(Move.col(k), Move.row(k),
                            Move.col(k), rowConverter(k, i));
                    if (checkJump(current2, false)) {
                        return true;
                    }
                } catch (AssertionError | StringIndexOutOfBoundsException s) {
                    continue;
                }
            }
        }
        return false;
    }

    /** Return true iff a jump is possible from the current board. */
    boolean jumpPossible() {
        for (int k = 0; k <= MAX_INDEX; k += 1) {
            if (jumpPossible(k)) {
                return true;
            }
        }
        return false;
    }

    /** Counts white pieces on board.
     * @return number of white pieces */
    public int whitePieces() {
        int count = 0;
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            if (board[i] == PieceColor.WHITE) {
                count++;
            }
        }
        return count;
    }

    /** Counts black pieces on board.
     * @return number of black pieces */
    public int blackPieces() {
        int count = 0;
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            if (board[i] == PieceColor.BLACK) {
                count++;
            }
        }
        return count;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1, or pass if C0 is '-'.  For moves
     *  other than pass, assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(Move.move(c0, r0, c1, r1, null));
    }

    /** Make the multi-jump C0 R0-C1 R1..., where NEXT is C1R1....
     *  Assumes the result is legal. */
    void makeMove(char c0, char r0, char c1, char r1, Move next) {
        makeMove(Move.move(c0, r0, c1, r1, next));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    void makeMove(Move mov) {
        assert legalMove(mov);
        if (!mov.isJump()) {
            memory[index(mov.col1(), mov.row1())]
                    = index(mov.col0(), mov.row0());
        }
        if (mov.isJump()) {
            memory[index(mov.col1(), mov.row1())] = -1;
        }
        PieceColor turn = _whoseMove;
        board[mov.toIndex()] = turn;
        board[mov.fromIndex()] = EMPTY;
        if (mov.isJump()) {
            board[mov.jumpedIndex()] = EMPTY;
        }

        if (!jumped) {
            undoMoves.add(mov);
        }
        if (mov.jumpTail() != null) {
            jumped = true;
            makeMove(mov.jumpTail());
        }
        jumped = false;
        _whoseMove = turn.opposite();
        boolean pieces = false;
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            if (board[i] == whoseMove()) {
                pieces = true;
                break;
            }
        }
        if (!isMove() || !pieces) {
            _gameOver = true;
        }
        setChanged();
        notifyObservers();
    }

    /** Undo the last move, if any. */
    void undo() {
        if (undoMoves.size() == 0) {
            return;
        }
        Move mov = undoMoves.get(undoMoves.size() - 1);
        undoMoves.remove(undoMoves.size() - 1);
        PieceColor prevTurn = whoseMove().opposite();
        undoHelper(mov, prevTurn);
        _whoseMove = prevTurn;
        _gameOver = false;
        setChanged();
        notifyObservers();
    }


    /** Helper for Undo.
     * @param mov is Move to undo
     * @param prevTurn is the previous players turn*/
    private void reverse(Move mov, PieceColor prevTurn) {
        board[mov.toIndex()] = EMPTY;
        board[mov.fromIndex()] = prevTurn;
        if (mov.isJump()) {
            board[mov.jumpedIndex()] = prevTurn.opposite();
        }
    }

    /** Helper for Undo.
     * @param mov is Move to undo
     * @param prevTurn is the previous players turn*/
    private void undoHelper(Move mov, PieceColor prevTurn) {
        if (mov.jumpTail() == null) {
            reverse(mov, prevTurn);
        } else {
            undoHelper(mov.jumpTail(), prevTurn);
            reverse(mov, prevTurn);
        }
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /** Return a text depiction of the board.  If LEGEND, supply row and
     *  column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        StringBuilder output = new StringBuilder(100);
        for (int i = (Move.SIDE * Move.SIDE - 1); i >= 0; i--) {
            if (i % 5 == 0) {
                output.append("  ");
                for (int j = i; j < i + 5; j++) {
                    if ((j - 4) % 5 == 0) {
                        output.append(board[j].shortName());
                    } else {
                        output.append(board[j].shortName() + " ");
                    }
                }
                if (legend) {
                    output.append(((i + 5) / 5));
                }
                if ((i + 4) != 4) {
                    output.append("\n");
                }
            }
        }
        if (legend) {
            output.append("1 2 3 4 5");
        }
        return output.toString();
    }

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {
        return (jumpPossible() || getMoves().size() > 0);
    }


    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** A new list to represent the board. */
    private PieceColor[] board = new PieceColor[Move.SIDE * Move.SIDE];

    /** A new memory list. */
    private Integer[] memory = new Integer[]{-1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    /** A new memory list. */
    private String[][] map = new String[Move.SIDE * Move.SIDE][];

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private class MoveList extends ArrayList<Move> {
    }
    /** List of moves to undo. */
    private MoveList undoMoves = new MoveList();

    /** boolean for making moves. */
    private boolean jumped = false;

    /** A read-only view of a Board. */
    private class ConstantBoard extends Board implements Observer {
        /** A constant view of this Board. */
        ConstantBoard() {
            super(Board.this);
            Board.this.addObserver(this);
        }

        @Override
        void copy(Board b) {
            assert false;
        }

        @Override
        void clear() {
            assert false;
        }

        @Override
        void makeMove(Move move) {
            assert false;
        }

        /** Undo the last move. */
        @Override
        void undo() {
            assert false;
        }

        @Override
        public void update(Observable obs, Object arg) {
            super.copy((Board) obs);
            setChanged();
            notifyObservers(arg);
        }
    }
    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        for (int i = 0; i < (Move.SIDE * Move.SIDE); i++) {
            if (this.board[i] != ((Board) obj).board[i]) {
                return false;
            }
        }
        if (this.whoseMove() == ((Board) obj).whoseMove()
                && this.gameOver() == ((Board) obj).gameOver()) {
            return true;
        }
        return false;

    }
}
