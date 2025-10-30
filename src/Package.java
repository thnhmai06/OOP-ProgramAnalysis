import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * <h1>{@link Package}</h1>
 *
 * <p>Một Tham chiếu tới Package đã được định nghĩa, nơi mà chứa các {@link SourceCode}. <br>
 * Tương đương với {@link java.lang.Package}. Không chứa thành phần con.
 */
public class Package extends Snapshot {
    public static final Pattern PATTERN = Pattern.compile("package ([\\w.]+);"); // [name]

    @Override
    void read(String definition) {
        Matcher match = PATTERN.matcher(definition);
        if (match.matches()) {
            fullName = match.group(1);
        }
    }

    public Package() {}

    public Package(String definition) {
        read(definition);
    }
}
