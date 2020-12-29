package com.abhinav.keyvalueds.service;

import java.io.File;

public interface FileService {
    File getInstance() throws Exception;
    void writeToFile(byte[] data) throws Exception;
    void setDatabasePath(String uri) throws Exception;
}
