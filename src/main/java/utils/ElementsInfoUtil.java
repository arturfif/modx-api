package utils;

import elements.ChunkInfo;
import elements.ElementInfo;
import elements.PluginInfo;
import elements.SnippetInfo;
import elements.templates.TemplateInfo;
import elements.VariableInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElementsInfoUtil {

    private static final String TEMPLATE_TAB_NAME = "tabTemplates";
    private static final String VARIABLE_TAB_NAME = "tabVariables";
    private static final String CHUNK_TAB_NAME = "tabChunks";
    private static final String SNIPPET_TAB_NAME = "tabSnippets";
    private static final String PLUGINS_TAB_NAME = "tabPlugins";

    private final List<TemplateInfo> templatesInfo;
    private final List<VariableInfo> variablesInfo;
    private final List<ChunkInfo> chunksInfo;
    private final List<SnippetInfo> snippetsInfo;
    private final List<PluginInfo> pluginsInfo;


    public ElementsInfoUtil(Document document, String siteUrl) {
        templatesInfo = getTemplatesInfo(document, siteUrl);
        variablesInfo = getVariablesInfo(document, siteUrl);
        chunksInfo = getChunksInfo(document, siteUrl);
        snippetsInfo = getSnippetsInfo(document, siteUrl);
        pluginsInfo = getPluginsInfo(document, siteUrl);
    }

    public static List<TemplateInfo> getTemplatesInfo(Document document, String siteUrl) {
        List<ElementInfo> elementsInfo = getElementsInfoByTabName(document, TEMPLATE_TAB_NAME, siteUrl);
        return elementsInfo.stream().map(elementInfo -> (TemplateInfo) elementInfo).collect(Collectors.toList());
    }

    public static List<VariableInfo> getVariablesInfo(Document document, String siteUrl) {
        List<ElementInfo> elementsInfo = getElementsInfoByTabName(document, VARIABLE_TAB_NAME, siteUrl);
        return elementsInfo.stream().map(elementInfo -> (VariableInfo) elementInfo).collect(Collectors.toList());
    }


    public static List<ChunkInfo> getChunksInfo(Document document, String siteUrl) {
        List<ElementInfo> elementsInfo = getElementsInfoByTabName(document, CHUNK_TAB_NAME, siteUrl);
        return elementsInfo.stream().map(elementInfo -> (ChunkInfo) elementInfo).collect(Collectors.toList());
    }

    public static List<SnippetInfo> getSnippetsInfo(Document document, String siteUrl) {
        List<ElementInfo> elementsInfo = getElementsInfoByTabName(document, SNIPPET_TAB_NAME, siteUrl);
        return elementsInfo.stream().map(elementInfo -> (SnippetInfo) elementInfo).collect(Collectors.toList());
    }

    public static List<PluginInfo> getPluginsInfo(Document document, String siteUrl) {
        List<ElementInfo> elementsInfo = getElementsInfoByTabName(document, PLUGINS_TAB_NAME, siteUrl);
        return elementsInfo.stream().map(elementInfo -> (PluginInfo) elementInfo).collect(Collectors.toList());
    }

    private static List<ElementInfo> getElementsInfoByTabName(Document document, String tabName, String siteUrl) {
        Element element = document.getElementById(tabName).getElementsByTag("ul").get(2);
        return getElementsInfo(element, siteUrl);
    }

    private static List<ElementInfo> getElementsInfo(Element element, String siteUrl) {
        List<ElementInfo> elementsInfo = new ArrayList<>();
        Elements categoryElements = element.getElementsByTag("li");
        for (Element categoryElement : categoryElements) {
            elementsInfo.addAll(getElementsInfoByCategory(categoryElement, siteUrl));
        }
        return elementsInfo;
    }

    private static List<ElementInfo> getElementsInfoByCategory(Element categoryElement, String siteUrl) {
        List<ElementInfo> elementsInfo = new ArrayList<>();

        String category = category(categoryElement);
        Elements elements = categoryElement.getElementsByTag("li");
        for (Element element : elements) {
            elementsInfo.add(getElementInfo(element, category, siteUrl));
        }
        return elementsInfo;
    }

    private static ElementInfo getElementInfo(Element element, String category, String siteUrl) {
        long id = id(element);
        String name = name(element);
        String description = description(element);

        return new ElementInfo.Builder()
                .id(id)
                .name(name)
                .description(description)
                .category(category)
                .siteUrl(siteUrl)
                .build();
    }

    private static String description(Element element) {
        String almostDescription = element.ownText(); // i.e " - description"
        return almostDescription.substring(3);
    }

    private static String name(Element element) {
        return element.getElementsByTag("a").first().ownText();
    }

    private static long id(Element element) {
        String almostNumber = element.getElementsByTag("small").first().ownText(); // i.e. "(5)"
        return Long.parseLong(almostNumber.substring(1, almostNumber.length() - 1));
    }

    private static String category(Element categoryElement) {
        return categoryElement.getElementsByTag("strong").first().ownText();
    }

    public List<TemplateInfo> getTemplatesInfo() {
        return templatesInfo;
    }

    public List<VariableInfo> getVariablesInfo() {
        return variablesInfo;
    }

    public List<ChunkInfo> getChunksInfo() {
        return chunksInfo;
    }

    public List<SnippetInfo> getSnippetsInfo() {
        return snippetsInfo;
    }

    public List<PluginInfo> getPluginsInfo() {
        return pluginsInfo;
    }
}
