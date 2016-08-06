package utils;

import exceptions.ModxException;
import files.DirectoryInfo;
import files.FileInfo;
import files.FileManager;
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

import static files.types.Action.Type.*;

public class FileManagerConverter {

    private static final long BYTE = 1L;
    private static final long KILOBYTE = 1024L;
    private static final long MEGABYTE = 1048576L;
    private static final long GIGABYTE = 1073741824L;
    private static final long TERABYTE = 1099511627776L;
    private static final long PETABYTE = 1125899906842624L;

    /**
     *
     * @param document
     * @return
     * @throws ModxException
     */
    public static FileManager getFileManager(Document document) throws ModxException {
        try {
            String path = document.getElementsByAttributeValue("name", "path").first().attr("value");
            List<FileInfo> fileInfos = getFileInfos(document);

            return new FileManager(path, fileInfos);

        } catch (ParseException e) {
            throw new ModxException(e);
        }
    }

    private static List<FileInfo> getFileInfos(Document document) throws ParseException {
        List<FileInfo> fileInfos = new ArrayList<>();

        Elements fileElements = document.getElementsByClass("sectionBody")
                .first().getElementsByTag("tbody").first().getElementsByTag("tr");

        for (int i = 1; i < fileElements.size(); i++) {
            fileInfos.add(fileInfoViaTableRow(fileElements.get(i)));
        }

        return fileInfos;
    }


    private static FileInfo fileInfoViaTableRow(Element trElement) throws ParseException {

        String name;
        String path;
        Date date;
        long sizeBytes;
        List<Action> actions;

        Elements tdElements = trElement.getElementsByTag("td");

        name = name(tdElements.get(0));
        path = path(tdElements.get(0));
        date = date(tdElements.get(1));
        sizeBytes = sizeBytes(tdElements.get(2));
        actions = actions(tdElements.get(3).getElementsByTag("a"), name);


        if(path == null) {
            return new FileInfo.Builder()
                    .modifiedDate(date)
                    .name(name)
                    .actions(actions)
                    .sizeBytes(sizeBytes)
                    .build();
        } else {
            return new DirectoryInfo.Builder()
                    .goInsideLink(path)
                    .modifiedDate(date)
                    .name(name)
                    .actions(actions)
                    .sizeBytes(sizeBytes)
                    .build();
        }
    }

    private static String name(Element element) {
        return element.ownText();
    }

    private static List<Action> actions(Elements actionElements, String name) {
        List<Action> actions = new ArrayList<>();

        for (Element element : actionElements) {
            String href = element.attr("href");

            // FIXME href should be correct
            if(href.startsWith("index.php?a=31&mode=view")) { actions.add(new Action(VIEW, href)); continue; }

            if(href.startsWith("index.php?a=31&mode=edit")) { actions.add(new Action(EDIT, href)); continue; }

            if(href.equals("../" + name)) { actions.add(new Action(DOWNLOAD, href)); continue; }

            if(href.contains("('" + name + "')")) { actions.add(new Action(DELETE, href)); }
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

    private static String path(Element element) {
        Elements aElement = element.getElementsByTag("a");

        if(!aElement.isEmpty()) {
           return aElement.attr("href");
        }

        return null;
    }


}
