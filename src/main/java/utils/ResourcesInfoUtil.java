package utils;

import resources.ResourceInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourcesInfoUtil {

    private static final int NODE_ID_INDEX = 4;

    public static List<ResourceInfo> getResources(Document document) {
        Map<Long, Long> parentMap = parentMap(document);

        Elements treeNodes = document.getElementsByClass("treeNode");

        return treeNodes.stream().map(treeNode -> getResource(parentMap, treeNode)).collect(Collectors.toList());
    }

    private static long id(Element treeNode) {
        String attrValue = treeNode.attr("onclick");
        int begin = attrValue.indexOf('(') + 1;
        int end = attrValue.indexOf(',');
        return Long.parseLong(attrValue.substring(begin, end));
    }

    private static Map<Long, Long> parentMap(Document document) {
        Elements nodes = document.getElementsByAttributeValueStarting("id", "node");
        Map<Long, Long> parentMap = new HashMap<>(nodes.size());

        for (Element node : nodes) {
            Long id = Long.parseLong(node.attr("id").substring(NODE_ID_INDEX));
            Long parentId = Long.parseLong(node.attr("p"));
            parentMap.put(id, parentId);
        }

        return parentMap;
    }

    private static ResourceInfo getResource(Map<Long, Long> parentMap, Element treeNode) {

        long id = id(treeNode);
        long parentId = parentMap.get(id);
        String name = name(treeNode);
        String url = url(treeNode);

        return new ResourceInfo(id, parentId, name, url);
    }

    private static String name(Element treeNode) {
        return getParameter(treeNode, "selectedObjectName='");
    }

    private static String url(Element treeNode) {
        return getParameter(treeNode, "selectedObjectUrl='");
    }

    private static String getParameter(Element treeNode, String innerAttr) {
        String attr = treeNode.attr("onmousedown");

        int beginIndex = attr.indexOf(innerAttr) + innerAttr.length();
        int endIndex = attr.indexOf('\'', beginIndex);

        return attr.substring(beginIndex, endIndex);
    }
}
