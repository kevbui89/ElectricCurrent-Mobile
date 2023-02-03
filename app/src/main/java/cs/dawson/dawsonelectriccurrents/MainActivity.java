package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import cs.dawson.dawsonelectriccurrents.cancelled.CancelledActivity;
import cs.dawson.dawsonelectriccurrents.database.FriendFinderDBHelper;
import cs.dawson.dawsonelectriccurrents.notes.NotesActivity;
import cs.dawson.dawsonelectriccurrents.weatherrequest.GPSTracker;
import cs.dawson.dawsonelectriccurrents.weatherrequest.WeatherRequest;

/**
 * This is the startup activity which launches if the user has credentials
 * @author Kevin Bui
 * @author Alessandro Ciotola
 * @author Hannah Ly
 * @author Maxime Lacasse
 * @version 1.0
 */

public class MainActivity extends MenuActivity
{
    private final static String TAG = MainActivity.class.getName();
    private FriendFinderDBHelper database;
    private final String USERS_PREFS = "user";
    private ImageView dawsonLogo;
    private String email;
    private String fname;
    private String lname;
    private String pw;

    // Declare the keys
    private static final String EMAIL = "email";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String PASSWORD = "pw";

    //Current weather variables
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    //Current temperature text view
    TextView currentTempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initalizing current temperature textView
        currentTempTextView = findViewById(R.id.currentTempTextView);

        //Current weather start up, display the temperature depending on device's location.
        //If the device doesn't have location on, it will ask the user to turn it on.
        onCurrentWeatherStartUp();

        database = new FriendFinderDBHelper(this);
        database.getWritableDatabase();

        SharedPreferences prefs = getSharedPreferences(USERS_PREFS, MODE_PRIVATE);
        email = prefs.getString(EMAIL, "");
        fname = prefs.getString(FIRSTNAME, "");
        lname = prefs.getString(LASTNAME, "");
        pw = prefs.getString(PASSWORD, "");
        Log.i(TAG, "EMAIL: " + email);
        Log.i(TAG, "FNAME: " + fname);
        Log.i(TAG, "LNAME: " + lname);
        Log.i(TAG, "PW: " + pw);
        if (prefs == null || email == null || email.equals("") || fname == null || fname.equals("") ||
                lname == null || lname.equals("") || pw == null || pw.equals("")){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Starts the dawson page into a browser
     * @param view
     */
    public void startDawsonPage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        startActivity(intent);
    }

    /**
     * Starts the about activity
     * @param view
     */
    public void startAbout(View view)
    {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the activity from the user selection
     * @param view
     */
    public void startActivityIntent(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        switch (view.getId())
        {
            case R.id.classCancelledBtn:
                intent = new Intent(this, CancelledActivity.class);
                break;
            case R.id.findTeacherBtn:
                intent = new Intent(this, FindTeacherActivity.class);
                break;
            case R.id.addCalendarBtn:
                intent = new Intent(this, CalendarActivity.class);
                break;
            case R.id.notesBtn:
                intent = new Intent(this, NotesActivity.class);
                break;
            case R.id.weatherBtn:
                intent = new Intent(this, WeatherActivity.class);
                break;
            case R.id.academicCalendarBtn:
                intent = new Intent(this, AcademicCalendarActivity.class);
                break;
            case R.id.findFriendBtn:
                intent = new Intent(this, FindFriendActivity.class);
                break;
            case R.id.findFreeFriendBtn:
                intent = new Intent(this, FindFreeFriends.class);
                break;
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }


    /**
     * Displays the current weather on the main activity
     */
    public void onCurrentWeatherStartUp(){

        //The API key that was genereated for my account on https://openweathermap.org/
        String apiKey = "&APPID=5b62062bcde765f123614e4c944f8027";

        String currentTemperature;

        WeatherRequest currentWeatherRequest = new WeatherRequest(null, apiKey, "3");

        //Current weather startup
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            } else {
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    //Grabbing the current weather from OpenWeatherApi.com
                    currentTemperature = parseJSONandReturnCurrentTemperature(
                            currentWeatherRequest.execute(apiKey,String.valueOf(latitude),String.valueOf(longitude)).get());


                    currentTempTextView.setText(currentTemperature);
                }else{
                    // GPS or Network is not enabled
                    // Ask user to enable through a dialog.
                    gps.showSettingsAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a json object and returns the current temperature
     * @param jsonCurTempResults
     * @return
     */
    public String parseJSONandReturnCurrentTemperature(String jsonCurTempResults) {

        String currentTemperature = null;

        if (jsonCurTempResults != null) {

            try {
                //Create a JSONArray with the String JSON results from 'doInBackground' method.
                JSONObject jsonObject = new JSONObject(jsonCurTempResults);

                //Grabbing the current temperature
                JSONObject main = jsonObject.getJSONObject("main");
                currentTemperature = main.getString("temp");

                //Grabbing the first item to then grab the weather.
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertKelvtoCelcius(currentTemperature);
        }

        return convertKelvtoCelcius(currentTemperature);

    } // end of parseCurrentTemperature

    /**
     * This method is used to convert the information that is given to us by the weather api (kelvin)
     * to celcius since not everyone is comfortable reading kevlin temperature when they want to know
     * what they have to wear in the morning.
     * @param kelv
     * @return
     */
    public String convertKelvtoCelcius(String kelv){
        Double celcius = Double.valueOf(kelv);
        celcius -= 273.15;
        NumberFormat formatter = new DecimalFormat("#0.00");
        return String.valueOf(formatter.format(celcius) + "C°");
    }


    /**
     * Method to easily log to logcat
     *
     * @param msg to be printed to logcat
     */
    public static void logIt(String msg) {
        final String TAG = "---------MAIN: ";
        Log.d(TAG, msg);
    }
}
