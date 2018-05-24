package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * The monster that has been selected to see the details of
     */
    public static final String PROFILE_SELECTED = "profile_selected";

    public static final String TAG = "profile detail";

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
    // TODO: Rename and change types and number of parameters
    public static ProfileDetailFragment newInstance(String param1, String param2) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        mSharedPref =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);


        getActivity().setTitle("Profile Details");


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

        mUsername = (TextView) v.findViewById(R.id.username);
        mName = (TextView) v.findViewById(R.id.name);
        mSightings = (TextView) v.findViewById(R.id.sightings);
        mFavorite = (TextView) v.findViewById(R.id.favorite);
        mBio = (TextView) v.findViewById(R.id.bio);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
