package net.cnheider.movieapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cnheider.movieapp.R;

/**
 * Created by heider on 27/01/17.
 */

public class PlaylistFragment extends Fragment {
  public static final String ARG_MOVIE_ID = "ARG_MOVIE_ID";
  Context mContext;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_seen, container, false);

    return view;
  }
}
