package elements;

import elements.ElementInfo;
import elements.templates.TemplateInfo;

public class VariableInfo extends ElementInfo {

    private VariableInfo(Builder builder) {
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
        public TemplateInfo build() {
            return (TemplateInfo) super.build();
        }
    }

}
