package elements;

public class ElementInfo {

    protected final long id;
    protected final String category;
    protected final String name;
    protected final String description;
    protected final String siteUrl;

    protected ElementInfo(Builder builder) {
        this.id = builder.id;
        this.category = builder.category;
        this.name = builder.name;
        this.description = builder.description;
        this.siteUrl = builder.siteUrl;
    }

    public static class Builder {
        private long id;
        private String category;
        private String name;
        private String description;
        private String siteUrl;

        public Builder id(long id) { this.id = id; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder siteUrl(String siteUrl) { this.siteUrl = siteUrl; return this; }
        public ElementInfo build() { return new ElementInfo(this); }
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSiteUrl() {
        return siteUrl;
    }
}
