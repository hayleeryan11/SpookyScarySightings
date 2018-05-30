package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;
import edu.tacoma.uw.css.haylee11.spookyboiz.data.SightingsDB;


/**
 * Fragment for the details of a sighting. Opened when user clicks on
 * monster in list view.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class SightingDetailFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * The current sighting selected for details
     */
    public static final String SIGHTING_SELECTED = "sighting_selected";

    /**
     * The URL used to delete a sighting
     */
    public static final String DELETE_URL = "http://spookyscarysightings.000webhostapp.com/deleteSighting.php?id=";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Username of sighting
    private TextView mUsername;

    //Date of sighting
    private TextView mDate;

    //Time of sighting
    private TextView mTime;

    //City where sighting was
    private TextView mCity;

    //State where sighting was
    private TextView mState;

    //Monster sighted
    private TextView mMonster;

    //Description of sighting
    private TextView mDesc;

    private Button mDelete;

    private TextView mUserText;

    private int mDeleteId;

    private int mToastFlag;

    SightingDetailFragment mThat;

    //Listener for fragment interaction
    private OnFragmentInteractionListener mListener;

    /**
     * Required empty constructor
     */
    public SightingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SightingDetailFragment.
     */
    public static SightingDetailFragment newInstance(String param1, String param2) {
        SightingDetailFragment fragment = new SightingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * When the fragment is create, this instantiates the view. In this case,
     * set the values of the TextViews
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sighting_detail, container, false);

        getActivity().setTitle("Sighting Details");

        mThat = this;
        //Assign values to TextView
        mUsername = (TextView) v.findViewById(R.id.username_input);
        mMonster = (TextView) v.findViewById(R.id.monster);
        mCity = (TextView) v.findViewById(R.id.city_state);
        mDate = (TextView) v.findViewById(R.id.date);
        mTime = (TextView) v.findViewById(R.id.time);
        mDesc = (TextView) v.findViewById(R.id.description);

        mDelete = (Button) v.findViewById(R.id.delete);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = urlBuilder();
                DeleteSightingTask sightAsyncTask = new DeleteSightingTask();
                sightAsyncTask.execute(new String[]{url});


            }
        });
        
        mUserText = (TextView) v.findViewById(R.id.username_text);

        return v;
    }

    /**
     * Sets the text of the TextViews in the layout to the details
     * @param sight The sighting the user is viewing the details of
     */
    public void updateView(Sighting sight) {
        if (sight != null) {

            mDeleteId = sight.getmId();

            mUsername.setText(sight.getmUsername());
            mCity.setText(sight.getmCity() + ", " + sight.getmState());
            mDate.setText(sight.getmDate());
            mTime.setText(sight.getmTime());
            mMonster.setText(sight.getmMonster());
            mDesc.setText(sight.getmDesc());

            if (sight.getmUserFlag() == 1) {
                mDelete.setVisibility(View.VISIBLE);
                mUserText.setVisibility(View.INVISIBLE);
                mUsername.setVisibility(View.INVISIBLE);
            } else {
                mDelete.setVisibility(View.INVISIBLE);
                mUserText.setVisibility(View.VISIBLE);
                mUsername.setVisibility(View.VISIBLE);
            }


        }

    }

    /**
     * Builds URl to delete a sighting
     * @return The URL
     */
    public String urlBuilder() {

        StringBuilder sb = new StringBuilder(DELETE_URL);
        try {
            sb.append(URLEncoder.encode(Integer.toString(mDeleteId), "UTF-8"));
        } catch(Exception e) {
            Toast.makeText(getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }


        return sb.toString();
    }

    /**
     * When the fragment is attached to the app, this instantiates the listener
     * @param context The context the fragment is in
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * Decides what happens when the app is resumed. In this care, the detail page
     * is updated
     */
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if(args != null) {
            updateView((Sighting)args.getSerializable(SIGHTING_SELECTED));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Inner class that extends AsynchTask. This class handles the retrieval of a sighting
     * and gets data from the database. This handles all the background
     * work that has to do with data sending in regards to report posting
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class DeleteSightingTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to download the list of sightings, Reason: " + e.getMessage();
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getActivity(), "Sighting Deleted!", Toast.LENGTH_SHORT)
                            .show();

                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Error! Sighting not deleted", Toast.LENGTH_SHORT)
                            .show();

                    getFragmentManager().popBackStack();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
