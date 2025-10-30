import java.util.List;

/** Week 10. Welcome to the OASIS World Final1! Champion round - Good luck! */
public class Week10 {
    public static List<String> getAllFunctions(String fileContent) {
        return new Package(fileContent).getMethods();
    }
}
