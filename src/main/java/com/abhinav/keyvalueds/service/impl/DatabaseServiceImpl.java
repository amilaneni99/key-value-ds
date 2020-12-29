package com.abhinav.keyvalueds.service.impl;

import com.abhinav.keyvalueds.exceptions.DatabaseServiceException;
import com.abhinav.keyvalueds.service.DatabaseService;
import com.abhinav.keyvalueds.service.FileService;
import com.abhinav.keyvalueds.shared.utils.Constants;
import com.abhinav.keyvalueds.shared.utils.ErrorMessages;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Date;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    FileService fileService;

    public File getFile() throws Exception {
        return fileService.getInstance();
    }

    @Override
    public JSONObject addValueByKey(String key, JSONObject pair) throws Exception {

        File db = this.getFile();
        JSONObject dbObjects = new JSONObject();

        JSONObject returnValue = pair;

        pair.remove("key");

        if(db.length() == 0) {

            dbObjects.put(key, pair);

            this.fileService.writeToFile(dbObjects.toJSONString().getBytes());
        }else {

            FileReader reader = new FileReader(db);
            JSONParser parser = new JSONParser();
            JSONObject pairs = (JSONObject) parser.parse(reader);

            if(pairs.containsKey(key)) throw new DatabaseServiceException(ErrorMessages.KEY_ALREADY_EXISTS.getErrorMessage());

            pairs.put(key, pair);

            reader.close();

            this.fileService.writeToFile(pair.toJSONString().getBytes());

        }

        return returnValue;
    }

    @Override
    public JSONObject getValueByKey(String key) throws Exception {

        FileReader reader = new FileReader(this.getFile());
        JSONParser parser = new JSONParser();

        JSONObject pairs = (JSONObject) parser.parse(reader);

        if (!pairs.containsKey(key)) throw new DatabaseServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        JSONObject requiredPair = (JSONObject) pairs.get(key);

        reader.close();

        if (((new Date().getTime()) - ((long) requiredPair.get("createdAt") + (long) requiredPair.get("ttl"))) >= 0) {

            deleteValueByKey(key);
            throw new DatabaseServiceException(ErrorMessages.KEY_EXPIRED.getErrorMessage());

        }

        return requiredPair;

    }

    @Override
    public void deleteValueByKey(String key) throws Exception {

        FileReader reader = new FileReader(this.getFile());
        JSONParser parser = new JSONParser();

        JSONObject pairs = (JSONObject) parser.parse(reader);

        if (!pairs.containsKey(key)) throw new DatabaseServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        pairs.remove(key);

        reader.close();

        this.fileService.writeToFile(pairs.toJSONString().getBytes());

    }

    @Override
    public boolean checkIfValid(String key, JSONObject value) {

        if (key.length() > Constants.MAX_KEY_SIZE) throw new DatabaseServiceException(ErrorMessages.INVALID_KEY.getErrorMessage());

        if ((value.toJSONString().length() / (1024)) > Constants.MAX_VALUE_SIZE) throw new DatabaseServiceException(ErrorMessages.VALUE_SIZE_EXCEEDED.getErrorMessage());

        return true;

    }
}
