import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link Class}</h1>
 *
 * Một Class. Yep. Bạn mong chờ điều gì chứ :))). <br>
 * Giống như {@link java.lang.Class}
 *
 * @apiNote checked
 */
public final class Class extends Definition {
    /**
     * Lọc ra các {@link Class} trong {@link HashSet} các {@link Definition}.
     *
     * @param definitions Các {@link Definition}
     * @return Các {@link Class}
     */
    public static List<Class> filter(List<Definition> definitions) {
        List<Class> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof Class) {
                res.add((Class) definition);
            }
        }
        return res;
    }

    /**
     * Tìm kiếm/Tạo {@link Class} phù hợp với tên {@code name} trong {@code definedClasses}.
     *
     * @param name Tên cần tìm
     * @param definedClasses Danh sách các {@lick Class} đã định nghĩa
     * @return Class phù hợp với {@code name}
     */
    public static Class filter(String name, List<Class> definedClasses) {
        // Đã là Full name
        boolean isFullName = Utilities.isClassExisted(name);

        // Nằm trong đống defined
        for (Class definedClass : definedClasses) {
            if (isFullName) {
                if (name.equals(definedClass.getFullName())) {
                    return definedClass;
                }
            } else if (name.equals(definedClass.getSimpleName())) {
                return definedClass;
            }
        }

        // Vẫn ko thấy, dành thử với với class sẵn có thường thấy trong java
        final String[] commons = {"java.util.", "java.lang."};
        for (String common : commons) {
            final var possibleName = common + name;
            if (Utilities.isClassExisted(possibleName)) {
                final Class newClass = new Class(possibleName); // không quan tâm tới package ở đây
                definedClasses.add(newClass);
                return newClass;
            }
        }

        // thua, hết cứu, vậy là lần cuối đi bên nhau cay đắng nhưng ko đau :<
        final Class newClass = new Class(name);
        definedClasses.add(newClass);
        return newClass;
    }

    @Override
    protected void readSignature(String signature, List<Definition> externalDefinition) {
        Matcher internalMatch = Patterns.CLASS.matcher(signature);
        if (internalMatch.matches()) {
            simpleName = internalMatch.group(1);
            return;
        }

        Matcher externalMatch = Patterns.IMPORT.matcher(signature);
        if (externalMatch.matches()) {
            final String patentName = externalMatch.group(1);
            simpleName = externalMatch.group(2);

            final List<Package> packages = Package.filter(externalDefinition);
            packages.removeIf(pkg -> !pkg.getFullName().equals(patentName));
            if (packages.isEmpty()) {
                simpleName =
                        String.format(
                                "%s%s%s", patentName, !patentName.isEmpty() ? "." : "", simpleName);
            } else {
                parent = packages.get(0);
            }
            return;
        }

        throw new IllegalArgumentException("Không đọc được Class: " + signature);
    }

    @Override
    public void readCodeBlock(Scanner source, List<Definition> externalDefinition) {
        // TODO: Logic đọc Method bắt đầu ở đây.
    }

    @Override
    public String getFullName() {
        final String parentFullName = (parent != null) ? parent.getFullName() : "";
        return String.format("%s.%s", parentFullName, simpleName);
    }

    private Class(String simpleName) {
        this.simpleName = simpleName;
    }

    public Class(Definition parent) {
        this.parent = parent;
    }

    public Class(
            Definition parent,
            String definition,
            Scanner source,
            LinkedList<Definition> externalDeclaration) {
        this(parent);
        readCodeBlock(source, externalDeclaration);
    }
}
