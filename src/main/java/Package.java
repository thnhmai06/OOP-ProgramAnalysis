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
    private static final String NO_NAME = "package ;";
    private Class main;

    /**
     * Lọc ra các {@link Package} trong {@link List} các {@link Definition}.
     *
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
        throw new IllegalArgumentException("Không đọc được Package: " + signature);
    }

    @Override
    protected void readCodeBlock(Scanner source, List<Definition> externalDefinition) {
        if (source.nextLine().contains("{")) {
            do {
                final String line = source.nextLine();
                if (line.contains("}")) {
                    break;
                }

                if (Patterns.IMPORT.matcher(line).matches()) {
                    localDeclared.add(new Class(this, line, getDeclared()));
                    continue;
                }
                if (Patterns.CLASS.matcher(line).matches()) {
                    main = new Class(this, line, getDeclared());
                    localDeclared.add(main);
                    continue;
                }
            } while (source.hasNextLine());
        }
    }

    public void readSignature(String signature) {
        readSignature(signature, List.of());
    }

    public void readCodeBlock(Scanner source) {
        readCodeBlock(source, List.of());
    }

    public void readAll(String signature, Scanner source) {
        readSignature(signature);
        readCodeBlock(source);
    }

    @Override
    public String getFullName() {
        return getSimpleName();
    }

    public Class getMain() {
        return main;
    }

    public void setMain(Class main) {
        this.main = main;
    }

    public Package(String code) {
        Utilities.removeStringAndComments(code);
        if (!code.startsWith("package")) {
            code = NO_NAME + "\n" + code;
        }
        Utilities.machineFormating(code);

        Scanner source = new Scanner(code);
        String signature = source.nextLine();
        readAll(signature, source);
    }
}
