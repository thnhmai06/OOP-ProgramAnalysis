import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Week 10. Welcome to the OASIS World Final1! Champion round - Good luck! */
public class Week10 {
    public static List<String> getAllFunctions(String fileContent) {
        final Package aPackage = new Package(fileContent);
        final List<Method> methods = Method.filter(aPackage.getLocalDeclared());
        List<String> res = new ArrayList<>();

        for (Method method : methods) {
            res.add(method.getFullName());
        }
        return res;
    }

    public static void main(String[] args) {
        File myObj =
                new File(
                        "D:\\Development\\Code\\java\\ProgramAnalysis\\src\\main\\resources\\test.java");
        StringBuilder builder = new StringBuilder();

        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                builder.append(data).append('\n');
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String content = builder.toString();
        //        System.out.println(content);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("result.java"));
            for (String line : getAllFunctions(content)) {
                writer.write(line);
                writer.write("\n");
            }
             writer.write(Utilities.machineFormating(Utilities.removeStringAndComments(content)));
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
