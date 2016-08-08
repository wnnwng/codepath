package com.codepath.nytsearch;

import com.codepath.nytsearch.models.Article;
import com.codepath.nytsearch.models.ArticleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by winnie_wang on 8/11/16.
 */
public interface NYTEndpointInterface {
    @GET("articlesearch.json")
    Call<ArticleResponse> getArticleResponse(@Query("q") String query, @Query("page") int page,
                                             @Query("sort") String sort,
                                             @Query("begin_date") String beginDate,
                                             @Query("fq") String categories,
                                             @Query("api-key") String apiKey);
}
