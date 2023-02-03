package cs.dawson.dawsonelectriccurrents.cancelled;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import cs.dawson.dawsonelectriccurrents.ChooseTeacherActivity;
import cs.dawson.dawsonelectriccurrents.MenuActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.beans.CancelledClass;

/**
 * Activity which will display the information of a course in TextViews.
 * Created by: Hannah Ly
 */
public class ShowCancelActivity extends MenuActivity implements Serializable
{
    private static final String CLASSCANCELLED = "ClassCancelled";
    private static final String TEACHER = "Teacher";
    private static final String SEARCHDATABASE = "SearchDatabase";
    private CancelledClass cancelledClass;
    private TextView title;
    private TextView course;
    private TextView teacher;
    private TextView date;

    /**
     * The onCreate method is called when the activity first begins.
     * The text for the TextViews are set here
     * Created by: Hannah Ly
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cancel);

        Intent intent = getIntent();
        cancelledClass = (CancelledClass) intent.getSerializableExtra(CLASSCANCELLED);

        title = (TextView) findViewById(R.id.tvTextTitle);
        course = (TextView) findViewById(R.id.tvTextCourse);
        teacher = (TextView) findViewById(R.id.tvTextTeacher);
        date = (TextView) findViewById(R.id.tvTextDate);

        title.setText(cancelledClass.getTitle());
        course.setText(cancelledClass.getCourse());
        teacher.setText(cancelledClass.getTeacher());
        date.setText(cancelledClass.getDateTimeCancelled());
    }

    /**
     * Method which puts the teacher data and database checking into the intent and then
     * the ChooseTeacherActivity is started.
     *
     * @param view
     */
    public void getTeacherInformation(View view)
    {
        Intent intent = new Intent(ShowCancelActivity.this, ChooseTeacherActivity.class);
        intent.putExtra(TEACHER, cancelledClass.getTeacher());
        intent.putExtra(SEARCHDATABASE, true);
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
