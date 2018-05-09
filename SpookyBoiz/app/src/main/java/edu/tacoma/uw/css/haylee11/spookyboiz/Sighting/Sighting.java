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
    String mCity;
    String mState;
    String mMonster;
    String mDesc;

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

    public static final String ID = "id";
    public static final String USERNAME = "user";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String MONSTER = "monster";
    public static final String DESC = "description";

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

    public static List<Sighting> parseCourseJSON(String courseJSON) throws JSONException {
        List<Sighting> sightList = new ArrayList<Sighting>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String[] date_time = obj.getString(Sighting.DATE).split("\\s+");
                Sighting sight = new Sighting(obj.getString(Sighting.ID), obj.getString(Sighting.USERNAME), obj.getString(Sighting.MONSTER)
                        , date_time[0], date_time[1], obj.getString(Sighting.CITY), obj.getString(Sighting.STATE),
                        obj.getString(Sighting.DESC));
                sightList.add(sight);
            }
        }
        return sightList;
    }

}
