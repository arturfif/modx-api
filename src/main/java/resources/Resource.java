package resources;

public class Resource {

    private String title;
    private String longTitle;
    private String description;
    private String urlAlias;
    private String linkAttributes;
    private String summary;
    private String template;
    private String menuTitle;
    private long menuIndex;
    private boolean showInMenu;
    private long resourceParentId;
    private String resourceContent;

    public Resource(Builder builder) {
        this.title = builder.title;
        this.longTitle = builder.longTitle;
        this.description = builder.description;
        this.urlAlias = builder.urlAlias;
        this.linkAttributes = builder.linkAttributes;
        this.summary = builder.summary;
        this.template = builder.template;
        this.menuTitle = builder.menuTitle;
        this.menuIndex = builder.menuIndex;
        this.showInMenu = builder.showInMenu;
        this.resourceParentId = builder.resourceParentId;
        this.resourceContent = builder.resourceContent;
    }

    public static class Builder {
        private String title;
        private String longTitle;
        private String description;
        private String urlAlias;
        private String linkAttributes;
        private String summary;
        private String template;
        private String menuTitle;
        private long menuIndex;
        private boolean showInMenu;
        private long resourceParentId;
        private String resourceContent;

        public Builder title(String title) { this.title = title; return this; }

        public Builder longTitle(String longTitle) { this.longTitle = longTitle; return this; }

        public Builder description(String description) { this.description = description; return this; }

        public Builder urlAlias(String urlAlias) { this.urlAlias = urlAlias; return this; }

        public Builder linkAttributes(String linkAttributes) { this.linkAttributes = linkAttributes; return this; }

        public Builder summary(String summary) { this.summary = summary; return this; }

        public Builder template(String template) { this.template = template; return this; }

        public Builder menuTitle(String menuTitle) { this.menuTitle = menuTitle; return this; }

        public Builder showInMenu(boolean showInMenu) { this.showInMenu = showInMenu; return this; }

        public Builder resourceParentId(long resourceParentId) { this.resourceParentId = resourceParentId; return this; }

        public Builder resourceContent(String resourceContent) { this.resourceContent = resourceContent; return this; }

        public Resource build() { return new Resource(this); }
    }


}
