import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;

public class Node {
    private final Path path;
    private final ArrayList<Path> dependencies;

    public Node(Path path, ArrayList<Path> folderFiles){
        this.path = path;
        dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path, defaultCharset());
            for (String line : lines) {
                if (line.contains("require")) {
                    String require = line.substring(line.indexOf('‘') + 1, line.lastIndexOf('’'));
                    for (Path filePath : folderFiles) {
                        if (require.contains(String.valueOf(filePath.getFileName()))) {
                            dependencies.add(filePath);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Пороизошла ошибка при чтении файла для нахождения зависимостей.");
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            System.out.println("Пороизошла ошибка при работе с файлом из каталога!");
            throw new RuntimeException(ex);
        }
    }

    public Path getPath() {
        return path;
    }

    public ArrayList<Path> getDependencies() {
        return dependencies;
    }
}
