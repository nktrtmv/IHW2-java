import java.nio.file.Path;
import java.util.ArrayList;

public class FileGraph {
    private ArrayList<Node> nodes;
    private ArrayList<Path> files;
    private int[][] adjacencyMatrix;

    public boolean sort() {
        if (isValid()){
            bfsSort();
            return true;
        }
        return false;
    }

    private void bfsSort(){
        // топологическая сортировка графа
    }

    public FileGraph(ArrayList<Path> folderFiles) {
        this.files = folderFiles;
        nodes = new ArrayList<>();
        for (Path file : files) {
            nodes.add(new Node(file, files));
        }
        adjacencyMatrix = new int[nodes.size()][nodes.size()];
        for (Node node: nodes) {
            for (Path dependency: node.getDependencies()) {
                adjacencyMatrix[files.indexOf(dependency)][files.indexOf(node.getPath())] = 1;
            }
        }
    }

    private boolean isValid(){
        for (int i = 0; i < adjacencyMatrix.length; ++i) {
            for (int j = 0; j < adjacencyMatrix[i].length; ++j) {
                if (i != j && adjacencyMatrix[i][j] == adjacencyMatrix[j][i] && adjacencyMatrix[i][j] != 0) {
                    System.out.println("Обнаружены файлы с ссылками друг на друга.");
                    return false;
                }
            }
        }

        // проверка на циклы

        return true;
    }

    public ArrayList<Path> getFiles() {
        return files;
    }
}