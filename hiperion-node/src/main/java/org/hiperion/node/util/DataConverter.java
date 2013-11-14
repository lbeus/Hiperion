package org.hiperion.node.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 15.03.13.
 * Time: 22:45
 */
public class DataConverter {

    private DataConverter() {
    }

    public static String getBase64String(byte[] byteData) {
        return Base64.encodeBase64String(byteData);
    }

    public static byte[] getBytes(String base64String) {
        return Base64.decodeBase64(base64String);
    }
}
