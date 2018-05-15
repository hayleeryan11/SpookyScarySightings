package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.PrivilegedAction;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;


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

        //Assign values to TextView
        mUsername = (TextView) v.findViewById(R.id.username);
        mMonster = (TextView) v.findViewById(R.id.monster);
        mCity = (TextView) v.findViewById(R.id.city);
        //mState = (TextView) v.findViewById(R.id.state);
        mDate = (TextView) v.findViewById(R.id.date);
        mTime = (TextView) v.findViewById(R.id.time);
        mDesc = (TextView) v.findViewById(R.id.description);

        return v;
    }

    /**
     * Sets the text of the TextViews in the layout to the details
     * @param sight The sighting the user is viewing the details of
     */
    public void updateView(Sighting sight) {
        if (sight != null) {
            mUsername.setText("User: " + sight.getmUsername());
            mCity.setText(sight.getmCity() + ", " + sight.getmState());
            //mState.setText(sight.getmState());
            mDate.setText("Date: " + sight.getmDate());
            mTime.setText("Time: " + sight.getmTime());
            mMonster.setText(sight.getmMonster());
            mDesc.setText("Description: \n" + sight.getmDesc());
        }

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
}
