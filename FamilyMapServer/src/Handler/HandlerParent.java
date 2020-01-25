package Handler;

import java.io.InputStream;
import java.util.Scanner;

public class HandlerParent {
    String convertToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        return result;
    }
}
