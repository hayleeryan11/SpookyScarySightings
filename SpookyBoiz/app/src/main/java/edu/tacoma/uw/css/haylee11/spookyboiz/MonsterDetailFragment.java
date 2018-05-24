package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;


/**
 * Fragment for the details of a monster. Opened when user clicks on
 * monster in list view.
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class MonsterDetailFragment extends Fragment {

    /**
     * First parameter
     */
    private static final String ARG_PARAM1 = "param1";

    /**
     * Second parameter
     */
    private static final String ARG_PARAM2 = "param2";

    /**
     * The monster that has been selected to see the details of
     */
    public static final String MONSTER_SELECTED = "monster_selected";

    public static final String TAG = "monster detail";

    // First parameter
    private String mParam1;

    //Second parameter
    private String mParam2;

    //Monster name
    private TextView  mMonster;

    //Monster description
    private TextView  mDescription;

    //When monster was last seen
    private TextView  mLastSeen;

    private Button mLink;

    //Listener for fragment interaction
    private OnFragmentInteractionListener mListener;

    /**
     * Required empty constructor
     */
    public MonsterDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonsterDetailFragment.
     */
    public static MonsterDetailFragment newInstance(String param1, String param2) {
        MonsterDetailFragment fragment = new MonsterDetailFragment();
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
     * When the fragment is create, this instantiates the view. In this case,
     * set the values of the TextViews
     * @param inflater The layout inflater
     * @param container The container the fragment is in
     * @param savedInstanceState The saved instance state
     * @return The view to be presented
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_monster_detail, container, false);

        getActivity().setTitle("Monster Details");

        //Set values of TextViews with retrieved data
        mMonster = (TextView) v.findViewById(R.id.monster);
        mLastSeen = (TextView) v.findViewById(R.id.last_seen);
        mDescription = (TextView) v.findViewById(R.id.desc);
        mLink = (Button) v.findViewById(R.id.link);



        return v;
    }

    /**
     * When the fragment is attached to the app, this instantiates the listener
     * @param context The context the fragment is in
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    }

    /**
     * Decides what happens when the app is resumed. In this care, the detail page
     * is updated
     */
    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if(args != null) {
            onUpdate((Monster)args.getSerializable(MONSTER_SELECTED));
        }
    }

    /**
     * Sets the text of the TextViews in the layout to the details
     * @param monster The monster the user is viewing the details of
     */
    public void onUpdate(final Monster monster) {
        if(monster != null) {
            Log.d(TAG, monster.getmMonster() + monster.getmLastSeen() + monster.getmLink());
            mMonster.setText(monster.getmMonster());
            if (monster.getmLastSeen() != null) {
                mLastSeen.setText("Last seen: \n" + monster.getmLastSeen());
            }
            if (monster.getmDescription() != null) {

                mDescription.setText(monster.getmDescription());
            }

            mLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(monster.getmLink()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
