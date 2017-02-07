package net.cnheider.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.cnheider.movieapp.movie.Movie;

/**
 * Created by heider on 02/02/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
  public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
  private static final String TAG = MovieDetailActivity.class.getSimpleName();
  private static final String MOVIE_APP_SHARE_HASHTAG = " #MovieApp";
  private Movie mMovie;
  private ImageView mPosterImageView;
  private TextView mTitleTextView, mRatingTextView, mSynopsisTextView, mReleaseDateTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    mPosterImageView = (ImageView) findViewById(R.id.movie_poster_image);
    mRatingTextView = (TextView) findViewById(R.id.movie_rating);
    mSynopsisTextView = (TextView) findViewById(R.id.movie_description);
    mTitleTextView = (TextView) findViewById(R.id.movie_title);
    mReleaseDateTextView = (TextView) findViewById(R.id.release_date);


    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_MOVIE)) {
      mMovie = (Movie) intent.getParcelableExtra(EXTRA_MOVIE);

      Picasso.with(this).load(mMovie.poster_image).into(mPosterImageView);
      mTitleTextView.setText(mMovie.title);
      mRatingTextView.setText(String.valueOf(mMovie.user_rating));
      mSynopsisTextView.setText(String.valueOf(mMovie.synopsis));
      mReleaseDateTextView.setText(mMovie.release_date);
    }

    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    myToolbar.inflateMenu(R.menu.detail);
    // Get a support ActionBar corresponding to this toolbar
    ActionBar ab = getSupportActionBar();

    // Enable the Up button
    ab.setDisplayHomeAsUpEnabled(true);
  }
}
