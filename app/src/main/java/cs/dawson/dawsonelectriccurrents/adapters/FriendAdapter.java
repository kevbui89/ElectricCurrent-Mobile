package cs.dawson.dawsonelectriccurrents.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import cs.dawson.dawsonelectriccurrents.FIndBreakActivity;
import cs.dawson.dawsonelectriccurrents.R;
import cs.dawson.dawsonelectriccurrents.beans.Teacher;

/**
 * Adapter that displays all the friends in a listview
 *
 * @author Kevin Bui
 * @author Maxime Lacasse
 * @version 1.0
 */

public class FriendAdapter extends BaseAdapter
{
    private static final String TAG = FriendAdapter.class.getName();
    private static final String MAILTO = "mailto";

    private Context context;
    private ArrayList<String> names;
    private ArrayList<String> email;
    private static LayoutInflater inflater;

    /**
     * Constructor
     * @param fba
     * @param names
     */
    public FriendAdapter(FIndBreakActivity fba, ArrayList<String> names, ArrayList<String> email) {
        this.context = fba;
        this.names = names;
        this.email = email;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public String getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FriendAdapter.PlaceHolder pc = new FriendAdapter.PlaceHolder();
        View rowView = inflater.inflate(R.layout.list_teacher, null);

        pc.fName = (TextView)rowView.findViewById(R.id.listItem);

        // Display the current teacher in the UI
        pc.fName.setText(names.get(position));

        //Container of the list of Teachers from the search
        final List<Teacher> searchedTeachers = new ArrayList<>();

        /**
         * Setting an onClickListener on the current row view
         */
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent sendEMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(MAILTO, email.get(position), null));
                sendEMail.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.from) + " " + context.getResources().getString(R.string.app_name));
                context.startActivity(Intent.createChooser(sendEMail, context.getResources().getString(R.string.sendemail)));
            }
        });

        return rowView;
    }

    /**
     * This class holds the View objects for 1 row
     */
    class PlaceHolder
    {
        TextView fName;
    }
}
