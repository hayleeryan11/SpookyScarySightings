package edu.tacoma.uw.css.haylee11.spookyscarysightings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void notifications(View view) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
}
