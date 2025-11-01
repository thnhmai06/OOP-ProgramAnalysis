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
public final class APackage extends Declaration {
    private static final String NO_NAME = "package ;";
    private AClass main;

    /**
     * Khởi tạo {@link APackage} mới.
     *
     * <p>Gọi {@link #readAll(String)} để bắt đầu đọc code
     *
     * @see #from(String)
     */
    public APackage() {}

    /**
     * Khởi tạo {@link APackage} mới, chỉ có tên.
     *
     * @param name Tên Package
     */
    public APackage(String name) {
        simpleName = name;
    }

    /**
     * Tạo một {@link APackage} mới từ source code.
     *
     * @param code Source code
     * @return Package tương ứng
     * @see #readAll(String)
     */
    public static APackage from(String code) {
        final APackage res = new APackage();
        res.readAll(code);
        return res;
    }

    @Override
    protected void readSignature(
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        Matcher internalMatch = Patterns.PACKAGE.matcher(signature);
        if (internalMatch.matches()) {
            simpleName = internalMatch.group(1);
            return;
        }
        throw new IllegalArgumentException("Không đọc được APackage: " + signature);
    }

    public void readSignature(String signature) {
        readSignature(signature, new ExtendedLinkedHashSet<>(), null);
    }

    @Override
    protected void readCodeBlock(
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        if (source.nextLine().contains("{")) {
            int balance = 0;
            do {
                final String line = source.nextLine();
                Matcher importMatch = Patterns.IMPORT.matcher(line);
                Matcher classMatch = Patterns.CLASS.matcher(line);

                if (importMatch.matches()) {
                    internalDeclaration.add(new AClass(this, line, getDeclared(), this));
                } else if (classMatch.find()) {
                    AClass clazz = new AClass(this, line, source, getDeclared(), this);

                    String access = classMatch.group(1);
                    if (access != null && access.equals("public") && main == null) {
                        main = clazz;
                    }
                    internalDeclaration.add(clazz);
                }

                // update balance
                balance += Utilities.countChar(line, '{');
                balance -= Utilities.countChar(line, '}');
            } while (source.hasNextLine() && balance >= 0);
        }
    }

    public void readCodeBlock(Scanner source) {
        readCodeBlock(source, new ExtendedLinkedHashSet<>(), null);
    }

    /**
     * Đọc hết {@link APackage} từ code.
     *
     * @param code Mã nguồn
     */
    public void readAll(String code) {
        code = Utilities.removeStringAndComments(code);
        if (!code.startsWith("package")) {
            code = NO_NAME + "\n" + code;
        }
        code = Utilities.machineFormating(code);

        Scanner source = new Scanner(code);
        String signature = source.nextLine();
        readSignature(signature);
        readCodeBlock(source);
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
}
