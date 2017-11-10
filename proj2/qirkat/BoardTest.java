package qirkat;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of the Board class.
 *  @author sahil
 */
public class BoardTest {

    private static final String INIT_BOARD =
            "  b b b b b\n  b b b b b\n  b b - w w\n  w w w w w\n  w w w w w";

    private static final String[] GAME =
        {"c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "c5-c4", "a3-c5-c3"};

    private static final String[] GAMETEST =
        {"c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "c5-c4"};

    private static final String GAME1_BOARD =
            "  b b - b b\n  b - - b b\n  - - w w w\n  w - - w w\n  w w b w w";

    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(Move.parseMove(s));
        }
    }

    @Test
    public void testInit1() {
        Board b0 = new Board();
        assertEquals(INIT_BOARD, b0.toString());
    }

    @Test
    public void testMoves1() {
        Board b0 = new Board();
        makeMoves(b0, GAME);
        assertEquals(GAME1_BOARD, b0.toString());
    }

    @Test
    public void testUndo() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        makeMoves(b0, GAME);
        Board b2 = new Board(b0);
        for (int i = 0; i < GAME.length; i += 1) {
            b0.undo();
        }
        assertEquals("failed to return to start", b1, b0);
        makeMoves(b0, GAME);
        assertEquals("second pass failed to reach same position", b2, b0);
    }



    @Test

    public void testGenerator() {
        Board b0 = new Board();
        makeMoves(b0, GAMETEST);
    }

    @Test

    public void jumpPossibleTest() {
        Board b0 = new Board();
        makeMoves(b0, GAMETEST);
    }


    @Test
    public void testinitMoves() {
        Board b0 = new Board();
        assertEquals(4, b0.getMoves().size());
    }

    @Test
    public void testLaterMoves() {
        Board b0 = new Board();
        String[] test = { "c2-c3", "c4-c2"};
        makeMoves(b0, test);
        assertEquals(1, b0.getMoves().size());
    }
    @Test
    public void testLaterMoves2() {
        Board b0 = new Board();
        String[] test = { "c2-c3", "c4-c2", "c1-c3", "a3-c1"};
        makeMoves(b0, test);
        assertEquals(1, b0.getMoves().size());
    }
    @Test
    public void testLegalMoves() {
        Board b0 = new Board();
        assertEquals(false, b0.legalMove(Move.move('a', '2', 'b', '2')));
    }

    @Test
    public void testLaterMoves3() {
        Board b0 = new Board();
        String[] test = { "c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "d4-c4", "b1-b2", "c4-d4"};
        makeMoves(b0, test);
        assertEquals(1, b0.getMoves().size());
    }





}
