package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * Fragment for creating a new account. Allows users to create a new account
 * which is then put into the database. Also logs the user in.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class CreateAccountFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * URL to append values to to create a new account in the database
     */
    private final static String COURSE_ADD_URL = "http://spookyscarysightings.000webhostapp.com/addUser.php?";

    /**
     * Tag for debugging
     */
    private static final String TAG = "CreateAccountFragment";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Listener for adding a new user
    private UserAddListener mListener;

    //First name of new user
    private EditText mFirstName;

    //Last name of new user
    private EditText mLastName;

    //Username of new user
    private EditText mUsername;

    //Password for new user
    private EditText mPassword;

    //Email for new user
    private EditText mEmail;

    //Confirmation for password
    private EditText mConfirm;

    /**
     * Required empty constructor
     */
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
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
     * When the fragment is create, this instantiates the view. In this case, takes
     * all the EditText values.
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);

        //Assigns fields to user entries
        mFirstName = (EditText) v.findViewById(R.id.f_name);
        mLastName = (EditText) v.findViewById(R.id.l_name);
        mUsername = (EditText) v.findViewById(R.id.username);
        mPassword = (EditText) v.findViewById(R.id.password);
        mEmail = (EditText) v.findViewById(R.id.email);
        mConfirm = (EditText) v.findViewById(R.id.confirm);

        getActivity().setTitle("Account Creation");

        //Creates button that creates a new user. Builds URL and starts AsyncTask
        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String url = buildUserURL(view);
                Log.i(TAG, url);
                mListener.loading();
                mListener.addUser(url);
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
        if (context instanceof UserAddListener) {
            mListener = (UserAddListener) context;
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
     * Based on the user input into the sign in fields, this builds a URL that will
     * be sent to the database for inputting.
     * @param v The view the user is in
     * @return A string representing the URL created
     */
    private String buildUserURL(View v) {
        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);

        try {

            String first = mFirstName.getText().toString();
            sb.append("first=");
            sb.append(URLEncoder.encode(first, "UTF-8"));

            String last = mLastName.getText().toString();
            sb.append("&last=");
            sb.append(URLEncoder.encode(last, "UTF-8"));

            String username = mUsername.getText().toString();
            sb.append("&username=");
            sb.append(URLEncoder.encode(username, "UTF-8"));

            String email = mEmail.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            String pwd = mPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            String confirm = mConfirm.getText().toString();
            sb.append("&confirm=");
            sb.append(URLEncoder.encode(confirm, "UTF-8"));

            Log.i(TAG, sb.toString());
        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
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
     * Interface that provides way to begin AsyncTask and the progress bar
     */
    public interface UserAddListener {
        //Starts AsyncTask
        void addUser(String url);

        //Progress bar
        void loading();
    }

}
