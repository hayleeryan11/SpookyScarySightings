package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * SignedInActivity is the activity that manages all fragments that the user can access before they
 * log into the app. It is the backbone on which the fragments are placed and communicate over.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class MainActivity extends AppCompatActivity implements SignInFragment.UserAddListener, CreateAccountFragment.UserAddListener{

    /**
     * Tag for debugging
     */
    private static final String TAG = "MainActivity";

    //Variable for current activity to use for inner class
    Activity that  = this;

    //Loading view for progress bar
    private View mLoadingView;

    //Animation length for progress bar
    private int mLongAnimationDuration;

    /**
     *Method for creating the activity, and what should be done on creation.
     * @param savedInstanceState The saved instance state as a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SignInFragment())

                    .commit();
        }
    }

    /**
     * Interaction method for when a user presses the button to go
     * to the Create and Account page.
     */
    @Override
    public void onCreateAccountInteraction() {
        //Starts new fragment
        CreateAccountFragment create = new CreateAccountFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, create)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Method for setting the progress bar to visible and to display
     * the loading screen
     */
    @Override
    public void loading() {
        mLoadingView = this.findViewById(R.id.loading_spinner);

        mLoadingView.setVisibility(View.VISIBLE);

        //Retrieve and cache the system's defaul "short" animation time
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
    }

    /**
     * Starts AsyncTask for adding a user to the database through the
     * CreateAccountFragment
     * @param url The URL created from the user's inputs into the fields
     */
    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

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
            Log.i(TAG, result);
            try {


                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success_create")) {
                    Toast.makeText(getApplicationContext(), "Account Created!",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(that, SignedInActivity.class);
                    startActivity(intent);

                } else if (status.equals("success_signIn")) {
                    Toast.makeText(getApplicationContext(), "Signed In!",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(that, SignedInActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "failed: " + jsonObject.get("error"),
                            Toast.LENGTH_LONG)
                            .show();
                    mLoadingView.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG)
                        .show();
                mLoadingView.setVisibility(View.INVISIBLE);

            }
        }
    }


}
