package edu.tacoma.uw.css.haylee11.spookyscarysightings;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.tacoma.uw.css.haylee11.spookyscarysightings.Sighting.SightingContent;

public class FrontPageActivity extends AppCompatActivity implements SightingFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
    }

    public void makeReport(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void preferences(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }

    public void info(View view) {
        DialogFragment frag = null;
        if (view.getId() == R.id.information) {
            frag = new InformationDialogFragment();
        } if (frag != null) {
            frag.show(getSupportFragmentManager(), "info");
        }
    }

    @Override
    public void onListFragmentInteraction(SightingContent.SightingItem item) {

    }

    public void getSightings(View view) {
        Fragment fragment = new SightingFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.front_page, fragment).addToBackStack(null).commit();

    }


//    @Override
//    public void onListFragmentInteraction(MonsterItem item) {
//
//    }
}
