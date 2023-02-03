package cs.dawson.dawsonelectriccurrents.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs.dawson.dawsonelectriccurrents.R;

/**
 * This is the custom adapter that is used to display the spinner in the weather activity.
 * It grabs the arraylist that the weather activity is giving it, which will display all the country names.
 * Created by maximelacasse on 2017-11-28.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> array;
    LayoutInflater inflter;

    public SpinnerAdapter(Context applicationContext, ArrayList<String> array) {
        this.context = applicationContext;
        this.array = array;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner, null);
        TextView names = (TextView) view.findViewById(R.id.textView);
        names.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
        names.setGravity(Gravity.CENTER);
        names.setText(array.get(i));
        return view;
    }


}
