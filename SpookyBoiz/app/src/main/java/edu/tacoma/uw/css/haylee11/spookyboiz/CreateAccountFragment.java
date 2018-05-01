package edu.tacoma.uw.css.haylee11.spookyboiz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserAddListener mListener;

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;

    private final static String COURSE_ADD_URL = "http://spookyscarysightings.000webhostapp.com/addUser.php?";
    private static final String TAG = "CreateAccountFragment";



    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
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
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);

        mFirstName = (EditText) v.findViewById(R.id.f_name);
        mLastName = (EditText) v.findViewById(R.id.l_name);
        mUsername = (EditText) v.findViewById(R.id.username);
        mPassword = (EditText) v.findViewById(R.id.password);
        mEmail = (EditText) v.findViewById(R.id.email);


        Button create = (Button) v.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                String url = buildUserURL(view);
                Log.i(TAG, url);
                mListener.addUser(url);
            }
        });

        return v;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserAddListener) {
            mListener = (UserAddListener) context;
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

    private String buildUserURL(View v) {
        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);

        try {
            String first = mFirstName.getText().toString();
            sb.append("first=");
            sb.append(URLEncoder.encode(first, "UTF-8"));

            String last = mLastName.getText().toString();
            sb.append("&last=");
            sb.append(URLEncoder.encode(last, "UTF-8"));

            String username = mUsername.getText().toString();
            sb.append("&username=");
            sb.append(URLEncoder.encode(username, "UTF-8"));

            String pwd = mPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            String email = mEmail.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            Log.i(TAG, sb.toString());
        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
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

    public interface UserAddListener {
        void addUser(String url);
    }

}
