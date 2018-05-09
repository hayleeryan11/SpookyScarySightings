package edu.tacoma.uw.css.haylee11.spookyboiz.Monster;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

/**
 * Created by hayleeryan on 5/7/18.
 */

public class Monster implements Serializable {

    String mMonster;
    int mId;
    String mDescription;
    String mLastSeen;

    private static final String TAG = "Monster";

    public Monster(int id, String monster, String description, String lastSeen) {
        mId = id;
        mMonster = monster;
        mLastSeen = lastSeen;
        mDescription = description;
    }

    public static final String ID = "monster_id";
    public static final String MONSTER = "name";
    public static final String LAST_SEEN = "last_seen";
    public static final String DESC = "description";



    public String getmMonster() {
        return mMonster;
    }

    public void setmMonster(String mMonster) {
        this.mMonster = mMonster;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmLastSeen() {
        return mLastSeen;
    }

    public void setmLastSeen(String mLastSeen) {
        this.mLastSeen = mLastSeen;
    }


    public static List<Monster> parseCourseJSON(String courseJSON) throws JSONException {
        List<Monster> monsterList = new ArrayList<Monster>();
        if (courseJSON != null) {
            JSONArray arr = new JSONArray(courseJSON);
            Log.i(TAG, "why");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Monster monster = new Monster(obj.getInt(Monster.ID), obj.getString(Monster.MONSTER), obj.getString(Monster.DESC),
                        obj.getString(Monster.LAST_SEEN));
                monsterList.add(monster);
            }
        }
        return monsterList;
    }
}
