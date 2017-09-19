package game2048;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Observable;

import static game2048.Main.BOARD_SIZE;


/** The state of a game of 2048.
 *  @author sahil and 61b staff
 */
class Model extends Observable {

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to _board[c][r].  Be careful! This is not the usual 2D matrix
     * numbering, where rows are numbered from the top, and the row
     * number is the *first* index. Rather it works like (x, y) coordinates.
     */

    /** Largest piece value. */
    static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    Model(int size) {
        _board = new Tile[size][size];
        _score = _maxScore = 0;
        _gameOver = false;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there. */
    Tile tile(int col, int row) {
        return _board[col][row];
    }

    /** Return the number of squares on one side of the board. */
    int size() {
        return _board.length;
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current score. */
    int score() {
        return _score;
    }

    /** Return the current maximum game score (updated at end of game). */
    int maxScore() {
        return _maxScore;
    }

    /** Clear the board to empty and reset the score. */
    void clear() {
        _score = 0;
        _gameOver = false;
        for (Tile[] column : _board) {
            Arrays.fill(column, null);
        }
        setChanged();
    }

    /** Add TILE to the board.  There must be no Tile currently at the
     *  same position. */
    void addTile(Tile tile) {
        assert _board[tile.col()][tile.row()] == null;
        _board[tile.col()][tile.row()] = tile;
        checkGameOver();
        setChanged();
    }

    /** Slide tiles without merging. @return true if slides.
     * take in @param side var*/
    private boolean slide(Side side) {
        boolean slid = false;
        for (int c = 0; c < BOARD_SIZE; c++) {
            for (int r = BOARD_SIZE - 2; r >= 0; r--) {
                if (vtile(c, r, side) != null)  {
                    for (int a = BOARD_SIZE - 1; a >= 0; a--) {
                        if (a > r) {
                            if (vtile(c, a, side) == null) {
                                setVtile(c, a, side, vtile(c, r, side));
                                slid = true;
                                break;

                            }
                        }
                    }
                }

            }
        }
        return slid;
    }

    /** @return true iff tiles can be merged.
     * take in @param side var */
    private boolean canmerge(Side side) {
        boolean canmerge = false;

        for (int c = 0; c < BOARD_SIZE; c++) {
            Tile prev = null;
            for (int r = BOARD_SIZE - 1; r >= 0; r--) {
                if (prev == null) {
                    prev = vtile(c, r, side);
                    continue;
                }
                if (vtile(c, r, side).value() == prev.value()) {
                    canmerge = true;
                } else {
                    prev = vtile(c, r, side);
                }
            }
        }
        return canmerge;
    }

    /** Returns true if tiles can be merged and merges tiles.
     * take in @param side var */
    private boolean merges(Side side) {
        boolean merged = false;

        for (int c = 0; c < BOARD_SIZE; c++) {
            Tile prev = null;
            for (int r = BOARD_SIZE - 1; r >= 0; r--) {
                if (vtile(c, r, side) == null) {
                    continue;
                }
                if (prev == null) {
                    prev = vtile(c, r, side);
                    continue;
                }
                if (vtile(c, r, side).value() == prev.value()) {
                    _score += (vtile(c, r, side).value() * 2);
                    setVtile(c, r, side, prev);
                    prev = null;
                    merged = true;
                } else {
                    prev = vtile(c, r, side);
                }



            }
        }
        return merged;
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board. */
    boolean tilt(Side side) {
        boolean changed;
        changed = false;
        boolean a = slide(side);
        boolean b = merges(side);
        slide(side);

        if (a || b) {
            changed = true;
        }

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Return the current Tile at (COL, ROW), when sitting with the board
     *  oriented so that SIDE is at the top (farthest) from you. */
    private Tile vtile(int col, int row, Side side) {
        return _board[side.col(col, row, size())][side.row(col, row, size())];
    }

    /** Move TILE to (COL, ROW), merging with any tile already there,
     *  where (COL, ROW) is as seen when sitting with the board oriented
     *  so that SIDE is at the top (farthest) from you. */
    private void setVtile(int col, int row, Side side, Tile tile) {
        int pcol = side.col(col, row, size()),
            prow = side.row(col, row, size());
        if (tile.col() == pcol && tile.row() == prow) {
            return;
        }
        Tile tile1 = vtile(col, row, side);
        _board[tile.col()][tile.row()] = null;

        if (tile1 == null) {
            _board[pcol][prow] = tile.move(pcol, prow);
        } else {
            _board[pcol][prow] = tile.merge(pcol, prow, tile1);
        }
    }

    /** Deternmine whether game is over and update _gameOver and _maxScore
     *  accordingly. */
    private void checkGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (_board[i][j] != null) {
                    if (_board[i][j].value() == MAX_PIECE) {
                        _gameOver = true;
                        if (_maxScore < _score) {
                            _maxScore = _score;
                        }
                        return;
                    }
                }
                if (_board[i][j] == null) {
                    _gameOver = false;
                    return;
                }
            }
        }

        if (!canmerge(Side.NORTH) && !canmerge(Side.SOUTH)
            && !canmerge(Side.EAST) && !canmerge(Side.WEST)) {
            _gameOver = true;

            _gameOver = false;
        }
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        out.format("] %d (max: %d)", score(), maxScore());
        return out.toString();
    }

    /** Current contents of the board. */
    private Tile[][] _board;
    /** Current score. */
    private int _score;
    /** Maximum score so far.  Updated when game ends. */
    private int _maxScore;
    /** True iff game is ended. */
    private boolean _gameOver;

}
