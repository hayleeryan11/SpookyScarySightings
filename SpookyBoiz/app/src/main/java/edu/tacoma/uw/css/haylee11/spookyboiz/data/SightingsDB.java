package edu.tacoma.uw.css.haylee11.spookyboiz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.R;
import edu.tacoma.uw.css.haylee11.spookyboiz.Sighting.Sighting;

public class SightingsDB {

    private final String SIGHTINGS_TABLE = "Sightings";
    public static final int DB_VERSION = 1;
    public static final String DB_NAME="Sightings.db";

    private SightingsDBHelper mSightingsDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Constructor for the SightingsDB class.
     * @param context
     */
    public SightingsDB(Context context) {
        mSightingsDBHelper = new SightingsDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mSightingsDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the sighting into the local sqlite table.
     * @param id is the sighting id.
     * @param user is the user who made the sighting.
     * @param monster is the monster in the sighting.
     * @param date is the date of the sighting.
     * @param time is the time of the sighting.
     * @param city is the city where the sighting was made.
     * @param state is the state where the sighting was made.
     * @param description is the description of the sighting.
     * @return true iff adding the sighting to the table was successful.
     */
    public boolean insertSighting(int id, String user, String monster, String date, String time,
                                  String city, String state, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("user", user);
        contentValues.put("monster", monster);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("city", city);
        contentValues.put("state", state);
        contentValues.put("description", description);

        long rowId = mSQLiteDatabase.insert("Sightings", null, contentValues);
        return rowId != -1;
    }

    /**
     * Delete all the data from the SIGHTINGS_TABLE.
     */
    public void deleteSightings() {
        mSQLiteDatabase.delete(SIGHTINGS_TABLE, null, null);
    }

    public List<Sighting> getSightings() {

        String[] columns = {
                "id", "user", "monster", "date", "time", "city", "state", "description"
        };

        Cursor c = mSQLiteDatabase.query(
                SIGHTINGS_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        c.moveToFirst();
        List<Sighting> list = new ArrayList<Sighting>();
        for (int i = 0; i < c.getCount(); i++) {
            int id = c.getInt(0);
            String user = c.getString(1);
            String monster = c.getString(2);
            String date = c.getString(3);
            String time = c.getString(4);
            String city = c.getString(5);
            String state = c.getString(6);
            String description = c.getString(7);
            Sighting sighting = new Sighting(
                    id, user, monster, date, time, city, state, description, 0);
            list.add(sighting);
            c.moveToNext();
        }
        return list;
    }

    /**
     * Inner class used to manage the local Sightings database.
     */
    class SightingsDBHelper extends SQLiteOpenHelper {

        private final String CREATE_SIGHTINGS_SQL;

        private final String DROP_SIGHTINGS_SQL;

        /**
         * Constructor for the DBHelper.
         * @param context
         * @param name
         * @param factory
         * @param version
         */
        public SightingsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                 int version) {
            super(context, name, factory, version);
            CREATE_SIGHTINGS_SQL = context.getString(R.string.CREATE_SIGHTINGS_SQL);
            DROP_SIGHTINGS_SQL = context.getString(R.string.DROP_SIGHTINGS_SQL);
        }

        /**
         * When the app is created, create the db.
         * @param sqLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_SIGHTINGS_SQL);
        }

        /**
         * When the app is updated, recreate the db.
         * @param sqLiteDatabase
         * @param i
         * @param i1
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_SIGHTINGS_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
