package com.ex.popmovie.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex.popmovie.R;
import com.ex.popmovie.data.Movie;
import com.squareup.picasso.Picasso;

// Creates a Review Adapter:
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private static final String BASIC_URL = "https://api.themoviedb.org/3/movie/";
    public Review[] reviews;
    /**
     * Cache of the children views for a list item.
     */
    public class ReviewHolder extends RecyclerView.ViewHolder{
        private final TextView tvAuthor;
        private final TextView tvContent;
// Create a constructor for this class that accepts a View as a parameter
// Call super(itemView)
// Using itemView.findViewById, get a reference to these layout's TextViews and save it.
        ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_small_poster);
            itemView.setOnClickListener(this);
        }
    }
    // Override onCreateViewHolder
    // Within onCreateViewHolder, inflate the list item xml into a view
    // Within onCreateViewHolder, return a new ViewHolder with the above view passed in as a parameter
    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false; // don't attach to parent immediately
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewHolder(view);
    }
    // Override onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Movie movie = reviewList.get(position);
        holder.reviewAuthor.setText(movie.getAuthor());
        holder.reviewContent.setText(movie.getContent());
    }
    // Override getItemCount
    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    // Create a setList method that saves the reviews to mList
    public void setList(Movie[] movies) {
        movieList = movies;
        notifyDataSetChanged(); // After you save movieList, call notifyDataSetChanged
    }
}