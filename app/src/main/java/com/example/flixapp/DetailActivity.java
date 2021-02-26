package com.example.flixapp;

import android.content.Intent;
import android.media.Rating;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixapp.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private Movie dMovie;
    private TextView detailTitle;
    private TextView detailDescription;
    private TextView dateReleased;
    private RatingBar movieRating;
    private YouTubePlayerView ytPrv;
    private static final String YT_API_KEY = "AIzaSyAvCo-5EM-Yw3fa6vZDzicWiwgBF6K-9Vg";
    private static final String YT_GET_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key="+YT_API_KEY+"&language=en-US";
    private AsyncHttpClient client;
    private Button backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);

        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        dateReleased = findViewById(R.id.detail_release_date);
        ytPrv = findViewById(R.id.video_item);
        movieRating = findViewById(R.id.ratings_bar);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });

        Log.d("DetailActivity", "OnCreate Called");
        if(getIntent().hasExtra("movie")){
            this.dMovie = getIntent().getParcelableExtra("movie");
        Log.d("DetailActivity", "MovieParsed");
        }

        detailTitle.setText(dMovie.getMovieName());
        detailDescription.setText(dMovie.getMovieDescription());
        dateReleased.setText(dMovie.getMovieReleaseDate());
        float rating = (float)dMovie.getMovieRating()/2;
        Log.d("Details", String.valueOf(rating));
        movieRating.setRating(rating);

        client.get(String.format(YT_GET_URL, dMovie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("Detail Activtiy", "YTGet success");
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){return;}
                    String ytKey = results.getJSONObject(0).getString("key");
                    Log.d("Detail Activity", "Get key success, "+ytKey);
                    initializeYoutube(ytKey);

                } catch (JSONException e) {
                    Log.d("Detail Activity", "get json array failure");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Detail Activtiy", "YTGet failure");
            }
        });

    }
    private void initializeYoutube(final String ytKey){
        ytPrv.initialize(ytKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("Detail Activty", "YT Initialize Success");
                youTubePlayer.cueVideo(ytKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("Detail Activty", "YT Initialize Failure");
            }
        });
    }
}
