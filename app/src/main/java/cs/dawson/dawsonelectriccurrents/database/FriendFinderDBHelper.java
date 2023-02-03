package cs.dawson.dawsonelectriccurrents.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cs.dawson.dawsonelectriccurrents.beans.User;

/**
 * DAO class providing all the CRUD functionality for the database
 * @author  Kevin
 * @version 1.0
 */

public class FriendFinderDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "FriendFinderDBHelper";
    private static final String DB_NAME = "friendfinder.db";
    private static final int DB_VERSION = 5;

    // User Table components
    public static final String TABLE_USERS = "USERS";
    public static final String COLUMN_USERID = "USER_ID";
    public static final String COLUMN_FIRSTNAME = "FNAME";
    public static final String COLUMN_LASTNAME = "LNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_USERPW = "USERPW";
    public static final String COLUMN_LASTUPDATED = "LASTUPDATED";

    private static final String TABLE1_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRSTNAME + " TEXT NOT NULL," +
                    COLUMN_LASTNAME + " TEXT NOT NULL," +
                    COLUMN_EMAIL + " TEXT NOT NULL," +
                    COLUMN_USERPW + " TEXT NOT NULL," +
                    COLUMN_LASTUPDATED + " TEXT NOT NULL DEFAULT current_timestamp" +
                    ");";

    /**
     * Constructor
     * @param context
     */
    public FriendFinderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE1_CREATE_USERS);
        Log.i(TAG, "The tables have been created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        Log.i(TAG, "The tables have been dropped.");
    }

    /**
     * Creates a new user in the database.
     * @param user
     */
    public void addUser(String[] user) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues content = new ContentValues();

        // Put the data in the appropriate column
        content.put(COLUMN_FIRSTNAME, user[0]);
        content.put(COLUMN_LASTNAME, user[1]);
        content.put(COLUMN_EMAIL, user[2]);
        content.put(COLUMN_USERPW, user[3]);
        content.put(COLUMN_LASTUPDATED, new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()));

        // Insert
        database.insert(TABLE_USERS, null, content);
        database.close();
    }

    /**
     * Edits an existing user.
     * @param user
     */
    public int editUser(String[] user) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put(COLUMN_FIRSTNAME, user[0]);
        content.put(COLUMN_LASTNAME, user[1]);
        content.put(COLUMN_EMAIL, user[2]);
        content.put(COLUMN_USERPW, user[3]);
        content.put(COLUMN_LASTUPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        String whereClause = COLUMN_USERID + "=?";
        String[] whereArguments = { "1" };
        int affectedRows = database.update(TABLE_USERS, content, whereClause, whereArguments);

        return affectedRows;
    }

    /**
     * Returns a list of user with the email provided
     * @param email
     * @return
     */
    public ArrayList<User> retrieverUserByEmail(String email) {
        ArrayList<User> list = new ArrayList<User>();
        SQLiteDatabase database = getReadableDatabase();

        String[] col = { COLUMN_USERID, COLUMN_FIRSTNAME, COLUMN_LASTNAME, COLUMN_EMAIL, COLUMN_USERPW, COLUMN_LASTUPDATED };
        Cursor cursor = database.query(TABLE_USERS, col, COLUMN_EMAIL + "=?", new String[] {email}, null, null, null);

        // Fetch the database for each row
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USERID));
            String fname = cursor.getString(cursor.getColumnIndex(COLUMN_FIRSTNAME));
            String lname = cursor.getString(cursor.getColumnIndex(COLUMN_LASTNAME));
            String mail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_USERPW));
            String lastUpdated = cursor.getString(cursor.getColumnIndex(COLUMN_LASTUPDATED));
            User user = new User(id, fname, lname, mail, password, lastUpdated);
            list.add(user);
        }
        database.close();
        return list;
    }
}
