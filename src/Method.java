import java.util.*;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link Method}</h1>
 *
 * Một phương thức trong {@link Class}. <br>
 * Giống {@link java.lang.reflect.Method}. Không chứa Code block.
 *
 * @apiNote checked
 */
public final class Method extends Definition {
    /**
     * Lọc ra các {@link Method} trong {@link List} các {@link Definition}.
     *
     * @param definitions Các {@link Definition}
     * @return Các {@link Method}
     */
    public static List<Method> filter(List<Definition> definitions) {
        List<Method> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof Method) {
                res.add((Method) definition);
            }
        }
        return res;
    }

    private Parameters parameters;

    /**
     * @deprecated Không hỗ trợ
     */
    @Override
    protected void readCodeBlock(Scanner source, List<Definition> externalDefinition) {}

    @Override
    protected void readSignature(String signature, List<Definition> externalDefinition) {
        Matcher match = Patterns.METHOD.matcher(signature);
        if (match.matches()) {
            simpleName = match.group(1);
            parameters = new Parameters(match.group(2), Class.filter(externalDefinition));
        }
    }

    @Override
    public String getFullName() {
        return String.format("%s(%s)", simpleName, parameters.toString());
    }

    public Method(Definition parent) {
        this.parent = parent;
    }

    public Method(Definition parent, String signature, List<Definition> externalDefinition) {
        this(parent);
        readSignature(signature, externalDefinition);
    }

    /** Những tham số được truyền vào {@link Method}. */
    private static final class Parameters {
        public LinkedHashMap<String, String> values = new LinkedHashMap<>();

        /**
         * Làm cho các Class trong {@code classes} thành Tên đầy đủ.
         *
         * @param classes Xâu cần thực hiện
         * @param definedClasses Các {@link Class} đã được định nghĩa
         * @return Xâu với các Class là Tên đầy đủ
         */
        private static String makeTypesFullName(String classes, List<Class> definedClasses) {
            HashMap<String, String> replacements = new HashMap<>();
            Matcher match = Patterns.METHOD_PARAMETER_TYPE.matcher(classes);
            while (match.find()) {
                final String className = match.group(1);
                final Class clazz = Class.filter(className, definedClasses);
                replacements.put(className, clazz.getFullName());
            }

            for (Map.Entry<String, String> replacement : replacements.entrySet()) {
                classes = classes.replace(replacement.getKey(), replacement.getValue());
            }
            return classes;
        }

        public Parameters(String definition, List<Class> definedClasses) {
            Matcher match = Patterns.METHOD_PARAMETER.matcher(definition);
            while (match.find()) {
                String name = match.group(2);
                String types = makeTypesFullName(match.group(1), definedClasses);
                values.put(name, types);
            }
        }

        @Override
        public String toString() {
            return String.join(",", values.values());
        }
    }
}
