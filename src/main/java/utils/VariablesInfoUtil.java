package utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import elements.templates.TemplateInfo;
import elements.VariableInfo;
import exceptions.ModxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VariablesInfoUtil {

    public static List<VariableInfo> obtainVariablesInfo(TemplateInfo templateInfo) throws ModxException {
        try {
            List<String> variableNames = obtainVariableNames(templateInfo);

            List<VariableInfo> variablesInfo = obtainVariablesInfo(templateInfo.getSiteUrl());

            return filterVariablesInfo(variableNames, variablesInfo);

        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }

    private static List<VariableInfo> filterVariablesInfo(List<String> variableNames, List<VariableInfo> variablesInfo) {
        List<VariableInfo> resultVariables = new ArrayList<>();
        for (VariableInfo variableInfo : variablesInfo) {
            if(variableNames.contains(variableInfo.getName())) resultVariables.add(variableInfo);
        }
        return resultVariables;
    }

    private static List<VariableInfo> obtainVariablesInfo(String siteUrl) throws UnirestException {
        String body = Unirest.get(siteUrl + "/manager/index.php")
                .header("Referer", siteUrl)
                .queryString("a", 76)
                .asString().getBody();

        Document document = Jsoup.parse(body);
        return ElementsInfoUtil.getVariablesInfo(document, siteUrl);
    }

    private static List<String> obtainVariableNames(TemplateInfo templateInfo) throws ModxException {
        try {
            String siteUrl = templateInfo.getSiteUrl();
            String body = Unirest.get(siteUrl + "/manager/index.php")
                    .header("Referer", siteUrl)
                    .queryString("a", 16)
                    .queryString("id", templateInfo.getId())
                    .asString().getBody();

            Document document = Jsoup.parse(body);
            Elements variableElements = document.getElementById("tabAssignedTVs").getElementsByTag("strong");

            return variableElements.stream().map(Element::ownText).collect(Collectors.toList());
        } catch (UnirestException e) {
            throw new ModxException(e);
        }
    }
}
