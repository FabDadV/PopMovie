package com.ex.popmovie.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ex.popmovie.R;
import com.ex.popmovie.ListFragment;
import com.ex.popmovie.data.Movie;
import com.squareup.picasso.Picasso;

// Creates a RecyclerView Adapter:
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = " ***RV: ";
    private static final String MOVIE_URL = "http://image.tmdb.org/t/p/w342";
    public Movie[] movieList;
    // An on-click handler that we've defined to make it easy for an Activity to interface with our RecyclerView
    private final RecyclerViewAdapterOnClickHandler mClickHandler;
    // The interface that receives onClick messages.
    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(int position);
    }
    /* Creates a RecyclerViewAdapter.
     * @param clickHandler The on-click handler for this adapter. This single handler is called when an item is clicked.
     */
    public RecyclerViewAdapter(RecyclerViewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }
    /**
     * Cache of the children views for a list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final ImageView ivPoster;
// Create a constructor for this class that accepts a View as a parameter
// Call super(itemView)
// Using itemView.findViewById, get a reference to these layout's TextViews and save it.
        ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_small_poster);
            itemView.setOnClickListener(this);
        }
        // This gets called by the child views during a click. @param v The View that was clicked
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
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
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false; // don't attach to parent immediately
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ViewHolder(view);
    }
    // Override onBindViewHolder
    // Set the text of the TextView to data for this list item's position
    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display data
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(position > 1) ListFragment.markPosition = position-2;
        Movie dataMovie = movieList[position];
        String posterUrl = MOVIE_URL + dataMovie.getPosterPath();
        Picasso.with(viewHolder.itemView.getContext())
                .load(posterUrl)
                .placeholder(R.drawable.the_movie_db)
                .error(R.drawable.no_poster)
                .into(viewHolder.ivPoster);
    }
    // Override getItemCount
    // Return 0 if mList is null, or the size of mList if it is not null
    /**
     * This method simply returns the number of items to display.
     * It is used behind the scenes to help layout our Views and for animations.
     * @return The number of items available in our data
     */
    @Override
    public int getItemCount() {
        if (movieList == null) return 0;
        return movieList.length;
    }
    /**
     * This method is used to set the response data on a DataAdapter if we've already created one.
     * This is handy when we get new data from the web but don't want to create a new RecyclerViewAdapter to display it.
     *   The new data to be displayed.
     */
    // Create a setList method that saves the movies to mList
    public void setList(Movie[] movies) {
        movieList = movies;
        notifyDataSetChanged(); // After you save movieList, call notifyDataSetChanged
    }
}