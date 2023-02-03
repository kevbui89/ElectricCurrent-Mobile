package cs.dawson.dawsonelectriccurrents.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.beans.CancelledClass;
import cs.dawson.dawsonelectriccurrents.cancelled.RssFeeder;
import cs.dawson.dawsonelectriccurrents.cancelled.ShowCancelActivity;
import cs.dawson.dawsonelectriccurrents.cancelled.ShowCancelledFriends;

/**
 * RssAsyncTask is responsible for getting the data from the RssFeed and will put them in
 * a list adapter with listener.
 * Created by: Alessandro Ciotola
 *
 */
public class RssAsyncTask extends AsyncTask<String, Void, List<CancelledClass>>
{
    private static final String TAG = RssAsyncTask.class.getName();
    private static final String NOCANCELLEDCLASSES = "No classes cancelled.";
    private static final String LISTSIZE = "List Size: ";
    private static final String CLASSCANCELLED = "ClassCancelled";
    private static final String COURSE = "course";

    private List<CancelledClass> cancelledClassList;
    private Context context;

    /**
     * Constructor which initializes the context.
     *
     * @param context
     */
    public RssAsyncTask(Context context)
    {
        this.context = context;
    }

    /**
     * Method which runs in the background, it gets the items from the RSSFeed.
     *
     * @param urls
     * @return
     */
    @Override
    protected List<CancelledClass> doInBackground(String... urls)
    {
        try
        {
            RssFeeder rssReader = new RssFeeder(urls[0]);
            return rssReader.getItems();

        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Method which will check if there are cancelled classes, if there are, add to the listview,
     * else, show No cancelled classes.
     *
     * @param result
     */
    @Override
    protected void onPostExecute(List<CancelledClass> result)
    {
        Log.d(TAG, LISTSIZE + result.size() + " " + result.get(0).getTitle());

        if(result.get(0).getTitle().equalsIgnoreCase(NOCANCELLEDCLASSES))
        {
            TextView noCancelledClasses = (TextView) ((Activity) context).findViewById(R.id.noCancelledClassesView);
            noCancelledClasses.setText(R.string.noCancelledClasses);
        }
        else
        {
            ListView cancelledListView = (ListView) ((Activity) context).findViewById(R.id.cancelledListView);
            cancelledClassList = result;

            List<String> courses = new ArrayList<>();
            for(int i = 0; i < result.size(); i++)
                courses.add(result.get(i).getTitle() + " " + result.get(i).getCourse());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.cancelled_class, courses);
            cancelledListView.setAdapter(adapter);
            cancelledListView.setOnItemClickListener(showCancelledClasses);
            cancelledListView.setOnItemLongClickListener(searchFriends);
        }
    }

    /**
     * Listener which will put the cancelled class into the intent and begin the activity.
     *
     */
    private AdapterView.OnItemClickListener showCancelledClasses = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(context, ShowCancelActivity.class);
            intent.putExtra(CLASSCANCELLED, cancelledClassList.get(position));

            context.startActivity(intent);
        }
    };

    /**
     * listener which will pass the cancelled class to the intent in order to use
     * the web api to search for friends with that cancelled class.
     *
     */
    private AdapterView.OnItemLongClickListener searchFriends = new AdapterView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            Intent intent = new Intent(context, ShowCancelledFriends.class);
            intent.putExtra(COURSE, cancelledClassList.get(position));

            context.startActivity(intent);

            return true;
        }
    };
}