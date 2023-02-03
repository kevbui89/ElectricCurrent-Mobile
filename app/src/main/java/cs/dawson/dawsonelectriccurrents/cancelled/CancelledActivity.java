package cs.dawson.dawsonelectriccurrents.cancelled;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cs.dawson.dawsonelectriccurrents.MenuActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.utilities.RssAsyncTask;

/**
 * CancelledActivity which is responsible for displaying a list
 * of all the courses that are cancelled by using an RSSFeed.
 * Created by: Alessandro Ciotola
 *
 */
public class CancelledActivity extends MenuActivity
{
    private static final String TAG = CancelledActivity.class.getName();
    private static final String THREADNAME = "Thread Name: ";

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
        setContentView(R.layout.activity_cancelled);

        RssAsyncTask task = new RssAsyncTask(this);
        task.execute(getString(R.string.cancelledClassUrl));
        Log.d(TAG, THREADNAME + Thread.currentThread().getName());
    }

    /**
     * Method which calls the super method on onCreateOptionsMenu to display the menu. Required
     * so code to show the menu will not have to be repeated for each activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}