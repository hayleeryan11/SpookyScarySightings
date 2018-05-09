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

public class MainActivity extends AppCompatActivity implements SignInFragment.UserAddListener, CreateAccountFragment.UserAddListener{


    private static final String TAG = "MainActivity";
    Activity that  = this;
    private View mLoadingView;
    private int mLongAnimationDuration;

    public String User;


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
    public void onCreateAccountInteraction() {
        CreateAccountFragment create = new CreateAccountFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, create)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void loading() {
        mLoadingView = this.findViewById(R.id.loading_spinner);

        mLoadingView.setVisibility(View.VISIBLE);

        //Retrieve and cache the system's defaul "short" animation time
        mLongAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);
    }

    @Override
    public void addUser(String url) {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

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
                    Toast.makeText(getApplicationContext(), "Account Created!",
                            Toast.LENGTH_LONG)
                            .show();
//                    HomePageFragment home = new HomePageFragment();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, home)
//                            .addToBackStack(null)
//                            .commit();
                    Intent intent = new Intent(that, SignedInActivity.class);
                    startActivity(intent);

                } else if (status.equals("success_signIn")) {
                    Toast.makeText(getApplicationContext(), "Signed In!",
                            Toast.LENGTH_LONG)
                            .show();
//                    HomePageFragment home = new HomePageFragment();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, home)
//                            .addToBackStack(null)
//                            .commit();
//                    User = SignInFragment.getUser();
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
