package com.codepath.nytsearch.activities;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.nytsearch.ArticleAdapter;
import com.codepath.nytsearch.EndlessRecyclerViewScrollListener;
import com.codepath.nytsearch.NYTEndpointInterface;
import com.codepath.nytsearch.fragments.FilterDialogFragment;
import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.R;
import com.codepath.nytsearch.models.ArticleResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.rvResults) RecyclerView rvResults;
    MenuItem miActionProgressItem;
    MenuItem searchItem;
    MenuItem filterItem;
    ArrayList<Article> articles = new ArrayList<Article>();;
    ArticleAdapter adapter;
    String query;
    StaggeredGridLayoutManager gridLayoutManager;
    public final static int REQUEST_FILTER_CODE = 55;

    public static final String ARTICLE_SEARCH_ENDPOINT = "https://api.nytimes.com/svc/search/v2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        JodaTimeAndroid.init(this);
        ButterKnife.bind(this);
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        setUpViews();
        setUpScrollListener();
    }

    private void setUpViews() {
        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(gridLayoutManager);
    }

    private void setUpScrollListener() {
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onArticleSearch(page, false);
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
                SearchActivity.this.query = query;
                onArticleSearch(0, true);
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
                FilterDialogFragment filterFragment = FilterDialogFragment.newInstance("Filter articles");
                filterFragment.show(fm, "fragment_filter");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProgressBar() {
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        miActionProgressItem.setVisible(false);
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
        showProgressBar();

        if (!isOnline()) {
            Toast.makeText(this, "You're not connected to the internet", Toast.LENGTH_SHORT).show();
            hideProgressBar();
            return;
        }

        String beginDate = null;
        String categories = null;
        String sort = null;

        DateTime beginTime = FilterDialogFragment.Filter.beginTime;
        if (FilterDialogFragment.Filter.categories.size() > 0) {
            categories = getNewsDeskValues();
        }

        if (beginTime != null) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            beginDate = fmt.print(beginTime);
        }

        if (FilterDialogFragment.Filter.sortOrder == FilterDialogFragment.SortOrder.NEWEST) {
            sort = FilterDialogFragment.SortOrder.NEWEST.toString().toLowerCase();
        } else if (FilterDialogFragment.Filter.sortOrder == FilterDialogFragment.SortOrder.OLDEST) {
            sort = FilterDialogFragment.SortOrder.OLDEST.toString().toLowerCase();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ARTICLE_SEARCH_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NYTEndpointInterface apiService = retrofit.create(NYTEndpointInterface.class);
        String apiKey = "5da63e75ae6f4fd5b15c042a3460e8c6";
        Call<ArticleResponse> call = apiService.getArticleResponse(query, page, sort, beginDate, categories, apiKey);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (clearData) {
                    articles.clear();
                }
                ArticleResponse ar = response.body();
                articles.addAll(ar.getResponse().getArticles());
                adapter.notifyDataSetChanged();
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();

        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            hideProgressBar();
            e.printStackTrace();
        }

        return false;
    }
}
