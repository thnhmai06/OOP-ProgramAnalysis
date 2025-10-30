import java.util.HashSet;

/**
 * <h1>{@link SourceCode}</h1>
 *
 * <br>Mã nguồn chương trình cần phân tích.
 */
public final class SourceCode {
    private Package aPackage;
    private final HashSet<SnapshotClass> declared = new HashSet<>();
    private Class mainClass;
}
