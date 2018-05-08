package edu.tacoma.uw.css.haylee11.spookyboiz.Sighting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hayleeryan on 5/7/18.
 */

public class Sighting implements Serializable {

    String mUsername;
    String mId;
    String mDate;
    String mTime;
    String mLocation;
    String mMonster;

    public Sighting(String id, String un, String monster, String date, String time, String local) {
        mUsername = un;
        mId = id;
        mDate = date;
        mTime = time;
        mLocation = local;
        mMonster = monster;
    }

    public static final String ID = "id";
    public static final String USERNAME = "user";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String LOCAL = "location";
    public static final String MONSTER = "monster";

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

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmMonster() {
        return mMonster;
    }

    public void setmMonster(String mMonster) {
        this.mMonster = mMonster;
    }

    public static List<Sighting> parseCourseJSON(String courseJSON) throws JSONException {
        List<Sighting> sightList = new ArrayList<Sighting>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Sighting sight = new Sighting(obj.getString(Sighting.ID), obj.getString(Sighting.USERNAME), obj.getString(Sighting.MONSTER)
                        , obj.getString(Sighting.DATE), obj.getString(Sighting.TIME), obj.getString(Sighting.LOCAL));
                sightList.add(sight);
            }
        }
        return sightList;
    }

}
