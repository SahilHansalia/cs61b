package qirkat;

import static qirkat.PieceColor.*;
import static qirkat.Command.Type.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author sahil
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + ": ";
    }

    @Override
    Move myMove() { // FIXME
        Command todo = game().getMoveCmnd(_prompt);
        if (todo == null) {
            return null;
        }
        if (todo.commandType() == Command.Type.CLEAR) {
            game().doClear(new String[1]);  //does this matter since its unused?
        }
        if (todo.commandType() == Command.Type.QUIT) {
            game().doQuit(new String[1]);
        }
        String[] myMove = todo.operands();

        Move make = Move.parseMove(myMove[0]);
        if ((make == null) || !game().board().legalMove(make)) { //change to comprehensive legal once completed
            game().reportError("illegal move, please try again", make);
            myMove();
        }

        return make;
    }

    /** Identifies the player serving as a source of input commands. */
    private String _prompt;
}

