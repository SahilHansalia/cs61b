package gitlet;


/** class to convert Commit object to sha hash.
 *  @author sahil
 */
public class SHAconverter {

    /** method to convert Commit object to sha hash.
     * @return sha
     * @param a is commit*/
    static String converter(Commit a) {
        String sha = "";
        String offset = "";
        String offset2 = "";
        if (a.getParent1() != null) {
            offset = a.getParent1().getDateStr();
            offset2 = a.getParent1().getName();
        }
        sha = Utils.sha1(a.getDateStr() + a.getName() + offset + offset2);
        return sha;
    }

}
