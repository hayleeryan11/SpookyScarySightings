package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment for the preferences page. Allows a user to change
 * basic aspects of the app.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class PreferencesFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Listener for fragment interaction
    private OnPreferFragmentInteractionListener mListener;

    /**
     * Required empty constructor
     */
    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferencesFragment.
     */
    public static PreferencesFragment newInstance(String param1, String param2) {
        PreferencesFragment fragment = new PreferencesFragment();
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
     * When the fragment is create, this instantiates the view. In this case, a
     * few buttons
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_preferences, container, false);

        //Creates button for notifications
        Button notify = (Button) v.findViewById(R.id.notifications);

        //Sets button click to open notification fragment
        notify.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                mListener.onNotifySettingsInteraction();
            }
        });

        //Creates button for updating profile
        Button update = (Button) v.findViewById(R.id.update);

        //Opens update profile fragment
        update.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
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
        if (context instanceof OnPreferFragmentInteractionListener) {
            mListener = (OnPreferFragmentInteractionListener) context;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnPreferFragmentInteractionListener {
        void onNotifySettingsInteraction();;
    }
}
