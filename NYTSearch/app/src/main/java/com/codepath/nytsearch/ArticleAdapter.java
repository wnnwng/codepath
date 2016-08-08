package com.codepath.nytsearch;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytsearch.activities.ArticleActivity;
import com.codepath.nytsearch.models.Article;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by winnie_wang on 8/9/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> articles;
    private Context context;
    private final int WITH_IMAGE = 0, WO_IMAGE = 1;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case WITH_IMAGE:
                View v1 = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolderWithImage(v1);
                break;
            case WO_IMAGE:
                View v2 = inflater.inflate(R.layout.item_article_result_wo_image, parent, false);
                viewHolder = new ViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new ViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case WITH_IMAGE:
                ViewHolderWithImage vh1 = (ViewHolderWithImage) holder;
                configureViewHolderWithImage(vh1, position);
                break;
            case WO_IMAGE:
                ViewHolder vh2 = (ViewHolder) holder;
                configureViewHolder(vh2, position);
                break;
            default:
                break;
        }
    }
    private void configureViewHolder(ViewHolder vh, int position) {
        Article article = articles.get(position);
        vh.tvTitle.setText(article.getHeadline());
        vh.bind(article);
    }

    private void configureViewHolderWithImage(ViewHolderWithImage vh, int position) {
        Article article = articles.get(position);
        String thumbnail = article.getThumbnailSrc();
        vh.tvTitle.setText(article.getHeadline());

        if(!TextUtils.isEmpty(thumbnail)) {
            Glide.with(getContext()) .load(thumbnail).into(vh.ivImage);
        }

        vh.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);

        if (article.getThumbnailSrc().length() > 0) {
            return WITH_IMAGE;
        }

        return WO_IMAGE;
    }

    public class ViewHolderWithImage extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivImage;

        public ViewHolderWithImage(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }

        public void bind(final Article item) {
            itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ArticleActivity.class);
                i.putExtra("article", Parcels.wrap(item));
                context.startActivity(i);
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

        public void bind(final Article item) {
            itemView.setOnClickListener(view -> {
                Intent i = new Intent(context, ArticleActivity.class);
                i.putExtra("article", Parcels.wrap(item));
                context.startActivity(i);
            });
        }
    }
}
