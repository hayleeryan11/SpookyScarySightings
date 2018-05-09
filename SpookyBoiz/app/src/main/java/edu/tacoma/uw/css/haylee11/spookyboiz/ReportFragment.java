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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner mMonster;
    private EditText mDate;
    private EditText mTime;
    private EditText mCity;
    private EditText mState;
    private EditText mDetails;

    private int mSightCount;

    private final static String REPORT_URL = "http://spookyscarysightings.000webhostapp.com/addSighting.php?";

    private static final String TAG = "ReportFragment";

    private SightingAddListener mSightListener;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.monsters, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setOnItemSelectedListener(this);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        mMonster = (Spinner) v.findViewById(R.id.spinner);
        mDate = (EditText) v.findViewById(R.id.date);
        mTime = (EditText) v.findViewById(R.id.time);
        mCity = (EditText) v.findViewById(R.id.city);
        mState = (EditText) v.findViewById(R.id.state);
        mDetails = (EditText) v.findViewById(R.id.notes);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSightListener = null;
    }


    private String buildSightingURL(View v) {
        StringBuilder sb = new StringBuilder(REPORT_URL);

        try {

            String id = Integer.toString(0);
            sb.append("&id=");
            sb.append(URLEncoder.encode(id, "UTF-8"));

            String user = "haylee11";
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

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    public interface SightingAddListener {
        public void addSighting(String url);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
