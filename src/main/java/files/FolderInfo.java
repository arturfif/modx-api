package files;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import exceptions.ModxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.FileManagerUtil;


public class FolderInfo extends Item {

    private final static int ACTION_VALUE = 31;
    private final static String FOLDER_DELETED_TEXT = "Folder already deleted.";
    private final String folderPath;

    private FolderInfo(Builder builder) {
        super(builder);
        this.folderPath = builder.folderPath;
    }

    public FileManager open() throws ModxException {
        try {
            if (isDeleted) {
                throw new ModxException(FOLDER_DELETED_TEXT);
            }
            String body = Unirest.get(indexUrl)
                    .queryString("a", ACTION_VALUE)
                    .queryString("mode", "drill")
                    .queryString("path", folderPath)
                    .asString().getBody();

            Document document = Jsoup.parse(body);
            return FileManagerUtil.getFileManager(document, siteUrl);
        } catch (UnirestException | ModxException e) {
            throw new ModxException(e);
        }
    }

    public void delete() throws ModxException {
        if (isDeleted) throw new ModxException("Folder already deleted.");
        Unirest.get(indexUrl)
                .queryString("a", ACTION_VALUE)
                .queryString("mode", "deletefolder")
                .queryString("path", path)
                .queryString("folderpath", folderPath);
        isDeleted = true;
    }


    public static class Builder extends Item.Builder {

        private String folderPath;

        public Builder folderPath(String folderPath) {
            this.folderPath = folderPath;
            return this;
        }

        public FolderInfo build() {
            return new FolderInfo(this);
        }

    }
}
