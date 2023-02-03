package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import cs.dawson.dawsonelectriccurrents.adapters.TeacherAdapter;
import cs.dawson.dawsonelectriccurrents.beans.Teacher;

/**
 * This class displays a listview of all the teachers that match a search
 * Once clicked, it will launch an activity with the teacher's contact information
 * @author Kevin Bui
 * @version 1.0
 */

public class ChooseTeacherActivity extends MenuActivity {

    private static final String TAG = ChooseTeacherActivity.class.getName();
    protected FirebaseDatabase mDatabase;
    private String selection;
    private String firstName;
    private String lastName;
    private String fullName;
    private String teacherName;
    private boolean searchDb;
    private ArrayList<String> allTeachersFullName;
    private ArrayList<Teacher> teachers;

    // Declare the keys
    private static final String SELECTION = "selection";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String TEACHER = "Teacher";
    private static final String SEARCHDATABASE = "SearchDatabase";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String LOCAL = "local";
    private static final String DEPARTMENT = "department";
    private static final String SECTOR = "sector";
    private static final String OFFICE = "office";
    private static final String POSITION = "position";
    private static final String FULLNAME_DB = "full_name";
    private static final String FIRSTNAME_DB = "first_name";
    private static final String LASTNAME_DB = "last_name";
    private static final String DEPARTMENT_DB = "departments";
    private static final String SECTOR_DB = "sectors";
    private static final String POSITION_DB = "positions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teacher);

        mDatabase = FirebaseDatabase.getInstance();

        // Fetches all the teachers from the firebase database
        getAllTeachers();

        // Get the bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selection = extras.getString(SELECTION);
            firstName = extras.getString(FIRSTNAME);
            lastName = extras.getString(LASTNAME);
            teacherName = extras.getString(TEACHER);
            searchDb = extras.getBoolean(SEARCHDATABASE);
        }

        // Set the full name
        fullName = firstName + " " + lastName;
    }

    /**
     * Opens the teacher information activity if no teacher were found in the user search
     */
    private void loadInitialActivity() {
        Intent teacherContact = new Intent(getApplicationContext(), TeacherContactActivity.class);
        startActivity(teacherContact);
    }

    /**
     * Gets all the teachers in the firebase database into an ArrayList of Strings
     * @return
     */
    private void getAllTeachers() {
        final ChooseTeacherActivity currentActivity = this;
        final ListView lv = (ListView)findViewById(R.id.listViewTeachers);
        Query query = mDatabase.getReference();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allTeachersFullName = new ArrayList<String>();
                teachers = new ArrayList<Teacher>();
                Log.i(TAG, "Data Snap - Getting all teachers");
                Log.i(TAG, "Data Snap Shot count - " + dataSnapshot.getChildrenCount());

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()) {
                    DataSnapshot ds = iterator.next();

                    String teacherFullName = (String)ds.child(FULLNAME_DB).getValue();
                    String unEncodedName = Html.fromHtml(teacherFullName).toString();
                    // Check if office and local are in the database, because some teachers dont have
                    String office = (String)ds.child(OFFICE).getValue();
                    if (office == null) {
                        office = "";
                    } else {
                        office = (String)ds.child(OFFICE).getValue();
                    }
                    String local = (String)ds.child(LOCAL).getValue();
                    if (local == null) {
                        local = "";
                    } else {
                        local = (String)ds.child(LOCAL).getValue();
                    }

                    if (searchDb) {
                        if (teacherName.equals(teacherFullName)) {
                            Teacher t = new Teacher(((String)ds.child(FIRSTNAME_DB).getValue()), (String)ds.child(LASTNAME_DB).getValue(),
                                    (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                    office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                    (String)ds.child(SECTOR_DB).child("0").getValue());
                            teachers.add(t);
                        }
                    } else if (selection.equals("like")) {
                        // Check if first name contains the users search
                        if (!firstName.equals("") && lastName.equals("")) {
                            String fn = teacherFullName.substring(0, teacherFullName.indexOf(" "));
                            if (fn.toLowerCase().trim().contains(firstName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);
                            }
                            // Check if the last name contains the user search
                        } else if (firstName.equals("") && !lastName.equals("")) {
                            String ln = teacherFullName.substring(teacherFullName.indexOf(" ") + 1);
                            if (ln.toLowerCase().trim().contains(lastName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);
                            }
                            // Both fields were set
                        } else if (!firstName.equals("") && !lastName.equals("")) {
                            if (teacherFullName.toLowerCase().trim().contains(fullName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);
                            }
                        }
                        // Check for exact names
                    } else if (selection.equals("exact")) {
                        // Check if first name contains the users search
                        if (!firstName.equals("") && lastName.equals("")) {
                            String fn = teacherFullName.substring(0, teacherFullName.indexOf(" "));
                            if (fn.toLowerCase().trim().equals(firstName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);
                            }
                            // Check if the last name contains the user search
                        } else if (firstName.equals("") && !lastName.equals("")) {
                            String ln = teacherFullName.substring(teacherFullName.indexOf(" ") + 1);
                            if (ln.toLowerCase().trim().equals(lastName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);
                            }
                            // Both fields were set
                        } else if (!firstName.equals("") && !lastName.equals("")) {
                            if (teacherFullName.toLowerCase().trim().equals(fullName.toLowerCase().trim())) {
                                allTeachersFullName.add(unEncodedName);
                                Teacher t = new Teacher((String)ds.child(FIRSTNAME_DB).getValue(), (String)ds.child(LASTNAME_DB).getValue(),
                                        (String)ds.child(FULLNAME_DB).getValue(), (String)ds.child(EMAIL).getValue(),
                                        office, local, (String)ds.child(DEPARTMENT_DB).child("0").getValue(), (String)ds.child(POSITION_DB).child("0").getValue(),
                                        (String)ds.child(SECTOR_DB).child("0").getValue());
                                teachers.add(t);

                            }
                        }
                    } // End if else
                } // End while iterator

                // Start activity if results == 1
                if (teachers.size() == 1) {
                    Intent teacherContact = new Intent(getApplicationContext(), TeacherContactActivity.class);
                    teacherContact.putExtra(FULLNAME, teachers.get(0).getFullName());
                    teacherContact.putExtra(EMAIL, teachers.get(0).getEmail());
                    teacherContact.putExtra(OFFICE, teachers.get(0).getOffice());
                    teacherContact.putExtra(LOCAL, teachers.get(0).getLocal());
                    teacherContact.putExtra(POSITION, teachers.get(0).getPosition());
                    teacherContact.putExtra(DEPARTMENT, teachers.get(0).getDepartment());
                    teacherContact.putExtra(SECTOR, teachers.get(0).getSector());
                    startActivity(teacherContact);
                } else if (teachers.size() == 0) {
                    loadInitialActivity();
                } else {
                    // Set the adapter to the list view
                    lv.setAdapter(new TeacherAdapter(currentActivity, allTeachersFullName, teachers));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled called - Unexpected Error ");
                Log.e(TAG, "Code : " + databaseError.getCode()
                        + " - Details : " + databaseError.getDetails()
                        + " - Message : " + databaseError.getMessage());
            }
        });
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
