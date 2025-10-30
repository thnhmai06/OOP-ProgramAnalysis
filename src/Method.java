import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * <h1>{@link Method}</h1>
 *
 * Một phương thức được viết trong {@link Class}.
 */
public final class Method extends Definition {
    private static final Pattern PATTERN =
            Pattern.compile("public static [\\s\\S]*? (\\w+)\\(([\\s\\S]*?)\\)"); // [name, params]

    private SnapshotClass parent;
    private Parameters parameters;

    @Override
    public void read(String definition, Scanner source, HashSet<SnapshotClass> declared) {
        Matcher match = PATTERN.matcher(definition);
        if (match.matches()) {
            simpleName = match.group(1);
            parameters = new Parameters(match.group(2), declared);
        }
    }

    @Override
    public Snapshot takeSnapshot() {
        return null;
    }

    @Override
    String getFullName() {
        return String.format("%s(%s)", simpleName, parameters.toString());
    }

    public SnapshotClass getParent() {
        return parent;
    }

    public void setParent(SnapshotClass parent) {
        this.parent = parent;
    }

    /** Các tham số truyền vào {@link Method}. */
    private static final class Parameters {
        private static final Pattern PATTERN =
                Pattern.compile(
                        "(?:final\\s+)?([\\w.]+(?:<[\\s\\S]*?>)?)(?:...|\\[])? (\\w+)"); // [types,
        // name]
        private static final Pattern PATTERN_TYPE = Pattern.compile("\\b([\\w.]+)\\b"); // [type]
        public LinkedHashMap<String, String> value = new LinkedHashMap<>();

        private static String makeTypesFullName(
                String types, HashSet<SnapshotClass> declared) {
            HashMap<String, SnapshotClass> replacements = new HashMap<>();
            Matcher match = PATTERN_TYPE.matcher(types);
            while (match.find()) {
                String rawType = match.group(1);
                SnapshotClass type = new SnapshotClass(rawType, declared);
                replacements.put(rawType, type);
            }

            for (Map.Entry<String, SnapshotClass> replacement: replacements.entrySet()) {
                types = types.replace(replacement.getKey(), replacement.getValue().getFullName());
            }
            return types;
        }

        public Parameters(String definition, HashSet<SnapshotClass> declared) {
            Matcher match = PATTERN.matcher(definition);
            while (match.find()) {
                String name = match.group(2);
                String types = makeTypesFullName(match.group(1), declared);
                value.put(name, types);
            }
        }

        @Override
        public String toString() {
            return String.join(",", value.values());
        }
    }
}
