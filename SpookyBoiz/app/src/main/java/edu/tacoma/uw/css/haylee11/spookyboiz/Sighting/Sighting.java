package edu.tacoma.uw.css.haylee11.spookyboiz.Sighting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.R;

/**
 * Sighting Item class, defines a Sighting
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class Sighting implements Serializable {

    /**
     * Sighting ID
     */
    public static final String ID = "id";

    /**
     * Username of who reported
     */
    public static final String USERNAME = "user";

    /**
     * Date of sighting
     */
    public static final String DATE = "date";

    /**
     * Time of sighting
     */
    public static final String TIME = "time";

    /**
     * City where sighting was
     */
    public static final String CITY = "city";

    /**
     * State where sighting was
     */
    public static final String STATE = "state";

    /**
     * Monster sighted
     */
    public static final String MONSTER = "monster";

    /**
     * Description of sighting
     */
    public static final String DESC = "description";

    public static final String TAG = "sight";

    //Username of who reported
    String mUsername;

    //Sighting ID
    String mId;

    //Date of sighting
    String mDate;

    //Time of sighting
    String mTime;

    //City where mosnter sighted
    String mCity;

    //State where monster sighting
    String mState;

    //Monster sighted
    String mMonster;

    //Decription of sighting
    String mDesc;

    /**
     * Constructs a new Sighting item
     * @param id ID of sighting
     * @param un Username of user
     * @param monster Monster name
     * @param date Date of sighting
     * @param time Time of sighting
     * @param city City where sighting was
     * @param state State where sighting was
     * @param desc Description of sighting
     */
    public Sighting(String id, String un, String monster, String date, String time, String city, String state, String desc) {
        mUsername = un;
        mId = id;
        mDate = date;
        mTime = time;
        mCity = city;
        mState = state;
        mMonster = monster;
        mDesc = desc;
    }


    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmMonster() {
        return mMonster;
    }

    public void setmMonster(String mMonster) {
        this.mMonster = mMonster;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    /**
     * Takes a JSON and parses it into strings, then putting those in a list
     * of sightings to display
     * @param sightJSON The JSON with sighting information
     * @return A list of monsters to display
     * @throws JSONException
     */
    public static List<Sighting> parseCourseJSON(String sightJSON, int flag, String user1, String user2) throws JSONException {

        List<Sighting> sightList = new ArrayList<Sighting>();
        if (sightJSON != null) {
            JSONArray arr = new JSONArray(sightJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String[] date_time = obj.getString(Sighting.DATE).split("\\s+");
                Sighting sight = new Sighting(obj.getString(Sighting.ID), obj.getString(Sighting.USERNAME), obj.getString(Sighting.MONSTER)
                        , date_time[0], date_time[1], obj.getString(Sighting.CITY), obj.getString(Sighting.STATE),
                        obj.getString(Sighting.DESC));
                if (flag == 1) {
                    if (obj.getString(Sighting.USERNAME).equals(user1)) {
                        sightList.add(sight);
                    }
                } else if (flag == 0) {
                    sightList.add(sight);
                } else if (flag == 2) {
                    if (obj.getString(Sighting.USERNAME).equals(user2)) {
                        sightList.add(sight);
                    }
                }
            }
        }
        return sightList;
    }

}
