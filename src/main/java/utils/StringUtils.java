package utils;

public class StringUtils {

    public static boolean isNullOr;

    public static String normalizeJson(String json) {
        if (json == null)
            return null;

        return json.replace("\r", "")
                .replace("\n", "")
                .replace("\uFEFF", "")
                .trim();
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() <= 0;
    }

}
