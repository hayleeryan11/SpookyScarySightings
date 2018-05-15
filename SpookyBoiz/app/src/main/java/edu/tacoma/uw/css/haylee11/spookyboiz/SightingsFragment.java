package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

/**
 * A fragment representing a list of Sightings
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class SightingsFragment extends Fragment {

    /**
     * Column count of list
     */
    private static final String ARG_COLUMN_COUNT = "column-count";

    /**
     * Tag for debugging
     */
    private static final String TAG = "SightingsList";

    /**
     * URL to send the command to retrieve sightings.
     */
    private static final String COURSE_URL = "http://spookyscarysightings.000webhostapp.com/list.php?cmd=sightings";

    //Column count field
    private int mColumnCount = 1;

    //List of Sightings to display
    private List<Sighting> mSightingList;

    //Listener to handle fragment interaction
    private OnListFragmentInteractionListener mListener;

    //Recyclerview that allows scolling
    private RecyclerView mRecyclerView;

    SharedPreferences mSharedPref;

    private int mFlag;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment.
     */
    public SightingsFragment() {

    }

    public SightingsFragment(int flag) {
        mFlag = flag;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount The number of columns in list
     * @return A new instance of fragment NotifySettingsFragment.
     */
    public static SightingsFragment newInstance(int columnCount) {
        SightingsFragment fragment = new SightingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * When the fragment is created, this method instantiates it
     * @param savedInstanceState The saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * When the fragment is create, this instantiates the view. Also instantiates the
     * RecyclerView and calls the AsyncTask
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sightings_list, container, false);

        mSharedPref = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //Starts AsyncTask and passes URL
            SightingsTask sightAsyncTask = new SightingsTask();
            sightAsyncTask.execute(new String[]{COURSE_URL});
        }
        return view;
    }

    /**
     * When the fragment is attached to the app, this instantiates the listener
     * @param context The context the fragment is in
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Handles when the fragment is detached, nullifying the listener
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Sighting item);
    }

    /**
     * Inner class that extends AsynchTask. This class handles the retrieval of a sighting
     * and gets data from the database. This handles all the background
     * work that has to do with data sending in regards to report posting
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class SightingsTask extends AsyncTask<String, Void, String> {

        /**
         * Overrides onPreExecute. Performs super task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Creates a URL connection to which we can send our URL carrying the command
         * to get data from the database. This does all work in the background for the user when
         * viewing sightings.
         * @param urls The URLs to be sent through the connection that hold the information
         *             to be passed to the database
         * @return The successful or failed result of connecting with the URL
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * After the background work has been executed, the result comes into this method
         * to be read. From there, we determine what to do (has it succeeded? Failed? Is
         * the data wrong?)
         * @param result The result from doInBackground (If the insertion/retrieving was
         *               successful or not.
         */
        @Override
        protected void onPostExecute(String result) {



            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                mSightingList = Sighting.parseCourseJSON(result, mFlag, mSharedPref);
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!mSightingList.isEmpty()) {
                mRecyclerView.setAdapter(new MySightingsRecyclerViewAdapter(mSightingList, mListener));
            }
        }
    }
}
