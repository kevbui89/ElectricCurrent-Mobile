package cs.dawson.dawsonelectriccurrents.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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

import cs.dawson.dawsonelectriccurrents.R;

/**
 * AsyncTask which is repsponsible for getting making the request to get all of
 * the friends from the cancelled class selected from the list.
 * Created by: Alessandro Ciotola
 *
 */
public class CancelledFriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>>
{
    private static final String TAG = CancelledFriendsAsyncTask.class.getName();
    private static final String USERNAME = "user";
    private static final String USER = "user";
    private static final String EMAIL = "email";
    private static final String PW = "pw";
    private static final String MAILTO = "mailto";
    private static final String NOCOURSEFOUND = "No course found.";

    private ArrayList<String> friendListNames;
    private ArrayList<String> friendListEmails;

    private String email;
    private String password;
    private String course;
    private String section;

    private Context context;

    /**
     * Constructor which initializes the context, course and section.
     *
     * @param context
     * @param course
     * @param section
     */
    public CancelledFriendsAsyncTask(Context context, String course, String section) {
        this.context = context;
        this.course = course;
        this.section = section;
    }

    /**
     * Method which occurs in the background and will send a request to the web api and
     * receieve an array containing all the friends who have the selected cancelled class.
     *
     * @param urls
     * @return
     */
    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        SharedPreferences prefs = context.getSharedPreferences(USER, context.MODE_PRIVATE);
        if (prefs != null) {
            email = prefs.getString(EMAIL, "");
            password = prefs.getString(PW, "");
        }

        String friendUrl = "http://dawsonfriendfinder2017.herokuapp.com/api/api/coursefriends?" +
                "email=" + email + "&password=" + password + "&course=" + course + "&section=" + section;

        Log.d(TAG, "doInBackground: " + friendUrl);

        try {
            URL url = new URL(friendUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
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
            for (int i = 0; i < jsonArray.length(); i++) {
                String email = jsonArray.getJSONObject(i).getString(EMAIL);
                String name = jsonArray.getJSONObject(i).getString(USERNAME);
                friendListNames.add(name);
                friendListEmails.add(email);
                Log.d(TAG, "doInBackground: " + email + " " + name);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return friendListNames;
    }

    /**
     * Method which will fill the listView.
     *
     * @param s
     */
    @Override
    protected void onPostExecute(ArrayList<String> s) {
        super.onPostExecute(s);
        fillListView();
    }

    /**
     * Method which either sets the listAdapter with all the friends who have that
     * cancelled class, or display no friends in the textview.
     *
     */
    public void fillListView() {
        ListView friendsListView = (ListView) ((Activity) context).findViewById(R.id.friendsCancelledListView);

        if (friendListNames == null || friendListNames.get(0).equalsIgnoreCase(NOCOURSEFOUND)) {
            TextView noFriendsCancelledTV = (TextView) ((Activity) context).findViewById(R.id.noFriendCancelledTV);
            noFriendsCancelledTV.setText(R.string.noFriendsCourse);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.cancelled_class, friendListNames);
            friendsListView.setAdapter(adapter);
            friendsListView.setOnItemClickListener(emailFriends);
        }
    }

    /**
     * Listener which will launch an activity to send an email to a friend.
     *
     */
    private AdapterView.OnItemClickListener emailFriends = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent sendEMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(MAILTO, friendListEmails.get(position), null));
            sendEMail.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.from) + " " + context.getResources().getString(R.string.app_name));
            context.startActivity(Intent.createChooser(sendEMail, context.getResources().getString(R.string.sendemail)));
        }
    };
}