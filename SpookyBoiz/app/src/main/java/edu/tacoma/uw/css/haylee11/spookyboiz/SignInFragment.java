package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * The fragment that contains the sign in page for the user. This is
 * the first page the user will see upon entering the app, and not
 * being logged in already.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class SignInFragment extends Fragment {

    /**
     * First param when creating new fragment
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second param when creating new fragment
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * URL used to send sign in parameters to the database for checking
     */
    private final static String SIGN_IN_URL = "http://spookyscarysightings.000webhostapp.com/login.php?";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Username of the user trying to log in
    private EditText mUsername;

    //Password of the user trying to log in
    private EditText mPassword;

    //Listener that listens for interaction with the fragment/buttons
    private UserAddListener mListener;


    //Loading view for progress bar
    private View mLoadingView;

    SharedPreferences mSharedPreferences;

    /**
     * Empty constructor (not needed)
     */
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
     * When the fragment view is created, this method instantiates it. We instantiate
     * the Create Account button, and the Sign In button
     * @param inflater  Layout inflater
     * @param container Container of the fragment
     * @param savedInstanceState Saved instance state
     * @return The view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        //Sets fields to what the user has entered
        mUsername = (EditText) v.findViewById(R.id.username_input);
        mPassword = (EditText) v.findViewById(R.id.password_input);


        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        mLoadingView = getActivity().findViewById(R.id.loading_spinner);

        //When sign in is pushed, listener builds URL and shows progress bar
        Button create = (Button) v.findViewById(R.id.sign_in);
        create.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String url = buildUserURL(view);
                SignInTask task = new SignInTask();
                task.execute(new String[]{url.toString()});
                loading();
            }
        });

        //When create account pressed, listener opens new fragment
        Button createAcc = (Button) v.findViewById(R.id.create);
        createAcc.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                mListener.onCreateAccountInteraction();
            }
        });

        return v;
    }

    /**
     * When the fragment is initially attached to the app, we instantiate
     * the listener
     * @param context Context that the fragment is in
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
     * When the fragment is detached, we nullify the listener
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Based on the user input into the sign in fields, this builds a URL that will
     * be sent to the database for checking.
     * @param v The view the user is in
     * @return A string representing the URL created
     */
    private String buildUserURL(View v) {
        StringBuilder sb = new StringBuilder(SIGN_IN_URL);

        try {
            //Append username
            String user = mUsername.getText().toString();
//            mListener.setPreferences(user);
            sb.append("username=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            //Append password
            String pass = mPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(pass, "UTF-8"));

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
     * activity.cating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSignInFragmentInteractionListener {

    }

    /**
     * Method for setting the progress bar to visible and to display
     * the loading screen
     */

    public void loading() {

        mLoadingView.setVisibility(View.VISIBLE);


    }

    /**
     * Provides a listener for the buttons on the fragment, as well as the
     * progress bar
     */
    public interface UserAddListener {
        void onCreateAccountInteraction();
    }

    /**
     * Inner class that extends AsynchTask. This class handles the creation of a user
     * and sends it off to the database to be inputted. This handles all the background
     * work that has to do with data sending in regards to creating an account
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    private class SignInTask extends AsyncTask<String, Void, String> {

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
                //Successfully signed in and returning profile information
                if (result.contains("f_name")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Signed In!",
                            Toast.LENGTH_LONG)
                            .show();

                    Profile p = Profile.parseCourseJSON(result);


                    //Inputs data of current user into SharedPreferences
                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .putString(getString(R.string.CURRENT_USER), p.getmUsername())
                            .putString(getString(R.string.FAVORITE), p.getmFavorite())
                            .putString(getString(R.string.BIO), p.getmBio())
                            .putString(getString(R.string.NAME), p.getmFName() + " " + p.getmLName())
                            .putInt(getString(R.string.SIGHTINGS), p.getmSightings())
                            .apply();

                    Intent i = new Intent(getActivity(), SignedInActivity.class);
                    startActivity(i);
                    //Returning list of monsters
                } else {
                    JSONObject jsonObject = new JSONObject(result);
                    Toast.makeText(getActivity().getApplicationContext(), "Failed: " + jsonObject.get("error").toString(), Toast.LENGTH_LONG)
                    .show();
                    mLoadingView.setVisibility(View.INVISIBLE);
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
