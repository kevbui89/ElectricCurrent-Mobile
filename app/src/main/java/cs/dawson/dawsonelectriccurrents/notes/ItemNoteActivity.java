package cs.dawson.dawsonelectriccurrents.notes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cs.dawson.dawsonelectriccurrents.MenuActivity;
import cs.dawson.dawsonelectriccurrents.R;

/**
 * ItemNoteActivity which will display the full note that has been pressed in the
 * NotesActivity.
 *
 * @author Alessandro Ciotola
 * @version 2017/11/25
 *
 */
public class ItemNoteActivity extends MenuActivity
{
    /**
     * Method which is called when the activity is first opened.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_note);

        String note = getIntent().getExtras().getString("note");
        displayNote(note);
    }

    /**
     * Method which sets the textview with the note that had been passed from the intent.
     *
     * @param note
     */
    private void displayNote(String note)
    {
        TextView tv = (TextView) findViewById(R.id.note);
        tv.setText(note);
    }

    /**
     * Event Listener which is called when one of the items in the List view has been pressed.
     * Will start the ItemNoteActivity and pass the note information to the intent.
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
