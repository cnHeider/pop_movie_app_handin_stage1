package net.cnheider.movieapp.data.review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.cnheider.movieapp.R;

import java.util.List;

/**
 * Created by heider on 22/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
  private static final String TAG = ReviewAdapter.class.getSimpleName();
  private final ReviewAdapter.ReviewAdapterOnClickHandler mClickHandler;
  private List<Review> mReviewDataset;
  private Context mContext;

  public ReviewAdapter(Context context, List<Review> myDataset, ReviewAdapter.ReviewAdapterOnClickHandler clickHandler) {
    mContext = context;
    mReviewDataset = myDataset;
    mClickHandler = clickHandler;
  }

  public void setReviewData(List<Review> reviewData) {
    mReviewDataset = reviewData;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface ReviewAdapterOnClickHandler {
    void onClick(Review review);
  }

  public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView review_author_tv;
    public final TextView review_content_tv;

    public ReviewAdapterViewHolder(View view) {
      super(view);
      review_author_tv = (TextView) view.findViewById(R.id.review_author);
      review_content_tv = (TextView) view.findViewById(R.id.review_content);
    }

    @Override
    public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      Review trailer = mReviewDataset.get(adapterPosition);
      mClickHandler.onClick(trailer);
    }
  }

  @Override
  public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
    ReviewAdapter.ReviewAdapterViewHolder viewHolder = new ReviewAdapter.ReviewAdapterViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder view_holder, int position) {
    final Review review = mReviewDataset.get(position);
    if (review.author != null)
      view_holder.review_author_tv.setText(review.author);
    if (review.content != null)
      view_holder.review_content_tv.setText(review.content);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public int getItemCount() {
    return mReviewDataset.size();
  }
}
