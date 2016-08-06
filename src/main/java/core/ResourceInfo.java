package core;

public class ResourceInfo {

    private final long id;

    private final long parentId;

    private final String name;

    private final String url;

    public ResourceInfo(long id, long parentId, String name, String url) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
