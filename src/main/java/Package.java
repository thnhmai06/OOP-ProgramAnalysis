import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link Package}</h1>
 *
 * <p>Một Package, nơi mà chứa các {@link Class}. <br>
 * Giống {@link java.lang.Package}.
 *
 * @apiNote checked
 */
public final class Package extends Definition {
    /**
     * Lọc ra các {@link Package} trong {@link List} các {@link Definition}.
     * @param definitions Các {@link Definition}
     * @return Các {@link Package}
     */
    public static List<Package> filter(List<Definition> definitions) {
        List<Package> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof Package) {
                res.add((Package) definition);
            }
        }
        return res;
    }

    @Override
    protected void readSignature(String signature, List<Definition> externalDefinition) {
        Matcher internalMatch = Patterns.PACKAGE.matcher(signature);
        if (internalMatch.matches()) {
            simpleName = internalMatch.group(1);
            return;
        }

        Matcher externalMatch = Patterns.IMPORT.matcher(signature);
        if (externalMatch.matches()) {
            simpleName = externalMatch.group(1);
            return;
        }

        throw new IllegalArgumentException("Không đọc được Package: " + signature);
    }

    @Override
    protected void readCodeBlock(Scanner source, List<Definition> externalDefinition) {
        //TODO: Tất cả logic bắt đầu từ đây
    }

    @Override
    public String getFullName() {
        return getSimpleName();
    }

    public Package(String signature) {
        readSignature(signature, List.of());
    }

    public Package (String signature, Scanner source) {
        this(signature);
        readCodeBlock(source, List.of());
    }
}
