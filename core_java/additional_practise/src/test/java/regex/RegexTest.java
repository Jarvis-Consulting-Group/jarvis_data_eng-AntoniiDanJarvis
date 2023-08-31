package regex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegexTest {
    PatternMatcherUtility patternMatcherUtility;

    @BeforeEach
    public void initializePatternMatcher(){
        patternMatcherUtility = new PatternMatcherUtility();
    }

    @Test
    public void shouldMatchJpeg() {
        String correctJPEG = "file.jpeg";
        assertTrue(patternMatcherUtility.matchJPEG(correctJPEG));
    }

    @Test
    public void shouldNotMatchJpeg() {
        String wrongJPEG = "file.jp";
        assertFalse(patternMatcherUtility.matchJPEG(wrongJPEG));

    }

    @Test
    public void shouldMatchIP() {
        String correctIP = "192.33.7.62";
        assertTrue(patternMatcherUtility.matchIP(correctIP));
    }

    @Test
    public void shouldNotMatchIP() {
        String wrongJpeg = "192.33.7";
        assertFalse(patternMatcherUtility.matchIP(wrongJpeg));
    }

    @Test
    public void shouldMatchEmptyLine() {
        String correctEmptyLine = "";
        assertTrue(patternMatcherUtility.isEmptyLine(correctEmptyLine));
    }

    @Test
    public void shouldNotMatchEmptyLine() {
        String wrongEmptyLine = "_";
        assertFalse(patternMatcherUtility.isEmptyLine(wrongEmptyLine));
    }
}

