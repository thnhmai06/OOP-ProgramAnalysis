import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Package/File mã nguồn. */
public final class Package {
    private final ClassName packageName = new ClassName();
    private final List<ClassName> definedClass = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    /**
     * Khởi tạo một Package/File code.
     *
     * @param lines các dòng trong file
     */
    public Package(List<String> lines) {
        int scopeLevel = -1;
        for (String line : lines) {
            if (scopeLevel < 0) {
                // Package
                final String packageRawName = Utilities.filterPackageName(line);
                if (packageRawName != null) {
                    packageName.setFullName(packageRawName);
                    continue;
                }

                // Import
                final String importRawName = Utilities.filterImportName(line);
                if (importRawName != null) {
                    final ClassName importName = new ClassName(importRawName);
                    definedClass.add(importName);
                    continue;
                }
            } else {
                // Class
                final String classRawName = Utilities.filterClassName(line);
                if (classRawName != null) {
                    final ClassName className =
                            new ClassName(packageName.getFullName(), classRawName);
                    definedClass.add(className);
                }
            }
            // Update Scope
            scopeLevel += Utilities.countChar(line, '{');
            scopeLevel -= Utilities.countChar(line, '}');
            // Method
            if (scopeLevel == 1) {
                final String methodRaw = Utilities.filterMethod(line, definedClass);
                if (methodRaw != null) {
                    methods.add(methodRaw);
                }
            }
        }
    }

    public ClassName getPackageName() {
        return packageName;
    }

    public List<ClassName> getDefinedClass() {
        return definedClass;
    }

    public List<String> getMethods() {
        return methods;
    }

    public Package(String fileContent) {
        this(Arrays.asList(fileContent.split("\n")));
    }
}
