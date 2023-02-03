package cs.dawson.dawsonelectriccurrents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * This fragment is used to display the academic calendar webview.
 * @author Kevin Bui
 * @vrsion 1.0
 */

public class CalendarWVFragment extends Fragment {

    private static final String TAG = CalendarWVFragment.class.getName();
    private String url;

    // Declare the keys
    private static final String YEAR = "year";
    private static final String SEMESTER = "semester";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_web_view, container, false);
        WebView wv = (WebView)view.findViewById(R.id.academicCalendarWv);
        // Check if the arguments are null, if not, set the default calendar
        if (this.getArguments() == null) {
            url = "https://www.dawsoncollege.qc.ca/registrar/fall-2017-day-division/";
        } else {
            String year = this.getArguments().getString(YEAR).toString();
            String semester = this.getArguments().getString(SEMESTER).toString();
            url = "https://www.dawsoncollege.qc.ca/registrar/" + semester + "-" + year + "-day-division/";

            Log.i(TAG, "URL: " + url);
        }
        wv.loadUrl(url);

        return view;
    }

}
