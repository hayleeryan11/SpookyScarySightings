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

    public static final String LINK = "link";

    //Name of the monster
    String mMonster;

    //Monster ID
    String mId;

    //Monster description
    String mDescription;

    //Date the mosnter was last seen based on sightings
    String mLastSeen;

    String mLink;


    /**
     * Constructs a new monster item
     * @param id Monster ID
     * @param monster Name
     * @param description Details of monster
     * @param lastSeen Date last spotted
     */
    public Monster(String id, String monster, String description, String lastSeen, String link) {
        mId = id;
        mMonster = monster;
        mLastSeen = lastSeen;
        mDescription = description;
        mLink = link;
    }

    public String getmMonster() {
        return mMonster;
    }

    public void setmMonster(String mMonster) {
        this.mMonster = mMonster;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
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

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
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
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Monster monster = new Monster(obj.getString(Monster.ID), obj.getString(Monster.MONSTER), obj.getString(Monster.DESC),
                        obj.getString(Monster.LAST_SEEN), obj.getString(Monster.LINK));
                monsterList.add(monster);
            }
        }
        return monsterList;
    }
}
