import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** Week 10. Welcome to the OASIS World Final1! Champion round - Good luck! */
public final class Week10 {
    /**
     * Lấy hết tất cả {@link AMethod} ở bên trong {@link AClass}, kể cả các class con.
     *
     * @param clazz Class muốn lấy
     * @return Tất cả các Method
     */
    private static List<AMethod> getAllMethodsRecursive(AClass clazz) {
        List<AMethod> methods = new LinkedList<>();
        for (Declaration declaration : clazz.getLocalDeclaration()) {
            if (declaration instanceof AMethod) {
                methods.add(((AMethod) declaration));
            } else if (declaration instanceof AClass) {
                methods.addAll(getAllMethodsRecursive((AClass) declaration));
            }
        }
        return methods;
    }

    /**
     *
     *
     * <h2>Lấy hết {@link AMethod} có trong {@link AClass} chính của {@link APackage}.</h2>
     *
     * <p>Yep. Đây là một solution khá là thử thách đối với em. Lúc đầu, em đi thẳng vào POP (hướng
     * thủ tục) luôn như thói quen chơi Lập trình thi đấu, nhưng trong quá trình làm, em nhận ra nó
     * rất khó để maintain và kiểm soát. Vậy nên em đã chuyển sang OOP để linh hoạt hơn, nhờ vậy em
     * thấy việc thiết kế và viết code trở nên thuận tiện hơn hẳn. Qua đây nó cho em một cái nhìn
     * khác về việc thiết kế hệ thống và lập trình ra nó. Em xin cảm ơn thầy/cô đã đưa ra một đề bài
     * về parsing syntax khá là hay và đầy thử thách này. Em mong lần sau em sẽ ko gặp phải bài này
     * nữa, vì huhu nó mất nhiều thời gian quá hic.
     *
     * @param fileContent Nội dung source code
     * @return Các method fullname có trong đây
     */
    public static List<String> getAllFunctions(String fileContent) {
        final APackage aPackage = APackage.from(fileContent);
        final AClass mainClass = aPackage.getMain();
        final List<AMethod> methods = getAllMethodsRecursive(mainClass);

        List<String> res = new ArrayList<>();
        for (AMethod method : methods) {
            res.add(method.getFullName());
        }
        return res;
    }
}
