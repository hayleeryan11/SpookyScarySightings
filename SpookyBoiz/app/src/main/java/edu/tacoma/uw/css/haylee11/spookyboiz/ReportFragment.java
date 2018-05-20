package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.Date;


/**
 * Fragment for the report fragment. Allows users to log their sightings
 * with the necessary information.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class ReportFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * URL to add report values to to input into database
     */
    private final static String REPORT_URL = "http://spookyscarysightings.000webhostapp.com/addSighting.php?";

    /**
     * Tag for debugging
     */
    private static final String TAG = "ReportFragment";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Listener for fragment interaction
    private OnFragmentInteractionListener mListener;

    //Spinner containing monster values
    private Spinner mMonster;

    //EditText for date
    private EditText mDate;

    //EditText for time
    private EditText mTime;

    //EditText for city
    private EditText mCity;

    //EditText for state
    private EditText mState;

    //EditText for details
    private EditText mDetails;

    //Listener for adding a sighting to the list/database
    private SightingAddListener mSightListener;

    SharedPreferences mSharedPref;

    /**
     * Required empty constructor
     */
    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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
     * When the fragment is create, this instantiates the view. In this case, all
     * of the UI elements that hold the information we want in our URL.
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        String[] arr = {"wow"};
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter =  new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setOnItemSelectedListener(this);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        getActivity().setTitle("Report a Sighting");

        //Assigns values in Spinner/EditText fields to our class fields
        mMonster = (Spinner) v.findViewById(R.id.spinner);
        mDate = (EditText) v.findViewById(R.id.date);
        mTime = (EditText) v.findViewById(R.id.time);
        mCity = (EditText) v.findViewById(R.id.city);
        mState = (EditText) v.findViewById(R.id.state);
        mDetails = (EditText) v.findViewById(R.id.notes);


        mSharedPref =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

        //Assign "done" button to build URL and start AsyncTask
        Button addSightingButton = (Button) v.findViewById(R.id.add_button);
        addSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildSightingURL(v);
                mSightListener.addSighting(url);
            }
        });

        return v;
    }

    /**
     * When the fragment is attached to the app, this instantiates the listener
     * @param context The context the fragment is in
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SightingAddListener) {
            mSightListener = (SightingAddListener) context;
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
        mSightListener = null;
    }

    /**
     * Based on the user input into the sign in fields, this builds a URL that will
     * be sent to the database for inputting.
     * @param v The view the user is in
     * @return A string representing the URL created
     */
    private String buildSightingURL(View v) {
        StringBuilder sb = new StringBuilder(REPORT_URL);

        try {

            String id = Integer.toString(0);
            sb.append("&id=");
            sb.append(URLEncoder.encode(id, "UTF-8"));

            String user = mSharedPref.getString(getString(R.string.CURRENT_USER), null);
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            String monster = mMonster.getSelectedItem().toString();
            sb.append("&monster=");
            sb.append(URLEncoder.encode(monster, "UTF-8"));


            String datetime = mDate.getText().toString() + " " + mTime.getText().toString() + ":00";
            Log.i(TAG, datetime);
            sb.append("&date=");
            sb.append(URLEncoder.encode(datetime, "UTF-8"));


            String city = mCity.getText().toString();
            sb.append("&city=");
            sb.append(URLEncoder.encode(city, "UTF-8"));

            String state = mState.getText().toString();
            sb.append("&state=");
            sb.append(URLEncoder.encode(state, "UTF-8"));

            String details = mDetails.getText().toString();
            sb.append("&description=");
            sb.append(URLEncoder.encode(details, "UTF-8"));

            Log.i(TAG, sb.toString());
        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
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
     * Interface that provides way to begin AsyncTask
     */
    public interface SightingAddListener {
        //Begins AsyncTask
        public void addSighting(String url);
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
}
