package gitlet;


/** class to convert Commit object to SHA hash.
 *  @author sahil
 */
public class SHAconverter {

    String SHA;

    SHAconverter(Commit a) {
        String offset = "";
        String offset2 = "";
        if (a.parent != null) {
            offset = a.parent.dateStr;
            offset2 = a.parent.name;
        }
        SHA = Utils.sha1(a.dateStr + a.name + offset + offset2);
    }

}
