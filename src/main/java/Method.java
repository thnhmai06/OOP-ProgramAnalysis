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

    @Override
    protected void readCodeBlock(Scanner source, List<Definition> externalDefinition) {
        int scopeLevel = 0;
        do {
            final String line = source.nextLine();
            for (int i=0; i<line.length(); ++i) {
                if (line.charAt(i) == '{') {
                    ++scopeLevel;
                } else if (line.charAt(i) == '}') {
                    --scopeLevel;
                }
            }
        } while (source.hasNextLine() && scopeLevel > 0);
    }

    public void readCodeBlock(Scanner source) {
        readCodeBlock(source, List.of());
    }

    @Override
    protected void readSignature(String signature, List<Definition> externalDefinition) {
        Matcher match = Patterns.METHOD.matcher(signature);
        if (match.matches()) {
            simpleName = match.group(1);
            // parent của method chỉ có thể là class
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
         * @param externalClasses Các {@link Class} đã được định nghĩa
         * @return Xâu với các Class là Tên đầy đủ
         */
        private static String makeClassFullName(String classes, List<Class> externalClasses) {
            HashMap<String, String> replacements = new HashMap<>();
            Matcher match = Patterns.METHOD_PARAMETER_TYPE.matcher(classes);
            while (match.find()) {
                final String className = match.group(1);
                final Class clazz = Class.filter(className, externalClasses);
                replacements.put(className, clazz.getFullName());
            }

            for (Map.Entry<String, String> replacement : replacements.entrySet()) {
                classes = classes.replace(replacement.getKey(), replacement.getValue());
            }
            return classes;
        }

        public Parameters(String signature, List<Class> externalClass) {
            Matcher match = Patterns.METHOD_PARAMETER.matcher(signature);
            while (match.find()) {
                String name = match.group(2);
                String types = makeClassFullName(match.group(1), externalClass);
                values.put(name, types);
            }
        }

        @Override
        public String toString() {
            return String.join(",", values.values());
        }
    }
}
