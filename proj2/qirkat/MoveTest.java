/* Author: Paul N. Hilfinger.  (C) 2008. */

package qirkat;

import org.junit.Test;
import static org.junit.Assert.*;

import static qirkat.Move.*;
import static qirkat.Board.*;

/** Test Move creation.
 *  @author
 */
public class MoveTest {

    @Test
    public void testMove1() {
        Move m = move('a', '3', 'b', '2');
        assertNotNull(m);
        assertFalse("move should not be jump", m.isJump());
    }

    @Test
    public void testJump1() {
        Move m = move('a', '3', 'a', '5');
        assertNotNull(m);
        assertTrue("move should be jump", m.isJump());
    }

    @Test
    public void testString() {
        assertEquals("a3-b2", move('a', '3', 'b', '2').toString());
        assertEquals("a3-a5", move('a', '3', 'a', '5').toString());
        assertEquals("a3-a5-c3", move('a', '3', 'a', '5',
                move('a', '5', 'c', '3')).toString());
    }

    @Test
    public void testParseString() {
        assertEquals("a3-b2", parseMove("a3-b2").toString());
        assertEquals("a3-a5", parseMove("a3-a5").toString());
        assertEquals("a3-a5-c3", parseMove("a3-a5-c3").toString());
        assertEquals("a3-a5-c3-e1", parseMove("a3-a5-c3-e1").toString());
    }

    @Test
    public void jumpedColTest() {
        Move m = move('a', '3', 'a', '5');
        assertNotNull(m);
        assertEquals('4', m.jumpedRow());

    }

    @Test
    public void concatTest() {
        Move m = move('a', '3', 'a', '5');
        Move j = move('a', '5', 'b', '3');
        Move p = move('b', '3', 'a', '5');
        assertNotNull(m);
        Move n = Move.move(m, j);
        assertEquals("a3-a5-b3", n.toString());
        assertTrue(n != m);
        Move q = Move.move(n, p);
        assertEquals("a3-a5-b3-a5", q.toString());
    }

}
