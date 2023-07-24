import lombok.Value;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Value
public class GrepStreamImpl implements Grep {
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

        listFiles()
                .flatMap(this::readLines)
                .filter(this::containsPattern)
                .reduce(String::concat)
                .ifPresent(this::writeToFile);
    }

    private Stream<File> listFiles() {

        return extractFilesFromRoot(new File(rootPath).listFiles()).stream();
    }

    private Stream<String> readLines(File file) {
        try {
            return Files.readAllLines(Path.of(file.getPath())).stream();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Reading from the file {" + file.getPath() + "} is failed", ex);
        }
    }

    private boolean containsPattern(String line){
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

    private List<File> extractFilesFromRoot(File[] rootDirFiles) {
        List<File> filterFiles = new ArrayList<>();
        for(File f : rootDirFiles){
            if(f.isDirectory()){
                filterFiles.addAll(extractFilesFromRoot(f.listFiles()));
            }
            else {
                filterFiles.add(f);
            }
        }
        return filterFiles;
    }
}
