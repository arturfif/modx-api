package files;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import exceptions.ModxException;
import files.types.Action;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static files.types.Action.Type.*;

public class FileInfo extends Item {

    private static final String ACTION_UNAVAILABLE_TEXT = "Action unavailable for this file.";

    private final List<Action> actions;
    private final String downloadPath;

    private final boolean isViewable;
    private final boolean isEditable;
    private final boolean isDownloadable;

    private FileInfo(Builder builder) {
        super(builder);
        this.downloadPath = builder.downloadPath;
        this.actions = builder.actions;
        this.isViewable = builder.isViewable;
        this.isEditable = builder.isEditable;
        this.isDownloadable = builder.isDownloadable;
    }

    public List<Action> getActions() {
        return actions;
    }

    public boolean isViewable() {
        return isViewable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String view() throws ModxException {
        if (!isViewable) {
            throw new ModxException(ACTION_UNAVAILABLE_TEXT);
        }
        try {
            String body = Unirest.get(indexUrl)
                    .header("Referer", indexUrl)
                    .queryString("a", 31)
                    .queryString("mode", "view")
                    .queryString("path", path + '/' + name)
                    .asString().getBody();

            Document document = Jsoup.parse(body);
            return document.getElementsByTag("textarea").first().text();
        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }

    public void edit() throws ModxException {
        if (!isEditable) {
            throw new ModxException(ACTION_UNAVAILABLE_TEXT);
        }
    }

    public File download(String folderPath) throws ModxException {
        if (!isDownloadable) {
            throw new ModxException(ACTION_UNAVAILABLE_TEXT);
        }
        try {
            InputStream inputStream = Unirest.get(siteUrl + downloadPath).asBinary().getRawBody();

            File file = new File(folderPath + '/' + name);
            file.createNewFile();

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            OutputStream outStream = new FileOutputStream(file);
            outStream.write(buffer);
            return file;

        } catch (IOException | UnirestException e) {
            throw new ModxException(e);
        }
    }

    public boolean delete() throws ModxException {
        if (isDeleted) return false;
        Unirest.get(indexUrl)
                .header("Referer", indexUrl)
                .queryString("a", 31)
                .queryString("mode", "delete")
                .queryString("path", path);
        return isDeleted = true;
    }

    public static class Builder extends Item.Builder {
        private List<Action> actions;
        private String downloadPath;
        private boolean isViewable;
        private boolean isEditable;
        private boolean isDownloadable;

        public Builder actions(List<Action> actions) {
            this.actions = actions;
            setActionFlags(actions);
            return this;
        }

        public Builder downloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
            return this;
        }

        private void setActionFlags(List<Action> actions) {
            List<Action.Type> actionTypes = new ArrayList<>();
            actions.stream().forEach(action -> actionTypes.add(action.getType()));

            isViewable = actionTypes.contains(VIEW);
            isEditable = actionTypes.contains(EDIT);
            isDownloadable = actionTypes.contains(DOWNLOAD);
        }

        public FileInfo build() {
            return new FileInfo(this);
        }
    }
}
