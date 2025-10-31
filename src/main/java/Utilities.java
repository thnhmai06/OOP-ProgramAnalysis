import java.lang.Class;

public final class Utilities {
    /**
     * Class có tên {@code fullName} có tồn tại không?
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

    /**
     * Format lại code để "máy" dễ đọc.
     *
     * @param code Source code gốc.
     * @return Code mà người khó đọc xd
     */
    public static String machineFormating(String code) {
        return code
                .replaceAll("\"[\\s\\S]*?\"", "") // ko có chỗ cho string constant xd
                .replaceAll("/\\*[\\s\\S]*?\\*/", "") // Xóa comment dạng /* */
                .replaceAll("//.*", "") // Xóa comment dạng //
                .replaceAll(" +(\\W) +", "$1") // xóa kiểu "a = b" -> "a=b" (aka làm sát lại gần nhau)
                .replaceAll("(\\s)\\s+", "$1") // Rút gọn double spacing
                .replaceAll("\\s*([{}])\\s*", "\n$1\n") // Format lại new line của code block
                .replaceAll("\n\n+", "\n") // Xóa double new line
        ;
    }

    private Utilities() {}
}
