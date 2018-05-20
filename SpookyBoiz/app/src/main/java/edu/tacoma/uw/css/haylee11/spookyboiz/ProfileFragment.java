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
import android.util.Log;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBERfa
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mUsername;
    private TextView mName;
    private TextView mSightings;
    private TextView mFavorite;
    private TextView mBio;

    private Profile mProfile;

    SharedPreferences mSharedPref;

    public static final String TAG = "Profile";

    private static final String PROFILE_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=profile";
    private static final String FAV_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=fav&current=";
    private static final String BIO_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=bio&current=";

    private OnFragmentInteractionListener mListener;

    private View mView;

    private static String mChangeFav;
    private static String mChangeBio;

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
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("My Profile");

        mSharedPref = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        mUsername = (TextView) v.findViewById(R.id.username);
        mName = (TextView) v.findViewById(R.id.name);
        mSightings = (TextView) v.findViewById(R.id.sightings);
        mFavorite = (TextView) v.findViewById(R.id.favorite);
        mBio = (TextView) v.findViewById(R.id.bio);

        String url = buildProfileURL(v, "profile");
        mListener.profileView(url);

        mUsername.setText(mSharedPref.getString(getString(R.string.CURRENT_USER), "user"));
        mName.setText(mSharedPref.getString(getString(R.string.NAME), "first last"));
        mSightings.setText(Integer.toString(mSharedPref.getInt(getString(R.string.SIGHTINGS), 0)));
        mFavorite.setText(mSharedPref.getString(getString(R.string.FAVORITE), "None"));
        mBio.setText(mSharedPref.getString(getString(R.string.BIO), "None"));


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


    private String buildProfileURL(View v, String action) {
        StringBuilder sb;
        String user = mSharedPref.getString(getString(R.string.CURRENT_USER), null);
        if (action.equals("profile")) {
            sb = new StringBuilder(PROFILE_URL);
            try {
                sb.append("&username=");
                sb.append(URLEncoder.encode(user, "UTF-8"));

            } catch(Exception e) {
                Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        } else if (action.equals("fav")) {
            sb = new StringBuilder(FAV_URL);
            try {
                sb.append(URLEncoder.encode(user, "UTF-8"));
                sb.append("&favorite=");
                sb.append(URLEncoder.encode(mChangeFav, "UTF-8"));

            } catch(Exception e) {
                Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        } else {
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
        Log.d(TAG, sb.toString());
        return sb.toString();
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

    public static class ChangeDialogFragment extends DialogFragment {

        int mChange;

        ProfileFragment mProf;

        public ChangeDialogFragment() {
            mProf = new ProfileFragment();
        }

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
                            if (mChange == 1) {
                                mChangeFav = input.getText().toString();
                                String url = mProf.buildProfileURL(mProf.mView, "fav");
                                mProf.mSharedPref
                                        .edit()
                                        .putString(getString(R.string.FAVORITE), mChangeFav)
                                        .apply();
                                mProf.mListener.profileView(url);

                                mProf.mFavorite.setText(mProf.mSharedPref.getString(getString(R.string.FAVORITE), "error"));
                            } else {
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void profileView(String url);
    }
}
