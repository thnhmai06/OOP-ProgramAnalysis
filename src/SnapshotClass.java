import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * <h1>{@link SnapshotClass}</h1>
 *
 * Một tham chiếu của Class từ bên ngoài được import vào {@link SourceCode}. <br>
 * Giống như một tham chiếu tới {@link Class}
 *
 * @see Class
 */
public class SnapshotClass extends Snapshot {
    private static final Pattern PATTERN =
            Pattern.compile("import(?: static)? ([\\w.]+);"); // [name]

    @Override
    public void read(String definition) {
        SnapshotClass snapshot = SnapshotClass.fromDefinition(definition);
        assert snapshot != null;
        if (snapshot != null) {
            this.fullName = snapshot.fullName;
        }
    }

    public static SnapshotClass fromDefinition(String definition) {
        Matcher match = PATTERN.matcher(definition);
        if (match.matches()) {
            return new SnapshotClass(match.group(1));
        }
        return null;
    }

    public SnapshotClass() {}

    public SnapshotClass(String fullName) {
        this.fullName = fullName;
    }

    public SnapshotClass(String name, HashSet<SnapshotClass> declared) {
        // Đã là FullName
        if (Utilities.isClassExisted(name)) {
            this.fullName = name;
            return;
        }

        // Nằm trong đống declared
        for (SnapshotClass snapshot : declared) {
            if (name.equals(snapshot.getSimpleName())) {
                this.fullName = snapshot.getFullName();
                return;
            }
        }

        // Vẫn ko thấy, dành thử với với class sẵn có thường thấy trong java
        final String[] commons = {"java.util.", "java.lang."};
        for (String common : commons) {
            if (Utilities.isClassExisted(common + name)) {
                this.fullName = common + name;
            }
        }

        // thua, hết cứu, vậy là lần cuối đi bên nhau cay đắng nhưng ko đau :<
        this.fullName = name;
    }

    public SnapshotClass(Class origin) {
        this.fullName = origin.getFullName();
    }
}
