package gitlet;


/** class to convert Commit object to SHA hash.
 *  @author sahil
 */
public class SHAconverter {

    String SHA;

    SHAconverter(Commit a) {
        SHA = Utils.sha1(a.dateStr + a.name);
    }

}
