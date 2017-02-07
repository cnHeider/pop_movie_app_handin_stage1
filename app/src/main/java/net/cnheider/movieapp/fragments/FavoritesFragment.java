package net.cnheider.movieapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.cnheider.movieapp.R;
import net.cnheider.movieapp.movie.Movie;
import net.cnheider.movieapp.movie.MovieAdapter;

/**
 * Created by heider on 27/01/17.
 */

public class FavoritesFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {
  RecyclerView mRecyclerView;
  RecyclerView.LayoutManager mLayoutManager;
  RecyclerView.Adapter mAdapter;
  Context mContext;

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

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

    mRecyclerView.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(view.getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);

    //Cursor cursor = MovieProvider.query(null, null, null, null, null, null);
    //mAdapter = new MovieAdapter(container.getContext(), cursor);
    mRecyclerView.setAdapter(mAdapter);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onClick(Movie movie) {
    Toast.makeText(mContext, movie.toString(), Toast.LENGTH_SHORT).show();
  }
}
