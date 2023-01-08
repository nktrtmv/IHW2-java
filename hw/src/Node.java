import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static java.nio.charset.Charset.defaultCharset;

/**
 * Вершина графа
 * */
public class Node {
    /**
     * Путь к файлу
     * */
    private final Path path;

    /**
     * Пути к файлам зависимостям
     * */
    private final ArrayList<Path> dependencies;

    /**
     * Конструктор считывает файлы на зависимости
     * */
    public Node(Path path, ArrayList<Path> folderFiles){
        this.path = path;
        dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path, defaultCharset());
            for (String line : lines) {
                if (line.contains("require")) {
                    String require = line.substring(line.indexOf('\'') + 1, line.lastIndexOf('\'')); // Кавычка обычная одинарная
//                    String require = line.substring(line.indexOf('‘') + 1, line.lastIndexOf('’')); // Кавычка из примера
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

    /**
     * Получение пути к файлу
     * */
    public Path getPath() {
        return path;
    }

    /**
     * Получние списка зависимостей
     * */
    public ArrayList<Path> getDependencies() {
        return dependencies;
    }
}
