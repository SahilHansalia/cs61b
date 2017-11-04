import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/** A set of String values.
 *  @author sahil
 */

public class ECHashStringSet implements StringSet {

    double maxLoad = 5.0;
    double minLoad = 0.2;
    double size = 0.0;
    LinkedList<String>[] map = new LinkedList[(int) maxLoad];

    public ECHashStringSet() {
        //LinkedList<String>[] map = new LinkedList[(int) maxLoad]; //need to cast?
    }
    @Override
    public void put(String s) {
        size ++;  //check indexing
        if (size > (maxLoad * map.length)) {
            resize();
        }
        LinkedList<String> a = new LinkedList<String>();
        map[(s.hashCode() & 0x7ffffff) % map.length] = a;
        a.add(s);

    }

    public void resize() {
        map = new LinkedList[2* (map.length)];
        size = 0;
        for (int i = 0; i < (map.length/2); i++) {
            if (map[i] != null) {
                for (String s : map[i]) {
                    put(s);
                }
            }


        }
    }
    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < (map.length); i++) {
            for (String s : map[i]) {
                result.add(s);

            }
        }
        return result;
    }

    @Override
    public boolean contains(String s) {
        return map[(s.hashCode() % map.length)].contains(s);
    }


}
