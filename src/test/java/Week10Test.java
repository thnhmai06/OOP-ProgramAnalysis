import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Week10Test {
    private String readResource(String path) throws Exception {
        URL url = getClass().getClassLoader().getResource(path);
        if (url == null) throw new FileNotFoundException("File not found: " + path);
        File f = new File(url.toURI());
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
        in.close();
        return new String(out.toByteArray(), StandardCharsets.UTF_8).replace("\r", "").trim();
    }

    private String readFile(String resourcePath) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(resourcePath);
        if (in == null) throw new FileNotFoundException("File not found: " + resourcePath);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
        in.close();
        return new String(out.toByteArray(), StandardCharsets.UTF_8).replace("\r", "").trim();
    }

    private void runCase(String caseName) throws Exception {
        String inputPath = readResource("cases/" + caseName + "/input.txt");
        String inputData = readFile(inputPath);
        String expected = readResource("cases/" + caseName + "/excepted.txt");
        List<String> result = Week10.getAllFunctions(inputData);
        String actual = result.toString().replace("\r", "").trim();
        assertEquals(expected, actual, caseName);
    }

    @Test
    void test_DatabaseUtils() throws Exception {
        runCase("test_DatabaseUtils");
    }

    @Test
    void test_DateUtils() throws Exception {
        runCase("test_DateUtils");
    }

    @Test
    void test_OSInfo() throws Exception {
        runCase("test_OSInfo");
    }

    @Test
    void test_PathUtils() throws Exception {
        runCase("test_PathUtils");
    }

    @Test
    void test_RandomArrayUtils() throws Exception {
        runCase("test_RandomArrayUtils");
    }

    @Test
    void test_RandomDateUtils() throws Exception {
        runCase("test_RandomDateUtils");
    }

    @Test
    void test_RandomNumberUtils() throws Exception {
        runCase("test_RandomNumberUtils");
    }

    @Test
    void test_VolumeInfo() throws Exception {
        runCase("test_VolumeInfo");
    }
}
