/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.cnheider.movieapp.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.cnheider.movieapp.R;
import net.cnheider.movieapp.data.movie.Movie;
import net.cnheider.movieapp.data.movie.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * This CustomCursorAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class FavoritesCursorAdapter extends RecyclerView.Adapter<FavoritesCursorAdapter.FavoritesViewHolder> {

  private final FavoritesCursorAdapterOnClickHandler mClickHandler;
  // Class variables for the Cursor that holds task data and the Context
  private Cursor mCursor;
  private Context mContext;


  /**
   * Constructor for the CustomCursorAdapter that initializes the Context.
   *
   * @param mContext the current Context
   */
  public FavoritesCursorAdapter(Context mContext, FavoritesCursorAdapterOnClickHandler clickHandler) {
    this.mContext = mContext;
    mClickHandler = clickHandler;
  }

  /**
   * When data changes and a re-query occurs, this function swaps the old Cursor
   * with a newly updated Cursor (Cursor c) that is passed in.
   */
  public Cursor swapCursor(Cursor c) {
    // check if this cursor is the same as the previous cursor (mCursor)
    if (mCursor == c) {
      return null; // bc nothing has changed
    }
    Cursor temp = mCursor;
    this.mCursor = c; // new cursor value assigned

    //check if this is a valid cursor, then update the cursor
    if (c != null) {
      this.notifyDataSetChanged();
    }
    return temp;
  }

  public interface FavoritesCursorAdapterOnClickHandler {
    void onClick(Movie movie);
  }

  class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.movie_image)
    ImageView posterView;
    @BindView(R.id.movie_rating)
    TextView ratingView;
    @BindView(R.id.movie_title)
    TextView titleView;

    /**
     * Constructor for the TaskViewHolders.
     *
     * @param itemView The view inflated in onCreateViewHolder
     */
    public FavoritesViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int db_id = (int) v.getTag();
      mCursor.moveToPosition(db_id);
      Movie movie = new Movie(mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)), mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)), Uri.parse(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI))), mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)), mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)), mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI)), mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI)));
      mClickHandler.onClick(movie);
    }
  }

  /**
   * Called when ViewHolders are created to fill a RecyclerView.
   *
   * @return A new TaskViewHolder that holds the view for each task
   */
  @Override
  public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    // Inflate the task_layout to a view
    View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie, parent, false);

    return new FavoritesViewHolder(view);
  }

  /**
   * Called by the RecyclerView to display data at a specified position in the Cursor.
   *
   * @param holder   The ViewHolder to bind Cursor data to
   * @param position The position of the data in the Cursor
   */
  @Override
  public void onBindViewHolder(FavoritesViewHolder holder, int position) {

    // Indices for the _id, description, and priority columns
    int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
    int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE_URI);
    int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
    int ratingIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);

    mCursor.moveToPosition(position); // get to the right location in the cursor

    // Determine the values of the wanted data
    int db_id = position;
    String imageUri = mCursor.getString(imageIndex);
    String title = mCursor.getString(titleIndex);
    String rating = mCursor.getString(ratingIndex);

    Glide.with(mContext).load(Uri.parse(imageUri)).into(holder.posterView);
    holder.itemView.setTag(db_id);
    holder.titleView.setText(title);
    holder.ratingView.setText(rating);

  }

  /**
   * Returns the number of items to display.
   */
  @Override
  public int getItemCount() {
    if (mCursor == null) {
      return 0;
    }
    return mCursor.getCount();
  }
}