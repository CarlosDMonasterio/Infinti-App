package org.ih.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * String utility methods
 *
 * @author Hector Plahar
 */
public class StringUtil {

    /**
     * @param str string object to verify
     * @return true if <code>str</code> is an empty string or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }


    public static String streamToString(InputStream inputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        return buf.toString(StandardCharsets.UTF_8.name());
    }
}
