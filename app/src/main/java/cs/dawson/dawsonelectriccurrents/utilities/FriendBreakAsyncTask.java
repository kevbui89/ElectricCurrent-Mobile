package cs.dawson.dawsonelectriccurrents.utilities;

import android.os.AsyncTask;
import android.util.Log;
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

import cs.dawson.dawsonelectriccurrents.FIndBreakActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.adapters.FriendAdapter;

/**
 * This class takes care of the async task of loading API calls
 * @author Kevin Bui
 * @version 1.0
 */

public class FriendBreakAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

    private FIndBreakActivity context;
    private String email;
    private String password;
    private String day;
    private String startTime;
    private String endTime;
    private ArrayList<String> names;
    private ArrayList<String> emails;
    private static final String NOFRIENDS = "No friend found";

    /**
     * Constructor
     * @param fba
     * @param email
     * @param password
     * @param day
     * @param startTime
     * @param endTime
     */
    public FriendBreakAsyncTask(FIndBreakActivity fba, String email, String password, String day, String startTime, String endTime) {
        this.context = fba;
        this.email = email;
        this.password = password;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    protected ArrayList<String> doInBackground(String... urls)
    {
        String friendUrl = "http://dawsonfriendfinder2017.herokuapp.com/api/api/friendbreaks?" +
                "email=" + email + "&password=" + password + "&day=" + day + "&starttime=" + startTime
                + "&endtime=" + endTime;

        logIt("URLFRIENDS: " + friendUrl);

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
            names = new ArrayList<>();
            emails = new ArrayList<>();
            for(int i = 0 ; i< jsonArray.length(); i++)
            {
                String email = jsonArray.getJSONObject(i).getString("email");
                String name = jsonArray.getJSONObject(i).getString("name");
                names.add(name);
                emails.add(email);
                Log.d("LINE", "doInBackground: " + email + " " + name);
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        return names;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s)
    {
        super.onPostExecute(s);
        fillListView();
    }

    /**
     * Fills the list view with friends
     */
    public void fillListView()
    {
        ListView friendsListView = (ListView) context.findViewById(R.id.listViewFriends);

        if(names == null || names.get(0).equalsIgnoreCase(NOFRIENDS)){
            TextView noFriendsTV = (TextView) context.findViewById(R.id.noFriendsBreaks);
            noFriendsTV.setText(context.getResources().getString(R.string.nofriendsonbreak));
        }
        else
        {
            FriendAdapter adapter = new FriendAdapter(context, names, emails);
            friendsListView.setAdapter(adapter);
        }
    }

    /**
     * Method to easily log to logcat
     *
     * @param msg to be printed to logcat
     */
    public static void logIt(String msg) {
        final String TAG = "---------WEATHER: ";
        Log.d(TAG, msg);
    }
}
