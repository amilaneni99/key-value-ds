package com.abhinav.keyvalueds.service.impl;

import com.abhinav.keyvalueds.service.FileService;
import com.abhinav.keyvalueds.shared.utils.Constants;
import com.abhinav.keyvalueds.shared.utils.ErrorMessages;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private String path = "E:/KeyValueDS/data/tmp";
    private File db = null;

    public String generateFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private File createFile() throws IOException {
        String fileName = generateFileName();

        File storageDirectory = new File(path);

        if (!storageDirectory.exists()){
            storageDirectory.mkdirs();
        }

        String fullPath = this.path+"/"+fileName+".json";

        File file = new File(fullPath);

        if(!file.exists()) file.createNewFile();

        return file;
    }

    @Override
    public File getInstance() throws Exception {
        if(this.db == null || (!this.db.exists())) {
            this.db = this.createFile();
        }

        double fileSize = this.db.length()/(1024 * 1024 * 1000);

        if(fileSize > Constants.MAX_FILE_SIZE) throw new Exception(ErrorMessages.FILE_SIZE_EXCEEDED.getErrorMessage());

        return this.db;
    }

    @Override
    public void writeToFile(byte[] data) throws Exception {

        FileOutputStream fileOutputStream = new FileOutputStream(this.getInstance());

        FileLock lock = fileOutputStream.getChannel().lock();

        fileOutputStream.write(data);

        lock.release();

        fileOutputStream.close();

    }

    @Override
    public void setDatabasePath(String uri) throws Exception {

        File rootDirectory = new File(uri);

        boolean status = rootDirectory.mkdirs();

        if (status == false) {
            throw new Exception(ErrorMessages.INVALID_PATH.getErrorMessage());
        }else {
            this.path = uri;
        }

    }
}
