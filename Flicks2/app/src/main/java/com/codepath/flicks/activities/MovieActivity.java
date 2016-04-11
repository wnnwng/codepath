package com.codepath.flicks.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.constants.MovieEndpoints;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.ui.MovieArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setupMovieAdapter();
        setupSwipeRefresh();
        setUpToolbar();
        retrieveMovieData(false);

        lvItems.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = movies.get(position);
            if(movie.getDisplay() == Movie.DisplayType.WITH_DETAILS) {
                Intent i = new Intent(MovieActivity.this, MovieDetailActivity.class);
                i.putExtra("movie", movie);
                startActivityForResult(i, REQUEST_CODE);
            } else { // display is backdrop
                Intent i = new Intent(MovieActivity.this, FullscreenTrailerActivity.class);
                i.putExtra("movieId", movie.getId());
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    private void setUpToolbar() {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) tb.findViewById(R.id.toolbar_title);
    }

    private void setupMovieAdapter() {
        boolean isVertical = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        lvItems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies, isVertical);
        lvItems.setAdapter(movieAdapter);
    }

    private void setupSwipeRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> retrieveMovieData(true));
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
    }

    private void retrieveMovieData(final boolean clearArray) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(MovieEndpoints.CURRENTLY_PLAYING, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray movieJsonResults = response.getJSONArray("results");
                    if (clearArray) {
                        movies.clear();
                    }
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
