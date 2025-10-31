import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 *
 *
 * <h1>{@link Declaration}</h1>
 *
 * <p>Một Định nghĩa. Yep. Nó là thành phần để tương tác khi code.
 *
 * @apiNote checked
 */
public abstract class Declaration {
    protected Declaration parent = null;
    protected final LinkedHashSet<Declaration> internalDeclaration = new LinkedHashSet<>();
    protected String simpleName;

    /**
     * Đọc Chữ kí của {@link Declaration} tương ứng.
     *
     * @param signature Chữ kí
     * @param externalDeclaration Các {@link Declaration} đã được khai báo bên ngoài
     * @param fallback Sử dụng {@link Declaration} này làm parent nếu không thấy parent
     * @see #readAll(String, Scanner, ExtendedLinkedHashSet, Declaration)
     */
    protected abstract void readSignature(
            String signature,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback);

    /**
     * Đọc Code block của {@link Declaration} tương ứng.
     *
     * @param source {@link Scanner} trỏ tới nơi bắt đầu Code block
     * @param externalDeclaration Các {@link Declaration} đã được khai báo bên ngoài
     * @param fallback Sử dụng {@link Declaration} này làm parent nếu không thấy parent
     * @see #readAll(String, Scanner, ExtendedLinkedHashSet, Declaration)
     */
    protected abstract void readCodeBlock(
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback);

    /**
     * Đọc Chữ kí và Code block của {@link Declaration} tương ứng.
     *
     * @param signature Chữ kí
     * @param source {@link Scanner} trỏ tới nơi bắt đầu Code block
     * @param externalDeclaration Các {@link Declaration} đã được khai báo bên ngoài
     * @see #readSignature(String, ExtendedLinkedHashSet, Declaration)
     * @see #readCodeBlock(Scanner, ExtendedLinkedHashSet, Declaration)
     */
    protected final void readAll(
            String signature,
            Scanner source,
            ExtendedLinkedHashSet<Declaration> externalDeclaration,
            Declaration fallback) {
        readSignature(signature, externalDeclaration, fallback);
        readCodeBlock(source, externalDeclaration, fallback);
    }

    /**
     * Lấy tất cả {@link Declaration} có thể truy cập được ở {@link Declaration} này.
     *
     * @return Các khai báo có thể dùng được
     */
    public final ExtendedLinkedHashSet<Declaration> getDeclared() {
        ExtendedLinkedHashSet<Declaration> declared =
                new ExtendedLinkedHashSet<>(internalDeclaration);
        if (parent != null) {
            declared.extend(parent.getDeclared()); // parent đã chứa nó
        } else {
            // trường hợp không tự chứa nó
            declared.extend(new LinkedHashSet<>(Collections.singletonList(this)));
        }
        declared.extend(internalDeclaration);
        return declared;
    }

    public final LinkedHashSet<Declaration> getLocalDeclaration() {
        return internalDeclaration;
    }

    /**
     * Tên Đầy đủ của Định nghĩa code (giống như {@link java.lang.Class#getCanonicalName()}). <br>
     * Ví dụ: {@code java.util.List}
     *
     * @return Tên đầy đủ
     * @see #getSimpleName()
     */
    public abstract String getFullName();

    /**
     * Tên Đơn giản của Định nghĩa code (giống như {@link java.lang.Class#getSimpleName()}). <br>
     * Ví dụ: {@code List}
     *
     * @return Tên Đơn giản
     * @see #getFullName()
     */
    public final String getSimpleName() {
        return simpleName;
    }

    public final void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public final Declaration getParent() {
        return parent;
    }

    public final void setParent(Declaration parent) {
        this.parent = parent;
    }
}
