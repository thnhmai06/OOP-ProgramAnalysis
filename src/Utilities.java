import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utils. */
public final class Utilities {
    private static final Pattern PACKAGE = Pattern.compile("package ([\\w.]+);"); // [name]
    private static final Pattern IMPORT =
            Pattern.compile("import(?: static)? ([\\w.]+);"); // [name]
    private static final Pattern CLASS =
            Pattern.compile(
                    "public (?:\\w+\\s+)*(?:class|interface) (\\w+)(?: extends \\w+)?(?: implements [\\w\\s,]+)?"); // [name]
    private static final Pattern METHOD =
            Pattern.compile("public static [\\s\\S]*? (\\w+)\\(([\\s\\S]*?)\\)"); // [name, params]
    private static final Pattern METHOD_PARAM =
            Pattern.compile(
                    "(?:final\\s+)?([\\w.]+(?:<[\\s\\S]*?>)?)(?:...|\\[])? (\\w+)"); // [type, name]
    private static final Pattern METHOD_PARAM_TYPE =
            Pattern.compile("\\b([\\w.]+)\\b"); // [typeName]

    /**
     * Lọc Tên Package từ dòng code.
     *
     * @param line Dòng code
     * @return Tên package (thô)
     */
    public static String filterPackageName(String line) {
        Matcher match = PACKAGE.matcher(line);
        if (match.matches()) {
            return match.group(1);
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
        Matcher match = IMPORT.matcher(line);
        if (match.matches()) {
            return match.group(1);
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
        Matcher match = CLASS.matcher(line);
        if (match.matches()) {
            return match.group(1);
        }
        return null;
    }

    /**
     * Đây có phải line method không.
     *
     * @param line Line cần kiểm tra
     * @return {@code true} nếu là line mô tả định nghĩa method, ngược lại {@code false}
     */
    public static boolean isMethodLine(String line) {
        Matcher match = METHOD.matcher(line);
        return match.matches();
    }

    /**
     * Lọc Method về định dạng mong muốn.
     *
     * @param line Dòng code chứa định nghĩa
     * @param definedClass Các class đã được định nghĩa
     * @return Method ở định dạng mong muốn
     */
    public static String filterMethod(String line, List<ClassName> definedClass) {
        Matcher match = METHOD.matcher(line);
        if (match.matches()) {
            final String name = match.group(1);
            final String rawParams = match.group(2);
            final String params = filterParams(rawParams, definedClass);

            return String.format("%s(%s)", name, params);
        }
        return null;
    }

    private static String filterParams(String params, List<ClassName> definedClass) {
        List<String> paramList = new ArrayList<>();
        Matcher match = METHOD_PARAM.matcher(params);
        while (match.find()) {
            String paramTypes = match.group(1);
            paramTypes = filterParamTypes(paramTypes, definedClass);
            paramList.add(paramTypes);
        }
        return String.join(",", paramList);
    }

    private static String filterParamTypes(String paramTypes, List<ClassName> definedClass) {
        HashMap<String, ClassName> typeReplacements = new HashMap<>();
        Matcher match = METHOD_PARAM_TYPE.matcher(paramTypes);
        while (match.find()) {
            final String rawClassName = match.group(1);
            typeReplacements.putIfAbsent(
                    rawClassName, ClassName.autoDetect(rawClassName, definedClass));
        }

        for (Map.Entry<String, ClassName> typeReplacement : typeReplacements.entrySet()) {
            paramTypes.replace(
                    typeReplacement.getKey(), typeReplacement.getValue().getFullName());
        }
        return paramTypes;
    }

    /**
     * Format lại code kiểu "máy" đọc.
     *
     * @param code Source code gốc.
     * @return Code người khó đọc
     */
    public static String machineFormating(String code) {
        return code.replaceAll("\"[\\s\\S]*?\"", "") // ko có chỗ cho string const xd
                .replaceAll("/\\*[\\s\\S]*?\\*/", "") // Xóa comment dạng /* */
                .replaceAll("//.*", "") // Xóa comment dạng //
                .replaceAll(" +(\\W) +", "$1") // xóa kiểu "a = b" -> "a=b" (aka sát lại gần nhau)
                .replaceAll("(\\s)\\s+", "$1") // Rút gọn double spacing
                .replaceAll("\\s*([{}])\\s*", "\n$1\n") // Format lại new line của code block
                .replaceAll("\n\n+", "\n") // Xóa double new line
        ;
    }

    /**
     * Class tên {@code fullName} có tồn tại không?
     *
     * @param fullName Tên đầy đủ của class
     * @return {@code true} nếu tồn tại, {@code false} nếu em cung dang voi thi con co hoi gi cho
     *     toi :<
     */
    public static boolean isClassExisted(String fullName) {
        try {
            Class<?> clazz = Class.forName(fullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
