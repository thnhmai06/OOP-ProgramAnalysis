import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * <h1>{@link Class}</h1>
 *
 * Một Class đầy đủ được định nghĩa trong {@link SourceCode}. <br>
 * Giống như {@link java.lang.Class}
 *
 * @see SnapshotClass
 */
public class Class extends Definition {
    public static final Pattern PATTERN =
            Pattern.compile("public (?:\\w+\\s+)*(?:class|interface) (\\w+)"); // [name]

    private Snapshot parent;
    private final HashSet<SnapshotClass> declared = new HashSet<>();
    private final HashSet<Method> methods = new HashSet<>();

    @Override
    public void read(String definition, Scanner source, HashSet<SnapshotClass> declared) {
        Matcher match = PATTERN.matcher(definition);
        if (match.matches()) {
            simpleName = match.group(1);
        } else return;
    }

    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public Snapshot takeSnapshot() {
        return new SnapshotClass(this);
    }

    public Snapshot getParent() {
        return parent;
    }

    public void setParent(Snapshot parent) {
        this.parent = parent;
    }

    public HashSet<SnapshotClass> getDeclared() {
        return declared;
    }

    public HashSet<Method> getMethods() {
        return methods;
    }

    public Class(Snapshot parent) {
        this.parent = parent;
        declared.add((SnapshotClass) takeSnapshot());
    }

    public Class(
            Snapshot parent, String definition, Scanner source, HashSet<SnapshotClass> declared) {
        this(parent);
        read(definition, source, declared);
    }
}
