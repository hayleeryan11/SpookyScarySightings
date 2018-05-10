package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

/**
 * SignedInActivity is the activity that manages all fragments that the user can access after they
 * log into the app. It is the backbone on which the fragments are placed and communicate over.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class SignedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PreferencesFragment.OnPreferFragmentInteractionListener,
        ReportFragment.OnFragmentInteractionListener, MonsterDetailFragment.OnFragmentInteractionListener,
        NotifySettingsFragment.OnNotifyFragmentInteractionListener, SightingsFragment.OnListFragmentInteractionListener,
        MonsterFragment.OnListFragmentInteractionListener, SignInFragment.OnSignInFragmentInteractionListener,
        CreateAccountFragment.OnFragmentInteractionListener, SightingDetailFragment.OnFragmentInteractionListener,
        ReportFragment.SightingAddListener {

    /* Tag for debugging */
    private static final String TAG = "SignedInActivity";


    /**
     *Method for creating the activity, and what should be done on creation.
     * @param savedInstanceState The saved instance state as a bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);


        //Starts sighting view fragment (as a homepage)
        SightingsFragment home = new SightingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_2, home)
                .addToBackStack(null)
                .commit();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pref) { //if the preferences button has been pressed, open new fragment
            PreferencesFragment prefer = new PreferencesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, prefer)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.action_info) { //if info button pressed, open About dialog
            AboutDialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getSupportFragmentManager(), "about");

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
            SightingsFragment sightings = new SightingsFragment();
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
            SightingsFragment sightings = new SightingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightings)
                    .addToBackStack(null)
                    .commit();
        //Not yet implemented
        } else if (id == R.id.nav_profile) {
        } else if (id == R.id.nav_update) {
        } else if (id == R.id.nav_notifications) {  //If we want to change notification settings, open notify settings fragment
            NotifySettingsFragment notify = new NotifySettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, notify)
                    .addToBackStack(null)
                    .commit();
        }
// else if (id == R.id.nav_send) {
//
//        }

        //Close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        /**
         * Opens NotifySettingsFragment when button pressed
         */
        @Override
        public void onNotifySettingsInteraction() {
            NotifySettingsFragment notify = new NotifySettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, notify)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * Opens monster list (MonsterFragment) when button pressed
         */
        @Override
        public void onNotifyMonsterSettingsInteraction() {
            MonsterFragment monster = new MonsterFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, monster)
                    .addToBackStack(null)
                    .commit();
        }

        /**
         * Opens UpdateProfileFragment when button pressed
         */
        @Override
        public void onUpdateProfileInteraction() {
            UpdateProfileFragment update = new UpdateProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, update)
                    .addToBackStack(null)
                    .commit();
        }

        /**
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
        public void addSighting(String url) {

            ReportTask task = new ReportTask();
            task.execute(new String[]{url.toString()});

            //Once done, pop back to previous view
            getSupportFragmentManager().popBackStackImmediate();
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
         * Inner class that extends AsynchTask. This class handles the creation of a report
         * and sends it off to the database to be inputted. This handles all the background
         * work that has to do with data sending in regards to report posting
         *
         * @author Haylee Ryan, Matt Frazier, Kai Stansfield
         */
        private class ReportTask extends AsyncTask<String, Void, String> {

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
                        Log.i(TAG, content.toString());

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
                Log.i(TAG, result);
                try {


                    JSONObject jsonObject = new JSONObject(result);
                    String status = (String) jsonObject.get("result");
                    if(status.equals("success_create")) {   //Successfully created account



                    } else if (status.equals("success_signIn")) {   //Successfully signed in
                        Toast.makeText(getApplicationContext(), "Signed In!",
                                Toast.LENGTH_LONG)
                                .show();
    //

                    } else {
                        Toast.makeText(getApplicationContext(), "failed: " + jsonObject.get("error"),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                            e.getMessage(), Toast.LENGTH_LONG)
                            .show();


                }
            }
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

    }
