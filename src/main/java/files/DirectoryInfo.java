package files;

public class DirectoryInfo extends FileInfo {

    private final String path;

    // index.php?a=31&mode=drill&path=Z%3A%2Fhome%2Feps.biz%2Fwww%2Fassets

    private DirectoryInfo(Builder builder) {
        super(builder);
        this.path = builder.path;
    }

    public static class Builder extends FileInfo.Builder {

        private String path;

        public Builder goInsideLink(String goInsideLink) { this.path = goInsideLink; return this; }

        public DirectoryInfo build() { return new DirectoryInfo(this); }

    }

    public FileManager goInside() {
        return null;
    }
}
