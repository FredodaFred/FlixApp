package com.example.flixapp.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixapp.R;
import com.example.flixapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class  MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    private AsyncHttpClient client;
    private int currentPage;
    private JsonHttpResponseHandler response;
    private final String TAG = "MovieAdapter";
    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY+"&page=" ;
    private OnMovieClickListener onMovieClickListenerA;

    public MovieItemAdapter(Context context, OnMovieClickListener onMovieClickListenerA) {
        this.context = context;
        this.onMovieClickListenerA = onMovieClickListenerA;
        this.client = new AsyncHttpClient(); //to fetch data from internet
        this.movies = new ArrayList<>();
        this.currentPage = 1;
        this.response = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results= jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: "+results);
                    movies.addAll(Movie.fromJsonArray(results));
                    notifyDataSetChanged();//KEY LINE OF CODE
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Kit json exception", e);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        };
        client.get(NOW_PLAYING_URL+currentPage, response);
    }

    public List<Movie> getMovies() {
        return movies;
    }
    public void fetchNewMovies(){
        if(currentPage < 1000){
            currentPage++;
            client.get(NOW_PLAYING_URL+currentPage, response);
        }
    }

    //Usually involves inflating a layout from xml and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "OnCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.movie_screen, parent, false);
        return new ViewHolder(movieView, onMovieClickListenerA);
    }
    //populating data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "OnBindViewHolder");
        //get the movie passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    //returns the amount of movies in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        OnMovieClickListener onMovieClickListener;

        public ViewHolder(@NonNull View itemView, OnMovieClickListener onMovieClickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.movie_name);
            tvOverview = itemView.findViewById(R.id.movie_description);
            ivPoster = itemView.findViewById(R.id.movie_picture);
            this.onMovieClickListener = onMovieClickListener;
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getMovieName());
            tvOverview.setText(movie.getMovieDescription());
            String imageURL;
            //if portrait use default path, else use backdrop path
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageURL = movie.getBackdropImage();
            }
            else{
                imageURL = movie.getMovieImage();
            }
            Glide.with(context).load(imageURL).into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            onMovieClickListener.onMovieClick(getAdapterPosition());
        }
    }
    public interface OnMovieClickListener{
        void onMovieClick(int position);
    }


}
