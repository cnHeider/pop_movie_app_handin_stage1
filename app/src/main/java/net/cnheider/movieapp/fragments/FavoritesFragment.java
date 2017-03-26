package net.cnheider.movieapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.cnheider.movieapp.MovieDetailActivity;
import net.cnheider.movieapp.R;
import net.cnheider.movieapp.data.FavoritesCursorAdapter;
import net.cnheider.movieapp.data.movie.Movie;
import net.cnheider.movieapp.data.movie.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by heider on 27/01/17.
 */

public class FavoritesFragment extends Fragment implements FavoritesCursorAdapter.FavoritesCursorAdapterOnClickHandler {
  @BindView(R.id.favorites_recycler_view)
  RecyclerView mRecyclerView;
  RecyclerView.LayoutManager mLayoutManager;
  FavoritesCursorAdapter mAdapter;
  Context mContext;
  private Unbinder unbinder;

  public FavoritesFragment() {
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_favorites, container, false);

    unbinder = ButterKnife.bind(this, view);

    mRecyclerView.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(view.getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);

    Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
        MovieContract.MovieEntry.COLUMN_FAVORITE + " = 1", null, null);
    mAdapter = new FavoritesCursorAdapter(mContext, this);
    mAdapter.swapCursor(movieCursor);
    mRecyclerView.setAdapter(mAdapter);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
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

  @Override
  public void onClick(Movie movie) {
    Toast.makeText(mContext, movie.toString(), Toast.LENGTH_SHORT).show();
    Intent intentToStartDetailActivity = new Intent(mContext, MovieDetailActivity.class);
    intentToStartDetailActivity.putExtra(MovieDetailActivity.EXTRA_MOVIE, (Parcelable) movie);
    startActivity(intentToStartDetailActivity);
  }
}
