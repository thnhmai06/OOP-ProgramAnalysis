package old;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** old.Package/File mã nguồn. */
public final class Package {
    private final List<ClassName> definedClass = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    /**
     * Khởi tạo một old.Package/File code.
     *
     * @param sourceCode Mã nguồn của code
     */
    public Package(String sourceCode) {
        sourceCode = Utilities.machineFormating(sourceCode);
        String[] lines = sourceCode.split("\n");

        // * Load các định nghĩa
        final ClassName currentClassScope = new ClassName();
        Stack<Boolean> inClassCodeBlock = new Stack<>();
        // chỉ có tác dụng cache xem khối mở ({) sắp đi tới qua có quan trọng hay không (theo định
        // nghĩa ở line trước đó)
        boolean isNextClassCodeBlock = false;
        for (String line : lines) {
            // ? Là 1 line định nghĩa (package, import, class)
            if (inClassCodeBlock.empty()) {
                // old.Package
                final String packageNameRaw = Utilities.filterPackageName(line);
                if (packageNameRaw != null) {
                    currentClassScope.setFullName(packageNameRaw);
                    continue;
                }

                // Import
                final String importNameRaw = Utilities.filterImportName(line);
                if (importNameRaw != null) {
                    final ClassName importName = new ClassName(importNameRaw);
                    definedClass.add(importName);
                    continue;
                }
            }
            // SnapshotClass
            final String classNameRaw = Utilities.filterClassName(line);
            if (classNameRaw != null) {
                currentClassScope.addChild(classNameRaw);
                isNextClassCodeBlock = true;

                definedClass.add(new ClassName(currentClassScope));
                continue;
            }

            // Method (cached để load sau)
            if (Utilities.isMethodLine(line)) {
                methods.add(line);
                continue;
            }

            // ? Là 1 line đóng/mở Code block
            // Update độ quan trọng của block hiện tại
            if (line.equals("{")) {
                inClassCodeBlock.add(isNextClassCodeBlock);
                isNextClassCodeBlock = false;
            } else if (line.equals("}")) {
                if (inClassCodeBlock.pop()) {
                    currentClassScope.popChild();
                }
            }
            System.out.println(definedClass.getLast().toString());
        }

        // * Parsing Method
        methods.replaceAll(line -> Utilities.filterMethod(line, definedClass));
    }

    public List<ClassName> getDefinedClass() {
        return definedClass;
    }

    public List<String> getMethods() {
        return methods;
    }
}
