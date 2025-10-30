import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utils. */
public final class Utilities {
    private static final Pattern CLEAN_METHOD_PATTERN =
            Pattern.compile("[\\s\\S]+ (\\w+)\\(([\\s\\S]*)\\)");

    /**
     * Chuẩn hóa xâu.
     *
     * @param line Xâu
     * @return Xâu sau khi chuẩn hóa
     */
    public static String getStandard(String line) {
        return line.trim().replaceAll("\\s+", " ").replaceAll("\\s*;+", ";");
    }

    /**
     * Lọc Tên Package từ dòng code.
     *
     * @param line Dòng code
     * @return Tên package (thô)
     */
    public static String filterPackageName(String line) {
        line = getStandard(line);
        if (line.startsWith("package ") && line.endsWith(";")) {
            return line.replace("package ", "").replace(";", "");
        }
        return null;
    }

    /**
     * Lọc Tên class được Import từ dòng Code.
     *
     * @param line Dòng code
     * @return Class được import (thô và đầy đủ)
     */
    public static String filterImportName(String line) {
        line = getStandard(line);
        if (line.startsWith("import ") && line.endsWith(";")) {
            return line.replace("import ", "").replace(";", "");
        }
        return null;
    }

    /**
     * Lọc tên class được định nghĩa trong file code.
     *
     * @param line Dòng code
     * @return Tên class đó
     */
    public static String filterClassName(String line) {
        line = getStandard(line);
        if (line.contains("public ") && line.contains("class ")) {
            line = line.replaceAll("\\s*\\{\\s*", "");
            return line.substring(line.lastIndexOf("class ") + "class ".length());
        }
        return null;
    }

    /**
     * Lọc Method về định dạng mong muốn.
     *
     * @param line Dòng code chứa định nghĩa
     * @param defined Các class đã được định nghĩa
     * @return Method ở định dạng mong muốn
     */
    public static String filterMethod(String line, List<ClassName> defined) {
        line = getStandard(line);
        if (line.contains("public static ") && !line.contains("class ")) {
            // Delete
            line = line.replaceAll("\\s*\\{\\s*", "").replace("public static ", "");
            if (line.contains(" throws")) {
                line = line.substring(0, line.lastIndexOf(" throws"));
            }

            // Re-create
            final List<String> parsedLine = parseMethod(line);
            line =
                    parsedLine.get(0)
                            + '('
                            + filterParams(parsedLine.get(parsedLine.size() - 1))
                            + ')';

            // Replacement
            for (ClassName d : defined) {
                line = line.replace(d.getSimpleName(), d.getFullName());
            }
            return line;
        }
        return null;
    }

    private static String filterParams(String params) {
        List<String> result = new ArrayList<>();
        String[] paramArray = params.split("\\s*,\\s*");
        for (String param : paramArray) {
            final List<String> words = new ArrayList<>(Arrays.asList(param.split("\\s+")));
            words.remove(words.size() - 1);
            result.add(String.join("", words));
        }
        return String.join(",", result);
    }

    private static List<String> parseMethod(String cleanLine) {
        Matcher matcher = CLEAN_METHOD_PATTERN.matcher(cleanLine);
        List<String> res = new ArrayList<String>();
        if (matcher.matches()) {
            res.add(matcher.group(1));
            res.add(matcher.group(2));
        }
        return res;
    }

    /**
     * Đếm cố kí tự {@code character}.
     *
     * @param string Xâu cần đếm.
     * @param character Kí tự
     * @return Số kí tự đó trong xâu
     */
    public static int countChar(String string, char character) {
        int count = 0;
        for (int i = 0; i < string.length(); ++i) {
            if (string.charAt(i) == character) {
                ++count;
            }
        }
        return count;
    }
}
