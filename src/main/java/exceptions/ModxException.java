package exceptions;

public class ModxException extends Exception {

    public ModxException(Exception e) { super(e); }

    public ModxException(String msg) { super(msg); }
}
