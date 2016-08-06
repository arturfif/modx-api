package core;

import exceptions.ModxException;
import files.FileManager;

import java.io.File;
import java.util.List;

public interface ModxApi {

    boolean login(char[] login, char[] password) throws ModxException;

    boolean logout() throws ModxException;

    boolean moveResource(long id, long newParentId) throws ModxException;

    List<ResourceInfo> getResourcesInfo() throws ModxException;

    boolean uploadImage(String destinationPath, File image) throws ModxException;

    FileManager getFileManager() throws ModxException;


}
