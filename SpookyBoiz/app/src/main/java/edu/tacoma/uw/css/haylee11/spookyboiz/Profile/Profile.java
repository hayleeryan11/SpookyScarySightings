package edu.tacoma.uw.css.haylee11.spookyboiz.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.OtherProfilesFragment;
import edu.tacoma.uw.css.haylee11.spookyboiz.ProfileFragment;

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
    private String mUsername;


    //First name
    private String mFName;

    //Last name
    private String mLName;

    //Number of sightings
    private int mSightings;

    //Favorite/most sighted monster
    private String mFavorite;

    //Bio of user
    private String mBio;


    /**
     * Constructor for Profile object
     *
     * @param un Username
     * @param f_name First Name
     * @param l_name Last name
     * @param sightings Number of Sightings
     * @param favorite Favorite monster
     * @param bio Short bio of user
     */
    public Profile(String un, String f_name, String l_name, int sightings, String favorite, String bio) {
        if (un.isEmpty()) {
            throw new IllegalArgumentException("Cannot create profile: There is no username");
        } else if (f_name.isEmpty()) {
            throw new IllegalArgumentException("Cannot create profile: There is no first name");
        } else if (l_name.isEmpty()) {
            throw new IllegalArgumentException("Cannot create profile: There is no last name");
        } else {
            mUsername = un;
            mFName = f_name;
            mLName = l_name;
            mSightings = sightings;
            mFavorite = favorite;
            mBio = bio;
        }
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


    /**
     * Parses the JSON of a single profile of the current user
     *
     * @param profileJSON The incoming JSON of the user information
     * @return The profile create for the user
     * @throws JSONException
     */
    public static Profile parseCourseJSON(String profileJSON) throws JSONException {

        if (profileJSON != null) {
            JSONObject obj = new JSONObject(profileJSON);
            Profile prof = new Profile(obj.getString(Profile.USERNAME), obj.getString(Profile.F_NAME), obj.getString(Profile.L_NAME), obj.getInt(Profile.SIGHTINGS),
                    obj.getString(Profile.FAVORITE), obj.getString(Profile.BIO));

            return prof;

        }

        return null;

    }

    /**
     * Takes a JSON and parses it into strings, then putting those in a list
     * of monsters to display
     * @param profileJSON The JSON with monster information
     * @return A list of monsters to display
     * @throws JSONException
     */
    public static List<Profile> parseProfileListJSON(String profileJSON) throws JSONException {
        List<Profile> profileList = new ArrayList<Profile>();
        if (profileJSON != null) {
            JSONArray arr = new JSONArray(profileJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Profile profile = new Profile(obj.getString(Profile.USERNAME), obj.getString(Profile.F_NAME), obj.getString(Profile.L_NAME), obj.getInt(Profile.SIGHTINGS),
                        obj.getString(Profile.FAVORITE), obj.getString(Profile.BIO));
                profileList.add(profile);
            }
        }
        return profileList;
    }

}

