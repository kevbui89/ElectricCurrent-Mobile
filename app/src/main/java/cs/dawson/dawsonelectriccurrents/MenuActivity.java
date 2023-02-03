package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class takes care of inflating the menu activity for each activity in this application
 * @author Alessandro Ciotola
 * @author Kevin Bui
 * @author Maxime Lacasse
 * @author Hannah Ly
 * @version 1.0
 */

public class MenuActivity extends AppCompatActivity
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    /**
     * Displays the option selections
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about:
                showAbout();
                return true;
            case R.id.dawson:
                showDawson();
                return true;
            case R.id.settings:
                showSettings();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the about activity
     */
    private void showAbout()
    {
        Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * Starts a web browser with the dawson computer science website
     */
    private void showDawson()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.dawsoncollege.qc.ca/computer-science-technology/"));
        startActivity(intent);
    }

    /**
     * Starts the settings activity
     */
    private void showSettings()
    {
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
