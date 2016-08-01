package com.codepath.flicks.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by winnie_wang on 8/1/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    boolean isVertical;

    private static class ViewHolder {
        TextView overview;
        TextView title;
        ImageView poster;
    }

    private static class BigViewHolder {
        ImageView poster;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies, boolean isVertical) {
        super(context, android.R.layout.simple_list_item_1, movies);
        this.isVertical = isVertical;
    }

    @Override
    public int getViewTypeCount() {
        return Movie.DisplayType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDisplay().ordinal();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        int type = getItemViewType(position);

        if (type == Movie.DisplayType.BACKDROP_ONLY.ordinal()) {
            BigViewHolder bigViewHolder;
            if (convertView == null) {
                convertView = getInflatedLayoutForMovie(type, parent);

                bigViewHolder = new BigViewHolder();
                bigViewHolder.poster = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                convertView.setTag(bigViewHolder);
            } else {
                bigViewHolder = (BigViewHolder) convertView.getTag();
            }

            Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.drawable.loading_icon).into(bigViewHolder.poster);

            return convertView;
        } else {
            ViewHolder standardViewHolder;

            if (convertView == null) {
                convertView = getInflatedLayoutForMovie(type, parent);

                standardViewHolder = new ViewHolder();
                standardViewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
                standardViewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
                standardViewHolder.poster = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                convertView.setTag(standardViewHolder);
            } else {
                standardViewHolder = (ViewHolder) convertView.getTag();
            }

            if (standardViewHolder.overview != null ) {
                standardViewHolder.overview.setText(movie.getOverview());
            }

            if (standardViewHolder.title != null ) {
                standardViewHolder.title.setText(movie.getTitle());
            }

            if (isVertical) {
                Picasso.with(getContext()).load(movie.getPosterImgPath()).placeholder(R.drawable.loading_icon).into(standardViewHolder.poster);
            } else {
                Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.drawable.loading_icon).into(standardViewHolder.poster);
            }
            // return the created view
            return convertView;
        }
    }

    private View getInflatedLayoutForMovie(int type, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (type == Movie.DisplayType.BACKDROP_ONLY.ordinal()) {
            return inflater.inflate(R.layout.item_movie_backdrop, parent, false);
        }

        return inflater.inflate(R.layout.item_movie, parent, false);
    }
}
