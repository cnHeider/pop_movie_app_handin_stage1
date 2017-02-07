package net.cnheider.movieapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import net.cnheider.movieapp.movie.Movie;
import net.cnheider.movieapp.movie.MovieAdapter;
import net.cnheider.movieapp.utilities.NetworkUtilities;
import net.cnheider.movieapp.utilities.SortingUtilities;
import net.cnheider.movieapp.utilities.TMDBUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscoverFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {
  private static final String TAG = DiscoverFragment.class.getSimpleName();
  Context mContext;
  RecyclerView mRecyclerView;
  RecyclerView.LayoutManager mLayoutManager;
  MovieAdapter mAdapter;
  ProgressBar mLoadingIndicator;
  ArrayList<Movie> mPopularMovies;
  FloatingActionButton mSortByActionButton;
  Spinner mSortBySpinner;
  LinearLayout mSortByLayout;
  private int mShortAnimationDuration;
  private int mCurrentSortSelected;

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
    int num_of_columns = 2;
    mLayoutManager = new GridLayoutManager(mContext, num_of_columns, GridLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(mLayoutManager);
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
      mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movieData) {
      mLoadingIndicator.setVisibility(View.INVISIBLE);
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
    if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
    } else {
      mPopularMovies = savedInstanceState.getParcelableArrayList("movies");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_discover, container, false);

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    mLoadingIndicator = (ProgressBar) view.findViewById(R.id.progressBar);

    mSortByActionButton = (FloatingActionButton) view.findViewById(R.id.sort_by_action_button);
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

    mSortByLayout = (LinearLayout) view.findViewById(R.id.sort_by_layout);

    mSortBySpinner = (Spinner) view.findViewById(R.id.sort_by_spinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.sort_by_options, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSortBySpinner.setAdapter(adapter);

    mCurrentSortSelected = -1; // Initially none is selected
    mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: pos: " + position + ", id: " + id + " selected: " + mCurrentSortSelected);
        String item = parent.getItemAtPosition(position).toString();
        switch (item) {
          case "Popularity":
            new FetchMoviesTask().execute("popular");
            slideOut();
            break;
          case "User Rating":
            new FetchMoviesTask().execute("top_rated");
            slideOut();
            break;
          case "Release Date":
            mPopularMovies = SortingUtilities.sortByReleaseDate(mPopularMovies, false);
            slideOut();
            mAdapter.setMovieData(mPopularMovies);
            break;
          case "Title":
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

    mPopularMovies = new ArrayList<>();

    if (NetworkUtilities.isOnline(mContext)) {
      new FetchMoviesTask().execute("popular");
    }

    mAdapter = new MovieAdapter(container.getContext(), mPopularMovies, this);
    mRecyclerView.setAdapter(mAdapter);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList("movies", mPopularMovies);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void applyStaggeredGridLayoutManager() {
    int num_of_columns = 2;
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
    //Toast.makeText(mContext, movie.toString(), Toast.LENGTH_SHORT).show();
    Intent intentToStartDetailActivity = new Intent(mContext, MovieDetailActivity.class);
    intentToStartDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, (Parcelable) movie);
    startActivity(intentToStartDetailActivity);
  }
}
