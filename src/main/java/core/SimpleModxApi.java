package core;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import exceptions.ModxException;
import files.FileManager;
import org.jsoup.Jsoup;
import utils.FileManagerFactory;
import utils.ResourceInfoFactory;

import java.util.List;

public class SimpleModxApi implements ModxApi {

    private static final String LOGIN_PROCESSOR_URL = "/manager/processors/login.processor.php";
    private static final String INDEX_URL = "/manager/index.php";
    private static final String EXPAND_TREE_URL = "/manager/index.php?a=1&f=nodes&indent=1&parent=0&expandAll=1";
    private static final String FILE_MANAGER_URL = "/manager/index.php?a=31";
    private static final int MOVE_ACTION_VALUE = 52;
    private static final int LOGOUT_VALUE = 8;
    private static final int FOUND_STATUS = 302;
    private static final String REFERER = "Referer";

    private final String siteUrl;

    private boolean isAuthorized = false;

    public SimpleModxApi(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public boolean login(String username, String password) throws ModxException {

        try {
            int status = Unirest.post(siteUrl + LOGIN_PROCESSOR_URL)
                    .queryString("username", String.valueOf(username))
                    .queryString("password", String.valueOf(password))
                    .asString().getStatus();

            return isAuthorized = (status == FOUND_STATUS);

        } catch (Exception e) {
            throw new ModxException(e);
        }
    }

    @Override
    public boolean logout() throws ModxException {

        try {
            if (!isAuthorized) return false;
            int status = Unirest.get(siteUrl + INDEX_URL)
                    .header(REFERER, siteUrl + INDEX_URL)
                    .queryString("a", LOGOUT_VALUE).asString().getStatus();

            return !(isAuthorized = !(status == FOUND_STATUS));
        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }

    @Override
    public boolean moveResource(long id, long newParentId) throws ModxException {

        try {
            return Unirest.post(siteUrl + INDEX_URL)
                    .header(REFERER, siteUrl + INDEX_URL)
                    .queryString("a", MOVE_ACTION_VALUE)
                    .queryString("id", id)
                    .queryString("new_parent", newParentId)
                    .asString().getBody().isEmpty();

        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }

    @Override
    public List<ResourceInfo> getResourcesInfo() throws ModxException {

        try {
            String body = Unirest.get(siteUrl + EXPAND_TREE_URL)
                    .header(REFERER, siteUrl + INDEX_URL).asString().getBody();
            return ResourceInfoFactory.getResources(Jsoup.parse(body));

        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }

    @Override
    public FileManager getFileManager() throws ModxException {

        try {
            String body = Unirest.get(siteUrl + FILE_MANAGER_URL)
                    .header(REFERER, siteUrl + FILE_MANAGER_URL)
                    .asString().getBody();
            return FileManagerFactory.getFileManager(Jsoup.parse(body), siteUrl);

        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }
}
