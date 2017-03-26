package net.cnheider.movieapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import net.cnheider.movieapp.MovieDetailActivity;
import net.cnheider.movieapp.R;
import net.cnheider.movieapp.data.FavoritesCursorAdapter;
import net.cnheider.movieapp.data.movie.Movie;
import net.cnheider.movieapp.data.movie.MovieAdapter;
import net.cnheider.movieapp.data.movie.MovieContract;
import net.cnheider.movieapp.utilities.NetworkUtilities;
import net.cnheider.movieapp.utilities.SortingUtilities;
import net.cnheider.movieapp.utilities.TMDBUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DiscoverFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {
  private static final String TAG = DiscoverFragment.class.getSimpleName();
  Context mContext;
  RecyclerView.LayoutManager mLayoutManager;
  MovieAdapter mAdapter;
  ArrayList<Movie> mPopularMovies;
  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;
  @BindView(R.id.progressBar)
  ProgressBar mLoadingIndicator;
  @BindView(R.id.sort_by_action_button)
  FloatingActionButton mSortByActionButton;
  @BindView(R.id.sort_by_layout)
  LinearLayout mSortByLayout;
  @BindView(R.id.sort_by_spinner)
  Spinner mSortBySpinner;
  private int mShortAnimationDuration;
  private int mCurrentSortSelected;
  private Unbinder unbinder;
  private Parcelable mLayoutState;
  private static final String LAYOUT_STATE_KEY = "layout_state";
  private static final String POP_MOVIE_KEY ="movies";

  public DiscoverFragment() {
    // Required empty public constructor
  }

  public static DiscoverFragment newInstance() {
    DiscoverFragment fragment = new DiscoverFragment();
    return fragment;
  }

  private void applyLinearLayoutManager() {
    mLayoutManager = new LinearLayoutManager(mContext);
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  private void applyGridLayoutManager() {
    mLayoutManager = new GridLayoutManager(mContext, calculateNoOfColumns(mContext), GridLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  public static int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int) (dpWidth / 180);
    return noOfColumns >= 2 ? noOfColumns : 2;
  }

  public void crossfade() {
    mSortByLayout.setAlpha(0f);
    mSortByLayout.setVisibility(View.VISIBLE);

    mSortByLayout.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);

    mSortByActionButton.animate().alpha(0f).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mSortByActionButton.setVisibility(View.INVISIBLE);
        mSortBySpinner.performClick();
      }
    });
  }

  public void crossfadeBack() {
    mSortByActionButton.setAlpha(0f);
    mSortByActionButton.setVisibility(View.VISIBLE);

    mSortByActionButton.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);

    mSortByLayout.animate().alpha(0f).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mSortByLayout.setVisibility(View.INVISIBLE);
      }
    });
  }

  public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
      URL MovieURL;
      if (params[0].equals("top_rated"))
        MovieURL = NetworkUtilities.getTopRatedURL();
      else
        MovieURL = NetworkUtilities.getPopularURL();

      try {
        String jsonResponse = NetworkUtilities.getResponseFromHttpUrl(MovieURL);
        Movie[] movieData = TMDBUtilities.getMoviesFromJson(jsonResponse);
        return new ArrayList<Movie>(Arrays.asList(movieData));

      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      if (mLoadingIndicator != null && mLoadingIndicator.getVisibility()==View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieData) {
      if (mLoadingIndicator != null && mLoadingIndicator.getVisibility()==View.INVISIBLE)
      if (movieData != null) {
        mPopularMovies = movieData;
        mAdapter.setMovieData(mPopularMovies);
      } else {
        Toast.makeText(mContext, "An error occurred while fetching movies", Toast.LENGTH_LONG).show();
      }
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public void onPause() {
    super.onPause();

    mLayoutState = mLayoutManager.onSaveInstanceState();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_discover, container, false);

    unbinder = ButterKnife.bind(this, view);

    mSortByActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mAdapter.setMovieData(mPopularMovies);
        mSortBySpinner.setVisibility(View.VISIBLE);
        //crossfade();
        slideIn();
      }
    });

    mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array
        .pref_sort_by_values, android.R
        .layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSortBySpinner.setAdapter(adapter);

    mCurrentSortSelected = -1; // Initially none is selected
    mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
          case 0:
            new FetchMoviesTask().execute("popular");
            slideOut();
            break;
          case 1:
            new FetchMoviesTask().execute("top_rated");
            slideOut();
            break;
          case 2:
            ArrayList<Movie> arrayList = new ArrayList<Movie>();
            Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
                MovieContract.MovieEntry.COLUMN_FAVORITE + " = 1", null, null);
            movieCursor.moveToFirst();
            while(!movieCursor.isAfterLast()) {
              Movie movie = new Movie(movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TMDB_ID)), movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)), Uri.parse(movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI))), movieCursor.getDouble(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)), movieCursor.getDouble(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)), movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS)), movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));

              arrayList.add(movie); //add the item
              movieCursor.moveToNext();
            }
            mPopularMovies = arrayList;
            mAdapter.setMovieData(mPopularMovies);
            break;
          case 3:
            mPopularMovies = SortingUtilities.sortByReleaseDate(mPopularMovies, false);
            slideOut();
            mAdapter.setMovieData(mPopularMovies);
            break;
          case 4:
            mPopularMovies = SortingUtilities.sortByTitle(mPopularMovies, true);
            slideOut();
            mAdapter.setMovieData(mPopularMovies);
            break;
          default:
            return;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        //crossfadeBack();
        slideOut();
      }
    });

    mRecyclerView.setHasFixedSize(true);

    applyStaggeredGridLayoutManager();

    if (savedInstanceState != null && savedInstanceState.containsKey(POP_MOVIE_KEY)) {
      mPopularMovies = savedInstanceState.getParcelableArrayList(POP_MOVIE_KEY);
    }else {
      mPopularMovies = new ArrayList<>();

      if (NetworkUtilities.isOnline(mContext)) {
        new FetchMoviesTask().execute("popular");
      }
    }
    mAdapter = new MovieAdapter(container.getContext(), mPopularMovies, this);
    mRecyclerView.setAdapter(mAdapter);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (mLayoutState != null) {
      mLayoutManager.onRestoreInstanceState(mLayoutState);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelableArrayList(POP_MOVIE_KEY, mPopularMovies);
    mLayoutState = mLayoutManager.onSaveInstanceState();
    outState.putParcelable(LAYOUT_STATE_KEY, mLayoutState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void applyStaggeredGridLayoutManager() {
    int num_of_columns = calculateNoOfColumns(mContext);
    mLayoutManager = new StaggeredGridLayoutManager(num_of_columns, StaggeredGridLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(mLayoutManager);
  }

  public void slideIn() {
    mSortByLayout.setVisibility(View.VISIBLE);
    mSortByLayout.setX(mRecyclerView.getWidth());
    int margin_left_side = 32;
    mSortByLayout.animate().x(margin_left_side).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mSortByActionButton.setVisibility(View.INVISIBLE);
        mSortBySpinner.performClick();
      }
    });
  }

  public void slideOut() {
    mSortByLayout.animate().x(mRecyclerView.getWidth()).setDuration(mShortAnimationDuration).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mSortByLayout.setVisibility(View.INVISIBLE);
        mSortByActionButton.setVisibility(View.VISIBLE);
      }
    });
  }

  @Override
  public void onClick(Movie movie) {
    Intent intentToStartDetailActivity = new Intent(mContext, MovieDetailActivity.class);
    intentToStartDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, (Parcelable) movie);
    startActivity(intentToStartDetailActivity);
  }
}
