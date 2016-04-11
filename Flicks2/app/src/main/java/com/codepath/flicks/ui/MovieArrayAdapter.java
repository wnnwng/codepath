package com.codepath.flicks.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.flicks.databinding.ItemMovieBackdropBinding;
import com.codepath.flicks.databinding.ItemMovieBinding;
import com.codepath.flicks.models.Movie;

import java.util.List;

/**
 * Created by winnie_wang on 8/1/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    public MovieArrayAdapter(Context context, List<Movie> movies, boolean isVertical) {
        super(context, android.R.layout.simple_list_item_1, movies);
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
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (type == Movie.DisplayType.BACKDROP_ONLY.ordinal()) {
            ItemMovieBackdropBinding binding;

            if (convertView == null) {
                binding = ItemMovieBackdropBinding.inflate(inflater, parent, false);
            } else {
                binding = (ItemMovieBackdropBinding) convertView.getTag();
            }
            convertView = binding.getRoot();
            binding.setMovie(movie);
            convertView.setTag(binding);

            return convertView;
        } else {
            ItemMovieBinding binding;

            if (convertView == null) {
                binding = ItemMovieBinding.inflate(inflater, parent, false);
            } else {
                binding = (ItemMovieBinding) convertView.getTag();
            }

            convertView = binding.getRoot();
            convertView.setTag(binding);
            binding.setMovie(movie);

            return convertView;
        }
    }
}
