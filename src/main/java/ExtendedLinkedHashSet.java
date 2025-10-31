import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 *
 *
 * <h1>{@link ExtendedLinkedHashSet}</h1>
 *
 * <p>Cấu trúc dữ liệu cho phép mở rộng {@link LinkedHashSet} mà vẫn giữ nguyên bản thế gốc của nó.
 *
 * <p>Sử dụng {@link #view()} để lấy ra view của set đã được mở rộng.
 *
 * @param <T> Kiểu dữ liệu lưu trữ
 */
public final class ExtendedLinkedHashSet<T> {
    private LinkedHashSet<T> base;
    private LinkedHashSet<LinkedHashSet<T>> extend = new LinkedHashSet<>();

    /**
     * Lấy ra clone của {@link LinkedHashSet} đã được mở rộng.
     *
     * @return List đã mở rộng
     */
    public LinkedHashSet<T> view() {
        LinkedHashSet<T> res = new LinkedHashSet<>(base);
        extend.forEach(res::addAll);
        return res;
    }

    /**
     * Mở rộng set.
     *
     * @param sets Các set tham gia
     */
    @SafeVarargs
    public final void extend(LinkedHashSet<T>... sets) {
        extend.addAll(Arrays.asList(sets));
    }

    /**
     * Mở rộng set, nhưng không tự cập nhật khi set mở rộng thay đổi.
     *
     * @param extendedSet Set mở rộng
     */
    public final void extend(ExtendedLinkedHashSet<T> extendedSet) {
        extend.addAll(extendedSet.extend);
    }

    /**
     * Thu hẹp set.
     *
     * @param sets Các set tham gia
     */
    @SafeVarargs
    public final void shink(LinkedHashSet<T>... sets) {
        Arrays.asList(sets).forEach(extend::remove);
    }

    public ExtendedLinkedHashSet() {
        base = new LinkedHashSet<>();
    }

    public ExtendedLinkedHashSet(LinkedHashSet<T> base) {
        this.base = base;
    }

    public LinkedHashSet<T> getBase() {
        return base;
    }

    public void setBase(LinkedHashSet<T> base) {
        this.base = base;
    }

    public LinkedHashSet<LinkedHashSet<T>> getExtend() {
        return extend;
    }

    public void setExtend(LinkedHashSet<LinkedHashSet<T>> extend) {
        this.extend = extend;
    }
}
