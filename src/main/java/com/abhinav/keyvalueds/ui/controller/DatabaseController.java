package com.abhinav.keyvalueds.ui.controller;

import com.abhinav.keyvalueds.service.DatabaseService;
import com.abhinav.keyvalueds.service.FileService;
import com.abhinav.keyvalueds.shared.utils.Constants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/db")
public class DatabaseController {
    @Autowired
    DatabaseService databaseService;

    @Autowired
    FileService fileService;

    @PostMapping(
            consumes ={MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public JSONObject create(@RequestBody String pair) throws Exception {
        JSONObject keyValueJSON = (JSONObject) new JSONParser().parse(pair);

        String key = (String) keyValueJSON.get("key");

        JSONObject value = (JSONObject) keyValueJSON.get("value");

        if(!keyValueJSON.containsKey("ttl")){
            keyValueJSON.put("ttl", Constants.TTL);
        }
        keyValueJSON.put("createdAt", new Date().getTime());

        databaseService.checkIfValid(key, new JSONObject(value));

        return databaseService.addValueByKey(key, keyValueJSON);
    }

    @RequestMapping(value = "/search/{key}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public JSONObject getEmployee(@PathVariable String key) throws Exception {
        return databaseService.getValueByKey(key);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String key) throws Exception {
        return databaseService.deleteValueByKey(key);
    }

    @PostMapping(
            path = "/path",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public void setDataBasePath(@RequestBody Map<String, Object> path) throws Exception {

        if (!path.containsKey("uri")) throw new Exception("URI required");
        this.fileService.setDatabasePath((String) path.get("uri"));
    }
}
