package qirkat;

import A.A.G;
import com.sun.tools.javac.util.Assert;

import java.util.*;

import static qirkat.PieceColor.*;
import static qirkat.Move.*;
import static qirkat.Game.*;

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

    PieceColor[] board = new PieceColor[25];
    PieceColor[] printing = new PieceColor[25];

    /** A new, cleared board at the start of the game. */
    Board() {
        // FIXME? //nah
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

        // FIXME  fixed?
        for (int i = 0; i < 10 ; i++) {
            board[i] = WHITE;
        }
        board[13] = WHITE;
        board[14] = WHITE;
        for (int j = 15; j < 25; j++ ) {
            board[j] = BLACK;
        }
        board[10] = BLACK;
        board[11] = BLACK;
        board[12] = EMPTY;
        //populate board with right color (do we have to reset? or can we get away with adding color where it should be)
        //what add does is it adds specified piece... if already piece it shift others.
        setChanged();
        notifyObservers();
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        // FIXME //fixed?
        _gameOver = b._gameOver;
        _whoseMove = b._whoseMove;
        board = b.board;
        // copy array elements, copy whosemove, and copy gameover

    }

    private void newBoard(Board a) {
        a._gameOver = _gameOver;
        a._whoseMove = _whoseMove;
        a.board = board;
    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY || nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }
        _gameOver = false;

        // FIXME
        //find out when its called?
        //whosemove = nextmove
        //gameover = false if no legal moves?

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
        for (int i = 0; i < 25; i++) {
            if (board[i] == whoseMove()) {
                pieces = true;
            }
        }
        if (!isMove() | !pieces) {
            _gameOver = true;
        }

        // FIXME ///whats left?

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
        return get(index(c, r)); // FIXME? no
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return board[k]; // FIXME  //fixed?
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);  // FIXME? no
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        // FIXME  fixed?
//        if (k < 5) {
//            board[k + 20] = v;
//        }
//        if (5 <= k && k < 10) {
//            board[k + 10] = v;
//        }
//        if (10 <= k && k < 15) {
//            board[k] = v;
//        }
//        if (15 <= k && k < 20) {
//            board[k - 10] = v;
//        }
//        if (20 <= k && k < 25) {
//            board[k - 20] = v;
//        }
        board[k] = v;
        //System.out.println(board[2]);
        //board[k] = v;
    }

    /** Return true iff MOV is legal on the current board. */
    //what is legal move?- no piece on the square. square has to be on grid.
    boolean legalMove(Move mov) {
        //return false; // FIXME
        if (!validSquare(mov.col0(), mov.row0()) | !validSquare(mov.col1(), mov.row1())) {
            return false; //check if starting and final is valid square
        }
        if (board[index(mov.col1(), mov.row1())] != EMPTY) {
            return false; //check if ending square is empty
        }

        if (whoseMove() == WHITE) {
            if (get(mov.col0(), mov.row0()) != WHITE | ((mov.row1() < mov.row0() && !mov.isJump())))
            //if ((mov.row1() < mov.row0() && !mov.isJump())) {
                return false;
            if (Character.getNumericValue(mov.row0()) == 5 && !mov.isJump()) {
                return false;
            }
            }

        if (whoseMove() == BLACK) {
            if (((mov.row1() > mov.row0()) && !mov.isJump())| get(mov.col0(), mov.row0()) != BLACK) {
                return false;
            }
            if (Character.getNumericValue(mov.row0()) == 1 && !mov.isJump()) {
                return false;
            }
        }

        return true;
        }


    ///if move.nextjump, recuse legalMove on next jump? -- 400 IQ
    //check whose move-- done
    //move.index(col0/col1) to put into array to check if its a valid color piece-- done
    //based on this- check if move is right direction (not down) & not to occupied square-- done
    //check if there is a valid jump?? where will i do this???



    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    void getMoves(ArrayList<Move> moves) {
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
        if (jumpPossible()) {
            return;
        }
        // FIXME //fixed
        if (k % 2 == 0) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    //alp.charAt(Move.alp.indexOf((char) Move.col(k)) + i);
                    //
                    try {
                        Move currentMove = move( Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        if (legalMove(currentMove)) {// try Integer.toString(a).charAt(0)
                            moves.add(currentMove);
                        }
                        }
                    catch (AssertionError | StringIndexOutOfBoundsException s ) {
                        //continue;
                    }


                }

                }
            }
        if (k % 2 == 1) {
            for (int i = -1; i <2; i++) {
                if (i == 0) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k), Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (legalMove(current)) {
                        moves.add(current);
                    }
                }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                    //continue;
                }


                try {
                    Move current2 = move( Move.col(k), Move.row(k),
                            Move.col(k), Move.row(k + i));
                    if (legalMove(current2)) {
                        moves.add(current2); }
                    }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                    //continue;
                }
            }
        }
        }
        //create all 8 possible not capturing moves pass into legal moves and add if legal?


    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        //generate jumps, pass checkJump, add to array
        // FIXME //fixed?
        if (k % 2 == 0) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if ((i == 0 && j == 0) | i == 1 | j == 1) {
                        continue;
                    }
                    try {
                        Move currentMove = move( Move.col(k), Move.row(k),
                                colConverter(k, i), rowConverter(k, j));
                        if (checkJump(currentMove, true)) {// try Integer.toString(a).charAt(0)
                            //helper
                            moves.add(currentMove);
                        }
                    }
                    catch (AssertionError | StringIndexOutOfBoundsException s) {
                        //continue;
                    }
                }

            }
        }
        if (k % 2 == 1) {
            for (int i = -2; i <3; i++) {
                if (i == 0 | i == -1 | i ==1) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k),  Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (checkJump(current, true)) {
                        moves.add(current);
                    }
                }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                    //continue;
                }

                try{
                    Move current2 = move(Move.col(k), Move.row(k),
                            Move.col(k), rowConverter(k, i));
                    if (checkJump(current2, true)) {
                        moves.add(current2);
                    }
                }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                    //continue;
                }
            }
        }
    }

    private void jumpHelper (Move move, ArrayList<Move> moves, int k) {
        boolean made_move = false;
        if (k % 2 == 0) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    if ((i == 0 && j == 0) | i == 1 | j == 1) {
                        continue;
                    }
                    try {
                        Move currentMove = move( Move.col(k), Move.row(k),
                                colConverter(k, i),rowConverter(k, j));
                        //boolean made_move = false;
                        if (checkJump(move(move, currentMove), true)) {// try Integer.toString(a).charAt(0)
                            made_move = true;
                            jumpHelper(move(move, currentMove), moves, kPacker(colConverter(k, i),rowConverter(k, j)));
                        }
                        if (!made_move) {
                            moves.add(move);
                        }
                    }
                    catch (AssertionError | StringIndexOutOfBoundsException s) {
                        //continue;
                    }
                }

            }
        }
        if (k % 2 == 1) {
            for (int i = -2; i <3; i++) {
                if (i == 0 | i == -1 | i ==1) {
                    continue;
                }
                try {
                    Move current = move(Move.col(k),  Move.row(k),
                            colConverter(k, i), Move.row(k));
                    if (checkJump(current, true)) {
                        moves.add(current);
                    }
                }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                    //continue;
                }
                try{
                    Move current2 = move(Move.col(k), Move.row(k),
                            Move.col(k), rowConverter(k, i));
                    if (checkJump(current2, true)) {
                        moves.add(current2);
                    }
                }
                catch (AssertionError | StringIndexOutOfBoundsException s) {
                   //continue;
                }
            }
        }
    }




    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        Board a = new Board();
        newBoard(a);
        return checkJumpHelper(mov, allowPartial, a);

        //fixme
        //check color of jumped over
        // check move? --- done
        //check that the sequence is a valid jump- check that it is a jump, check that its allowed
        //deal with recursion of jumps if allowed partial
    }

    boolean checkJumpHelper(Move mov, boolean allowPartial, Board a) {
        if (!validSquare(mov.col0(), mov.row0()) | !validSquare(mov.col1(), mov.row1())) {    //recycled from legalmove
            return false; //check if starting and final is valid square
        }
        if (whoseMove() == WHITE) {
            if (get(mov.col0(), mov.row0()) != WHITE | ((mov.row1() < mov.row0() && !mov.isJump())))
                //if ((mov.row1() < mov.row0() && !mov.isJump())) {
                return false;
        }

        if (whoseMove() == BLACK) {
            if (((mov.row1() > mov.row0()) && !mov.isJump()) | get(mov.col0(), mov.row0()) != BLACK) {
                return false;
            }
        }                               //recycled from legal move
        if (!mov.isJump()) {
            return false;
        }
        if (a.board[mov.jumpedIndex()] != whoseMove().opposite()) {
            return false;
        }
        if (a.board[index(mov.col1(), mov.row1())] != EMPTY) {
            return false; //check if ending square is empty
        }
        a.board[mov.jumpedIndex()] = EMPTY;
        if (allowPartial) {
            if (mov.jumpTail() != null) {
                checkJumpHelper(mov.jumpTail(), allowPartial, a); //recurse for tails
            }
        }
        return true;
    }

