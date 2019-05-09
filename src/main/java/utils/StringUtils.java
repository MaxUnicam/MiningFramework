package utils;

public class StringUtils {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() <= 0;
    }

}
