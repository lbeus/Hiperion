package org.hiperion.core.util;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 15.03.13.
 * Time: 13:04
 */
public class DataKeyCreator {

    public static final String KEY_DELIMITER = "-";

    private DataKeyCreator() {
    }

    public static String createKey(String... keyMembers) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (String keyMember : keyMembers) {
            stringBuilder.append(keyMember);
            if (keyMembers.length != (++i)) {
                stringBuilder.append(KEY_DELIMITER);
            }
        }
        return stringBuilder.toString();
    }
}
