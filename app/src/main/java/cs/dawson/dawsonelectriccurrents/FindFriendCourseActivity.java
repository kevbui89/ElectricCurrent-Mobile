package cs.dawson.dawsonelectriccurrents;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * FindFriendCourseActivity sends a request to find where the selected friend is
 * at that current period in time.
 * Created by: Alessandro Ciotola, Hannah Ly
 *
 */
public class FindFriendCourseActivity extends MenuActivity
{
    // Declare the keys
    private static final String TAG = FindFriendCourseActivity.class.getName();
    private static final String FRIENDEMAIL = "friendEmail";
    private static final String PATTERN = "EEEE";
    private static final String USERNAME = "user";
    private static final String EMAIL = "email";
    private static final String PW = "pw";
    private static final String COURSEJSON = "course";
    private static final String SECTIONJSON = "section";
    private static final String COURSE = "Course: ";
    private static final String SECTION = "\nSection: ";


    private boolean isAvailable = false;
    private TextView friendAvailable;
    private TextView classInfo;
    private String email;
    private String time;
    private String asweek;
    private String password;
    private String course;
    private String section;

    /**
     * The onCreate method is called when the activity first begins.
     * Gets the current time and then starts the AsyncTask.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend_course);

        email = getIntent().getExtras().getString(FRIENDEMAIL);

        friendAvailable = (TextView) findViewById(R.id.availableView);
        classInfo = (TextView) findViewById(R.id.classInfoView);

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN, Locale.US);
        asweek = dateFormat.format(now);

        int hours = new Time(System.currentTimeMillis()).getHours();
        int minutes = new Time(System.currentTimeMillis()).getMinutes();
        time = hours + "" + minutes;

        CourseAsyncTask course = new CourseAsyncTask();
        course.execute();
    }

    /**
     * CourseAsyncTask will send a request to the web api asking for the friends course.
     *
     */
    private class CourseAsyncTask extends AsyncTask<String, Void, ArrayList<String>>
    {
        /**
         * Method done in the background. Will send the request and add the course and section
         * to an ArrayList.
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
            String friendUrl = "http://dawsonfriendfinder2017.herokuapp.com/api/api/whereisfriend?" +
                    "email=" + email + "&password=" + password +
                    "&friendemail=" + email + "&day=" + asweek + "&time=" + time;
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

                Log.d(TAG, "doInBackground: " + returningResults.toString());

                JSONArray jsonObject = new JSONArray(returningResults.toString());
                    course = jsonObject.getJSONObject(0).getString(COURSEJSON);
                    section = jsonObject.getJSONObject(0).getString(SECTIONJSON);
                    Log.d(TAG, "doInBackground: " + course + " " + section);

                if(course != null || !course.equals("") || section != null || !section.equals(""))
                    isAvailable = true;
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        /**
         *
         * Method which will wither display firend not available or show the friend's course.
         *
         * @param s
         */
        @Override
        protected void onPostExecute(ArrayList<String> s)
        {
            super.onPostExecute(s);

            if(!isAvailable)
                friendAvailable.setText(R.string.notAvailableText);
            else {
                friendAvailable.setText(getString(R.string.availableText));
                classInfo.setText(COURSE + course + SECTION + section);
            }

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
