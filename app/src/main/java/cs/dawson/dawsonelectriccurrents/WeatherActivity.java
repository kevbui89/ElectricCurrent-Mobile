package cs.dawson.dawsonelectriccurrents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cs.dawson.dawsonelectriccurrents.adapters.SpinnerAdapter;

public class WeatherActivity extends MenuActivity {

    EditText cityinput;
    Spinner countrySpinner;
    ArrayList<String> countryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityinput = findViewById(R.id.cityinput);
        countrySpinner = findViewById(R.id.countrySpinner);

        countryNames = getCountryNames();

        //Calling my custom adapter for displaying the country names.
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getApplicationContext(),countryNames);

        countrySpinner.setAdapter(spinnerAdapter);

        countrySpinner.setSelection(36);

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

    public void startFiveDayForecast(View view)
    {
        //Getting the country selected from the user.
        int indexOfCountrySelected = countrySpinner.getSelectedItemPosition();

        //Country name selected by user
        String countrySelected = countryNames.get(indexOfCountrySelected);

        //Creating an intent and sending it over to the fiveDayForecastActivity.
        Intent intent = new Intent(this, FiveDayForecastActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city", cityinput.getText().toString());
        bundle.putString("countrycode", getCountryCode(countrySelected));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * This grabs the country names
     * @return
     */
    public ArrayList getCountryNames(){
        //Get all the available locales
        Locale[] locale = Locale.getAvailableLocales();
        //Create an arrayList of strings to place the countries.
        ArrayList<String> countries = new ArrayList<String>();
        //SIngle country, which will be added to the arrayList.
        String country;
        //Iterate through all of the locales and add it to the countries
        //Check for duplicates and if the country's length is 0 (empty).
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        //Sort the arrayList
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        return countries;
    }

    /**
     * Converts a country name into a countryISO code to be able to use the openWeatherApi country code feature.
     * Credits to Vlad: https://stackoverflow.com/users/3833411/vlad
     * Post help: https://stackoverflow.com/questions/28503225/get-country-code-from-country-name-in-android/28503619#28503619
     * @param countryName
     * @return
     */
    public String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            // Map all country names and codes in key - value pairs.
            countryMap.put(name, code);
        }
        // Get the country code for the given country name using the map.
        // Here you will need some validation or better yet
        // a list of countries to give to user to choose from.
        String countryCode = countryMap.get(countryName); // "NL" for Netherlands.

        return countryCode;

    }

    /**
     * Method to easily log to logcat
     *
     * @param msg to be printed to logcat
     */
    public static void logIt(String msg) {
        final String TAG = "-------------WEATHER: ";
        Log.d(TAG, msg);
    }


}
