package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
//import edu.tacoma.uw.css.haylee11.spookyboiz.data.MonsterDB;

/**
 * Fragment for the list of monsters
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class MonsterFragment extends Fragment {

    /**
     * Column count of list
     */
    private static final String ARG_COLUMN_COUNT = "column-count";

    /**
     * Tag for debugging
     */
    private static final String TAG = "MonsterList";

    /**
     * URL to send the command to retrieve monsters.
     */
    private static final String ALL_MONSTER_URL = "http://spookyscarysightings.000webhostapp.com/listMonster.php?cmd=monsters";

    /**
     * URL used to search the list of monsters based on user query
     */
    private static final String SEARCH_MONSTER_URL = "http://spookyscarysightings.000webhostapp.com/listMonster.php?cmd=search";

    //Column count field
    private int mColumnCount = 1;

    //Listener to handle fragment interaction
    private OnListFragmentInteractionListener mListener;

    //Recyclerview that allows scolling
    private RecyclerView mRecyclerView;

    private EditText mSearch;

    private Button mGo;

    //List of monsters
    private List<Monster> mMonsterList;

    //Local monster database
//    private MonsterDB mMonsterDB;

    private View mLoadingView;
    private int mLongAnimationDuration;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MonsterFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount The number of columns in list
     * @return A new instance of fragment MonsterFragment.
     */
    public static MonsterFragment newInstance(int columnCount) {
        MonsterFragment fragment = new MonsterFragment();
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

        setHasOptionsMenu(true);
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
        View view = inflater.inflate(R.layout.fragment_monster_list, container, false);

        mLoadingView = getActivity().findViewById(R.id.loading_spinner);

        //Retrieve and cache the system's defaul "short" animation time
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        getActivity().setTitle("Monsters");

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                MonsterTask monsterTask = new MonsterTask();
                monsterTask.execute(new String[]{ALL_MONSTER_URL});
            } else {
//                Toast.makeText(view.getContext(),
//                        "No network connection available. Displaying locally stored data.",
//                        Toast.LENGTH_SHORT).show();
//
//                if (mMonsterDB == null) {
//                    mMonsterDB = new MonsterDB(getActivity());
//                }
//                if (mMonsterList == null) {
//                    mMonsterList = mMonsterDB.rgetMonster();
//                }
//
//                mRecyclerView.setAdapter(new MyMonsterRecyclerViewAdapter(mMonsterList, mListener));
            }

        }
        return view;
    }

    /**
     * When view created, we need to set up the menu, in this case the search menu
     * @param menu The search menu
     * @param inflater The inflater for the menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * When a menu item is selected, carry out these actions
     * @param item The menu item selected
     * @return If an item is selected or not
     */
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
     * Class that creates a pop up dialog for searching the list of monsters. This includes taking
     * the user's query and matching it against the database.
     *
     * @author Haylee Ryan, Matthew Frazier, Kai Stansfield
     */
    public static class SearchDialogFragment extends DialogFragment {

        MonsterFragment mMonster;
        List<Monster> mMonsterList;
        EditText mQuery;
        Spinner mKey;

        /**
         * Constructs a new Monster Fragment to use for method calls
         */
        public SearchDialogFragment() {
            mMonster = new MonsterFragment();
        }

        /**
         * Constructs new local Monster Fragment with the given fragment
         * @param mons The monster fragment instance to reference
         */
        @SuppressLint("ValidFragment")
        public SearchDialogFragment(MonsterFragment mons) {
            mMonster = mons;
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

            //Set spinner and edittext
            mKey = (Spinner) v.findViewById(R.id.spinner);
            mQuery = (EditText) v.findViewById(R.id.search);
            mQuery.setHint(R.string.monster);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.monster_search, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mKey.setAdapter(adapter);

            builder.setView(v);

            //About message
            builder.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String key = mKey.getSelectedItem().toString();
                    String query = mQuery.getText().toString();


                    MonsterFragment.SearchDialogFragment.SearchTask monsAsyncTask = new MonsterFragment.SearchDialogFragment.SearchTask();
                    monsAsyncTask.execute(new String[]{urlBuilder(key, query)});
                }
            });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        /**
         * Builds URl to search monster table with
         * @param key The serach field the user is using
         * @param query The result they are looking for
         * @return The URL
         */
        public String urlBuilder(String key, String query) {

            StringBuilder sb = new StringBuilder(SEARCH_MONSTER_URL);
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

        /**
         * AsyncTask that retrieves data based on the user's query in the Monster table
         *
         * @author Haylee Ryan, Matthew Frazier, Kai Stansfield
         */
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
                    Toast.makeText(mMonster.getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                try {
                    mMonsterList = Monster.parseMonsterJSON(result);
                    Toast.makeText(mMonster.getActivity().getApplicationContext(), "Showing your search results", Toast.LENGTH_SHORT)
                            .show();
                } catch (JSONException e) {
                    Toast.makeText(mMonster.getActivity().getApplicationContext(), "No results", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (!mMonsterList.isEmpty()) {
                    mMonster.crossfade();
                    mMonster.mRecyclerView.setAdapter(new MyMonsterRecyclerViewAdapter(mMonsterList, mMonster.mListener));

                }
            }
        }
    }

    /**
     * Creates a crossfade animation
     */
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
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Monster item);
    }

    /**
     * Inner class that extends AsynchTask. This class handles the retrieval of a monster
     * and gets data from the database. This handles all the background
     * work that has to do with data sending in regards to report posting
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class MonsterTask extends AsyncTask<String, Void, String> {

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
                mMonsterList = Monster.parseMonsterJSON(result);

            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            //For data stroed locally using SQLite
            if (!mMonsterList.isEmpty()) {
//                if (mMonsterDB == null) {
//                    mMonsterDB = new MonsterDB(getActivity());
//                }
//
//                mMonsterDB.deleteMonsters();
//
//                for (int i = 0; i < mMonsterList.size(); i++) {
//                    Monster monster = mMonsterList.get(i);
//                    mMonsterDB.insertCourse(monster.getmId(),
//                            monster.getmMonster(),
//                            monster.getmDescription(),
//                            monster.getmLastSeen(),
//                            monster.getmLink());
//                }
                crossfade();
                mRecyclerView.setAdapter(new MyMonsterRecyclerViewAdapter(mMonsterList, mListener));
            }
        }
    }
}
