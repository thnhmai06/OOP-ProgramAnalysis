import java.util.HashSet;
import java.util.Scanner;

/**
 *
 *
 * <h1>{@link Definition}</h1>
 *
 * Một {@link Declaration} được khai báo đầy đủ trong {@link SourceCode}, tức chứa đầy đủ các thành
 * phần con nếu có.
 *
 * @see Snapshot
 */
public abstract non-sealed class Definition extends Declaration {
    protected String simpleName;

    public abstract void read(String definition, Scanner source, HashSet<SnapshotClass> declared);

    public abstract Snapshot takeSnapshot();

    @Override
    public String getSimpleName() {
        return simpleName;
    }
}
