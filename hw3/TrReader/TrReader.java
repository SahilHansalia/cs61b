import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author
 */
public class TrReader extends Reader {
    private Reader str;
    private String from, to;

    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(0) to TO.charAt(0), etc., leaving other characters
     *  unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        this.str = str;
        this.from = from;
        this.to = to;
    }

//    cbuf - Destination buffer
//    off - Offset at which to start storing characters
//    len - Maximum number of characters to read
    // returns: The number of characters read, or -1 if the end of the stream has been reached
    @Override
    public int read(char[] cbuf, int off , int len) throws IOException {
        int start = off;
        int end = off + len;
        for (int i = start; i < end; i++) {
            //char curr = cbuf[i];
            if (from.indexOf(cbuf[i]) == -1) {
                continue;
            }
            else {
                cbuf[i] = to.charAt(from.indexOf(cbuf[i]));
                //cbuf[i] = cbuf[i]

            }
        }
    return str.read(cbuf, off, len);
    }


    @Override
    public void close() throws IOException {
        str.close();






        }

    // FILL IN
    // NOTE: Until you fill in the right methods, the compiler will
    //       reject this file, saying that you must declare TrReader
    //     abstract.  Don't do that; define the right methods instead!
}


