package files;

import java.util.ArrayList;
import java.util.List;

public class ItemContainer {

    private List<FileInfo> fileInfos = new ArrayList<>();
    private List<FolderInfo> folderInfos = new ArrayList<>();

    public void add(Item item) {
        Class<? extends Item> clazz = item.getClass();
        if(clazz == FileInfo.class) fileInfos.add((FileInfo) item);
        else folderInfos.add((FolderInfo) item);
    }

    public List<FileInfo> getFilesInfo() {
        return fileInfos;
    }

    public List<FolderInfo> getFoldersInfo() {
        return folderInfos;
    }
}
