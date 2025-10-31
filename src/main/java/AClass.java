import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link AClass}</h1>
 *
 * <p>Một AClass. Yep. Bạn mong chờ điều gì chứ :))). <br>
 * Giống như {@link java.lang.Class}
 *
 * @apiNote checked
 */
public final class AClass extends Definition {
    /**
     * Lọc ra các {@link AClass} trong {@link HashSet} các {@link Definition}.
     *
     * @param definitions Các {@link Definition}
     * @return Các {@link AClass}
     */
    public static List<AClass> filter(List<Definition> definitions) {
        List<AClass> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof AClass) {
                res.add((AClass) definition);
            }
        }
        return res;
    }

    /**
     * Tìm kiếm/Tạo {@link AClass} phù hợp với tên {@code name} trong {@code definedClasses}. <br>
     * <i> aka tìm bố mẹ cho trẻ lạc :> </i>
     *
     * @param name Tên cần tìm
     * @param definedClasses Danh sách các {@link AClass} đã định nghĩa
     * @param fallback Sử dụng {@link Definition} này làm parent nếu không thấy parent
     * @return AClass phù hợp với {@code name}
     */
    public static AClass find(String name, List<AClass> definedClasses, Definition fallback) {
        // Đã là Full name
        boolean isFullName = AUtilities.isClassExisted(name);

        // Nằm trong đống defined
        for (AClass definedClass : definedClasses) {
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
            final String possibleName = common + name;
            if (AUtilities.isClassExisted(possibleName)) {
                final AClass newClass =
                        new AClass(possibleName); // không quan tâm tới package ở đây
                definedClasses.add(newClass);
                return newClass;
            }
        }

        // thua, hết cứu, vậy là lần cuối đi bên nhau cay đắng nhưng ko đau :<
        final AClass newClass = new AClass(name);
        // ngược lại -> kiểu nguyên thủy
        if (Character.isUpperCase(name.charAt(0))) {
            newClass.parent = fallback;
        }
        definedClasses.add(newClass);
        return newClass;
    }

    @Override
    protected void readSignature(
            String signature, List<Definition> externalDefinition, Definition fallback) {
        Matcher externalMatch = Patterns.IMPORT.matcher(signature);
        if (externalMatch.matches()) {
            final String parentName = externalMatch.group(1);
            simpleName = externalMatch.group(2);

            // parent của class có thể là package, hoặc class
            if (!parentName.isEmpty()) {
                for (Definition definition : externalDefinition) {
                    if (definition.getFullName().equals(parentName)) {
                        parent = definition;
                        return;
                    }
                }
                simpleName = String.format("%s.%s", parentName, simpleName);
            }
            return;
        }

        Matcher internalMatch = Patterns.CLASS.matcher(signature);
        if (internalMatch.find()) {
            simpleName = internalMatch.group(1);
            return;
        }

        throw new IllegalArgumentException("Không đọc được AClass: " + signature);
    }

    @Override
    public void readCodeBlock(
            Scanner source, List<Definition> externalDefinition, Definition fallback) {
        LinkedHashMap<AMethod, String> holder = new LinkedHashMap<>();
        final List<Definition> definedClassAndPackage = getDeclared(); // lazy
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                final String line = source.nextLine();

                // TH AClass lồng class (bỏ qua)
                //                if (Patterns.CLASS.matcher(line).matches()) {
                //                    final AClass clazz = new AClass(this, line, source,
                // definedClassAndPackage);
                //                    localDeclared.add(clazz);
                //                    definedClassAndPackage.add(clazz); // lazy update
                //                } else
                if (Patterns.METHOD.matcher(line).find()) {
                    final AMethod method = new AMethod(this);
                    localDeclared.add(method);
                    holder.put(method, line);

                    method.readCodeBlock(source, fallback);
                }

                // update balance
                balance += AUtilities.countChar(line, '{');
                balance -= AUtilities.countChar(line, '}');
            } while (source.hasNextLine() && balance >= 0);
        }

        for (Map.Entry<AMethod, String> hold : holder.entrySet()) {
            final AMethod method = hold.getKey();
            final String signature = hold.getValue();
            method.readSignature(signature, this.getDeclared(), fallback);
        }
    }

    @Override
    public String getFullName() {
        if (parent == null) {
            return getSimpleName();
        }
        return String.format("%s.%s", parent.getFullName(), simpleName);
    }

    private AClass(String simpleName) {
        this.simpleName = simpleName;
    }

    public AClass(
            Definition parent,
            String signature,
            List<Definition> externalDeclaration,
            Definition fallback) {
        this.parent = parent;
        readSignature(signature, externalDeclaration, fallback);
    }

    public AClass(
            Definition parent,
            String signature,
            Scanner source,
            List<Definition> externalDeclaration,
            Definition fallback) {
        this(parent, signature, externalDeclaration, fallback);
        readCodeBlock(source, externalDeclaration, fallback);
    }
}
