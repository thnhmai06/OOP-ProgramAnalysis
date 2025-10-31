import java.util.ArrayList;
import java.util.List;

/** Week 10. Welcome to the OASIS World Final1! Champion round - Good luck! */
public class Week10 {
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
        final APackage aPackage = new APackage(fileContent);
        final AClass mainClass = aPackage.getMain();
        final List<AMethod> methods = AMethod.filter(mainClass.getLocalDeclared());
        List<String> res = new ArrayList<>();

        for (AMethod method : methods) {
            res.add(method.getFullName());
        }
        return res;
    }
}
