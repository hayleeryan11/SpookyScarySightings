package edu.tacoma.uw.css.haylee11.spookyscarysightings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyscarysightings.MonsterNotify.MonsterNotifyContent;
import edu.tacoma.uw.css.haylee11.spookyscarysightings.MonsterNotifyFragment.OnListFragmentInteractionListener;
import edu.tacoma.uw.css.haylee11.spookyscarysightings.MonsterNotify.MonsterNotifyContent.MonsterNotifyItem;

import java.util.List;


public class MyMonsterNotifyRecyclerViewAdapter extends RecyclerView.Adapter<MyMonsterNotifyRecyclerViewAdapter.ViewHolder> {

    private final List<MonsterNotifyContent.MonsterNotifyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMonsterNotifyRecyclerViewAdapter(List<MonsterNotifyContent.MonsterNotifyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_monsternotify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public MonsterNotifyContent.MonsterNotifyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
