import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class GrepTest {
    @Test
    public void shouldFindLine() throws IOException {
        Grep grep = initializeGrep("hello", "src/test/resources", "src/test/resources/out_file_dir/output.txt");
        grep.process();
        assertTrue(Files.readAllLines(Path.of("src/test/resources/out_file_dir/output.txt")).contains("hello"));
    }

    @Test
    public void shouldThrownExceptionWithIncorrectRootPath() {
        Grep grep = initializeGrep("hello", "incorrectPath", "");
        assertThrows(IllegalArgumentException.class, grep::process);
    }

    @Test
    public void shouldThrownExceptionWithIncorrectOutPath() {
        Grep grep = initializeGrep("hello", "src/test/resources", "incorrectPath");
        assertThrows(IllegalArgumentException.class, grep::process);
    }

    protected Grep initializeGrep(String regex, String rootPath, String outPath) {
        return new GrepImpl(regex, rootPath, outPath);
    }
}

