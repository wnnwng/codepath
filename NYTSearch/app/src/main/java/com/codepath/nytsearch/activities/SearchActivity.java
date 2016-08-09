package com.codepath.nytsearch.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.codepath.nytsearch.EndlessScrollListener;
import com.codepath.nytsearch.fragments.FilterDialogFragment;
import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.ArticleArrayAdapter;
import com.codepath.nytsearch.R;
import com.loopj.android.http.*;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    GridView gvResults;
    MenuItem searchItem;
    MenuItem filterItem;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String query;
    public final static int REQUEST_FILTER_CODE = 55;

    public static final String ARTICLE_SEARCH_ENDPOINT = "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=5da63e75ae6f4fd5b15c042a3460e8c6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_article_search);
        setUpViews();
        setUpListeners();
    }

    private void setUpViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
    }

    private void setUpListeners() {
        gvResults.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(this, ArticleActivity.class);
            Article article = articles.get(position);
            i.putExtra("article", Parcels.wrap(article));
            startActivity(i);
        });
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                onArticleSearch(page, false);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        filterItem = menu.findItem(R.id.action_filter);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                SearchActivity.this.query = query;
                onArticleSearch(0, true);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fm = getSupportFragmentManager();
                FilterDialogFragment editNameDialogFragment = FilterDialogFragment.newInstance("Filter articles");
                editNameDialogFragment.show(fm, "fragment_filter");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getNewsDeskValues() {
        Set<FilterDialogFragment.Categories> categories = FilterDialogFragment.Filter.categories;
        String result = "news_desk:(";

        for (FilterDialogFragment.Categories category: categories) {
            result += "\"" + category.toString() + "\"";
        }

        return result + ")";
    }

    public void onArticleSearch(int page, boolean clearData) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        DateTime beginTime = FilterDialogFragment.Filter.beginTime;
        params.put("q", query);
        params.put("page", page);
        if (FilterDialogFragment.Filter.categories.size() > 0) {
            params.put("fq", getNewsDeskValues());
        }

        if (beginTime != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            params.put("begin_date", fmt.print(beginTime));
        }

        if (FilterDialogFragment.Filter.sortOrder == FilterDialogFragment.SortOrder.NEWEST) {
            params.put("sort", FilterDialogFragment.SortOrder.NEWEST.toString().toLowerCase());
        } else if (FilterDialogFragment.Filter.sortOrder == FilterDialogFragment.SortOrder.OLDEST) {
            params.put("sort", FilterDialogFragment.SortOrder.OLDEST.toString().toLowerCase());
        }

        client.get(ARTICLE_SEARCH_ENDPOINT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    if (clearData) {
                        adapter.clear();
                    }
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                };
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

}
