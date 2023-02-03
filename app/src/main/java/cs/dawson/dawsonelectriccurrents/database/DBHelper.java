package cs.dawson.dawsonelectriccurrents.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBHelper class which is responsible for creating the database and tables required for
 * the Notes section of the application. Contains methods required to get a specific note from
 * the database or insert a new note into the database. *
 * @author Alessandro Ciotola
 * @version 2017/11/25
 *
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NOTES = "notes";
    public static final String COL_ID = "_id";
    public static final String COL_SHORTNOTE = "shortNote";
    public static final String COL_FULLNOTE = "fullNnote";
    private static DBHelper dbHelper = null;

    /**
     * Constructor that calls the super constructor which creates a helper
     * object to create, open, and/or manage a database.
     *
     * @param context
     */
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Method which is called when the database is made for the first time.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createDatabase(db);
    }

    /**
     * Method which is called when the database is needs to be upgraded.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DBHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion);
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        }
        catch (SQLException ex)
        {
            Log.e(DBHelper.class.getName(), "onUpgrade: " + ex.getMessage());
            throw ex;
        }
        createDatabase(db);
    }

    /**
     * Method which creates the table required for keeping notes.
     *
     * @param db
     */
    private void createDatabase(SQLiteDatabase db)
    {
        try
        {
            String createNoteDB = "create table " + TABLE_NOTES + "(" +
                    COL_ID + " integer primary key autoincrement, " +
                    COL_SHORTNOTE + " text not null, " +
                    COL_FULLNOTE + " text not null);";
            db.execSQL(createNoteDB);
        }
        catch(SQLException ex)
        {
            Log.d(DBHelper.class.getName(), "CreateDatabase: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Method which ensures that only one database helper exists across the app's lifecycle
     *
     * @param context
     * @return
     */
    public static DBHelper getDBHelper(Context context)
    {
        if (dbHelper == null)
            dbHelper = new DBHelper(context.getApplicationContext());
        return dbHelper;
    }

    /**
     * Method which will get all of the notes from the database.
     *
     * @return
     */
    public Cursor getNotes()
    {
        return getReadableDatabase().query(TABLE_NOTES, null, null, null, null, null, null);
    }

    /**
     * Method which will insert a new note into the database.
     *
     * @param sNote
     * @param fNote
     * @return
     */
    public long insertNote(String sNote, String fNote)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_SHORTNOTE, sNote);
        cv.put(COL_FULLNOTE, fNote);

        long code = getWritableDatabase().insert(TABLE_NOTES, null, cv);
        return code;
    }

    /**
     * Method which will remove a note from the database
     *
     * @param id
     * @return
     */
    public int removeNote(int id)
    {
        return getWritableDatabase().delete(TABLE_NOTES, COL_ID + "=?",
                new String[] { String.valueOf(id) });
    }
}
