package files;

import files.types.Action;

import java.util.Date;
import java.util.List;

public class FileInfo {

    private final String name;
    private final Date modifiedDate;
    private final long sizeBytes;
    private final List<Action> actions;

    FileInfo(Builder builder) {
        this.name = builder.name;
        this.modifiedDate = builder.modifiedDate;
        this.sizeBytes = builder.sizeBytes;
        this.actions = builder.actions;
    }

    public static class Builder {
        private String name;
        private Date modifiedDate;
        private long sizeBytes;
        private List<Action> actions;

        public Builder name(String name) { this.name = name; return this; }

        public Builder modifiedDate(Date modifiedDate) { this.modifiedDate = modifiedDate; return this; }

        public Builder sizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; return this; }

        public Builder actions(List<Action> actions) { this.actions = actions; return this; }

        public FileInfo build() { return new FileInfo(this); }
    }

    public String getName() {
        return name;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public List<Action> getActions() {
        return actions;
    }


}
