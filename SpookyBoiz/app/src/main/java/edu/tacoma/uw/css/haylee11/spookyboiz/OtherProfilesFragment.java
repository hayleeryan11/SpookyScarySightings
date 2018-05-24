package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OtherProfilesFragment extends Fragment {

    /**
     * Column count of list
     */
    private static final String ARG_COLUMN_COUNT = "column-count";

    /**
     * Tag for debugging
     */
    private static final String TAG = "ProfileList";

    /**
     * URL to send the command to retrieve monsters.
     */
    private static final String PROFILE_URL = "http://spookyscarysightings.000webhostapp.com/listProfiles.php?cmd=profiles";
    private static final String SEARCH_PROFILE_URL = "http://spookyscarysightings.000webhostapp.com/listProfiles.php?cmd=search";

    //Column count field
    private int mColumnCount = 1;

    //Listener to handle fragment interaction
    private OtherProfilesFragment.OnListFragmentInteractionListener mListener;

    //Recyclerview that allows scolling
    private RecyclerView mRecyclerView;

    //List of monsters
    private List<Profile> mProfileList;

    private View mLoadingView;
    private int mLongAnimationDuration;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OtherProfilesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OtherProfilesFragment newInstance(int columnCount) {
        OtherProfilesFragment fragment = new OtherProfilesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);

        mLoadingView = getActivity().findViewById(R.id.loading_spinner);

        //Retrieve and cache the system's defaul "short" animation time
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        getActivity().setTitle("Hunters");

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            OtherProfilesFragment.ProfileTask profileAsyncTask = new OtherProfilesFragment.ProfileTask();
            profileAsyncTask.execute(new String[]{PROFILE_URL});
        }
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {           //if the preferences button has been pressed, open new fragment

            SearchDialogFragment newFragment = new SearchDialogFragment(this);
            newFragment.show(getActivity().getSupportFragmentManager(), "search");
        }

        return super.onOptionsItemSelected(item);
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static class SearchDialogFragment extends DialogFragment {

        OtherProfilesFragment mProf;
        List<Profile> mProfileList;
        EditText mQuery;
        Spinner mKey;

        public SearchDialogFragment() {
            mProf = new OtherProfilesFragment();
        }

        @SuppressLint("ValidFragment")
        public SearchDialogFragment(OtherProfilesFragment prof) {
            mProf = prof;
        }

        /**
         * When the button is pressed to create the dialog, this method is
         * called to instantiate the dialog and it's contents.
         * @param savedInstanceState The saved instance
         * @return The Dialog box to pop up
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View v = inflater.inflate(R.layout.search_dialog_layout, null);

            mKey = (Spinner) v.findViewById(R.id.spinner);
            mQuery = (EditText) v.findViewById(R.id.search);
            mQuery.setHint(R.string.profile);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.profile_search, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mKey.setAdapter(adapter);

            builder.setView(v);

            //About message
            builder.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String key;

                    if (mKey.getSelectedItem().toString().equals("First Name")) {
                        key = "f_name";
                    } else if (mKey.getSelectedItem().toString().equals("Last Name")) {
                        key = "l_name";
                    } else {
                        key = mKey.getSelectedItem().toString();
                    }
                    String query = mQuery.getText().toString();


                    OtherProfilesFragment.SearchDialogFragment.SearchTask profAsyncTask = new OtherProfilesFragment.SearchDialogFragment.SearchTask();
                    profAsyncTask.execute(new String[]{urlBuilder(key, query)});
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        public String urlBuilder(String key, String query) {

            StringBuilder sb = new StringBuilder(SEARCH_PROFILE_URL);
            try {
                sb.append("&key=");
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("&value=");
                sb.append(URLEncoder.encode(query, "UTF-8"));
            } catch(Exception e) {
                Toast.makeText(getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }


            return sb.toString();
        }

        /**
         * Class that creates the spinner object (drop down menu) for the
         * distances
         */
        public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

            /**
             * When an item is selected, do the following
             * @param parent Parent AdapterView
             * @param view View user is in
             * @param pos Position of item selected
             * @param id Id of the item selected
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            }

            /**
             * When nothing is selected, do the following
             * @param parent Parent AdapterView
             */
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        }

        private class SearchTask extends AsyncTask<String, Void, String> {

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
                    Toast.makeText(mProf.getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                try {
                    mProfileList = Profile.parseProfileListJSON(result);
                    Toast.makeText(mProf.getActivity().getApplicationContext(), "Showing your search results", Toast.LENGTH_SHORT)
                            .show();
                } catch (JSONException e) {
                    Toast.makeText(mProf.getActivity().getApplicationContext(),"No results", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!mProfileList.isEmpty()) {
                    mProf.crossfade();
                    mProf.mRecyclerView.setAdapter(new MyProfileRecyclerViewAdapter(mProfileList, mProf.mListener));

                }
            }
        }
    }

    private void crossfade() {
        // Animate the loading view to 0% opacity. After the animation ends, 
        // set its visibility to GONE as an optimization step (it won't 
        // participate in layout passes, etc.)
        mLoadingView.animate()
                .alpha(0f)
                .setDuration(mLongAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });
        //Set the content view to 0% opacity but visible, so that is it visible
        //but fully transparent during the animation
        mRecyclerView.setAlpha(0f);
        mRecyclerView.setVisibility(View.VISIBLE);

        //Animate the content view to 100% opacity, and clear any animation
        //listener set on the view
        mRecyclerView.animate()
                .alpha(1f)
                .setDuration(mLongAnimationDuration)
                .setListener(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Profile item);
    }


    /**
     * Inner class that extends AsynchTask. This class handles the retrieval of a monster
     * and gets data from the database. This handles all the background
     * work that has to do with data sending in regards to report posting
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class ProfileTask extends AsyncTask<String, Void, String> {

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
         * viewing monsters.
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
                    response = "Unable to download the list of profiles, Reason: " + e.getMessage();
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
                mProfileList = Profile.parseProfileListJSON(result);

            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!mProfileList.isEmpty()) {
                crossfade();
                mRecyclerView.setAdapter(new MyProfileRecyclerViewAdapter(mProfileList, mListener));
            }
        }
    }
}
