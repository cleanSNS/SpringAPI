package cleanbook.com.util;

import java.util.UUID;

public class CommonUtils {
    private static final String CATEGORY_PREFIX = "/";

    public static String createFileName(String category, String originalFileName) {
        return category + CATEGORY_PREFIX + UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
