/** Utilities. */
public final class Utilities {
    // Regex: java\.(?:[\w$]+\.)+
    public static final String[] commonPackages = {
        "java.util.",
        "java.lang.",
        "java.",
        "java.time.",
        "java.nio.",
        "java.io.",
        "java.sql.",
        "java.nio.file.",
        "java.util.function.",
        "java.time.temporal."
    };

    private Utilities() {}

    /**
     * AClass có tên {@code fullName} có tồn tại không.
     *
     * @param fullName Tên đầy đủ của class
     * @return {@code true} nếu tồn tại, {@code false} nếu em cung dang voi thi con co hoi gi cho
     *     toi :<
     */
    public static boolean isClassExisted(String fullName) {
        try {
            java.lang.Class<?> clazz = java.lang.Class.forName(fullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Xóa tất cả string cố định và comment trong code.
     *
     * @param code Mã nguồn
     * @return Code không có comment và string cố định
     */
    public static String removeStringAndComments(String code) {
        return code.replaceAll("\"[\\s\\S]*?\"", "") // ko có chỗ cho string constant xd
                .replaceAll("/\\*[\\s\\S]*?\\*/", "") // Xóa comment dạng /* */
                .replaceAll("//.*", "") // Xóa comment dạng //
                .trim();
    }

    /**
     * Format lại code để "máy" dễ đọc.
     *
     * @param code Source code gốc.
     * @return Code mà người khó đọc xd
     */
    public static String machineFormating(String code) {
        return code.replaceAll(
                                "\\s*([(,=+\\-*/)])\\s+",
                                "$1") // xóa space quanh dấu phân cách (trừ dấu chấm)
                        .replaceAll("\\.\\.\\.", "") // xóa varargs
                        .replaceAll("(\\s)\\s+", "$1") // Rút gọn double spacing
                        .replaceAll(
                                "(package [\\s\\S]*?);", "$1 {") // đưa package về format code block
                        .replaceAll(
                                "\\s*([{}])\\s*", "\n$1\n") // Format lại new line của code block
                        .replaceAll("\n\n+", "\n") // Xóa double new line
                + "}" // close của package
        ;
    }

    /**
     * Đếm số kí tự {@code character}.
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
