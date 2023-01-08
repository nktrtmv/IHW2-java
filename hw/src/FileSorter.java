import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileSorter {
    private final String folder;
    private final String result;
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
            FileGraph fileGraph = new FileGraph(folderFiles);
            if (fileGraph.sort()) {
                folderFiles = fileGraph.getFiles();
                return true;
            }
        } catch (RuntimeException ex) {
            System.out.println("Ошибка при создании графа и его сортировке.");
        }
        return false;
    }

    public void mergeFiles() {
        Path resultPath = Paths.get(result);
        folderFiles.stream()
                .flatMap(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .forEach(line -> {
                    try {
                        Files.writeString(resultPath, line + System.lineSeparator());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }
}
