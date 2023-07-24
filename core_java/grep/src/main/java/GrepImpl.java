import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
public class GrepImpl implements Grep {
    static final Logger logger = LoggerFactory.getLogger(GrepApplication.class);
    String regex;
    String rootPath;
    String outPath;

    @Override
    public void process() {

        if(Files.notExists(Path.of(rootPath))){
            throw new IllegalArgumentException("Provided root path {" + rootPath + "} is wrong");
        }

        if(Files.notExists(Path.of(outPath))){
            throw new IllegalArgumentException("Provided output path {" + outPath + "} is wrong");
        }

        List<File> files = listFiles(rootPath);
        logger.info("Directory " + rootPath + " contains " + files.size() + " files");

        StringBuilder builder = new StringBuilder();
        for (File file : files){
            List<String> lines = readLines(file);
            for(String line : lines){
                if(isContainsPattern(line)){
                    builder.append(line).append("\n");
                }
            }
        }

        String mathcedLines = builder.toString();
        logger.info("Matched lines: \n" + mathcedLines);
        writeToFile(mathcedLines);
    }

    private List<File> listFiles(String path) {
        File[] rootDirFiles = new File(path).listFiles();

        List<File> filterFiles = new ArrayList<>();
        for(File f : rootDirFiles){
            if(f.isDirectory()){
                filterFiles.addAll(listFiles(f.getPath()));
            }
            else {
                filterFiles.add(f);
            }
        }

        return filterFiles;
    }

    private List<String> readLines(File file) {
        try {
            return Files.readAllLines(Path.of(file.getPath()));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Reading from the file {" + file.getPath() + "} is failed", ex);
        }
    }

    private boolean isContainsPattern(String line){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private void writeToFile(String lines) {
        try {
            Files.write(Path.of(outPath), lines.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new RuntimeException("Writing to the file {" + outPath + "} is filed", ex);
        }
    }
}
