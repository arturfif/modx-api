package files;

import java.util.Date;

public class Item {

    final String siteUrl;
    final String indexUrl;
    final String name;
    final String path;
    final Date modifiedDate;
    final long sizeBytes;
    boolean isDeleted = false;

    Item(Builder builder) {
        this.siteUrl = builder.siteUrl;
        this.indexUrl = siteUrl + "/manager/index.php";
        this.name = builder.name;
        this.path = builder.path;
        this.modifiedDate = builder.modifiedDate;
        this.sizeBytes = builder.sizeBytes;
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public static class Builder {

        String siteUrl;
        String name;
        String path;
        Date modifiedDate;
        long sizeBytes;

        public Builder siteUrl(String siteUrl) { this.siteUrl = siteUrl; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder path(String path) { this.path = path; return this; }
        public Builder modifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; return this; }
        public Builder sizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; return this; }

        public Item build() {
            return new Item(this);
        }

    }

}
