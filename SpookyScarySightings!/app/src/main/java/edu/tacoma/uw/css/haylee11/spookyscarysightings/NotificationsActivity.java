package edu.tacoma.uw.css.haylee11.spookyscarysightings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import edu.tacoma.uw.css.haylee11.spookyscarysightings.MonsterNotify.MonsterNotifyContent;

public class NotificationsActivity extends AppCompatActivity implements MonsterNotifyFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Spinner spinner = (Spinner) findViewById(R.id.distance);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.distance, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setOnItemSelectedListener(this);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    @Override
    public void onListFragmentInteraction(MonsterNotifyContent.MonsterNotifyItem item) {

    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {

            // Another interface callback
        }
    }



    public void getList(View view) {
//        FrameLayout temp = new FrameLayout(this);
//
//        temp.setLayoutParams(new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//// set an id to the layout
//        temp.setId(R.id.temp); // some positive integer
//// set the layout as Activity content
//        setContentView(temp);
 //Finally , add the fragment
        Fragment fragment = new MonsterNotifyFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.notify_layout, fragment).addToBackStack(null).commit();
        
    }
}
