import java.util.LinkedList;


/** A set of String values.
 *  @author sahil
 */

class ECHashStringSet implements StringSet {

    double maxLoad = 5.0;
    double minLoad = 0.2;
    double size = 0.0;
    LinkedList<String>[] map;

    ECHashStringSet() {
        LinkedList<String>[] map = new LinkedList[(int) maxLoad]; //need to cast?
    }

    public void put(String s) {
        if (size > (maxLoad * map.length)) {
            //resize array
        }
    }



}
