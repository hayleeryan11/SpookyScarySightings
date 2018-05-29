package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;


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
    private OnFragmentInteractionListener mListener;

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


    //Loading view for progress bar
    private View mLoadingView;

    SharedPreferences mSharedPreferences;

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



        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        mLoadingView = getActivity().findViewById(R.id.loading_spinner);

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
                loading();

               AddUserTask task = new AddUserTask();
               task.execute(new String[]{url.toString()});

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
     * Method for setting the progress bar to visible and to display
     * the loading screen
     */

    public void loading() {

        mLoadingView.setVisibility(View.VISIBLE);


    }

    /**
     * Inner class that extends AsynchTask. This class handles the creation of a user
     * and sends it off to the database to be inputted. This handles all the background
     * work that has to do with data sending in regards to creating an account
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {

        /**
         * Overrides onPreExecute. Performs super task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Creates a URL connection to which we can send our URL carrying the data we want
         * to put into the database. This does all work in the background for the user when
         * creating a new account.
         * @param urls The URLs to be sent through the connection that hold the information
         *             to be passed to the database
         * @return The successful or failed result of connecting with the URL
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();



                    InputStream content = urlConnection.getInputStream();
                    Log.i(TAG, content.toString());

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));

                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add user/sign in, Reason: " + e.getMessage();
                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * After the background work has been executed, the result comes into this method
         * to be read. From there, we determine what to do (has it succeeded? Failed? Is
         * the data wrong?)
         * @param result The result from doInBackground (If the insertion/retrieving was
         *               successful or not.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.contains("f_name")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Account Created!",
                            Toast.LENGTH_LONG)
                            .show();
                    Profile p = Profile.parseCourseJSON(result);
                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .putString(getString(R.string.CURRENT_USER), p.getmUsername())
                            .putString(getString(R.string.FAVORITE), p.getmFavorite())
                            .putString(getString(R.string.BIO), p.getmBio())
                            .putString(getString(R.string.NAME), p.getmFName() + " " + p.getmLName())
                            .putInt(getString(R.string.SIGHTINGS), p.getmSightings())
                            .apply();
                    Intent intent = new Intent(getActivity(), SignedInActivity.class);
                    startActivity(intent);
                    //failed in making account
                }  else {
                    JSONObject jsonObject = new JSONObject(result);
                    Toast.makeText(getActivity().getApplicationContext(), "Failed: " + jsonObject.get("error").toString(), Toast.LENGTH_LONG)
                            .show();
                    mLoadingView.setVisibility(View.INVISIBLE);
                    mSharedPreferences
                            .edit()
                            .putString(getString(R.string.CURRENT_USER), null)
                            .putString(getString(R.string.PROFILE), null)
                            .apply();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG)
                        .show();
                mLoadingView.setVisibility(View.INVISIBLE);
                mSharedPreferences
                        .edit()
                        .putString(getString(R.string.CURRENT_USER), null)
                        .putString(getString(R.string.PROFILE), null)
                        .apply();

            }
        }
    }

}
