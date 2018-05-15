package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;


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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    private List<Profile> mProfile;

    SharedPreferences mSharedPref;

    public static final String TAG = "Profile";

    private static final String PROFILE_URL = "http://spookyscarysightings.000webhostapp.com/userProfile.php?cmd=profile" ;

    private OnFragmentInteractionListener mListener;

    private View mView;

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
        mView = inflater.inflate(R.layout.fragment_profile, container, false);



        mUsername = (TextView) mView.findViewById(R.id.username);
        mName = (TextView) mView.findViewById(R.id.name);
        mSightings = (TextView) mView.findViewById(R.id.sightings);
        mFavorite = (TextView) mView.findViewById(R.id.favorite);
        mBio = (TextView) mView.findViewById(R.id.bio);

        mSharedPref = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        String url = buildProfileURL(mView);
        mListener.profileView(url);

        mProfile = new ArrayList<Profile>();

        try {
            mProfile = Profile.parseCourseJSON(mSharedPref.getString(getString(R.string.PROFILE), null));

            mSharedPref
                    .edit()
                    .putString(getString(R.string.NAME), mProfile.get(0).getmFName() + " " + mProfile.get(0).getmLName())
                    .putInt(getString(R.string.SIGHTINGS), mProfile.get(0).getmSightings())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Profile> prof = mListener.getProfile();



            mUsername.setText(mProfile.get(0).getmUsername());
            mName.setText(mProfile.get(0).getmFName() + " " + mProfile.get(0).getmLName());
            mSightings.setText("Sightings:" + mProfile.get(0).getmSightings());
            mFavorite.setText("Favorite Monster: " + mProfile.get(0).getmFavorite());
            mBio.setText(mProfile.get(0).getmBio());

        return mView;
    }

    public void updateView() {

        String url = buildProfileURL(mView);
        mListener.profileView(url);

        try {
            mProfile = Profile.parseCourseJSON(mSharedPref.getString(getString(R.string.PROFILE), null));

            mSharedPref
                    .edit()
                    .putString(getString(R.string.CURRENT_USER), mProfile.get(0).getmUsername())
                    .putString(getString(R.string.NAME), mProfile.get(0).getmFName() + " " + mProfile.get(0).getmLName())
                    .putInt(getString(R.string.SIGHTINGS), mProfile.get(0).getmSightings())
                    .apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mUsername.setText(mProfile.get(0).getmUsername());
        mName.setText(mProfile.get(0).getmFName() + " " + mProfile.get(0).getmLName());
        mSightings.setText("Sightings:" + mProfile.get(0).getmSightings());
        mFavorite.setText("Favorite Monster: " + mProfile.get(0).getmFavorite());
        mBio.setText(mProfile.get(0).getmBio());

    }

    private String buildProfileURL(View v) {
        StringBuilder sb = new StringBuilder(PROFILE_URL);
        String user = mSharedPref.getString(getString(R.string.CURRENT_USER), null);
        try {
            sb.append("&username=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }


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

    /**
     * Decides what happens when the app is resumed. In this care, the detail page
     * is updated
     */
//    @Override
//    public void onResume() {
//        super.onResume();
//            updateView();
//
//    }

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
        void profileView(String url);
        List<Profile> getProfile();
    }
}
