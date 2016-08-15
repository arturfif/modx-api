package core;

import exceptions.ModxException;
import files.FileManager;
import resources.ResourceInfo;

import java.util.List;

public interface ModxApi {

    boolean login(String login, String password) throws ModxException;

    boolean logout() throws ModxException;

    boolean moveResource(long id, long newParentId) throws ModxException;

    List<ResourceInfo> getResourcesInfo() throws ModxException;

    FileManager getFileManager() throws ModxException;




}
