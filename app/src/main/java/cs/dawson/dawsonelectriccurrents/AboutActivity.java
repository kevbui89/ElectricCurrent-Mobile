package cs.dawson.dawsonelectriccurrents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This class displays the information about the ElectricCurrent team.
 * It also displays a blurb of information about each team member.
 * @author Alessandro Ciotola
 * @author Hannah Ly
 * @version 1.0
 */

public class AboutActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * Displays a blurb depending on the user click
     * @param view
     */
    public void showCreatorBlurb(View view)
    {
        String name = "";
        String info = "";
        switch (view.getId())
        {
            case R.id.maxTextView:
                name = getString(R.string.maxText);
                info = getString(R.string.maxInfo);
                break;
            case R.id.aleTextView:
                name = getString(R.string.aleText);
                info = getString(R.string.aleInfo);
                break;
            case R.id.hannahTextView:
                name = getString(R.string.hannahText);
                info = getString(R.string.hannahInfo);
                break;
            case R.id.kevinTextView:
                name = getString(R.string.kevinText);
                info = getString(R.string.kevinInfo);
                break;
        }

        // Displays a blurb
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(info)
                .setTitle(name);
        builder.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                //When the button is Pressed, dismiss the dialog.
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Starts the dawson computer science page into a browser
     * @param view
     */
    public void startDawsonCompSciPage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
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
