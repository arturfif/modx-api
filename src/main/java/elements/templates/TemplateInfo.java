package elements.templates;

import elements.ElementInfo;
import elements.VariableInfo;
import exceptions.ModxException;
import utils.VariablesInfoUtil;

import java.util.List;

public class TemplateInfo extends ElementInfo {

    private TemplateInfo(Builder builder) {
        super(builder);
    }

    public static class Builder extends ElementInfo.Builder {

        @Override
        public Builder id(long id) { return (Builder) super.id(id); }

        @Override
        public Builder category(String category) { return (Builder) super.category(category); }

        @Override
        public Builder name(String name) { return (Builder) super.name(name); }

        @Override
        public Builder description(String description) { return (Builder) super.description(description); }

        @Override
        public Builder siteUrl(String siteUrl) { return (Builder) super.siteUrl(siteUrl); }

        @Override
        public TemplateInfo build() { return (TemplateInfo) super.build(); }
    }

    public Template obtainTemplate() throws ModxException {
        List<VariableInfo> variablesInfo = VariablesInfoUtil.obtainVariablesInfo(this);
        return new Template(id, variablesInfo);
    }


}
