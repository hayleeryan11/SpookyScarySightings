package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.MonsterFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Monster} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class MyMonsterRecyclerViewAdapter extends RecyclerView.Adapter<MyMonsterRecyclerViewAdapter.ViewHolder> {

    //List of monsters to be displayed
    private final List<Monster> mValues;

    //Listener for fragment interaction
    private final OnListFragmentInteractionListener mListener;

    /**
     * Constructor instantiates listener and list of monsters
     * @param items List of monsters in view
     * @param listener Listener of the fragment
     */
    public MyMonsterRecyclerViewAdapter(List<Monster> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Called in the holder view is created. Inflates the layout
     * @param parent The parent ViewGroup
     * @param viewType The type of view
     * @return The new ViewHolder for the list
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_monster, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Sets up the values displayed in the list and handles click of the view
     * @param holder The view holder
     * @param position The position of the list
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //Displays monster name
        holder.mIdView.setText(mValues.get(position).getmMonster());
        //Displays when monster was last seen
        holder.mContentView.setText(mValues.get(position).getmLastSeen());

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
        public Monster mItem;

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
