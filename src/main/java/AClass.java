import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
public final class AClass extends Declaration {
    /**
     * Parse hết tên Generic có trong {@code rawGenerics} về {@link AClass}.
     *
     * @param rawGenerics Generics ở dạng thô
     * @return Các {@link AClass} tương ứng mỗi generic
     */
    public static LinkedHashSet<Declaration> parsingGenerics(String rawGenerics) {
        LinkedHashSet<String> genericNames = Patterns.getAllGenericName(rawGenerics);
        LinkedHashSet<Declaration> res = new LinkedHashSet<>();
        if (!genericNames.isEmpty()) {
            genericNames.forEach(name -> res.add(new AClass(name)));
        }
        return res;
    }

    /**
     * Tìm kiếm/Tạo {@link AClass} phù hợp với tên {@code name} trong {@code declared}. <br>
     * <i> aka tìm bố mẹ cho trẻ lạc :> </i>
     *
     * @param name Tên cần tìm
     * @param declared Danh sách các {@link AClass} đã định nghĩa
     * @param fallback Sử dụng {@link Declaration} này làm parent nếu không thấy parent
     * @return AClass phù hợp với {@code name}
     */
    public static AClass find(
            String name, ExtendedLinkedHashSet<Declaration> declared, Declaration fallback) {
        // Đã là Full name
        boolean isFullName = Utilities.isClassExisted(name);

        // Nằm trong đống declared
        for (Declaration declaration : declared.view()) {
            if (!(declaration instanceof AClass)) {
                continue;
            }
            if (isFullName) {
                if (name.equals(declaration.getFullName())) {
                    return (AClass) declaration;
                }
            } else if (name.equals(declaration.getSimpleName())) {
                return (AClass) declaration;
            }
        }

        // Java class commons
        for (String common : Utilities.commonPackages) {
            final String possibleName = common + name;
            if (Utilities.isClassExisted(possibleName)) {
                final AClass newClass =
                        new AClass(possibleName); // không quan tâm tới package ở đây
                declared.getBase().add(newClass);
                return newClass;
            }
        }

        // thua, hết cứu, vậy là lần cuối đi bên nhau cay đắng nhưng ko đau :<
        // chắc chưa bạn? Nếu không có import thì nó vẫn có thể là cùng package là không cần import
        // nè xdxd
        final AClass newClass = new AClass(name);
        if (Character.isUpperCase(name.charAt(0))) {
            newClass.parent = fallback;
        }
        declared.getBase().add(newClass);
        return newClass;
    }

    @Override
    protected void readSignature(
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        Matcher externalMatch = Patterns.IMPORT.matcher(signature);
        if (externalMatch.matches()) {
            final String parentName = externalMatch.group(1);
            simpleName = externalMatch.group(2);

            // parent của class có thể là package, hoặc class
            if (!parentName.isEmpty()) {
                for (Declaration declaration : externalDeclaration.view()) {
                    if (declaration.getFullName().equals(parentName)) {
                        parent = declaration;
                        return;
                    }
                }
                parent = new APackage(parentName);
                externalDeclaration.getBase().add(parent);
            }
            return;
        }

        Matcher internalMatch = Patterns.CLASS.matcher(signature);
        if (internalMatch.matches()) {
            simpleName = internalMatch.group(2);
            String rawGenerics = internalMatch.group(3);
            internalDeclaration.addAll(AClass.parsingGenerics(rawGenerics));
            return;
        }

        throw new IllegalArgumentException("Không đọc được AClass: " + signature);
    }

    @Override
    public void readCodeBlock(
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        LinkedHashMap<AMethod, String> holder = new LinkedHashMap<>();
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                final String line = source.nextLine();

                if (Patterns.CLASS.matcher(line).matches()) {
                    final AClass clazz = new AClass(this, line, source, getDeclared(), fallback);
                    internalDeclaration.add(clazz);
                } else if (Patterns.METHOD.matcher(line).find()) {
                    final AMethod method = new AMethod(this);
                    internalDeclaration.add(method);
                    holder.put(method, line);

                    method.readCodeBlock(source, fallback);
                }

                // update balance
                balance += Utilities.countChar(line, '{');
                balance -= Utilities.countChar(line, '}');
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

    public AClass(String simpleName) {
        this.simpleName = simpleName;
    }

    public AClass(
            Declaration parent,
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        this.parent = parent;
        readSignature(signature, externalDeclaration, fallback);
    }

    public AClass(
            Declaration parent,
            String signature,
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        this(parent, signature, externalDeclaration, fallback);
        readCodeBlock(source, externalDeclaration, fallback);
    }
}
