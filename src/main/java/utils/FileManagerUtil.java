package utils;

import exceptions.ModxException;
import files.*;
import files.types.Action;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static files.types.Action.Type.*;

public class FileManagerUtil {

    private static final long BYTE = 1L;
    private static final long KILOBYTE = 1024L;
    private static final long MEGABYTE = 1048576L;
    private static final long GIGABYTE = 1073741824L;
    private static final long TERABYTE = 1099511627776L;
    private static final long PETABYTE = 1125899906842624L;

    private static String SITE_URL;
    private static String MANAGER_PATH;

    private static String TOP_LEVEL_URL;
    private static String UP_ONE_LEVEL_URL;

    public static FileManager getFileManager(Document document, String siteUrl) throws ModxException {
        try {
            defineStaticFields(document, siteUrl);
            ItemContainer itemContainer = getItemContainer(document);

            return new FileManager.Builder()
                    .itemContainer(itemContainer)
                    .path(MANAGER_PATH)
                    .siteUrl(SITE_URL)
                    .topLevelUrl(TOP_LEVEL_URL)
                    .upOneLevelUrl(UP_ONE_LEVEL_URL)
                    .build();

        } catch (ParseException e) {
            throw new ModxException(e);
        }
    }

    private static void defineManagerUrls(Document document) {
        Elements hrefs = document.getElementsByAttributeValueStarting("href", "index.php?a=31&mode=drill&path=");
        if(!hrefs.isEmpty()) {
            Elements managerUrls = new Elements();
            for (Element href : hrefs) {
                boolean isManagerUrl = true;
                for (Element parent : href.parents()) {
                    if(Objects.equals(parent.tag().getName(), "table")) {
                        isManagerUrl = false;
                        break;
                    }
                }
                if(isManagerUrl) managerUrls.add(href);
            }
            if(managerUrls.size() == 2) {
                TOP_LEVEL_URL = managerUrls.get(0).attr("href");
                UP_ONE_LEVEL_URL = managerUrls.get(1).attr("href");
            }
        }
    }

    private static void defineStaticFields(Document document, String siteUrl) {
        SITE_URL = siteUrl;
        Elements elementsByAttributeValue = document.getElementsByAttributeValue("name", "path");
        MANAGER_PATH = elementsByAttributeValue.first().attr("value");
        defineManagerUrls(document);
    }

    private static ItemContainer getItemContainer(Document document) throws ParseException {
        ItemContainer container = new ItemContainer();

        Elements fileElements = document.getElementsByClass("sectionBody")
                .first().getElementsByTag("tbody").first().getElementsByTag("tr");

        for (int i = 1; i < fileElements.size(); i++) {
            container.add(ItemInfoViaTableRow(fileElements.get(i)));
        }

        return container;
    }


    private static Item ItemInfoViaTableRow(Element trElement) throws ParseException {

        boolean isFile;
        String name;
        Date date;
        long sizeBytes;
        List<Action> actions;
        String downloadPath;

        Elements tdElements = trElement.getElementsByTag("td");

        isFile = isFile(tdElements.get(0));
        name = name(tdElements.get(0));
        date = date(tdElements.get(1));
        sizeBytes = sizeBytes(tdElements.get(2));
        actions = actions(tdElements.get(3), name);
        downloadPath = downloadPath(tdElements.get(3));

        if(isFile) {
            return new FileInfo.Builder()
                    .actions(actions)
                    .downloadPath(downloadPath)
                    .siteUrl(SITE_URL)
                    .path(MANAGER_PATH)
                    .modifiedDate(date)
                    .name(name)
                    .sizeBytes(sizeBytes)
                    .build();
        } else {
            return new FolderInfo.Builder()
                    .folderPath(MANAGER_PATH + name)
                    .siteUrl(SITE_URL)
                    .path(MANAGER_PATH)
                    .modifiedDate(date)
                    .name(name)
                    .sizeBytes(sizeBytes)
                    .build();
        }
    }

    private static String downloadPath(Element element) {
        Elements pathElements = element.getElementsByAttributeValueStarting("href", "../");
        if(!pathElements.isEmpty()) {
            return  pathElements.first().attr("href").substring(2);
        }
        return null;

    }

    private static String name(Element element) {
        return element.ownText();
    }

    private static List<Action> actions(Element element, String name) {
        Elements actionElements = element.getElementsByTag("a");

        List<Action> actions = new ArrayList<>();

        for (Element action : actionElements) {
            String href = action.attr("href");

            if(href.startsWith("index.php?a=31&mode=view")) { actions.add(new Action(VIEW)); continue; }

            if(href.startsWith("index.php?a=31&mode=edit")) { actions.add(new Action(EDIT)); continue; }

            if(href.startsWith("../")) { actions.add(new Action(DOWNLOAD)); }

        }
        return actions;
    }


    private static long sizeBytes(Element element) {
        String sizeInfo = element.text();
        String[] info = sizeInfo.split(" ");
        float value = Float.valueOf(info[0]);
        String dimension = info[1];

        long multiplier = BYTE;

        switch (dimension) {
            case "B":  break;
            case "KB": multiplier = KILOBYTE; break;
            case "MB": multiplier = MEGABYTE; break;
            case "GB": multiplier = GIGABYTE; break;
            case "TB": multiplier = TERABYTE; break;
            case "PB": multiplier = PETABYTE; break;
        }

        return Math.round(value * multiplier);
    }

    private static Date date(Element element) throws ParseException {
        String text = element.text();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy kk:mm:ss");
        return dateFormat.parse(text);
    }

    private static boolean isFile(Element element) {
        Elements aElements = element.getElementsByTag("a");
        return aElements.isEmpty();
    }


}
