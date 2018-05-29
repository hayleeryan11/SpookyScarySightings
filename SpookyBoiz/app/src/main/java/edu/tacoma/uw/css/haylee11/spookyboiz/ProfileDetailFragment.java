package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;


/**
 * Class that hold the details of another user's profile, which the current
 * user can view through the Find Hunters tab
 *
 * @author Haylee Ryan, Matthew Frazier, Kai Stansfield
 */
public class ProfileDetailFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * The monster that has been selected to see the details of
     */
    public static final String PROFILE_SELECTED = "profile_selected";

    //Username
    TextView mUsername;

    //First name
    TextView mName;

    //Number of sightings
    TextView mSightings;

    //Favorite/most sighted monster
    TextView mFavorite;

    //Bio of user
    TextView mBio;

    Button mSightButton;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SharedPreferences mSharedPref;

    /**
     * Required empty constructor
     */
    public ProfileDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileDetailFragment.
     */
    public static ProfileDetailFragment newInstance(String param1, String param2) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created
     * @param savedInstanceState The saved instance state
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
     * Called when the view of the fragment is created. Sets up all UI elements
     *
     * @param inflater Inflates the view
     * @param container Container the view is inside of
     * @param savedInstanceState The saved instance state
     *
     * @return The final composed view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        mSharedPref =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);


        getActivity().setTitle("Profile Details");


        //Button to view other user's sightings
        mSightButton = (Button) v.findViewById(R.id.button);
        mSightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SightingsFragment sightings = new SightingsFragment(2);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_2, sightings)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Initialize TextViews to be filled
        mUsername = (TextView) v.findViewById(R.id.username_input);
        mName = (TextView) v.findViewById(R.id.name);
        mSightings = (TextView) v.findViewById(R.id.sightings);
        mFavorite = (TextView) v.findViewById(R.id.favorite);
        mBio = (TextView) v.findViewById(R.id.bio);

        return v;
    }

    /**
     * When the fragment is attached, set listener ect.
     *
     * @param context Context of the fragment
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
     * When the fragment is detached, set listener to null
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Decides what happens when the app is resumed. In this case, the detail page
     * is updated
     */
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if(args != null) {
            onUpdate((Profile)args.getSerializable(PROFILE_SELECTED));
        }
    }

    /**
     * Updates the details when a user is chosen to see the details of
     * @param profile The profile object the user wants to view
     */
    public void onUpdate(final Profile profile) {
        if(profile != null) {
            mSharedPref
                    .edit()
                    .putString(getString(R.string.PROFILE_VIEW), profile.getmUsername())
                    .apply();
            mUsername.setText(profile.getmUsername());
            mName.setText(profile.getmFName() + " " + profile.getmLName());
            mSightings.setText(Integer.toString(profile.getmSightings()));
            mFavorite.setText(profile.getmFavorite());
            mBio.setText(profile.getmBio());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

    }
}
