package com.oose2015.grapevine;

import org.json.JSONObject;

/**
 * Created by venussoontornprueksa on 11/11/15.
 */
public class Utils {

    /**
     * Checks if a json object really has the attribute for a certain key.
     * @param jsonObject the json object to check
     * @param key the key to look for
     * @return true if the object contains the key, false otherwise
     */
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }
}
