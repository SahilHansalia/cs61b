// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static db61b.Utils.*;
import static db61b.Tokenizer.*;

/** An object that reads and interprets a sequence of commands from an
 *  input source.
 *  @author Sahil and 61b staff */
class CommandInterpreter {

    /* STRATEGY.
     *
     *   This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar.  These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     *    <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement.  The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule.  For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token.  For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value.  We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     *    _input.next("create");
     *    _input.next("table");
     *    String name = name();
     *    Table table = tableDefinition();
     *    _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement.  The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument.  Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar).  Fortunately, our grammar has
     * been written with this problem in mind.  When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token.  For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     *     if (_input.nextIs("(")) {
     *         _input.next("(");
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * As a convenience, you can also write this as
     *
     *     if (_input.nextIf("(")) {
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <column name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     *     ... = columnName();
     *     while (_input.nextIs(",")) {
     *         _input.next(",");
     *         ... = columnName();
     *     }
     *
     * or if you prefer even greater concision:
     *
     *     ... = columnName();
     *     while (_input.nextIf(",")) {
     *         ... = columnName();
     *     }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     */


    /** A new CommandInterpreter executing commands read from INP, writing
     *  prompts on PROMPTER, if it is non-null. */
    CommandInterpreter(Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = new Database();
    }

    /** Parse and execute one statement from the token stream.  Return true
     *  iff the command is something other than quit or exit. */
    boolean statement() {
        switch (_input.peek()) {
        case "create":
            createStatement();
            break;
        case "load":
            loadStatement();
            break;
        case "exit": case "quit":
            exitStatement();
            return false;
        case "*EOF*":
            return false;
        case "insert":
            insertStatement();
            break;
        case "print":
            printStatement();
            break;
        case "select":
            selectStatement();
            break;
        case "store":
            storeStatement();
            break;
        default:
            throw error("unrecognizable command");
        }
        return true;
    }

    /** Parse and execute a create statement from the token stream. */
    void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = name();
        Table table = tableDefinition();
         _database.put(name, table); //added
        _input.next(";");
        }



    /** Parse and execute an exit or quit statement. Actually does nothing
     *  except check syntax, since statement() handles the actual exiting. */
    void exitStatement() {
        if (!_input.nextIf("quit")) {
            _input.next("exit");
        }
        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = tableName();
        _input.next("values");
        _input.next("(");

        ArrayList<String> vals = new ArrayList<>();
        vals.add(literal());
        while (_input.nextIf(",")) {
            vals.add(literal());
        }
        table.add(vals.toArray(new String[vals.size()]));
        //int cols = table.columns();

        //String[] values = new String[cols];

//        while (true) {
//            int k = 0; //added
//            _input.next("(");
//            while (k < cols - 1) {    //added
//            values[k] = literal();    //added
//            _input.next(",");    //added
//            k ++;   }    //added
//            values[k] = literal();   //added
//            _input.next(")");
//            table.add(values);
//            if (!_input.nextIf(",")) {
////                break;
//            }
//        }
        _input.next(")");
        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    void loadStatement() {
        _input.next("load");    //added entire method
        String Name = name();
        _input.next(";");
        _database.put(Name, Table.readTable(Name));
        System.out.printf("Loaded %s.db%n", Name);

    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String name = _input.peek();
        Table table = tableName();
        table.writeTable(name); //added
        System.out.printf("Stored %s.db%n", name);
        _input.next(";");
    }

    /** Parse and execute a print statement from the token stream. */
    void printStatement() {    //wrote method
        _input.next("print");
        String name = _input.peek();
        Table table = tableName();
        table.print();
        System.out.println("Contents of " + name+":");   //does this work?
        _input.next(";");

    }

    /** Parse and execute a select statement from the token stream. */
    void selectStatement() {
        Table table = selectClause();
        System.out.println("Search results:");
        table.print();
        _input.next(";");
    }

    /** Parse and execute a table definition, returning the specified
     *  table. */
    Table tableDefinition() {
        Table table;
        if (_input.nextIf("(")) {
            List<String> cols = new ArrayList<String>();        //added
            cols.add(columnName());          //added
            while (_input.nextIf(",")) {        //added
                cols.add(columnName());          //added
          }
            table = new Table(cols);
        } else {
            _input.next("as");
            table = selectClause();
        }
        _input.next(";");   //added
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table. */
    Table selectClause() {     //wrote method  //select Firstname, Lastname, Grade from students, enrolled where CCN = '21001';
        _input.next("select");
        ArrayList<String> cols = new ArrayList<>();
        cols.add(columnName());
        while (_input.nextIf(",")) {
            cols.add(columnName());
        }
        _input.next("from");
        Table[] tableList = new Table[2];
        tableList[0] = tableName();
        if (_input.nextIf(",")) {
            tableList[1] = tableName();
            if (_input.nextIf("where")) {
                return tableList[0].select(tableList[1], cols, conditionClause(tableList));
            }
            else {
                return tableList[0].select(tableList[1], cols, null);
            }
        }
        else {
            tableList[1] = null;
            if (_input.nextIf("where")) {
               return tableList[0].select(cols, conditionClause(tableList));
            }
            else {
                return tableList[0].select(cols, null);
            }
        }
    }

    /** Parse and return a valid name (identifier) from the token stream. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /** Parse and return a valid column name from the token stream. Column
     *  names are simply names; we use a different method name to clarify
     *  the intent of the code. */
    String columnName() {
        return name();
    }

    /** Parse a valid table name from the token stream, and return the Table
     *  that it designates, which must be loaded. */
    Table tableName() {
        String name = name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /** Parse a literal and return the string it represents (i.e., without
     *  single quotes). */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /** Parse and return a list of Conditions that apply to TABLES from the
     *  token stream.  This denotes the conjunction (`and') of zero
     *  or more Conditions. */
    ArrayList<Condition> conditionClause(Table... tables) {      // wrote method
        ArrayList<Condition> conditions = new ArrayList<>();
        conditions.add(condition(tables));  //is condition list always of form (condition and condition and condition)???
        while (_input.nextIf("and")) {
            conditions.add(condition(tables));
        }
        return conditions;
    }

    /** Parse and return a Condition that applies to TABLES from the
     *  token stream. */
    Condition condition(Table... tables) {  // REPLACE WITH SOLUTION
        String name1 = name();
        String relation = _input.next();
        if (_input.nextIs(LITERAL)) {
            return new Condition(new Column(name1, tables), relation, literal());
        }
        else {
            String name2 = name();
            return new Condition(new Column(name1, tables), relation, new Column(name2, tables));
        }
    }

    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    /** The command input source. */
    private Tokenizer _input;
    /** Database containing all tables. */
    private Database _database;
}
