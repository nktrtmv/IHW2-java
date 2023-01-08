import java.nio.file.Path;
import java.util.*;

/**
 * Граф файлов
 * */
public class FileGraph {
    /**
     * Список всех файлов
     * */
    private ArrayList<Path> files;
    /**
     * Словарь - список смежности
     * */
    private final HashMap<Path, ArrayList<Node>> adjacencyList;

    /**
     * Топологическая сортировка файлов в графе и выявление циклов.
     * */
    public boolean sort() {
        HashMap<Path, Integer> inDegree = new HashMap<>();
        for (Path path : adjacencyList.keySet()) {
            inDegree.put(path, (new Node(path, files).getDependencies()).size());
        }

        Queue<Path> queue = new LinkedList<>();
        for (Path path : inDegree.keySet()) {
            if (inDegree.get(path) == 0) {
                queue.add(path);
            }
        }

        int countOfVisited = 0;

        ArrayList<Path> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Path path = queue.poll();
            result.add(path);

            for (Node node : adjacencyList.get(path)) {
                inDegree.put(node.getPath(),inDegree.get(node.getPath()) - 1);
                if (inDegree.get(node.getPath()) == 0){
                    queue.add(node.getPath());
                }
            }
            countOfVisited++;
        }

        files = result;
        if (countOfVisited != adjacencyList.size()) {
            System.out.println("Обнаружен цикл.");
            return false;
        }

        return true;
    }

    /**
     * Конструктор создающий список смежности по списку файлов
     * */
    public FileGraph(ArrayList<Path> folderFiles) {
        this.files = folderFiles;
        ArrayList<Node> nodes = new ArrayList<>();
        for (Path file : files) {
            nodes.add(new Node(file, files));
        }
        adjacencyList = new HashMap<>();
        for (Node node : nodes) {
            adjacencyList.put(node.getPath(), new ArrayList<>());
        }
        for (Node node : nodes) {
            for (Path dependency : node.getDependencies()) {
                adjacencyList.get(dependency).add(node);
            }
        }
    }

    /**
     * Получение списка файлов
     * */
    public ArrayList<Path> getFiles() {
        return files;
    }
}