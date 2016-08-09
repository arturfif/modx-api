package files;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import exceptions.ModxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.FileManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileManager {

    private static final int ACTION_VALUE = 31;
    private final String siteUrl;
    private final String indexUrl;
    private final String path;
    private final ItemContainer itemContainer;
    private final String topLevelUrl;
    private final String upOneLevelUrl;


    private FileManager(Builder builder) {
        this.siteUrl = builder.siteUrl;
        this.indexUrl = siteUrl + "/manager/index.php";
        this.path = builder.path;
        this.itemContainer = builder.itemContainer;
        this.topLevelUrl = builder.topLevelUrl;
        this.upOneLevelUrl = builder.upOneLevelUrl;
    }

    private FileManager getFileManager(String path) throws ModxException {
        try {
            String body = Unirest.get(indexUrl)
                    .queryString("a", ACTION_VALUE)
                    .queryString("mode", "drill")
                    .queryString("path", path)
                    .asString().getBody();

            Document document = Jsoup.parse(body);
            return FileManagerFactory.getFileManager(document, siteUrl);
        } catch (UnirestException | ModxException e) {
            throw new ModxException(e);
        }
    }

    public void createFolder(String folderName) {
        Unirest.get(indexUrl)
                .queryString("a", ACTION_VALUE)
                .queryString("mode", "newfolder")
                .queryString("path", path)
                .queryString("name", folderName);
    }

    public FileManager toTopLevel() throws ModxException {
        return topLevelUrl == null ? this : getFileManager(topLevelUrl);
    }

    public FileManager upOneLevel() throws ModxException {
        return upOneLevelUrl == null ? this : getFileManager(upOneLevelUrl);
    }

    public void uploadFile(File file) throws ModxException {
        try {
            Unirest.post(indexUrl)
                    .header("Referer", indexUrl)
                    .queryString("a", ACTION_VALUE)
                    .field("path", path)
                    .field("userfile[0]", file, Files.probeContentType(Paths.get(file.toURI())))
                    .asString().getBody();
        } catch (UnirestException | IOException e) {
            throw new ModxException(e);
        }
    }

    public List<FileInfo> getFilesInfo() {
        return Collections.unmodifiableList(itemContainer.getFilesInfo());
    }

    public List<FolderInfo> getFoldersInfo() {
        return Collections.unmodifiableList(itemContainer.getFoldersInfo());
    }

    public static class Builder {
        private String siteUrl;
        private String path;
        private ItemContainer itemContainer;
        private String topLevelUrl;
        private String upOneLevelUrl;

        public Builder siteUrl(String siteUrl) {
            this.siteUrl = siteUrl;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder itemContainer(ItemContainer itemContainer) {
            this.itemContainer = itemContainer;
            return this;
        }

        public Builder topLevelUrl(String topLevelUrl) {
            this.topLevelUrl = topLevelUrl;
            return this;
        }

        public Builder upOneLevelUrl(String upOneLevelUrl) {
            this.upOneLevelUrl = upOneLevelUrl;
            return this;
        }

        public FileManager build() {
            return new FileManager(this);
        }
    }
}
