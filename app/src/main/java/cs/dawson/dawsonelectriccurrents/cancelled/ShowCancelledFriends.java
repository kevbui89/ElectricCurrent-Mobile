package cs.dawson.dawsonelectriccurrents.cancelled;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cs.dawson.dawsonelectriccurrents.MenuActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.beans.CancelledClass;
import cs.dawson.dawsonelectriccurrents.utilities.CancelledFriendsAsyncTask;

/**
 * ShowCancelledFriendsActivity which which gets the cancelled class information and
 * executes an AsyncTask to display a list of friends who have that cancelled class.
 * Created by: Alessandro Ciotola
 *
 */
public class ShowCancelledFriends extends MenuActivity
{
    private static final String TAG = ShowCancelledFriends.class.getName();
    private static final String REGEX = "\\s+";
    private static final String COURSE = "course";

    /**
     * The onCreate method is called when the activity first begins.
     * Information of the course is taken from the intent and the AsyncTask is started here.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cancelled_friends);

        CancelledClass cancelledClass = (CancelledClass) getIntent().getSerializableExtra(COURSE);
        String[] title = cancelledClass.getTitle().split(REGEX);
        String course = title[0];
        String section = title[1];

        Log.d(TAG, "onCreate: " + course + " " + section);

        CancelledFriendsAsyncTask task = new CancelledFriendsAsyncTask(this, course, section);
        task.execute();
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
