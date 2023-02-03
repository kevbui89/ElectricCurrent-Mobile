package cs.dawson.dawsonelectriccurrents.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;
import android.widget.TextView;

import cs.dawson.dawsonelectriccurrents.ChooseTeacherActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.TeacherContactActivity;
import cs.dawson.dawsonelectriccurrents.beans.Teacher;

/**
 * An adapter that will load a ListView in the UI and set the appropriate onClick() listener
 *
 * @author Kevin
 * @version 1.0
 */

public class TeacherAdapter extends BaseAdapter {
    private static final String TAG = TeacherAdapter.class.getName();
    private Context context;
    private ArrayList<String> teachers;
    private ArrayList<Teacher> sTeacher;
    private static LayoutInflater inflater;

    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String OFFICE = "office";
    private static final String LOCAL = "local";
    private static final String POSITION = "position";
    private static final String DEPARTMENT = "department";
    private static final String SECTOR = "sector";


    /**
     * Constructor
     * @param cta
     * @param teachers
     */
    public TeacherAdapter(ChooseTeacherActivity cta, ArrayList<String> teachers, ArrayList<Teacher> sTeacher) {
        this.context = cta;
        this.teachers = teachers;
        this.sTeacher = sTeacher;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return teachers.size();
    }

    @Override
    public String getItem(int position) {
        return teachers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlaceHolder pc = new PlaceHolder();
        View rowView = inflater.inflate(R.layout.list_teacher, null);

        pc.tName = (TextView)rowView.findViewById(R.id.listItem);

        // Display the current teacher in the UI
        pc.tName.setText(teachers.get(position));

        //Container of the list of Teachers from the search
        final List<Teacher> searchedTeachers = new ArrayList<>();

        /**
         * Setting an onClickListener on the current row view
         */
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent teacherContact = new Intent(context, TeacherContactActivity.class);
                teacherContact.putExtra(FULLNAME, sTeacher.get(position).getFullName());
                teacherContact.putExtra(EMAIL, sTeacher.get(position).getEmail());
                teacherContact.putExtra(OFFICE, sTeacher.get(position).getOffice());
                teacherContact.putExtra(LOCAL, sTeacher.get(position).getLocal());
                teacherContact.putExtra(POSITION, sTeacher.get(position).getPosition());
                teacherContact.putExtra(DEPARTMENT, sTeacher.get(position).getDepartment());
                teacherContact.putExtra(SECTOR, sTeacher.get(position).getSector());
                context.startActivity(teacherContact);
            }
        });

        return rowView;
    }

    /**
     * This class holds the View objects for 1 row
     */
    class PlaceHolder
    {
        TextView tName;
    }
}

