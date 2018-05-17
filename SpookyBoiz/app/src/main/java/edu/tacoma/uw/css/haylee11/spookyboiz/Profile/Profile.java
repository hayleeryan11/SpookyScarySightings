package edu.tacoma.uw.css.haylee11.spookyboiz.Profile;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

/**
 * Created by hayleeryan on 5/13/18.
 */

public class Profile  implements Serializable {

    public static final String TAG = "Profile";


    /**
     * Username
     */
    public static final String USERNAME = "username";

    /**
     * First name
     */
    public static final String F_NAME = "f_name";

    /**
     * Last name
     */
    public static final String L_NAME = "l_name";

    /**
     * Number of sightings
     */
    public static final String SIGHTINGS = "sightings";

    /**
     * Favorite/most sighted monster
     */
    public static final String FAVORITE = "favorite";

    /**
     * Bio of user
     */
    public static final String BIO = "bio";




    //Username
    String mUsername;


    //First name
    String mFName;

    //Last name
    String mLName;

    //Number of sightings
    int mSightings;

    //Favorite/most sighted monster
    String mFavorite;

    //Bio of user
    String mBio;


    public Profile(String un, String f_name, String l_name, int sightings, String favorite, String bio) {
        mUsername = un;
        mFName = f_name;
        mLName = l_name;
        mSightings = sightings;
        mFavorite = favorite;
        mBio = bio;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmFName() {
        return mFName;
    }

    public void setmFName(String mFName) {
        this.mFName = mFName;
    }

    public String getmLName() {
        return mLName;
    }

    public void setmName(String mLName) {
        this.mLName = mLName;
    }

    public int getmSightings() {
        return mSightings;
    }

    public void setmSightings(int mSightings) {
        this.mSightings = mSightings;
    }

    public String getmFavorite() {
        return mFavorite;
    }

    public void setmFavorite(String mFavorite) {
        this.mFavorite = mFavorite;
    }

    public String getmBio() {
        return mBio;
    }

    public void setmBio(String mBio) {
        this.mBio = mBio;
    }

    public static Profile parseCourseJSON(String profileJSON) throws JSONException {

        if (profileJSON != null) {
            JSONObject obj = new JSONObject(profileJSON);
                //JSONObject obj = new JSONObject(profileJSON);
                Profile prof = new Profile(obj.getString(Profile.USERNAME), obj.getString(Profile.F_NAME), obj.getString(Profile.L_NAME), obj.getInt(Profile.SIGHTINGS),
                        obj.getString(Profile.FAVORITE), obj.getString(Profile.BIO));

                return prof;

        }

        return null;

    }

//    public interface profileFace {
//        public Profile getProfile(String result);
//    }
}

