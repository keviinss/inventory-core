package com.app.inventory.helpers;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonMapping {

    public static JSONObject objToJsonObject(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        JSONObject data_object = new JSONObject(json);
        return data_object;
    }

    public static String objToString(Object obj) {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        return json;
    }

    public static List<?> getListFromObj(JSONObject obj, String key_name) {
        List<?> dataList = new ArrayList<>();
        if (obj.has(key_name)) {
            dataList = obj.getJSONArray(key_name).toList();
        }
        return dataList;
    }

    public static String getValObject(String json_string, String key_name) {
        String data = "";
        JSONObject obj = new JSONObject(json_string);
        if (obj.has(key_name)) {
            key_name = obj.get(key_name).toString();
            data = key_name;
        }
        return data;
    }

    public static JSONArray groupByArray(String key, JSONArray rows) {

        JSONArray result = new JSONArray();
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < rows.length(); i++) {
            String identity = rows.getJSONObject(i).getString(key);
            if (!groups.contains(identity)) {
                groups.add(identity);
                result.put(rows.getJSONObject(i));
            }
        }

        return result;
    }

    public static JSONArray findByArray(String key, String value, JSONArray rows) {

        JSONArray result = new JSONArray();
        for (int i = 0; i < rows.length(); i++) {
            String identity = rows.getJSONObject(i).getString(key);
            if (identity.equals(value)) {
                result.put(rows.getJSONObject(i));
            }
        }

        return result;
    }

}
