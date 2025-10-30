/** Tên đầy đủ của một class. */
public final class ClassName {
    private String fullName;

    /**
     * Chỉ lấy tên của lớp.
     *
     * @return Tên đơn giản
     */
    public String getSimpleName() {
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * Một class con trong class này.
     *
     * @param childName Tên class con.
     * @return Tên đầy đủ class con
     */
    public ClassName makeChild(String childName) {
        return new ClassName(this.toString(), childName);
    }

    public void setFullName(String... components) {
        fullName = String.join(".", components);
    }

    public ClassName(String... components) {
        setFullName(components);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
