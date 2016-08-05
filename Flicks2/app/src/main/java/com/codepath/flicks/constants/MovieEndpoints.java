package com.codepath.flicks.constants;

/**
 * Created by winnie_wang on 8/4/16.
 */
public final class MovieEndpoints {
    private MovieEndpoints() { }

    public final static String TRAILERS = "https://api.themoviedb.org/3/movie/%s/trailers?api_key=f64df2643b6b52a62df19cd6fae8156d";
    public final static String CURRENTLY_PLAYING = "https://api.themoviedb.org/3/discover/movie?api_key=f64df2643b6b52a62df19cd6fae8156d";
}
