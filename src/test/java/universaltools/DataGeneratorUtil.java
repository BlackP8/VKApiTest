package universaltools;

/**
 * @author Pavel Romanov 14.02.2023
 */

public class DataGeneratorUtil {
    private static final int MIN_RANGE = 1;
    private static final String LATIN_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";

    public static int generateRandomNumber(int max) {
        return (int)(Math.random() * (max - MIN_RANGE + 1) + MIN_RANGE);
    }

    public static String generateRandomString(int stringLength) {
        StringBuilder stringBuilder = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int index = generateRandomNumber(LATIN_STRING.length() - 1);
            stringBuilder.append(LATIN_STRING.charAt(index));
        }
        return stringBuilder.toString();
    }
}
