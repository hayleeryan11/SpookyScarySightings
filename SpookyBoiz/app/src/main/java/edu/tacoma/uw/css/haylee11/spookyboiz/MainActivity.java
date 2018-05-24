package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;

/**
 * SignedInActivity is the activity that manages all fragments that the user can access before they
 * log into the app. It is the backbone on which the fragments are placed and communicate over.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class MainActivity extends AppCompatActivity implements SignInFragment.UserAddListener, CreateAccountFragment.UserAddListener {

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

    //Shared Preferences object to keep track of login
    private SharedPreferences mSharedPreferences;

    private String[] mMonsters;

    /**
     *Method for creating the activity, and what should be done on creation.
     * @param savedInstanceState The saved instance state as a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        mLoadingView = this.findViewById(R.id.loading_spinner);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("    Welcome !");

        String[] url = {"http://spookyscarysightings.000webhostapp.com/monsters.php"};
        AddUserTask task = new AddUserTask();
        task.execute(url);

        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
//            onReportOpening();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new SignInFragment())
                    .commit();
            mSharedPreferences
                            .edit()
                            .putString(getString(R.string.PROFILE), null)
                            .apply();
        } else {
//            onReportOpening();
            Intent i = new Intent(this, SignedInActivity.class);
            startActivity(i);
            finish();
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

        mLoadingView.setVisibility(View.VISIBLE);

        //Retrieve and cache the system's default "short" animation time
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);


//        onReportOpening();
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
     * Used to store the username of the person currently logged on
     * @param user The name of the user
     */
    @Override
    public void setPreferences(String user) {
        mSharedPreferences
                .edit()
                .putString(getString(R.string.CURRENT_USER), user)
                .apply();

    }

    public static String parseMonstersJSON(String monsterJSON) throws JSONException {
        StringBuilder sb = new StringBuilder();
        if (monsterJSON != null) {
            JSONArray arr = new JSONArray(monsterJSON);
            int i = 0;
            for (; i < arr.length() - 1; i++) {
                JSONObject obj = arr.getJSONObject(i);
                sb.append(obj.getString("name") + ",");
            }

            JSONObject obj = arr.getJSONObject(i);
            sb.append(obj.getString("name"));
        }
        Log.d(TAG, sb.toString());
        return sb.toString();
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
                    Toast.makeText(getApplicationContext(), "Signed In!",
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
                    Toast.makeText(getApplicationContext(),mSharedPreferences.getString(getString(R.string.CURRENT_USER), "wrong"),
                            Toast.LENGTH_LONG)
                            .show();
//                    mMonsters = parseMonstersJSON(result);

                    Intent i = new Intent(that, SignedInActivity.class);
                    startActivity(i);
//                    finish();

                } else if (result.contains("Bigfoot")) {
                    String monsters = parseMonstersJSON(result);
                    mSharedPreferences
                            .edit()
                            .putString(getString(R.string.MONSTER_ARR), monsters)
                            .apply();
                }

                else if (result.contains("success_create")) {
                    Toast.makeText(getApplicationContext(), "Account Created!",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(that, SignedInActivity.class);
                    startActivity(intent);

                }  else {
                    Toast.makeText(getApplicationContext(), "failed",
                            Toast.LENGTH_LONG)
                            .show();
                    mLoadingView.setVisibility(View.INVISIBLE);
                    mSharedPreferences
                            .edit()
                            .putString(getString(R.string.CURRENT_USER), null)
                            .putString(getString(R.string.PROFILE), null)
                            .apply();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
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
