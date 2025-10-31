import java.util.regex.Pattern;

public final class Patterns {
    public static final Pattern PACKAGE =
            java.util.regex.Pattern.compile("package ([\\w.]+);"); // [package]
    public static final Pattern IMPORT =
            Pattern.compile("import(?: static)? ([\\w.]+)\\.(\\w+);"); // [package, class]
    public static final Pattern CLASS =
            Pattern.compile("public (?:\\w+\\s+)*(?:class|interface) (\\w+)"); // [class]

    public static final Pattern METHOD =
            Pattern.compile("public static [\\s\\S]*? (\\w+)\\(([\\s\\S]*?)\\)"); // [name, params]
    public static final Pattern METHOD_PARAMETER = // [classes, name]
            Pattern.compile("(?:final\\s+)?([\\w.]+(?:<[\\s\\S]*?>)?)(?:...|\\[])? (\\w+)");
    public static final Pattern METHOD_PARAMETER_TYPE =
            Pattern.compile("\\b([\\w.]+)\\b"); // [class] x1

    private Patterns() {}
}
