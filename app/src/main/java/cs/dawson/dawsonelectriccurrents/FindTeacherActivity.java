package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity containing all the functionality to search for teachers
 * @author Kevin
 * @version 1.0
 */
public class FindTeacherActivity extends MenuActivity {

    private static final String TAG = FindTeacherActivity.class.getName();
    public ImageButton search;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_teacher);

        search = (ImageButton)findViewById(R.id.searchTeacherBtn);
        firstNameInput = (EditText)findViewById(R.id.firstNameTeacher);
        lastNameInput = (EditText) findViewById(R.id.lastNameTeacher);
        rg = (RadioGroup)findViewById(R.id.radioGroupTeacher);

        // Set initial value for radio group
        rg.check(R.id.like);
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

    /**
     * Searches for the teacher
     */
    public void searchTeacher(View view) {
        if (validateFields()) {
            String selection = getSelectionValue();
            String fn = firstNameInput.getText().toString();
            String ln = lastNameInput.getText().toString();
            Intent intent = new Intent(this, ChooseTeacherActivity.class);
            intent.putExtra("selection", selection);
            intent.putExtra("firstname", fn);
            intent.putExtra("lastname", ln);
            startActivity(intent);
        } else {
            errorMessage();
        }
    }

    /**
     * Validates if all required fields are filled up
     * @return
     */
    private boolean validateFields() {

        String fn = firstNameInput.getText().toString();
        String ln = lastNameInput.getText().toString();
        Log.i(TAG, "Firstname Teacher: " + fn);
        Log.i(TAG, "Lastname Teacher: " + ln);
        boolean valid = false;

        if (!fn.equals("") || !ln.equals("")) {
            valid = true;
        }

        // Checks if any radio button was selected
        if (rg.getCheckedRadioButtonId() == -1) {
            valid = false;
        }

        return valid;
    }

    /**
     * Returns the search selection
     *
     * @return
     */
    public String getSelectionValue() {
        String selection = "";
        int id = rg.getCheckedRadioButtonId();
        Log.i(TAG, "RadioGroup ID: " + id);
        Log.i(TAG, "Like ID: " + R.id.like);
        Log.i(TAG, "Exact ID: " + R.id.exact);

        if (id == R.id.like)
            selection = "like";
        else if (id == R.id.exact)
            selection = "exact";

        return selection;
    }

    /**
     * Pops a toast message to validate the year
     */
    public void errorMessage() {
        Toast.makeText(this, R.string.invalidInput,
                Toast.LENGTH_LONG).show();

    }

    /**
     * Saves the state of the application in a bundle
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("firstname", ((EditText) findViewById(R.id.firstNameTeacher)).getText().toString());
        savedInstanceState.putString("lastname", ((EditText) findViewById(R.id.lastNameTeacher)).getText().toString());
        savedInstanceState.putInt("radio", rg.getCheckedRadioButtonId());
    }

    /**
     * Restores the state of the application when the state is changed
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String fn = savedInstanceState.getString("firstname");
        String ln = savedInstanceState.getString("lastname");
        int radioBtnSelection = savedInstanceState.getInt("radio");

        // Sets the textview
        ((TextView) findViewById(R.id.firstNameTeacher)).setText(fn);
        ((TextView) findViewById(R.id.lastNameTeacher)).setText(ln);

        // Checks the correct radio button
        if (radioBtnSelection == R.id.like){
            rg.check(R.id.like);
        } else if (radioBtnSelection == R.id.exact) {
            rg.check(R.id.exact);
        }

    }
}
