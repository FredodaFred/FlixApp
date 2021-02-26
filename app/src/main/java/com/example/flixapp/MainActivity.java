package com.example.flixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.AbsListView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixapp.adapters.MovieItemAdapter;
import com.example.flixapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity implements MovieItemAdapter.OnMovieClickListener {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "Main Activity";
    public List<Movie> movies;
    public boolean isScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        //create an adapter
        MovieItemAdapter movieAdapter = new MovieItemAdapter(this, this::onMovieClick);
        //set a layout manager on the recycler view
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMovies.setLayoutManager(manager);
        //set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //add scroll listener for endless scrolling
        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems = manager.getChildCount();
                int scrollOutItems = manager.findFirstCompletelyVisibleItemPosition();
                int totalItems = manager.getItemCount();
                if(isScrolling && (currentItems+scrollOutItems == totalItems)){
                    Log.d(TAG, "New data fetched");
                    movieAdapter.fetchNewMovies();
                }
            }
        });

        movies = movieAdapter.getMovies();

    }

    @Override
    public void onMovieClick(int position) {
        Log.d(TAG, "Movie item clicked");
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie", movies.get(position));
        startActivity(intent);
    }
}