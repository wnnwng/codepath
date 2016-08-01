package com.codepath.flicks.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by winnie_wang on 8/1/16.
 */
public class Movie {
    private final double POPULAR_RATING_THRESHOLD = 5.0;
    private String overview;
    private String title;
    private String posterImgPath;
    private String backdropImgPath;
    private Double rating;
    private DisplayType display;

    public enum DisplayType {
        WITH_DETAILS, BACKDROP_ONLY
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterImgPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterImgPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropImgPath);
    }

    public DisplayType getDisplay() { return display; }

    public Movie(JSONObject json) throws JSONException {
        this.overview = json.getString("overview");
        this.title = json.getString("original_title");
        this.posterImgPath = json.getString("poster_path");
        this.backdropImgPath = json.getString("backdrop_path");
        this.rating = json.getDouble("vote_average");
        this.display = isPopular() ? DisplayType.BACKDROP_ONLY : DisplayType.WITH_DETAILS;
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> movieList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                movieList.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movieList;
    }

    public boolean isPopular() {
        return rating > POPULAR_RATING_THRESHOLD;
    }
}
