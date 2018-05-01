package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.MonsterContent;
import edu.tacoma.uw.css.haylee11.spookyboiz.MonsterInfo.MonsterInfoContent;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.SightingContent;

public class MainActivity extends AppCompatActivity implements SignInFragment.OnSignInFragmentInteractionListener,
        HomePageFragment.OnHomeFragmentInteractionListener, CreateAccountFragment.OnFragmentInteractionListener,
        PreferencesFragment.OnPreferFragmentInteractionListener, ReportFragment.OnFragmentInteractionListener,
        NotifySettingsFragment.OnNotifyFragmentInteractionListener, MonsterNotifyFragment.OnListFragmentInteractionListener,
        SightingsFragment.OnListFragmentInteractionListener, MonsterInfoFragment.OnListFragmentInteractionListener,
        CreateAccountFragment.UserAddListener {


    private static final String TAG = "MainActivity";

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

    @Override
    public void onSignInInteraction() {
        HomePageFragment home = new HomePageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, home)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateAccountInteraction() {
        CreateAccountFragment create = new CreateAccountFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, create)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPreferInteraction() {
        PreferencesFragment prefer = new PreferencesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, prefer)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onReportInteraction() {
        ReportFragment report = new ReportFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, report)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSightingInteraction() {
        SightingsFragment sightings = new SightingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, sightings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onExploreInteraction() {
        MonsterInfoFragment explore = new MonsterInfoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, explore)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNotifySettingsInteraction() {
        NotifySettingsFragment notify = new NotifySettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, notify)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNotifyMonsterSettingsInteraction() {
        MonsterNotifyFragment monster = new MonsterNotifyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, monster)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdateProfileInteraction() {
        UpdateProfileFragment update = new UpdateProfileFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, update)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

        getSupportFragmentManager().popBackStackImmediate();
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {

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

                    Log.i(TAG, urlConnection.toString());

                    InputStream content = urlConnection.getInputStream();


                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));

                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add user, Reason: " + e.getMessage();
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
            Log.d(TAG, result);
            try {


                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Course successfully added!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "failed to add: " + jsonObject.get("error"),
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(MonsterContent.MonsterItem item) {

    }

    @Override
    public void onListFragmentInteraction(SightingContent.SightingItem item) {

    }

    @Override
    public void onListFragmentInteraction(MonsterInfoContent.MonsterInfoItem item) {

    }
}
