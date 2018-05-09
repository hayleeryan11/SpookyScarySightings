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

public class SignedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PreferencesFragment.OnPreferFragmentInteractionListener,
        ReportFragment.OnFragmentInteractionListener, MonsterDetailFragment.OnFragmentInteractionListener,
        NotifySettingsFragment.OnNotifyFragmentInteractionListener, SightingsFragment.OnListFragmentInteractionListener,
        MonsterFragment.OnListFragmentInteractionListener, SignInFragment.OnSignInFragmentInteractionListener,
        CreateAccountFragment.OnFragmentInteractionListener, SightingDetailFragment.OnFragmentInteractionListener,
        ReportFragment.SightingAddListener {

    private static final String TAG = "SignedInActivity";

    public String User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);



        SightingsFragment home = new SightingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_2, home)
                .addToBackStack(null)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pref) {
            PreferencesFragment prefer = new PreferencesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, prefer)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.action_info) {
            AboutDialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getSupportFragmentManager(), "about");

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report) {
            ReportFragment report = new ReportFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, report)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_view) {
            SightingsFragment sightings = new SightingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightings)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_explore) {
            MonsterFragment explore = new MonsterFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, explore)
                    .addToBackStack(null)
                    .commit();
        }  else if (id == R.id.nav_mine) {
            SightingsFragment sightings = new SightingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, sightings)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_profile) {
        } else if (id == R.id.nav_update) {
        } else if (id == R.id.nav_notifications) {
            NotifySettingsFragment notify = new NotifySettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, notify)
                    .addToBackStack(null)
                    .commit();
        }
// else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


        @Override
        public void onNotifySettingsInteraction() {
            NotifySettingsFragment notify = new NotifySettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, notify)
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onNotifyMonsterSettingsInteraction() {
            MonsterFragment monster = new MonsterFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, monster)
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onUpdateProfileInteraction() {
            UpdateProfileFragment update = new UpdateProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_2, update)
                    .addToBackStack(null)
                    .commit();
        }

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

    @Override
    public void addSighting(String url) {

        ReportTask task = new ReportTask();
        task.execute(new String[]{url.toString()});

        getSupportFragmentManager().popBackStackImmediate();
    }


    public static class AboutDialogFragment extends DialogFragment {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage("Spooky Scary Sightings is an app that allows users to " +
                        "log sightings of creatures. From Bigfoot to UFOs to ghosts, " +
                        "Spooky Scary Sightings gives monster hunters a place to log sighitngs of" +
                        " these creatures and to make friends and share their finds along the way!")
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

    //                            Intent intent = new Intent(getActivity(), Dolphin.class);
    //                            startActivity(intent);
                            }
                        });
//                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        });
                // Create the AlertDialog object and return it
                return builder.create();
            }
        }




    private class ReportTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            try {


                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success_create")) {



                } else if (status.equals("success_signIn")) {
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


        @Override
        public void onFragmentInteraction(Uri uri) {

        }

    }
