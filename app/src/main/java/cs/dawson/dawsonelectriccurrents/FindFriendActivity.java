package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cs.dawson.dawsonelectriccurrents.cancelled.CancelledActivity;


/**
 * FindFriendActivity sends a request to find all friends and displays a list of friends that the
 * user currently has.
 * Created by: Alessandro Ciotola, Hannah Ly
 *
 */
public class FindFriendActivity extends MenuActivity
{
    private static final String TAG = FindFriendActivity.class.getName();
    private static final String NOFRIENDS = "User has no friends.";
    private static final String FRIENDEMAIL = "friendEmail";
    private static final String USERNAME = "user";
    private static final String EMAIL = "email";
    private static final String PW = "pw";

    private ArrayList<String> friendListNames;
    private ArrayList<String> friendListEmails;
    private String email;
    private String password;

    /**
     * The onCreate method is called when the activity first begins.
     * The AsyncTask is started here.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        FriendsAsyncTask task = new FriendsAsyncTask();
        task.execute();
    }

    /**
     * Method which checks if there are friends returned, if not display a no friends message, else
     * display a list of friends.
     *
     */
    public void fillListView()
    {
        ListView friendsListView = (ListView) findViewById(R.id.friendsListView);

        if(friendListNames == null || friendListNames.get(0).equalsIgnoreCase(NOFRIENDS)){
            TextView noFriendsTV = (TextView) findViewById(R.id.noFriendTV);
            noFriendsTV.setText(R.string.noFriends);
        }
        else
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancelled_class, friendListNames);
            friendsListView.setAdapter(adapter);
            friendsListView.setOnItemClickListener(showFriends);
        }
    }

    /**
     * Listener which takes the selected friend and put it into an intent, then starts the FindFriendCourseActivity.
     *
     */
    private AdapterView.OnItemClickListener showFriends = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(FindFriendActivity.this, FindFriendCourseActivity.class);
            intent.putExtra(FRIENDEMAIL, friendListEmails.get(position));

            startActivity(intent);
        }
    };

    /**
     * FriendAsyncTask sends the request and fills the list.
     *
     */
    private class FriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>>
    {
        /**
         * Method done in the background which sends the request to the web api for all friends.
         *
         * @param urls
         * @return
         */
        @Override
        protected ArrayList<String> doInBackground(String... urls)
        {
            SharedPreferences prefs = getSharedPreferences(USERNAME, MODE_PRIVATE);
            if (prefs != null) {
                // Edit the textviews for the current shared preferences
                email = prefs.getString(EMAIL, "");
                password = prefs.getString(PW, "");
            }

            Log.d(TAG, "doInBackground: " + email + " " + password);

            String friendUrl = "http://dawsonfriendfinder2017.herokuapp.com/api/api/allfriends?" +
                    "email=" + email + "&password=" + password;

            try
            {
                URL url = new URL(friendUrl);
                HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                boolean isError = httpConnection.getResponseCode() >= 400;
                InputStream is = isError ? new BufferedInputStream(httpConnection.getErrorStream()) :
                        new BufferedInputStream(httpConnection.getInputStream());

                String result = "";
                StringBuilder returningResults = new StringBuilder();
                InputStreamReader inStreamResults = new InputStreamReader(is);
                BufferedReader readBuffer = new BufferedReader(inStreamResults);

                while ((result = readBuffer.readLine()) != null)
                    returningResults.append(result);

                JSONArray jsonArray = new JSONArray(returningResults.toString());
                friendListNames = new ArrayList<>();
                friendListEmails = new ArrayList<>();
                for(int i = 0 ; i< jsonArray.length(); i++)
                {
                    String email = jsonArray.getJSONObject(i).getString("email");
                    String name = jsonArray.getJSONObject(i).getString("name");
                    friendListNames.add(name);
                    friendListEmails.add(email);
                    Log.d(TAG, "doInBackground: " + email + " " + name);
                }
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            return friendListNames;
        }

        /**
         * Method which fills the array after the lists have been set.
         *
         * @param s
         */
        @Override
        protected void onPostExecute(ArrayList<String> s)
        {
            super.onPostExecute(s);
            fillListView();
        }
    }

    /**
     * Method which calls the super method on onCreateOptionsMenu to display the menu. Required
     * so code to show the menu will not have to be repeated for each activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Method which calls the super method on onOptionsItemSelected to add functionality to the menu
     * buttons without having to repeat the code for each activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

}