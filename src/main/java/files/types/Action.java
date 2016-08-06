package files.types;

public class Action {

    public enum Type {
        VIEW,
        DOWNLOAD,
        EDIT,
        DELETE
    }

    private final Type type;

    private final String path;

    public Action(Type type, String path) {
        this.type = type;
        this.path = path;
    }
}