//    boolean singleJumpChecker(Move mov) {
//        if (!legalMove(mov)) {
//            return false;
//        }
//        if (!mov.isJump()) {
//            return false;
//        }
//        return board[mov.jumpedIndex()] == whoseMove().opposite();
//    }

    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {
        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        ArrayList<Move> jumps = new ArrayList<>();
        getJumps(jumps, k);
        return (jumps.size() > 0);
            // FIXME  fixed

        // if getJumps adds anything then true else false
        //maybe run and create array
    }
    boolean jumpPossibleEasy(int k) {
        return true;
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
//        jumped = false;
        PieceColor turn = _whoseMove;
        board[mov.toIndex()] = turn;
        board[mov.fromIndex()] = EMPTY;
        if (mov.isJump()) {
            board[mov.jumpedIndex()] = EMPTY;
        }
        if (!jumped) {
            moves.add(mov);
        }
        //moves.add(mov);
        if (mov.jumpTail() != null) {
            jumped = true;
            makeMove(mov.jumpTail());
        }
        jumped = false;
        _whoseMove = turn.opposite();
        //moves.add(mov);
//        System.out.println(moves);
        boolean pieces = false;
        for (int i = 0; i < 25; i++) {
            if (board[i] == whoseMove()) {
                pieces = true;
                break;
            }
        }
//        if (!isMove() | !pieces) {
//            _gameOver = true;
//            //report winner????
//
//        }

        // FIXME
        //fixed???

        setChanged();
        notifyObservers();
    }

    /** Undo the last move, if any. */
    void undo() {

        Move mov = moves.get(moves.size() - 1);
//        System.out.println(mov);
        moves.remove(moves.size() - 1);
        PieceColor prevTurn = whoseMove().opposite();
        undoHelper(mov, prevTurn);
        //assert legalMove(mov);
//        board[mov.toIndex()] = EMPTY;
//        board[mov.fromIndex()] = prevTurn;
//        if (mov.isJump()) {
//            board[mov.jumpedIndex()] = prevTurn.opposite();
//        }
//        if (mov.jumpTail() != null) {
//            makeMove(mov.jumpTail()); //create undo helper if needed that looks like
//        }

        _whoseMove = prevTurn;
        _gameOver = false;

//        if (mov.jumpTail() != null) {
//            undo(mov.jumpTail());
//        }

        // FIXME
        //????????????????? find previous move
        //to backtrack, find mov, flip cols and add to prev location
        //if jumped add tile jumped over
        //switch whose move it is
        //switch game over??
        //everytime you do makeMove keep track of moves and add them to lost below

        setChanged();
        notifyObservers();
    }

    private void reverse(Move mov, PieceColor prevTurn) {
        board[mov.toIndex()] = EMPTY;
        board[mov.fromIndex()] = prevTurn;
        if (mov.isJump()) {
            board[mov.jumpedIndex()] = prevTurn.opposite();
        }
    }

    private void undoHelper(Move mov, PieceColor prevTurn) {
        if (mov.jumpTail() == null) {
            reverse(mov, prevTurn);
        }
        else {
            undoHelper(mov.jumpTail(), prevTurn);
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
        // FIXME
            for (int i = 24; i >= 0; i--) {
                if (i % 5 == 0) {
                    output.append("  ");
                    for (int j = i; j < i + 5; j++) {
                        if ((j-4) % 5 == 0) {
                            output.append(board[j].shortName());
                        }
                        else {output.append(board[j].shortName() + " "); } }
                    if (legend){
                        output.append(((i + 5)/5));
                    }
                    if ((i + 4) != 4) {
                        output.append("\n");
                    }


                }

//            output.append(board[i].shortName() + " ");
//            if (legend){
//                if (i % 5 == 0) {
//                    output.append((i-1)/4 + 1); //is legend supposed to be on other side?
//                    output.append("\n");
//                }
//            }
//            else {
//                if (i % 5 == 0 && i != 0) {
//                    output.append("\n");
//                }
//            }
        }
        if (legend) {
            output.append("1 2 3 4 5");
        }
//        System.out.println(output.toString());

        //Board.get for Piececolor. Iterate through i and j and if col % 5 = 0, tag on new line. if legend, add i/j value
        //if legend at bottom,
        return output.toString();
    }

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {
        // FIXME //fixed??
//        return (jumpPossible() | getMoves().size() > 0);
        return (getMoves().size() > 0);
        // arraylistmove and _whoseMove?
    }


    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private class MoveList extends ArrayList<Move> {
    }
    MoveList moves = new MoveList();
    MoveList jumps = new MoveList();
    boolean jumped = false;

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
    public boolean equals(Object obj) {
        if (this.board == ((Board) obj).board && this.whoseMove() == ((Board) obj).whoseMove() && this.gameOver() == ((Board) obj).gameOver()) {
            return true;
        }
        return false;

    }
}
