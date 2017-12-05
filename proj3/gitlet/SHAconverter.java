package gitlet;


/**
 * Created by test on 12/4/2017.
 */
public class SHAconverter {

    String SHA;

    SHAconverter(Commit a) {
        SHA = Utils.sha1(a);
    }

}
