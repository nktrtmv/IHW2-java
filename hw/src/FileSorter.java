import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileSorter {
    private String folder;
    private String result;
    private ArrayList<Path> folderFiles;

    public FileSorter(String folder, String result) throws IOException {
        this.folder = folder;
        this.result = result;
        getFolderFiles();
    }

    private void getFolderFiles() throws IOException {
        folderFiles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(folder))) {
            walk.filter(Files::isRegularFile).sorted().forEach(folderFiles::add);
        }
    }

    public boolean sort() {
        try {
            Graph graph = new Graph(folderFiles);
            graph.sort();
            folderFiles = graph.folderFiles;
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public void mergeFiles() {
        Path resultPath = Paths.get(result);
        folderFiles.stream()
                .flatMap(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(line -> {
                    try {
                        Files.writeString(resultPath, line + System.lineSeparator());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
