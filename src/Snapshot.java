/**
 *
 *
 * <h1>{@link Snapshot}</h1>
 *
 * Một {@link Declaration} tham chiếu đến {@link Definition} và cố định, không bao gồm các thành
 * phần con nếu có.
 *
 * @see Definition
 */
public abstract non-sealed class Snapshot extends Declaration {
    protected String fullName;

    abstract void read(String definition);

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getSimpleName() {
        if (getFullName().isEmpty() || !getFullName().contains(".")) {
            return getFullName();
        }
        return getFullName().substring(getFullName().lastIndexOf(".") + 1);
    }
}
