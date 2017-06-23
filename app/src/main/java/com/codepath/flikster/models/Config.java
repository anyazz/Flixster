package com.codepath.flikster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anyazhang on 6/22/17.
 */

public class Config {

    // base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;
    // the backdrop size to use when fetching imgs
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // get image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use option at index 3 or w342 as fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse backdrop sizes and use option at index1 or w780 as fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w480");
    }

    // helper method for creating urls
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path); //concatenate all tree
    }
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
