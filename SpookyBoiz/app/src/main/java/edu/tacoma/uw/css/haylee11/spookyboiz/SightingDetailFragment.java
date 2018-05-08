package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SightingDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SightingDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SightingDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mUsername;
    private TextView mDate;
    private TextView mTime;
    private TextView mLocation;
    private TextView mMonster;

    public static final String SIGHTING_SELECTED = "sighting_selected";

    private OnFragmentInteractionListener mListener;

    public SightingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SightingDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SightingDetailFragment newInstance(String param1, String param2) {
        SightingDetailFragment fragment = new SightingDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sighting_detail, container, false);
        mUsername = (TextView) v.findViewById(R.id.username);
        mMonster = (TextView) v.findViewById(R.id.monster);
        mLocation = (TextView) v.findViewById(R.id.location);
        mDate = (TextView) v.findViewById(R.id.date);
        mTime = (TextView) v.findViewById(R.id.time);



        return v;
    }

    public void updateView(Sighting sight) {
        if (sight != null) {
            mUsername.setText(sight.getmUsername());
            mLocation.setText(sight.getmLocation());
            mDate.setText(sight.getmDate());
            mTime.setText(sight.getmTime());
            mMonster.setText(sight.getmMonster());
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if(args != null) {
            updateView((Sighting)args.getSerializable(SIGHTING_SELECTED));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
