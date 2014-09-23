package com.keran.marinov.itunezfeed.http;

import com.keran.marinov.itunezfeed.list.FeedEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by linxmap on 16/09/14.
 */
public class JsonParserManager {

    private final String TAG = "JsonParserManager";
    private static final boolean LOGGING_ENABLED = true;

    private final String JSON_VAR_FEED = "feed";
    private final String JSON_VAR_ENTRY = "entry";
    private final String JSON_VAR_IM_NAME = "im:name";
    private final String JSON_VAR_LABEL = "label";
    private final String JSON_VAR_IM_IMAGE = "im:image";


    public ArrayList<FeedEntry> getFeedList(String httpResponseBody) {
        ArrayList<FeedEntry> list = new ArrayList<FeedEntry>();

        try {
            JSONObject jsonObject = new JSONObject(httpResponseBody);
            JSONObject jsonObjectFeed = jsonObject.getJSONObject(JSON_VAR_FEED);
            JSONArray arrayList = jsonObjectFeed.getJSONArray(JSON_VAR_ENTRY);
            if (arrayList != null && arrayList.length() > 0) {
                //L.log(TAG, LOGGING_ENABLED, "arrayList.length() :" + arrayList.length());
                for (int i = 0; i < arrayList.length(); i++) {
                    JSONObject listItem = arrayList.getJSONObject(i);
                    String songTitle = getTitle(listItem);
                    String imageUrl = getImageUrl(listItem);
                    list.add(new FeedEntry(songTitle, imageUrl));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public String getTitle(JSONObject jsonObject) {
        String songTitle = "";
        try {
            JSONObject jsonObjectImName = jsonObject.getJSONObject(JSON_VAR_IM_NAME);
            songTitle = jsonObjectImName.getString(JSON_VAR_LABEL);
            // L.log(TAG, LOGGING_ENABLED, "songTitle :" + songTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songTitle;

    }

    public String getImageUrl(JSONObject jsonObject) {
        String imageUrl = "";
        try {
            JSONArray arrayList = jsonObject.getJSONArray(JSON_VAR_IM_IMAGE);
            if (arrayList != null && arrayList.length() > 0) {
                //L.log(TAG, LOGGING_ENABLED, "arrayList.length() :" + arrayList.length());
                for (int i = 0; i < arrayList.length(); i++) {
                    JSONObject listItem = arrayList.getJSONObject(i);
                    imageUrl = listItem.getString(JSON_VAR_LABEL);

                }
            }
            //L.log(TAG, LOGGING_ENABLED, "imageUrl :" + imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;

    }
}
