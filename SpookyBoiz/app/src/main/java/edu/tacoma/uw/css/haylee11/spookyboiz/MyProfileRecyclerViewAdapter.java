package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyboiz.OtherProfilesFragment.OnListFragmentInteractionListener;
import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;

import java.util.List;

public class MyProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyProfileRecyclerViewAdapter.ViewHolder> {

    private final List<Profile> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyProfileRecyclerViewAdapter(List<Profile> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_other_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //Displays monster name
        holder.mIdView.setText(mValues.get(position).getmUsername());
        //Displays when monster was last seen
        holder.mContentView.setText(mValues.get(position).getmFName() + " " +  mValues.get(position).getmLName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Gives the number of items in list
     * @return The number of items in list
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * View Holder class creates a ViewHolder object that describes an item view
     * and metadata about its place within the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Profile mItem;

        /**
         * Constructor for ViewHolder. Instantiates fields
         * @param view The view its holding
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        /**
         * Gives the string of the view holder
         * @return ViewHolder in String form
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
