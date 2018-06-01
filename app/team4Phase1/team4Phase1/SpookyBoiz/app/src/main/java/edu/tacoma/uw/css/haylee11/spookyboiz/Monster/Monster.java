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
 * Monster Item class, defines a monster
 *
 * @author Haylee Ryan, Matt Frazier, Kai Stansfield
 */
public class Monster implements Serializable {

    /**
     * Tag for debugging
     */
    private static final String TAG = "Monster";

    /**
     * Monster ID
     */
    public static final String ID = "monster_id";

    /**
     * Monster name
     */
    public static final String MONSTER = "name";

    /**
     * Date monster last seen
     */
    public static final String LAST_SEEN = "last_seen";

    /**
     * Description of monster
     */
    public static final String DESC = "description";

    //Name of the monster
    String mMonster;

    //Monster ID
    int mId;

    //Monster description
    String mDescription;

    //Date the mosnter was last seen based on sightings
    String mLastSeen;

    /**
     * Constructs a new monster item
     * @param id Monster ID
     * @param monster Name
     * @param description Details of monster
     * @param lastSeen Date last spotted
     */
    public Monster(int id, String monster, String description, String lastSeen) {
        mId = id;
        mMonster = monster;
        mLastSeen = lastSeen;
        mDescription = description;
    }

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


    /**
     * Takes a JSON and parses it into strings, then putting those in a list
     * of monsters to display
     * @param monsterJSON The JSON with monster information
     * @return A list of monsters to display
     * @throws JSONException
     */
    public static List<Monster> parseCourseJSON(String monsterJSON) throws JSONException {
        List<Monster> monsterList = new ArrayList<Monster>();
        if (monsterJSON != null) {
            JSONArray arr = new JSONArray(monsterJSON);
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
