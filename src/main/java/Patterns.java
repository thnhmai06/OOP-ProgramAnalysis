import java.util.LinkedHashSet;
import java.util.regex.Pattern;

public final class Patterns {
    // ! PACKAGE ở dạng Code block, không phải dạng thường
    // [package]
    public static final Pattern PACKAGE = Pattern.compile("package ([\\s\\S]*?)");
    // [package, class]
    public static final Pattern IMPORT = Pattern.compile("import(?: static)? ([\\w.]+)\\.(\\w+);");
    // [access, class, generics]
    public static final Pattern CLASS =
            Pattern.compile(
                    "(public|protected|private)? (?:\\w+\\s+)*"
                            + "(?:class|interface|enum) (\\w+)"
                            + "(?:<([\\s\\S]*)>)?"
                            + "(?:(?: extends|implements)[\\s\\S]*)?");
    // [generics, name, params]
    public static final Pattern METHOD =
            Pattern.compile(
                    "static (?:<([\\s\\S]*?)>)?" + "[^=]*? (\\w+)\\(([\\s\\S]*?)\\)[\\s\\S]*?");
    // [class] x1
    public static final Pattern METHOD_PARAMETER_TYPE = Pattern.compile("((?:[\\w.]+)?[A-Z]\\w*)");

    /**
     * Lấy tên của tất cả các Generic trên signature.
     *
     * @param generics Generics ở signature
     * @return Tên tất cả các generic có mặt
     */
    public static LinkedHashSet<String> getAllGenericName(String generics) {
        LinkedHashSet<String> res = new LinkedHashSet<>();
        if (generics == null || generics.isEmpty()) {
            return res;
        }
        StringBuilder current = new StringBuilder();
        boolean write = true;
        int balance = 0;
        for (char c : generics.toCharArray()) {
            if (c == '<') {
                ++balance;
                continue;
            } else if (c == '>') {
                --balance;
                continue;
            }

            if (balance == 0 && c == ',') {
                res.add(current.toString());
                current.setLength(0);
                write = true;
                continue;
            }

            if (write) {
                if (c == ' ') {
                    write = false;
                    continue;
                }

                current.append(c);
            }
        }
        if (current.length() > 0) {
            res.add(current.toString());
        }
        return res;
    }

    private Patterns() {}
}
