package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * This class displays the teacher information coming from a bundle
 * @author Kevin Bui
 * @version 1.0
 */

public class TeacherContactActivity extends MenuActivity {

    private static final String TAG = TeacherContactActivity.class.getName();
    private String email;
    private String local;
    private TextView emailTv;
    private TextView localTv;

    // Declare the keys
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String OFFICE = "office";
    private static final String LOCAL = "local";
    private static final String POSITION = "position";
    private static final String DEPARTMENT = "department";
    private static final String SECTOR = "sector";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_contact);

        // Check if the bundle is null and sets
        if (savedInstanceState != null) {
            ((TextView)findViewById(R.id.fullNameTv)).setText(savedInstanceState.getString(FULLNAME));
            ((TextView)findViewById(R.id.emailTeacherTv)).setText(savedInstanceState.getString(EMAIL));
            ((TextView)findViewById(R.id.officeTv)).setText(savedInstanceState.getString(OFFICE));
            ((TextView)findViewById(R.id.localTv)).setText(savedInstanceState.getString(LOCAL));
            ((TextView)findViewById(R.id.positionTv)).setText(savedInstanceState.getString(POSITION));
            ((TextView)findViewById(R.id.departmentTv)).setText(savedInstanceState.getString(DEPARTMENT));
            ((TextView)findViewById(R.id.sectorTv)).setText(savedInstanceState.getString(SECTOR));
        }

        // Get the bundle
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            ((TextView)findViewById(R.id.noteachersfound)).setText(R.string.noteacherfound);
            ((TextView)findViewById(R.id.nameStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.emailStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.officeStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.localStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.positionStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.departmentStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.sectorStatic)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.teacherInfoTv)).setVisibility(View.INVISIBLE);
        } else {
            ((TextView)findViewById(R.id.fullNameTv)).setText(extras.getString(FULLNAME).toString());
            ((TextView)findViewById(R.id.emailTeacherTv)).setText(extras.getString(EMAIL).toString());
            ((TextView)findViewById(R.id.officeTv)).setText(extras.getString(OFFICE).toString());
            ((TextView)findViewById(R.id.localTv)).setText(extras.getString(LOCAL).toString());
            ((TextView)findViewById(R.id.positionTv)).setText(extras.getString(POSITION).toString());
            ((TextView)findViewById(R.id.departmentTv)).setText(extras.getString(DEPARTMENT).toString());
            ((TextView)findViewById(R.id.sectorTv)).setText(extras.getString(SECTOR).toString());
            email = extras.getString(EMAIL);
            local = extras.getString(LOCAL);
            emailTv = (TextView)findViewById(R.id.emailTeacherTv);
            localTv = (TextView)findViewById(R.id.localTv);

            // Sets an onclick listener to send an email
            emailTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent sendEMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                    sendEMail.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.from) + " " + getResources().getString(R.string.app_name));
                    startActivity(Intent.createChooser(sendEMail, getResources().getString(R.string.sendemail)));
                }
            });

            // Sets an onclick listener to call
            localTv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:" + local));
                    startActivity(Intent.createChooser(call, getResources().getString(R.string.call)));
                }
            });
        }
    }

    /**
     * Saves the state of the activity
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "Inside onSaveInstanceState");

        outState.putString(FULLNAME, ((TextView)findViewById(R.id.fullNameTv)).getText().toString());
        outState.putString(EMAIL, ((TextView)findViewById(R.id.emailTeacherTv)).getText().toString());
        outState.putString(LOCAL, ((TextView)findViewById(R.id.localTv)).getText().toString());
        outState.putString(POSITION, ((TextView)findViewById(R.id.positionTv)).getText().toString());
        outState.putString(OFFICE, ((TextView)findViewById(R.id.officeTv)).getText().toString());
        outState.putString(DEPARTMENT, ((TextView)findViewById(R.id.departmentTv)).getText().toString());
        outState.putString(SECTOR, ((TextView)findViewById(R.id.sectorTv)).getText().toString());
    }

    /**
     * Restores the previous state of the application
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG, "Inside onRestoreInstanceState");

        String fn = savedInstanceState.getString(FULLNAME);
        String email = savedInstanceState.getString(EMAIL);
        String local = savedInstanceState.getString(LOCAL);
        String pos = savedInstanceState.getString(POSITION);
        String office = savedInstanceState.getString(OFFICE);
        String dep = savedInstanceState.getString(DEPARTMENT);
        String sec = savedInstanceState.getString(SECTOR);

        ((TextView) findViewById(R.id.fullNameTv)).setText(fn);
        ((TextView) findViewById(R.id.emailTeacherTv)).setText(email);
        ((TextView) findViewById(R.id.localTv)).setText(local);
        ((TextView) findViewById(R.id.officeTv)).setText(office);
        ((TextView) findViewById(R.id.positionTv)).setText(pos);
        ((TextView) findViewById(R.id.departmentTv)).setText(dep);
        ((TextView) findViewById(R.id.sectorTv)).setText(sec);
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
