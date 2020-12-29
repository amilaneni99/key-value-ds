package com.abhinav.keyvalueds.service;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface DatabaseService {
    JSONObject addValueByKey(String key, JSONObject object) throws Exception;
    JSONObject getValueByKey(String key) throws Exception;
    ResponseEntity<String> deleteValueByKey(String key) throws Exception;
    boolean checkIfValid(String key, JSONObject value);
}
