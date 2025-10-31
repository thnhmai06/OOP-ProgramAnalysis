import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
public final class AMethod extends Definition {
    /**
     * Lọc ra các {@link AMethod} trong {@link List} các {@link Definition}.
     *
     * @param definitions Các {@link Definition}
     * @return Các {@link AMethod}
     */
    public static List<AMethod> filter(List<Definition> definitions) {
        List<AMethod> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof AMethod) {
                res.add((AMethod) definition);
            }
        }
        return res;
    }

    private Parameters parameters;

    @Override
    protected void readCodeBlock(
            Scanner source, List<Definition> externalDefinition, Definition fallback) {
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                // * Không cần code trong method
                // ...

                final String line = source.nextLine();
                balance += AUtilities.countChar(line, '{');
                balance -= AUtilities.countChar(line, '}');
            } while (source.hasNextLine() && balance >= 0);
        }
    }

    public void readCodeBlock(Scanner source, Definition fallback) {
        readCodeBlock(source, new LinkedList<>(), fallback);
    }

    @Override
    protected void readSignature(
            String signature, List<Definition> externalDefinition, Definition fallback) {
        Matcher match = Patterns.METHOD.matcher(signature);
        if (match.matches()) {
            simpleName = match.group(1);
            // parent của method chỉ có thể là class
            parameters =
                    new Parameters(match.group(2), AClass.filter(externalDefinition), fallback);
        }
    }

    @Override
    public String getFullName() {
        return String.format("%s(%s)", simpleName, parameters.toString());
    }

    public AMethod(Definition parent) {
        this.parent = parent;
    }

    public AMethod(
            Definition parent,
            String signature,
            List<Definition> externalDefinition,
            Definition fallback) {
        this(parent);
        readSignature(signature, externalDefinition, fallback);
    }

    /**
     * Những tham số được truyền vào {@link AMethod}. Đại diện cho Tất cả các tham số truyền vào
     * {@link AMethod}.
     */
    private static final class Parameters {
        public LinkedHashMap<String, String> values = new LinkedHashMap<>();

        /**
         * Làm cho các AClass trong {@code classes} thành Tên đầy đủ.
         *
         * @param classes Xâu cần thực hiện
         * @param externalClasses Các {@link AClass} đã được định nghĩa
         * @return Xâu với các AClass là Tên đầy đủ
         */
        private static String makeClassFullName(
                String classes, List<AClass> externalClasses, Definition fallback) {
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

        public Parameters(String signature, List<AClass> externalClass, Definition fallback) {
            Matcher match = Patterns.METHOD_PARAMETER.matcher(signature);
            while (match.find()) {
                String name = match.group(2);
                String types = makeClassFullName(match.group(1), externalClass, fallback);
                values.put(name, types);
            }
        }

        @Override
        public String toString() {
            return String.join(",", values.values());
        }
    }
}
