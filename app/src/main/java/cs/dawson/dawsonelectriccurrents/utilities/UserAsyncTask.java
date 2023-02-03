package cs.dawson.dawsonelectriccurrents.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;

import cs.dawson.dawsonelectriccurrents.SettingsActivity;
import cs.dawson.dawsonelectriccurrents.beans.User;
import cs.dawson.dawsonelectriccurrents.database.FriendFinderDBHelper;

/**
 * Performs database manipulation with user table using background thread
 * @author Kevin
 * @version 1.0
 */

public class UserAsyncTask extends AsyncTask<Void, Void, ArrayList<User>> {

    private static final String TAG = "UserAsyncTask";
    private Activity activity;
    private Options option;
    private FriendFinderDBHelper database;
    private String data[];
    private String email;
    private final String USER_PREFS = "user";

    // Keys
    private static final String USERID = "userId";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "email";
    private static final String PW = "pw";
    private static final String LASTUPDATED = "lastUpdated";

    /**
     * Constructor
     * @param option
     * @param settings
     * @param database
     * @param data
     */
    public UserAsyncTask(Options option, SettingsActivity settings, FriendFinderDBHelper database, String[] data) {
        this.option = option;
        this.activity = settings;
        this.database = database;
        this.data = data;
    }

    /**
     * Constructor
     * @param option
     * @param context
     * @param database
     * @param email
     */
    public UserAsyncTask(Options option, Activity context, FriendFinderDBHelper database, String email) {
        this.option = option;
        this.activity = context;
        this.database = database;
        this.email = email;
    }

    /**
     * Returns an ArrayList of the user(s) with the specified email
     * Not that multiple users should ever be sent, be we do not have email as primary key, so I guess
     * on a rare occassion this could happen.
     * @param voids
     * @return
     */
    @Override
    protected ArrayList<User> doInBackground(Void... voids) {
        ArrayList<User> list = null;

        switch (option) {
            case ADD_USER:
                database.addUser(data);
                break;
            case MODIFY_USER:
                database.editUser(data);
                break;
            default:
                list = new ArrayList<User>();
                break;
        }

        // Get the current user
        list = database.retrieverUserByEmail(data[2]);
        Log.i(TAG, "Email: " + data[2]);
        User currUser = list.get(0);
        Log.i(TAG, "Current User Email: " + currUser.getEmail());

        // Save in shared preferences
        SharedPreferences prefs = activity.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putInt(USERID, currUser.getUserId());
        e.putString(FIRSTNAME, currUser.getFirstName());
        e.putString(LASTNAME, currUser.getLastName());
        e.putString(EMAIL, currUser.getEmail());
        e.putString(PW, currUser.getPassword());
        e.putString(LASTUPDATED, currUser.getLastUpdated());
        e.commit();
        return list;
    }
}
