/**
 *
 *
 * <h1>{@link Declaration}</h1>
 *
 * Một Định nghĩa cho code xuất hiện trong {@link SourceCode}.
 */
public sealed abstract class Declaration permits Definition, Snapshot {
    /**
     * Tên Đầy đủ của Định nghĩa code (giống như {@link java.lang.Class#getCanonicalName()}). <br>
     * Ví dụ: {@code java.util.List}
     *
     * @return Tên đầy đủ
     *
     * @see #getSimpleName()
     */
    abstract String getFullName();

    /**
     * Tên Đơn giản của Định nghĩa code (giống như {@link java.lang.Class#getSimpleName()}). <br>
     * Ví dụ: {@code List}
     *
     * @return Tên Đơn giản
     *
     * @see #getFullName()
     */
    abstract String getSimpleName();
}
