package net.cnheider.movieapp.data.trailer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.cnheider.movieapp.R;

import java.util.List;

/**
 * Created by heider on 22/03/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
  private static final String TAG = TrailerAdapter.class.getSimpleName();
  private final TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler;
  private List<Trailer> mTrailerDataset;
  private Context mContext;

  public TrailerAdapter(Context context, List<Trailer> myDataset, TrailerAdapter.TrailerAdapterOnClickHandler clickHandler) {
    mContext = context;
    mTrailerDataset = myDataset;
    mClickHandler = clickHandler;
  }

  public void setTrailerData(List<Trailer> trailerData) {
    mTrailerDataset = trailerData;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface TrailerAdapterOnClickHandler {
    void onClick(Trailer trailer);
  }

  public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final ImageButton trailer_play_button;
    public final TextView trailer_name;
    public final TextView trailer_resolution;

    public TrailerAdapterViewHolder(View view) {
      super(view);
      trailer_play_button = (ImageButton) view.findViewById(R.id.trailer_play_button);
      trailer_name = (TextView) view.findViewById(R.id.trailer_name);
      trailer_resolution = (TextView) view.findViewById(R.id.trailer_resolution);

      trailer_play_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      Trailer trailer = mTrailerDataset.get(adapterPosition);
      mClickHandler.onClick(trailer);
    }
  }

  @Override
  public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
    TrailerAdapter.TrailerAdapterViewHolder viewHolder = new TrailerAdapter.TrailerAdapterViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder view_holder, int position) {
    final Trailer trailer = mTrailerDataset.get(position);
    view_holder.trailer_name.setText(trailer.name);
    view_holder.trailer_resolution.setText(String.valueOf(trailer.size));
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public int getItemCount() {
    return mTrailerDataset.size();
  }
}
