package files.types;

public class Action {

    private final Type type;

    public Action(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        VIEW,
        DOWNLOAD,
        EDIT
    }
}
