import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link AMethod}</h1>
 *
 * <p>Một phương thức trong {@link AClass}. <br>
 * Giống {@link java.lang.reflect.Method}. Không chứa Code block.
 *
 * @apiNote checked
 */
public final class AMethod extends Declaration {
    private Parameters parameters;

    public AMethod(Declaration parent) {
        this.parent = parent;
    }

    public AMethod(
            Declaration parent,
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        this(parent);
        readSignature(signature, externalDeclaration, fallback);
    }

    @Override
    protected void readCodeBlock(
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                // * Không cần code trong method
                // ...

                final String line = source.nextLine();
                balance += Utilities.countChar(line, '{');
                balance -= Utilities.countChar(line, '}');
            } while (source.hasNextLine() && balance >= 0);
        }
    }

    public void readCodeBlock(Scanner source, Declaration fallback) {
        readCodeBlock(source, new ExtendedLinkedHashSet<>(), fallback);
    }

    @Override
    protected void readSignature(
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        Matcher match = Patterns.METHOD.matcher(signature);
        if (match.find()) {
            String rawGenerics = match.group(1);
            internalDeclaration.addAll(AClass.parsingGenerics(rawGenerics));

            simpleName = match.group(2);
            parameters = new Parameters(match.group(3), getDeclared(), fallback);
        }
    }

    @Override
    public String getFullName() {
        return simpleName + '(' + (parameters != null ? parameters.toString() : "") + ')';
    }

    /**
     * Những tham số được truyền vào {@link AMethod}. Đại diện cho Tất cả các tham số truyền vào
     * {@link AMethod}.
     */
    private static final class Parameters {
        public LinkedHashMap<String, String> values = new LinkedHashMap<>();

        public Parameters(
                String signature,
                ExtendedLinkedHashSet<Declaration> externalClass,
                Declaration fallback) {
            //            Matcher match = Patterns.METHOD_PARAMETER.matcher(signature);
            //            while (match.find()) {
            //                String name = match.group(2);
            //                String types = makeClassFullName(match.group(1), externalClass,
            // fallback);
            //                values.put(name, types);
            //            }
            StringBuilder type = new StringBuilder();
            StringBuilder name = new StringBuilder();
            boolean readingName = false;
            int balance = 0;

            for (char c : signature.toCharArray()) {
                if (c == '<') {
                    ++balance;
                    type.append(c);
                    continue;
                } else if (c == '>') {
                    --balance;
                    type.append(c);
                    continue;
                }

                if (balance == 0) {
                    if (c == ' ') {
                        readingName = true;
                        continue;
                    } else if (c == ',') {
                        values.put(
                                name.toString().trim(),
                                makeClassFullName(type.toString().trim(), externalClass, fallback));

                        // reset
                        type.setLength(0);
                        name.setLength(0);
                        readingName = false;
                        continue;
                    }
                }

                if (readingName) {
                    name.append(c);
                } else {
                    type.append(c);
                }
            }

            // param cuối cùng
            if (name.length() > 0 && type.length() > 0) {
                values.put(
                        name.toString().trim(),
                        makeClassFullName(type.toString().trim(), externalClass, fallback));
            }
        }

        /**
         * Làm cho các AClass trong {@code classes} thành Tên đầy đủ.
         *
         * @param classes Xâu cần thực hiện
         * @param externalClasses Các {@link AClass} đã được định nghĩa
         * @return Xâu với các AClass là Tên đầy đủ
         */
        private static String makeClassFullName(
                String classes,
                ExtendedLinkedHashSet<Declaration> externalClasses,
                Declaration fallback) {
            HashMap<String, String> replacements = new HashMap<>();
            Matcher match = Patterns.METHOD_PARAMETER_TYPE.matcher(classes);
            while (match.find()) {
                final String className = match.group(1);
                final AClass clazz = AClass.find(className, externalClasses, fallback);
                replacements.put(className, clazz.getFullName());
            }

            for (Map.Entry<String, String> replacement : replacements.entrySet()) {
                classes = classes.replace(replacement.getKey(), replacement.getValue());
            }
            return classes;
        }

        @Override
        public String toString() {
            return String.join(",", values.values());
        }
    }
}
