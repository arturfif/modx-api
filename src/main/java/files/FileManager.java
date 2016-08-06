package files;

import java.util.List;

public class FileManager {

    private final String path;

    private final List<FileInfo> fileInfos;

    public FileManager(String path, List<FileInfo> fileInfos) {
        this.path = path;
        this.fileInfos = fileInfos;
    }

    public boolean newDirectory(String directoryName) {
        return false;
    }

    public boolean toTopLevel() {
        return false;
    }

    public boolean upOneLevel() {
        return false;
    }
}
