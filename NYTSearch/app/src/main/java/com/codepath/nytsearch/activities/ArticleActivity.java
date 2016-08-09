package com.codepath.nytsearch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.R;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        WebView webview = (WebView) findViewById(R.id.wvArticle);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(article.getWebUrl());
                return true;
            }
        });
        webview.loadUrl(article.getWebUrl());
    }
}
