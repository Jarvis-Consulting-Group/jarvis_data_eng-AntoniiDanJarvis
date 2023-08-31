public class GrepStreamTest extends GrepTest {
    @Override
    protected Grep initializeGrep(String regex, String rootPath, String outPath) {
        return new GrepStreamImpl(regex, rootPath, outPath);
    }
}

