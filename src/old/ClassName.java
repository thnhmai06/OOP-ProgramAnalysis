package old;

import java.util.List;

/** Tên của một class/package. */
public final class ClassName {
    private StringBuilder fullName = new StringBuilder();

    /**
     * Chỉ lấy tên đơn giản của lớp.
     *
     * @return Tên đơn giản
     */
    public String getSimpleName() {
        if (fullName.isEmpty()) {
            return "";
        }
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    /**
     * Tự kiểm tra xem tên đầy đủ của class có thể là gì.
     *
     * @param className Tên class
     * @param definedClass Những class đã được định nghĩa trong code
     * @return Tên đầy đủ
     */
    public static ClassName autoDetect(String className, List<ClassName> definedClass) {
        // Đã là tên đầy đủ
        if (Utilities.isClassExisted(className)) {
            return new ClassName(className);
        }

        // Nằm trong đống đã định nghĩa
        for (ClassName clazz : definedClass) {
            if (className.equals(clazz.getSimpleName())) {
                return clazz;
            }
        }

        // Vẫn ko thấy, dành thử với với class trong java
        final String[] commons = {"java.util.", "java.lang."};
        for (String common : commons) {
            if (Utilities.isClassExisted(common + className)) {
                return new ClassName(common, className);
            }
        }

        // thua, hết cứu, vậy là lần cuối đi bên nhau cay đắng nhưng ko đau :<
        return new ClassName(className);
    }

    /**
     * Thêm con vào sau tên class.
     *
     * @param childs Tên các con
     */
    public void addChild(String... childs) {
        for (String child : childs) {
            if (!fullName.isEmpty()) {
                fullName.append('.');
            }
            fullName.append(child);
        }
    }

    /** Xóa con ở cuối tên class. */
    public void popChild() {
        if (!fullName.toString().contains(".")) {
            return;
        }
        fullName.delete(fullName.lastIndexOf("."), fullName.length());
    }

    public String getFullName() {
        return fullName.toString();
    }

    public void setFullName(String... children) {
        if (!fullName.isEmpty()) {
            fullName.delete(0, fullName.length());
        }
        addChild(children);
    }

    public ClassName(String... children) {
        setFullName(children);
    }

    public ClassName(ClassName other) {
        setFullName(other.getFullName());
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
