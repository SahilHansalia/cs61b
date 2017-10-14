package db61b;

import ucb.junit.textui;

/** The suite of all JUnit tests for the qirkat package.
 *  @author P. N. Hilfinger
 */
public class UnitTest {

    public static void main(String[] ignored) {
        textui.runClasses(TestDatabase.class,
                TestCondition.class, TestTable.class);
    }

}
