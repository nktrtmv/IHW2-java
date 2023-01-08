import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Запуск сортировки через граф и слияние файлов
 * */
public class FileSorter {
    /**
     * Папка тестов
     * */
    private final String folder;

    /**
     * Путь к файлу вывода
     * */
    private final String result;

    /**
     * Тестовые файлы
     * */
    private ArrayList<Path> folderFiles;

    /**
     * Конструктор для инициализации полей и обновление списка файлов тестовой директории
     * */
    public FileSorter(String folder, String result) throws IOException {
        this.folder = folder;
        this.result = result;
        getFolderFiles();
    }

    /**
     * Обновление списка файлов тестовой директории
     * */
    private void getFolderFiles() throws IOException {
        folderFiles = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(folder))) {
            walk.filter(Files::isRegularFile).filter(p -> !String.valueOf(p).contains(".DS_Store")).sorted().forEach(folderFiles::add);
        }
    }

    /**
     * Запуск сортировки через граф
     * */
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

    /**
     * Слияние файлов в одие и вывод
     * */
    public void mergeFiles() {
        Path resultPath = Paths.get(result);
        try {
            Files.writeString(resultPath, "");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
                        Files.writeString(resultPath, line + System.lineSeparator() + System.lineSeparator(), StandardOpenOption.APPEND);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }
}
