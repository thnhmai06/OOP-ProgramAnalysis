import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 *
 *
 * <h1>{@link APackage}</h1>
 *
 * <p>Một APackage, nơi mà chứa các {@link AClass}. <br>
 * Giống {@link java.lang.Package}.
 *
 * @apiNote checked
 */
public final class APackage extends Definition {
    private static final String NO_NAME = "package ;";
    private AClass main;

    /**
     * Lọc ra các {@link APackage} trong {@link List} các {@link Definition}.
     *
     * @param definitions Các {@link Definition}
     * @return Các {@link APackage}
     */
    public static List<APackage> filter(List<Definition> definitions) {
        List<APackage> res = new LinkedList<>();
        for (Definition definition : definitions) {
            if (definition instanceof APackage) {
                res.add((APackage) definition);
            }
        }
        return res;
    }

    @Override
    protected void readSignature(
            String signature, List<Definition> externalDefinition, Definition fallback) {
        Matcher internalMatch = Patterns.PACKAGE.matcher(signature);
        if (internalMatch.matches()) {
            simpleName = internalMatch.group(1);
            return;
        }
        throw new IllegalArgumentException("Không đọc được APackage: " + signature);
    }

    public void readSignature(String signature, Definition fallback) {
        readSignature(signature, new LinkedList<>(), fallback);
    }

    @Override
    protected void readCodeBlock(
            Scanner source, List<Definition> externalDefinition, Definition fallback) {
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                final String line = source.nextLine();

                if (Patterns.IMPORT.matcher(line).matches()) {
                    localDeclared.add(new AClass(this, line, getDeclared(), fallback));
                } else if (Patterns.CLASS.matcher(line).matches()) {
                    main = new AClass(this, line, source, getDeclared(), fallback);
                    localDeclared.add(main);
                }

                // update balance
                balance += AUtilities.countChar(line, '{');
                balance -= AUtilities.countChar(line, '}');
            } while (source.hasNextLine() && balance >= 0);
        }
    }

    public void readCodeBlock(Scanner source, Definition fallback) {
        readCodeBlock(source, new LinkedList<>(), fallback);
    }

    public void readAll(String signature, Scanner source, Definition fallback) {
        readSignature(signature, fallback);
        readCodeBlock(source, fallback);
    }

    @Override
    public String getFullName() {
        return getSimpleName();
    }

    public AClass getMain() {
        return main;
    }

    public void setMain(AClass main) {
        this.main = main;
    }

    /**
     * Khởi tạo một {@link APackage} từ code.
     *
     * @param code Mã nguồn
     */
    public APackage(String code) {
        code = AUtilities.removeStringAndComments(code);
        if (!code.startsWith("package")) {
            code = NO_NAME + "\n" + code;
        }
        code = AUtilities.machineFormating(code);

        Scanner source = new Scanner(code);
        String signature = source.nextLine();
        readAll(signature, source, this);
    }
}
