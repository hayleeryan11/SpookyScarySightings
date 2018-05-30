package edu.tacoma.uw.css.haylee11.spookyboiz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.haylee11.spookyboiz.Monster.Monster;
import edu.tacoma.uw.css.haylee11.spookyboiz.R;

public class MonsterDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Monster.db";

    private MonsterDBHelper mMonsterDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    private static final String MONSTER_TABLE = "Monster";

    public MonsterDB(Context context) {
        mMonsterDBHelper = new MonsterDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mMonsterDBHelper.getWritableDatabase();
    }

    /**
     * Inserts a Monster into the database.
     * @param id is the id of the id of the monster.
     * @param name is the name of the monster.
     * @param desc is the monster description.
     * @param last_seen is the date the monster was last seen.
     * @param link is the url link to online resources about the monster.
     * @return the row the data was entered at in the database.
     */
    public boolean insertCourse(String id, String name, String desc, String last_seen, String link) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("desc", desc);
        contentValues.put("last_seen", last_seen);
        contentValues.put("link", link);

        long rowId = mSQLiteDatabase.insert("Monster", null, contentValues);
        return rowId != -1;
    }

    /**
     * Deletes the monster table from the local database.
     */
    public void deleteMonsters() {
        mSQLiteDatabase.delete(MONSTER_TABLE, null, null);
    }

    /**
     * Inner class used to manage the local SQL database.
     */
    class MonsterDBHelper extends SQLiteOpenHelper {

        private final String CREATE_MONSTER_SQL;

        private final String DROP_MONSTER_SQL;

        public MonsterDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
            super(context, name, factory, version);
            CREATE_MONSTER_SQL = context.getString(R.string.CREATE_MONSTER_SQL);
            DROP_MONSTER_SQL = context.getString(R.string.DROP_MONSTER_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_MONSTER_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_MONSTER_SQL);
            onCreate(sqLiteDatabase);
        }
    }

    /**
     * Retrieves the data from the monster database as a list.
     * @return a list of monsters and all their corresponding data.
     */
    public List<Monster> rgetMonster() {
        String[] columns = {"id", "name", "desc", "last_seen", "link"};

        Cursor c = mSQLiteDatabase.query(
                MONSTER_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
        List<Monster> list = new ArrayList<Monster>();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(0);
            String name = c.getString(1);
            String desc = c.getString(2);
            String last_seen = c.getString(3);
            String link = c.getString(4);
            Monster monster = new Monster(name, desc, last_seen, link);
            list.add(monster);
            c.moveToNext();
        }

        return list;
    }
}
