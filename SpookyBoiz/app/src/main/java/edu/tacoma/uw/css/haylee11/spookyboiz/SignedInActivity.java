package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.Profile.Profile;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;


/**
 * SignedInActivity is the activity that manages all fragments that the user can access after they
 * log into the app. It is the backbone on which the fragments are placed and communicate over.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class SignedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ReportFragment.OnFragmentInteractionListener, MonsterDetailFragment.OnFragmentInteractionListener, SightingsFragment.OnListFragmentInteractionListener,
        MonsterFragment.OnListFragmentInteractionListener, SignInFragment.OnSignInFragmentInteractionListener,
        CreateAccountFragment.OnFragmentInteractionListener, SightingDetailFragment.OnFragmentInteractionListener,
        ReportFragment.SightingAddListener, ProfileFragment.OnFragmentInteractionListener,
        OtherProfilesFragment.OnListFragmentInteractionListener, ProfileDetailFragment.OnFragmentInteractionListener {


    SharedPreferences mSharedPref;

    /**
     *Method for creating the activity, and what should be done on creation.
     * @param savedInstanceState The saved instance state as a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        //Sets up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Sets up drawer navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initializes SharedPreferences
        mSharedPref =
                getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);


        //Starts sighting view fragment (as a homepage)
        ProfileFragment home = new ProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_2, home, "PROFILE")
                .commit();

    }

    /**
     * Tells activity what to do when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        //Closes drawer on back press
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Creates the menu in toolbar when prompted
     * @param menu The menu to be displayed in the toolbar
     * @return True if successful, false if not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        //Sets TextView for current user info in nav bar
        TextView mNavName = (TextView) findViewById(R.id.name_nav);
        TextView mNavUsername = (TextView) findViewById(R.id.user_nav);
        TextView mNavSightings = (TextView) findViewById(R.id.sightings_nav);

        mNavName.setText(mSharedPref.getString(getString(R.string.NAME), null));
        mNavUsername.setText(mSharedPref.getString(getString(R.string.CURRENT_USER), null));
        mNavSightings.setText("Sightings: " + Integer.toString(mSharedPref.getInt(getString(R.string.SIGHTINGS), 0)));
        return true;
    }

    /**
     * Determines what happens when a menu item is selected
     * @param item What menu item the user selected
     * @return True if successful, false if not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_info) {    //if info button pressed, open About dialog
            AboutDialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getSupportFragmentManager(), "about");

        } else if (id == R.id.action_logout) {
            mSharedPref.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .apply();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Determines what happens when an item inside the navigation drawer has been selected
     * @param item The item the user has selected
     * @return True, assuming the app did not crash
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report) {    //if we want to make a report, open report fragment
            ReportFragment report = new ReportFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, report)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_view) {   //if we want to view sightings, open sighings list (fragment)
            SightingsFragment sightings = new SightingsFragment(0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightings)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_explore) {    //if we want to learn about monsters, open monster list (fragment)
            MonsterFragment explore = new MonsterFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, explore)
                    .addToBackStack(null)
                    .commit();
        }  else if (id == R.id.nav_mine) {      //If we want to see the sightings we have posted, open sightings list (fragment)
            SightingsFragment sightings = new SightingsFragment(1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightings)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_other_profiles) {
            OtherProfilesFragment profiles = new OtherProfilesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, profiles)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_profile) {
            ProfileFragment profile = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, profile)
                    .addToBackStack(null)
                    .commit();
        }

        //Close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


        /**
<<<<<<< HEAD
         * When we interact with the MonsterFragment (list of monsters), then we want
         * to open the details about the monster. This opens the detail fragment
         * @param item The monster the user chose from the list
         */
        @Override
        public void onListFragmentInteraction(Monster item) {
            MonsterDetailFragment monsterDetailFragment = new MonsterDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable(MonsterDetailFragment.MONSTER_SELECTED, item);
            monsterDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, monsterDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * When a user goes to report a sighting, this method is called to start the
         * AsyncTask and execute the user's command
         * @param url The URL created from the user's inputs
         */
        @Override
        public void addSighting(String url, String mMonster, String mDate, String mTime,
                                String mCity, String mState, String mDetails, Bitmap image) {


            ReportTask task = new ReportTask(image);
            task.execute(new String[]{url.toString()});

            // Provide option for sending an email
            DialogFragment fragment = ShareSightingDialogFragment.newInstance(mMonster, mDate,
                    mTime, mCity, mState, mDetails,
                    mSharedPref.getString(getString(R.string.NAME), null));
            fragment.show(getSupportFragmentManager(), "share");

            //Once done, pop back to previous view
            getSupportFragmentManager().popBackStackImmediate();
        }


        /**
         * Starts the AsyncTask to view a profile
         * @param url The url sent to the webservice to retrieve information
         */
        @Override
        public void profileView(String url) {

            ProfileTask task = new ProfileTask();
            task.execute(new String[]{url.toString()});

        }

        /**
         * Handles when the Profile Detail Fragment is interacted with, sending the
         * user to the detail of the profile from the list
         * @param item The profile the user chose
         */
        @Override
        public void onListFragmentInteraction(Profile item) {
            ProfileDetailFragment profileDetailFragment = new ProfileDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable(ProfileDetailFragment.PROFILE_SELECTED, item);
            profileDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, profileDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * When the SightingFragment (lsit of sightings0 iss interacted with, this
         * method opens the SightingsDetailFragment to show the details of the sighting.
         * @param item The sighting that the user chose.
         */
        @Override
        public void onListFragmentInteraction(Sighting item) {
            SightingDetailFragment sightDetailFragment = new SightingDetailFragment();
            Bundle args = new Bundle();
            args.putSerializable(SightingDetailFragment.SIGHTING_SELECTED, item);
            sightDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * Handles the interaction of a fragment
         * @param uri The Uniform resource identifier
         */
        @Override
        public void onFragmentInteraction(Uri uri) {

        }

        /**
         * Inner class that allows the creation and use of the About dialog
         *
         * @author Haylee Ryan, Matt Frazier, Kai Stansfield
         */
        public static class AboutDialogFragment extends DialogFragment {

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
                    //About message
                    builder.setMessage("Spooky Scary Sightings is an app that allows users to " +
                            "log sightings of creatures. From Bigfoot to UFOs to ghosts, " +
                            "Spooky Scary Sightings gives monster hunters a place to log sighitngs of" +
                            " these creatures and to make friends and share their finds along the way!")
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    // Create the AlertDialog object and return it
                    return builder.create();
                }
            }



        /**
         * Inner class that extends AsyncTask. This class handles the creation of a report
         * and sends it off to the database to be inputted. This handles all the background
         * work that has to do with data sending in regards to report posting
         *
         * @author Haylee Ryan, Matt Frazier, Kai Stansfield
         */
        private class ReportTask extends AsyncTask<String, Void, String> {

            Bitmap mImage;
            String convertedImage;
            HashMap<String, String> mHash;
            String mPOSTURL;

            ReportTask(Bitmap image) {
                mImage = image;
            }
            /**
             * Overrides onPreExecute. Performs super task
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                ByteArrayOutputStream byteArrayOutputStreamObject ;
                byteArrayOutputStreamObject = new ByteArrayOutputStream();
                mImage.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStreamObject);
                byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                convertedImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
                Log.e("Size of converted", String.valueOf(convertedImage.length()));
                mPOSTURL = "image_path=" + convertedImage;
                Log.d("Tag", mPOSTURL);
                Log.d("TAG", String.valueOf(byteArrayVar.length));
            }

            /**
             * Creates a URL connection to which we can send our URL carrying the data we want
             * to put into the database. This does all work in the background for the user when
             * generating a report.
             * @param urls The URLs to be sent through the connection that hold the information
             *             to be passed to the database
             * @return The successful or failed result of connecting with the URL
             */
            @Override
            protected String doInBackground(String... urls) {
                String response = "";
                HttpURLConnection urlConnection = null;
                HashMap<String,String> hash = new HashMap<String,String>();
                hash.put("image_path", convertedImage);
                for (String url : urls) {
                    try {
                        URL urlObject = new URL(url);
                        urlConnection = (HttpURLConnection) urlObject.openConnection();
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setDoInput(true);
                        urlConnection.setDoOutput(true);

                        OutputStream OPS = urlConnection.getOutputStream();
                        BufferedWriter bufferedWriterObject = new BufferedWriter(
                                new OutputStreamWriter(OPS, "UTF-8"));
                        bufferedWriterObject.write(setupPOST(hash));
                        bufferedWriterObject.flush();
                        bufferedWriterObject.close();
                        OPS.close();

                        InputStream content = urlConnection.getInputStream();

                        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));

                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    } catch (Exception e) {
                        response = "Unable to make report, Reason: " + e.getMessage();
                    } finally {
                        if(urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
                //Log.i(TAG, response);
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
                    JSONObject jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("result");
                    if (status.equals("Sighting added")) {   //Successfully signed in
                        Toast.makeText(getApplicationContext(), "Sighting Added!",
                                Toast.LENGTH_LONG)
                                .show();


                    } else {
                        Toast.makeText(getApplicationContext(), "failed: " + jsonObject.get("error"), //Sighting failed
                                Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong with the data" +   //Something wrong with data
                            e.getMessage(), Toast.LENGTH_LONG)
                            .show();

                }
            }

            /**
             * Puts together the POST message and encodes it for URL safety
             *
             * @param Hash A has containing both the prefix for the POST and the data
             * @return A concatenated POST string
             */
            public String setupPOST(HashMap<String, String> Hash) {
                StringBuilder builder = new StringBuilder();

                for (Map.Entry<String, String> KEY : Hash.entrySet()) {
                    try {
                        builder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                        builder.append("=");
                        builder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                    } catch (Exception e) {
                        Log.e("ERROR!", Log.getStackTraceString(e));
                    }
                }

                return builder.toString();
            }
        }

    /**
     * Inner class that creates a dialog providing the option to share sightings via email.
     *
     * @author Haylee Ryan, Matt Frazier, Kai Stansfield
     */
    public static class ShareSightingDialogFragment extends DialogFragment {

        /**
         * Creates a new instance of ShareSightingDialogFragment providing arguments to autofill
         * the email if the email option is selected.
         *
         * @param sMonster is the monster from the sighting.
         * @param sDate is the date of the sighting.
         * @param sTime is the time of the sighting.
         * @param sCity is the city where the sighting occurred.
         * @param sState is the state where the sighting occurred.
         * @param sDetails is the detailed description of the sighting.
         * @param sName is the full name of the user submitting the sighting.
         * @return a new ShareSightingDialogFragment containing the parameters.
         */
        public static ShareSightingDialogFragment newInstance(String sMonster, String sDate, String sTime,
                                                       String sCity, String sState,
                                                       String sDetails, String sName) {
            ShareSightingDialogFragment f = new ShareSightingDialogFragment();
            Bundle args = new Bundle();
            args.putString("mMonster", sMonster);
            args.putString("mDate", sDate);
            args.putString("mTime", sTime);
            args.putString("mCity", sCity);
            args.putString("mState", sState);
            args.putString("mDetails", sDetails);
            args.putString("mName", sName);
            f.setArguments(args);
            return f;
        }

        /**
         * Creates the dialog providing the option to send sighting information as an email.
         *
         * @param savedInstanceState is the state of the constructed dialog.
         * @return the email sighting dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String mMonster = getArguments().getString("mMonster");
            final String mDate = getArguments().getString("mDate");
            final String mTime = getArguments().getString("mTime");
            final String mCity = getArguments().getString("mCity");
            final String mState = getArguments().getString("mState");
            final String mDetails = getArguments().getString("mDetails");
            final String mName = getArguments().getString("mName");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_share_shighting)
                    .setPositiveButton(R.string.share_email, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    "Spooky Scary Sightings Notification: " + mMonster +
                                            " Sighting!");
                            emailIntent.putExtra(Intent.EXTRA_TEXT,
                                    mName + " would like to report a monster sighting..." +
                                            "\n\n\nWhat: " + mMonster +
                                            "\n\nWhen: " + mDate + " at " + mTime +
                                            "\n\nWhere: " + mCity + ", " + mState +
                                            "\n\nDescription: " + mDetails +
                                            "\n\n\nSincerely, Spooky Scary Sightings");
                            try {
                                startActivity(Intent.createChooser(emailIntent,
                                        "Send mail..."));
                                getActivity().finish();
                                Log.i("DONE!", "Finished sending email...");
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(),
                                        "There is no email application installed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.share_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dismiss();
                                }
                            });

            return builder.create();
        }
    }


        /**
         * Inner class that extends AsynchTask. This class handles the creation of a profile
         * based on the information of the current user logged in.
         *
         * @author Haylee Ryan, Matt Frazier, Kai Stansfield
         */
        private class ProfileTask extends AsyncTask<String, Void, String> {


            SharedPreferences mSharedPrefs;

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
             * generating a report.
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
                        //Log.i(TAG, content.toString());

                        BufferedReader buffer = new BufferedReader(new InputStreamReader(content));

                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }
                    } catch (Exception e) {
                        response = "Unable to make report, Reason: " + e.getMessage();
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

                //Initialize SharedPreferences
                mSharedPrefs = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);


                if (result.startsWith("Unable to")) {   //Failed to get info
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                try {
                   Profile prof = Profile.parseCourseJSON(result);

                   //Put current user info into SharedPreferences for access
                   mSharedPrefs
                            .edit()
                            .putString(getString(R.string.CURRENT_USER), prof.getmUsername())
                            .putString(getString(R.string.NAME), prof.getmFName() + " " + prof.getmLName())
                            .putInt(getString(R.string.SIGHTINGS), prof.getmSightings())
                            .putString(getString(R.string.FAVORITE), prof.getmFavorite())
                            .putString(getString(R.string.BIO), prof.getmBio())
                            .apply();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                return;

            }
        }
}

