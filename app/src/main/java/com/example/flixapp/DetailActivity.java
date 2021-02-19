package com.example.flixapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.flixapp.models.Movie;

public class DetailActivity extends AppCompatActivity {
    private Movie dMovie;
    private TextView detailTitle;
    private TextView detailDescription;
    private TextView dateReleased;
    private TextView movieRating;
    private ImageView imgPrv;
    private Button backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        getSupportActionBar().hide();

        detailTitle = findViewById(R.id.detail_title);
        detailDescription = findViewById(R.id.detail_description);
        dateReleased = findViewById(R.id.detail_release_date);
        imgPrv = findViewById(R.id.video_item);
        movieRating = findViewById(R.id.detail_rating);
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
        dateReleased.setText("Release Date: " + dMovie.getMovieReleaseDate());
        movieRating.setText(String.valueOf(dMovie.getMovieRating()) +"/10");
        Glide.with(this).load(dMovie.getBackdropImage()).into(imgPrv);

    }
}
