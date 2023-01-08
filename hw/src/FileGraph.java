import java.nio.file.Path;
import java.util.ArrayList;

public class FileGraph {
    private ArrayList<Node> nodes;
    private ArrayList<Path> files;
    private int[][] adjacencyMatrix;

    public void sort(){

    }

    public FileGraph(ArrayList<Path> folderFiles){
        this.files = folderFiles;
    }

    public ArrayList<Path> getFiles(){
        return  files;
    }
}