package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcherUtility implements RegexPractise {
    private final static Pattern JPEG_PATTERN = Pattern.compile("[a-zA-Z0-9._ -]+[.](jpg|jpeg)$");
    private final static Pattern IP_PATTERN = Pattern.compile("([0-9]{1,3})([.][0-9]{1,3}){3}");
    private final static Pattern EMPTY_LINE_PATTERN = Pattern.compile("^\\s*$");

    @Override
    public boolean matchJPEG(String filename) {
        return match(JPEG_PATTERN, filename);
    }
    @Override
    public boolean matchIP(String ip) {
        return match(IP_PATTERN, ip);
    }
    @Override
    public boolean isEmptyLine(String line) {
        return match(EMPTY_LINE_PATTERN, line);
    }

    private boolean match(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }
}