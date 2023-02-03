package cs.dawson.dawsonelectriccurrents.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import cs.dawson.dawsonelectriccurrents.FindFriendActivity;
import cs.dawson.dawsonelectriccurrents.FindFriendCourseActivity;
import cs.dawson.dawsonelectriccurrents.database.DBHelper;
import cs.dawson.dawsonelectriccurrents.MenuActivity;
import cs.dawson.dawsonelectriccurrents.R;

/**
 * Notes activity which will display a list of notes from the database which are populated
 * by the user when they enter any information into the text field and press the "Add Note" button.
 *
 * @author Alessandro Ciotola
 * @author Hannah Ly
 * @author Kevin Bui
 * @author Maxime Lacasse
 * @version 2017/11/25
 *
 */
public class NotesActivity extends MenuActivity
{
    private static DBHelper dbHelper ;
    private SimpleCursorAdapter sCurAdapter;
    private Cursor cursor;

    /**
     * Method which is called when the activity is first opened.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

       showNotes();

       Toast.makeText(this, R.string.deleteNote,
                Toast.LENGTH_LONG).show();
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

    /**
     * Method which is called when the add button is pressed. If information is added to the
     * text field, then add that data to the database and refresh the view.
     *
     * @param view
     */
    public void insertNote(View view)
    {
        EditText et = (EditText) findViewById(R.id.noteText);
        String note  = et.getText().toString();

        if(!note.equals("") && note.length() > 10)
        {
            et.setText("");
            dbHelper.insertNote(note.substring(0, 8), note);
        }
        else
        {
            et.setText("");
            et.setHint(R.string.errorNotesText);
        }
        refreshView();
    }

    /**
     * After the Activity has begun, display the list of notes from the database.
     *
     */
    private void showNotes()
    {
        dbHelper = DBHelper.getDBHelper(this);
        ListView lv = (ListView) findViewById(R.id.noteList);
        cursor = dbHelper.getNotes();
        String[] from = { DBHelper.COL_SHORTNOTE };
        int[] to = { R.id.noteView };

        sCurAdapter = new SimpleCursorAdapter(this, R.layout.note_row, cursor, from, to);
        lv.setAdapter(sCurAdapter);
        lv.setOnItemClickListener(showNote);
        lv.setOnItemLongClickListener(deleteNote);
    }
    /**
     * Method which will reset the view in order to display all the notes when a new note is
     * added.
     *
     */
    public void refreshView()
    {
        cursor = dbHelper.getNotes();
        sCurAdapter.changeCursor(cursor);
        sCurAdapter.notifyDataSetChanged();
    }

    /**
     * onItemClickListener which will display the  full note from the DB
     *
     */
    private AdapterView.OnItemClickListener showNote = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String note = cursor.getString(cursor.getColumnIndex("fullNnote"));

            Log.d("DBHELPER", "onClick: " + note + " " + cursor.getColumnIndex("shortNote"));

            Intent intent = new Intent(NotesActivity.this, ItemNoteActivity.class);
            intent.putExtra("note", note);
            startActivity(intent);
        }
    };

    /**
     * onItemLongClickListener which will delete the note which you are holding on.
     *
     */
    private AdapterView.OnItemLongClickListener deleteNote = new AdapterView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            int positionDB = cursor.getInt(cursor.getColumnIndex("_id"));
            DBHelper dbHelper = DBHelper.getDBHelper(NotesActivity.this);
            dbHelper.removeNote(positionDB);

            refreshView();

            return true;
        }
    };
}
