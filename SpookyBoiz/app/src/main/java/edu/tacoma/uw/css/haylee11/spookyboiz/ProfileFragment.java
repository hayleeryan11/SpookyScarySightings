package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;

/**
 * Builds the current user's profile for viewing
 *
 * @author Haylee Ryan, Matthew Frazier, Kai Stansfield
 */
public class ProfileFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBERfa
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * URL for gathering information on current user for profile
     */
    private static final String PROFILE_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=profile";

    /**
     * URL used to change the current user's favorite monster
     */
    private static final String FAV_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=fav&current=";

    /**
     * URL used to change the current user's bio
     */
    private static final String BIO_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=bio&current=";

    private String mParam1;
    private String mParam2;

    private TextView mUsername;
    private TextView mName;
    private TextView mSightings;
    private TextView mFavorite;
    private TextView mBio;

    private Profile mProfile;

    SharedPreferences mSharedPref;


    private OnFragmentInteractionListener mListener;

    private View mView;

    private static String mChangeFav;
    private static String mChangeBio;

    /**
     * Required empty constructor
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("My Profile");

        mSharedPref = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        mUsername = (TextView) v.findViewById(R.id.username_input);
        mName = (TextView) v.findViewById(R.id.name);
        mSightings = (TextView) v.findViewById(R.id.sightings);
        mFavorite = (TextView) v.findViewById(R.id.favorite);
        mBio = (TextView) v.findViewById(R.id.bio);

        getActivity().setTitle("Profile");

        String url = buildProfileURL(v, "profile");
        mListener.profileView(url);

        mUsername.setText(mSharedPref.getString(getString(R.string.CURRENT_USER), "user"));
        mName.setText(mSharedPref.getString(getString(R.string.NAME), "first last"));
        mSightings.setText(Integer.toString(mSharedPref.getInt(getString(R.string.SIGHTINGS), 0)));
        mFavorite.setText(mSharedPref.getString(getString(R.string.FAVORITE), "None"));
        mBio.setText(mSharedPref.getString(getString(R.string.BIO), "None"));

        //Set up buttons for changing attributes (opens dialog fragment)

        Button fav = (Button) v.findViewById(R.id.fav_change);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialogFragment newFragment = new ChangeDialogFragment(1, ProfileFragment.this);
                newFragment.show(getActivity().getSupportFragmentManager(), "fav_change");
            }
        });


        Button bio = (Button) v.findViewById(R.id.bio_change);
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDialogFragment newFragment = new ChangeDialogFragment(2, ProfileFragment.this);
                newFragment.show(getActivity().getSupportFragmentManager(), "bio_change");
            }
        });

        mView = v;

        return v;
    }


    /**
     * Builds URl to either get profile, change favorite monster, or change bio
     * @param v The current view
     * @param action Action to take place
     * @return The URL
     */
    private String buildProfileURL(View v, String action) {
        StringBuilder sb;
        String user = mSharedPref.getString(getString(R.string.CURRENT_USER), null);
        if (action.equals("profile")) { //If we are getting profile info
            sb = new StringBuilder(PROFILE_URL);
            try {
                sb.append("&username=");
                sb.append(URLEncoder.encode(user, "UTF-8"));

            } catch(Exception e) {
                Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        } else if (action.equals("fav")) { //If we are changing favorite
            sb = new StringBuilder(FAV_URL);
            try {
                sb.append(URLEncoder.encode(user, "UTF-8"));
                sb.append("&favorite=");
                sb.append(URLEncoder.encode(mChangeFav, "UTF-8"));

            } catch(Exception e) {
                Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        } else {                        //If we are changing bio
            sb = new StringBuilder(BIO_URL);
            try {
                sb.append(URLEncoder.encode(user, "UTF-8"));
                sb.append("&bio=");
                sb.append(URLEncoder.encode(mChangeBio, "UTF-8"));

            } catch(Exception e) {
                Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
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
     * Dialog pop up that allows user to enter new attribute which is then changed in profile
     * and in database
     *
     * @author Haylee Ryan, Matthew Frazier, Kai Stansfield
     */
    public static class ChangeDialogFragment extends DialogFragment {

        int mChange;

        ProfileFragment mProf;

        /**
         * Constructs a new Profile Fragment to use for method calls
         */
        public ChangeDialogFragment() {
            mProf = new ProfileFragment();
        }

        /**
         * Constructs new local Profile Fragment with the given fragment
         * @param change Flag determining what we are changing
         * @param prof Instance of Profile Fragment to set up
         */
        @SuppressLint("ValidFragment")
        public ChangeDialogFragment(int change, ProfileFragment prof) {
            mChange = change;
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

            final EditText input = new EditText(this.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
//                    AlertDialog alert = builder.create();
            //About message
            if (mChange == 0) {
                builder.setMessage("Enter new username:");
            } else if (mChange == 1) {
                builder.setMessage("Enter new favorite monster:");
            } else {
                builder.setMessage("Enter new bio:");
            }
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (mChange == 1) { //If we are changing the favorite monster
                                mChangeFav = input.getText().toString();
                                String url = mProf.buildProfileURL(mProf.mView, "fav");
                                mProf.mSharedPref
                                        .edit()
                                        .putString(getString(R.string.FAVORITE), mChangeFav)
                                        .apply();
                                mProf.mListener.profileView(url);

                                mProf.mFavorite.setText(mProf.mSharedPref.getString(getString(R.string.FAVORITE), "error"));
                            } else {    //if we are changing the bio
                                mChangeBio = input.getText().toString();
                                String url = mProf.buildProfileURL(mProf.mView, "bio");
                                mProf.mSharedPref
                                        .edit()
                                        .putString(getString(R.string.BIO), mChangeBio)
                                        .apply();
                                mProf.mListener.profileView(url);

                                mProf.mBio.setText(mProf.mSharedPref.getString(getString(R.string.BIO), "error"));
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });



            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void profileView(String url);
    }
}
