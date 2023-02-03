package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;

import cs.dawson.dawsonelectriccurrents.adapters.SpinnerAdapter;

/**
 * This class asks for user input for day and time and sends the user input into a bundle to FindBreakActivity.
 * @author Kevin Bui
 * @author Maxime Lacasse
 * @version 1.0
 */
public class FindFreeFriends extends MenuActivity
{

    // Declare variables
    private Spinner daySpinner;
    private Spinner startSpinner;
    private Spinner endSpinner;
    private ArrayList<String> listDay;
    private ArrayList<String> listTime;

    // Declare the keys
    private final static String DAY = "day";
    private final static String MONDAY = "Monday";
    private final static String TUESDAY = "Tuesday";
    private final static String WEDNESDAY = "Wednesday";
    private final static String THURSDAY = "Thursday";
    private final static String FRIDAY = "Friday";
    private final static String START = "starttime";
    private final static String END = "endtime";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_free_friends);

        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        startSpinner = (Spinner) findViewById(R.id.timeStartSpinner);
        endSpinner = (Spinner) findViewById(R.id.endTimeSpinner);

        // Sets the list of days
        listDay = getDays();
        SpinnerAdapter da = new SpinnerAdapter(getApplicationContext(), listDay);
        daySpinner.setAdapter(da);

        // Sets the list of time
        listTime = getTime();
        SpinnerAdapter time = new SpinnerAdapter(getApplicationContext(), listTime);
        startSpinner.setAdapter(time);
        endSpinner.setAdapter(time);


    }

    /**
     * Returns an array list of days
     * @return
     */
    private ArrayList<String> getDays() {
        ArrayList<String> days = new ArrayList<>();
        days.add(MONDAY);
        days.add(TUESDAY);
        days.add(WEDNESDAY);
        days.add(THURSDAY);
        days.add(FRIDAY);
        return days;
    }

    /**
     * Returns an array of all the times by jumps of 30 minutes
     * @return
     */
    private ArrayList<String> getTime() {
        ArrayList<String> timeList = new ArrayList<>();
        int time = 1000;
        while (time < 1700) {
            timeList.add(Integer.toString(time));
            time += 30;
            timeList.add(Integer.toString(time));
            time += 70;
        }
        timeList.add(Integer.toString(time));
        return timeList;
    }

    /**
     * Starts the find break activity
     * @param view
     */
    public void startFindFriendBreaks(View view) {
        //Getting the data selected from the user.
        int indexOfDaySelected = daySpinner.getSelectedItemPosition();
        int indexOfStartTimeSelected = startSpinner.getSelectedItemPosition();
        int indexOfEndTimeSelected = endSpinner.getSelectedItemPosition();

        //Data selected by user
        String daySelected = listDay.get(indexOfDaySelected);
        String startSelected = listTime.get(indexOfStartTimeSelected);
        String endSelected = listTime.get(indexOfEndTimeSelected);

        //Creating an intent and sending it over to the fiveDayForecastActivity.
        Intent intent = new Intent(this, FIndBreakActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DAY, daySelected);
        bundle.putString(START, startSelected);
        bundle.putString(END, endSelected);
        intent.putExtras(bundle);
        startActivity(intent);

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
