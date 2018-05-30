package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * Fragment for the report fragment. Allows users to log their sightings
 * with the necessary information.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class ReportFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * URL to add report values to to input into database
     */
    private final static String REPORT_URL = "http://spookyscarysightings.000webhostapp.com/addSighting.php?";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Listener for fragment interaction
    private OnFragmentInteractionListener mListener;

    //Spinner containing monster values
    private Spinner mMonster;

    //EditText for date
    private EditText mDate;

    private DatePickerDialog.OnDateSetListener mDatePicker;

    private Calendar mCalendar;

    //EditText for time
    private EditText mTime;

    private TimePickerDialog.OnTimeSetListener mTimePicker;

    //EditText for city
    private EditText mCity;

    //EditText for state
    private Spinner mState;

    //EditText for details
    private EditText mDetails;

    //Listener for adding a sighting to the list/database
    private SightingAddListener mSightListener;

    //Button to choose a picture from the gallery.
    private ImageButton mChoosePicture;

    //Button to take a picture.
    private ImageButton mTakePicture;

    //Image pulled from gallery or taken at the moment.
    private Bitmap mImage;

    //Shared preferences of the user.
    private SharedPreferences mSharedPref;

    /**
     * Required empty constructor
     */
    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * When the fragment is created, this method instantiates it
     * @param savedInstanceState The saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * When the fragment is create, this instantiates the view. In this case, all
     * of the UI elements that hold the information we want in our URL.
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        mSharedPref =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

        String[] arr = mSharedPref.getString(getString(R.string.MONSTER_ARR), "none").split(",");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter =  new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, arr);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        getActivity().setTitle("    Report a Sighting");

        //Assigns values in Spinner/EditText fields to our class fields
        mMonster = (Spinner) v.findViewById(R.id.spinner);
        mDate = (EditText) v.findViewById(R.id.date_text);
        mTime = (EditText) v.findViewById(R.id.time_text);

        //Instantiates new Calendar object
        mCalendar= new Calendar() {
            @Override
            protected void computeTime() {

            }

            @Override
            protected void computeFields() {

            }

            @Override
            public void add(int i, int i1) {

            }

            @Override
            public void roll(int i, boolean b) {

            }

            @Override
            public int getMinimum(int i) {
                return 0;
            }

            @Override
            public int getMaximum(int i) {
                return 0;
            }

            @Override
            public int getGreatestMinimum(int i) {
                return 0;
            }

            @Override
            public int getLeastMaximum(int i) {
                return 0;
            }
        };

        //Buttons for taking a photo or choosing one from the gallery.
        mChoosePicture = (ImageButton) v.findViewById(R.id.select_image);
        mChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("choose/select", "CLICK");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent, "Select Image From Gallery"), 1);
            }
        });

        mTakePicture = (ImageButton) v.findViewById(R.id.take_image);
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("take/take", "CLICK");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 2);
                }
            }
        });

        //Sets up date picker listener
        mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        //Sets onclick of edit text to open data picker
        mDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date d = new Date();
                new DatePickerDialog(ReportFragment.this.getContext(), mDatePicker, 2018, 04, 01)
                        .show();
            }
        });

        //Sets up time picker listener
        mTimePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                mCalendar.set(Calendar.MINUTE, minute);
                updateTimeLabel();
            }
        };

        //Sets on click of edit text to open time picker
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(ReportFragment.this.getContext(), mTimePicker, mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        mCity = (EditText) v.findViewById(R.id.city);

        mState = (Spinner) v.findViewById(R.id.spinner2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.states, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mState.setAdapter(adapter2);

        mDetails = (EditText) v.findViewById(R.id.notes);


        mSharedPref =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

        //Assign "done" button to build URL and start AsyncTask
        Button addSightingButton = (Button) v.findViewById(R.id.add_button);
        addSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildSightingURL(v);
                mSightListener.addSighting(url, mMonster.getSelectedItem().toString(),
                        mDate.getText().toString(), mTime.getText().toString(),
<<<<<<< HEAD
                        mCity.getText().toString(), mState.getSelectedItem().toString(),
                        mDetails.getText().toString());
=======
                        mCity.getText().toString(), mState.getText().toString(),
                        mDetails.getText().toString(), mImage);
>>>>>>> 0c4a4462b8263fac622c2525d9f417b69a1d4f5f
            }
        });

        return v;
    }

    /**
     * This method processes the intent data from choosing or taking a picture, and saves the picture
     *
     * @param RQC The request code sent by launched intents.
     * @param RC The result code.
     * @param data The intent sent through, where we get the photo from.
     */
    @Override
    public void onActivityResult(int RQC, int RC, Intent data) {
        super.onActivityResult(RQC, RC, data);

        if (RQC == 1 && RC == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                mImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                mChoosePicture.setImageBitmap(mImage);
                mTakePicture.setImageBitmap(mImage);
            } catch (IOException e) {
                Log.d("Tag", Log.getStackTraceString(e));
            }
        } else if (RQC == 2 && RC == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            mImage = (Bitmap) extras.get("data");
            mChoosePicture.setImageBitmap(mImage);
            mTakePicture.setImageBitmap(mImage);
        }
    }

    /**
     * Updates the text of the edit text to date chosen
     */
    private void updateDateLabel() {
        mDate.setText(Integer.toString(mCalendar.get(Calendar.YEAR)) + "-" + Integer.toString(mCalendar.get(Calendar.MONTH)) +
                "-" + Integer.toString(mCalendar.get(Calendar.DAY_OF_MONTH)));
    }

    /**
     * Updates text of edit text to time chosen
     */
    private void updateTimeLabel() {
        mTime.setText(Integer.toString(mCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(mCalendar.get(Calendar.MINUTE)));
    }

    /**
     * When the fragment is attached to the app, this instantiates the listener
     * @param context The context the fragment is in
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SightingAddListener) {
            mSightListener = (SightingAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Handles when the fragment is detached, nullifying the listener
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSightListener = null;
    }

    /**
     * Based on the user input into the sign in fields, this builds a URL that will
     * be sent to the database for inputting.
     * @param v The view the user is in
     * @return A string representing the URL created
     */
    private String buildSightingURL(View v) {
        StringBuilder sb = new StringBuilder(REPORT_URL);

        try {

            String id = Integer.toString(0);
            sb.append("&id=");
            sb.append(URLEncoder.encode(id, "UTF-8"));

            String user = mSharedPref.getString(getString(R.string.CURRENT_USER), null);
            sb.append("&user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            String monster = mMonster.getSelectedItem().toString();
            sb.append("&monster=");
            sb.append(URLEncoder.encode(monster, "UTF-8"));


            String datetime = mDate.getText().toString() + " " + mTime.getText().toString();
            sb.append("&date=");
            sb.append(URLEncoder.encode(datetime, "UTF-8"));


            String city = mCity.getText().toString();
            sb.append("&city=");
            sb.append(URLEncoder.encode(city, "UTF-8"));

            String state = mState.getSelectedItem().toString();
            sb.append("&state=");
            sb.append(URLEncoder.encode(state, "UTF-8"));

            String details = mDetails.getText().toString();
            sb.append("&description=");
            sb.append(URLEncoder.encode(details, "UTF-8"));

        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }


        return sb.toString();
    }

    /**
     * Interface of fragment interaction (empty)
     */
    public interface OnFragmentInteractionListener {
    }

    /**
     * Class that creates the spinner object (drop down menu) for the
     * distances
     */
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        /**
         * When an item is selected, do the following
         * @param parent Parent AdapterView
         * @param view View user is in
         * @param pos Position of item selected
         * @param id Id of the item selected
         */
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        /**
         * When nothing is selected, do the following
         * @param parent Parent AdapterView
         */
        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    /**
     * Interface that provides way to begin AsyncTask
     */
    public interface SightingAddListener {
        //Begins AsyncTask
        public void addSighting(String url, String mMonster, String mDate, String mTime,
                                String mCity, String mState, String mDetails, Bitmap image);
    }


}
