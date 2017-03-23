package net.cnheider.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import net.cnheider.movieapp.data.movie.Movie;
import net.cnheider.movieapp.data.movie.MovieContract;
import net.cnheider.movieapp.data.review.Review;
import net.cnheider.movieapp.data.review.ReviewAdapter;
import net.cnheider.movieapp.data.trailer.Trailer;
import net.cnheider.movieapp.data.trailer.TrailerAdapter;
import net.cnheider.movieapp.utilities.NetworkUtilities;
import net.cnheider.movieapp.utilities.TMDBUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heider on 02/02/17.
 */

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
                                                                      ReviewAdapter.ReviewAdapterOnClickHandler {
  public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
  private static final String TAG = MovieDetailActivity.class.getSimpleName();
  private static final String MOVIE_APP_SHARE_HASHTAG = " #MovieApp";
  @BindView(R.id.movie_title)
  TextView mTitleTextView;
  @BindView(R.id.movie_description)
  TextView mSynopsisTextView;
  @BindView(R.id.release_date)
  TextView mReleaseDateTextView;
  @BindView(R.id.movie_rating)
  TextView mRatingTextView;
  @BindView(R.id.movie_trailers_recycler_view)
  RecyclerView mMovieTrailerRecyclerView;
  @BindView(R.id.movie_reviews_recycler_view)
  RecyclerView mMovieReviewListView;
  MenuItem favorites_button, seen_button, playlist_button;
  @BindView(R.id.scrollview_parent_movie_detail)
  ScrollView scrollView;


  Context mContext;
  TrailerAdapter mTrailerAdapter;
  ReviewAdapter mReviewAdapter;
  Menu mMenu;
  boolean favorite = false, seen = false, playlist = false;
  private Movie mMovie;
  private List<Trailer> mTrailers;
  private List<Review> mReviews;

  public class FetchReviewTask extends AsyncTask<Movie, Void, ArrayList<Review>> {
    @Override
    protected ArrayList<Review> doInBackground(Movie... params) {
      Log.d(TAG, "doInBackground: " + String.valueOf(params[0].id));
      URL reviewURL = NetworkUtilities.getReviewURL(String.valueOf(params[0].id));

      try {
        String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(reviewURL);
        Review[] reviewData = TMDBUtilities.getReviewsForMovieJson(jsonResponse);
        return new ArrayList<Review>(Arrays.asList(reviewData));

      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviewData) {
      if (reviewData != null) {
        mReviews = reviewData;
        mReviewAdapter.setReviewData(mReviews);
      } else {
        Toast.makeText(mContext, "An error occurred while fetching reviews", Toast.LENGTH_LONG).show();
      }
    }
  }

  public class FetchTrailersTask extends AsyncTask<Movie, Void, ArrayList<Trailer>> {
    @Override
    protected ArrayList<Trailer> doInBackground(Movie... params) {
      Log.d(TAG, "doInBackground: " + String.valueOf(params[0].id));
      URL trailerURL = NetworkUtilities.getTrailerURL(String.valueOf(params[0].id));

      try {
        String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(trailerURL);
        Trailer[] trailerData = TMDBUtilities.getTrailersForMovieJson(jsonResponse);
        return new ArrayList<Trailer>(Arrays.asList(trailerData));

      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailerData) {
      if (trailerData != null) {
        mTrailers = trailerData;
        mTrailerAdapter.setTrailerData(mTrailers);
      } else {
        Toast.makeText(mContext, "An error occurred while fetching trailers", Toast.LENGTH_LONG).show();
      }
    }
  }

  @Override
  public void onClick(Review review) {

  }

  @Override
  public void onClick(Trailer trailer) {
    openTrailerInYT(trailer.key);
  }

  private void openTrailerInYT(String videoId) {
    Uri video_uri = Uri.parse("vnd.youtube:" + videoId);

    Intent intent = new Intent(Intent.ACTION_VIEW, video_uri);
    //intent.setData(video_uri);

    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    } else {
      Log.d(TAG, "Couldn't call " + video_uri.toString() + ", no receiving apps installed!");
      video_uri = Uri.parse("http://www.youtube.com/watch?v=" + videoId);
      intent = new Intent(Intent.ACTION_VIEW, video_uri);
      startActivity(intent);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);

    mContext = this;

    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_MOVIE)) {
      mMovie = (Movie) intent.getParcelableExtra(EXTRA_MOVIE);

      Glide.with(mContext).load(mMovie.poster_image).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
          Drawable drawable = new BitmapDrawable(bitmap);
          scrollView.setBackground(drawable);
        }
      });
      mTitleTextView.setText(mMovie.title);
      mRatingTextView.setText(String.valueOf(mMovie.user_rating));
      mSynopsisTextView.setText(String.valueOf(mMovie.synopsis));
      mReleaseDateTextView.setText(mMovie.release_date);
    }

    mTrailers = new ArrayList<Trailer>();
    mReviews = new ArrayList<Review>();

    mMovieTrailerRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    mMovieTrailerRecyclerView.setLayoutManager(layoutManager);
    mTrailerAdapter = new TrailerAdapter(this, mTrailers, this);
    mMovieTrailerRecyclerView.setAdapter(mTrailerAdapter);

    mMovieReviewListView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
    mMovieReviewListView.setLayoutManager(layoutManager2);
    mReviewAdapter = new ReviewAdapter(this, mReviews, this);
    mMovieReviewListView.setAdapter(mReviewAdapter);


    new FetchTrailersTask().execute(mMovie);
    new FetchReviewTask().execute(mMovie);

    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    myToolbar.inflateMenu(R.menu.detail);
    // Get a support ActionBar corresponding to this toolbar
    ActionBar ab = getSupportActionBar();

    // Enable the Up button
    if (ab != null)
      ab.setDisplayHomeAsUpEnabled(true);
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.detail, menu);

    favorites_button = menu.findItem(R.id.action_favorite);
    seen_button = menu.findItem(R.id.action_seen);
    playlist_button = menu.findItem(R.id.action_add_to_playlist);

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {

    updateTaskbar();

    return super.onPrepareOptionsMenu(menu);
  }

  private void updateTaskbar() {
    if (getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
        MovieContract.MovieEntry.COLUMN_TMDB_ID + "=" + mMovie.id + " AND " +
        MovieContract.MovieEntry.COLUMN_FAVORITE + "= 1", null, null).getCount() > 0) {
      favorites_button.setIcon(R.drawable.ic_star_yellow_24dp);
      favorite = true;
    } else {
      favorites_button.setIcon(R.drawable.ic_star_border_black_24dp);
      favorite = false;
    }
    if (getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
        MovieContract.MovieEntry.COLUMN_TMDB_ID + "=" + mMovie.id + " AND " +
        MovieContract.MovieEntry.COLUMN_SEEN + "= 1", null, null).getCount() > 0) {
      seen_button.setIcon(R.drawable.ic_check_box_black_24dp);
      seen = true;
    } else {
      seen_button.setIcon(R.drawable.ic_check_box_outline_blank_black_24dp);
      seen = false;
    }
    if (getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
        MovieContract.MovieEntry.COLUMN_TMDB_ID + "=" + mMovie.id + " AND " +
        MovieContract.MovieEntry.COLUMN_PLAYLIST + "= 1", null, null).getCount() > 0) {
      playlist_button.setIcon(R.drawable.ic_not_interested_black_24dp);
      playlist = true;
    } else {
      playlist_button.setIcon(R.drawable.ic_playlist_add_black_24dp);
      playlist = false;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int item_id = item.getItemId();
    ContentValues cv = new ContentValues();

    cv.put(MovieContract.MovieEntry.COLUMN_TMDB_ID, mMovie.id);
    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.title);
    cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, mMovie.synopsis);
    cv.put(MovieContract.MovieEntry.COLUMN_IMAGE_URI, mMovie.poster_image.toString());
    cv.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.user_rating);
    cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY, mMovie.popularity);
    cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, mMovie.synopsis);
    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.release_date);

    if (item_id == R.id.action_favorite) {
      if (favorite) {
        cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 0);
        Toast.makeText(this, "Removed from favorite", Toast.LENGTH_SHORT).show();
      } else {
        cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
        Toast.makeText(this, "Added to favorite", Toast.LENGTH_SHORT).show();
      }
    }
    if (item_id == R.id.action_seen) {
      if (seen) {
        cv.put(MovieContract.MovieEntry.COLUMN_SEEN, 0);
        Toast.makeText(this, "Removed from seen", Toast.LENGTH_SHORT).show();
      } else {
        cv.put(MovieContract.MovieEntry.COLUMN_SEEN, 1);
        Toast.makeText(this, "Added to seen", Toast.LENGTH_SHORT).show();
      }
    }
    if (item_id == R.id.action_add_to_playlist) {
      if (playlist) {
        cv.put(MovieContract.MovieEntry.COLUMN_PLAYLIST, 0);
        Toast.makeText(this, "Removed from playlist", Toast.LENGTH_SHORT).show();
      } else {
        cv.put(MovieContract.MovieEntry.COLUMN_PLAYLIST, 1);
        Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
      }
    }

    if (getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
        MovieContract.MovieEntry.COLUMN_TMDB_ID + "=" + mMovie.id, null, null).getCount() > 0) {
      getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, cv,
          MovieContract.MovieEntry.COLUMN_TMDB_ID + "=" + mMovie.id, null);
    } else {
      getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
    }
    updateTaskbar();
    return super.onOptionsItemSelected(item);
  }
}
