import java.nio.file.Path;
import java.util.*;

public class FileGraph {
    private ArrayList<Path> files;
    private final HashMap<Path, ArrayList<Node>> adjacencyList;
    private final ArrayList<Path> result;

    public boolean sort() {
        if (!hasLoop()) {
            sortFiles();
            return true;
        }
        return false;
    }

    private void sortFiles() {
        HashSet<Path> visited = new HashSet<>();
        for (Path path : adjacencyList.keySet()) {
            if (!visited.contains(path)) {
                visited.addAll(bfs(path));
            }
        }
        files = result;
    }

    private HashSet<Path> bfs(Path path) {
        HashSet<Path> visited = new HashSet<>();
        ArrayDeque<Path> queue = new ArrayDeque<>();
        queue.addLast(path);
        while (!queue.isEmpty()){
            result.add(queue.getFirst());
            visited.add(queue.getFirst());
            for (Node node : adjacencyList.get(queue.pop())){
                queue.addLast(node.getPath());
            }
        }
        return visited;
    }

    public FileGraph(ArrayList<Path> folderFiles) {
        this.files = folderFiles;
        result = new ArrayList<>();
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

    private boolean hasLoop() {
        boolean flag = false;
        for (Path dependency : adjacencyList.keySet()) {
            for (Node node : adjacencyList.get(dependency)) {
                flag = flag || dfs(dependency, node.getPath());
            }
        }

        return flag;
    }

    private boolean dfs(Path start, Path current) {
        if (current == start) {
            return true;
        }
        boolean flag = false;
        for (Node node : adjacencyList.get(current)) {
            flag = flag || dfs(start, node.getPath());
        }

        return flag;
    }

    public ArrayList<Path> getFiles() {
        return files;
    }
}