package cs.dawson.dawsonelectriccurrents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cs.dawson.dawsonelectriccurrents.weatherrequest.WeatherRequest;


/**
 * This class is used to generate an httpURLConnection to the https://openweathermap.org/
 * website to gather information on the weather using the five day forecast feature.
 * Created by maximelacasse on 2017-11-22.
 */

public class FiveDayForecastActivity extends MenuActivity {

    // The API key that was genereated for my account on https://openweathermap.org/
    public String apiKey = "&APPID=5b62062bcde765f123614e4c944f8027";
    // The city that the user wants to check the weather for.
    public String city;
    public String countryCode;

    // Latitude and longitude of the device.
    public String longitude;
    public String latitude;


    // Weather details for 5 day range.
    TextView weatherDay1;
    TextView weatherDay2;
    TextView weatherDay3;
    TextView weatherDay4;
    TextView weatherDay5;
    TextView cityname;
    TextView uvIndex;
    TextView dawsonweathertitle;
    HorizontalScrollView horizontalscrollView;

    // WeatherRequest class objects in global scope.
    WeatherRequest forecastRequest;
    WeatherRequest uvIndexRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiveday_forecast);

        Bundle b = getIntent().getExtras();
        city = b.getString("city");
        countryCode = b.getString("countrycode");

        forecastRequest = new WeatherRequest(city, apiKey, "1");
        uvIndexRequest = new WeatherRequest(city, apiKey, "2");

        setUpWeatherDisplays();

        //Calling the WeatherRequest class to demand a request with the weather forecast with user input.
        try {
            parseJSONandDisplayForecast(forecastRequest.execute(city, apiKey, countryCode).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    } // end of onCreate

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
     * onPostExecute in the WeatherRequest class passed us the information that
     * doInBackground() passed him (The JSON weather information that we need to parse)
     * we now have the JSON information in a string, we create a JSONObject with that string
     * to be able to manipulate and go around in the JSON file, using JSONArrays and JSONObjects.
     * Whilst parsing the JSONObject, we will be setting the textfields to display the results to the user.
     * @param jsonForecastResult Whatever doInBackground returns is this parameter, in this case, JSON info as a String.
     */
    @SuppressLint("SetTextI18n")
    public void parseJSONandDisplayForecast(String jsonForecastResult){
        //Checks if s is null, if it is, then the user typed a bad city name and we did not
        //get any results from the website, which results in a crash when trying to use
        //bufferreader on a bad HttpURLConnection. This will redirect to a error page for user.
        if (jsonForecastResult != null) {

            try {
                //Create a JSONObject with the String JSON results from 'doInBackground' method.
                JSONObject jsonObject = new JSONObject(jsonForecastResult);


                //Grabbing the latitude and longitude to then ship it to another weatherRequest
                //For the UV index.
                JSONObject jsonCity = jsonObject.getJSONObject("city");
                String lat = jsonCity.getJSONObject("coord").getString("lat");
                String lon = jsonCity.getJSONObject("coord").getString("lon");

                this.latitude = lat;
                this.longitude = lon;

                String[] uvIndexes = parseJSONandReturnUV(uvIndexRequest.execute(city,apiKey,latitude,longitude).get());

                //Grabbing the first item to then grab the weather.
                JSONArray jsonItems = jsonObject.getJSONArray("list");

                /**
                 * Checking and pushing condiitons that will grab only the next weather
                 * information in a 3 hour range depending on our current time.
                 * 1. Create a SimpleDateFormat to create the format that we need and get the
                 * current time which grabs the hour.
                 * 2. For loop that will iterate through checking if current time is LESS
                 * than the current time's hour. The for loop will increase by 3 every iteration
                 * since the openWeatherChannel sends out new information every 3 hours.
                 * This will ensure that we are grabbing the next future weather information.
                 * 3. Grab the information from dt_txt to aquire the hour and day for step 4 condition.
                 * 4. In the for loop for actual grabbing of the information, make sure to
                 * create a condition that it will only grab information from the sections that
                 * have the time that we established in this for loop.
                 */

                //1. Creating a SimpleDateFormat to format the current date/time with ease.
                SimpleDateFormat sdf = new SimpleDateFormat("HHdd");
                String currentHourDay = sdf.format(new Date()); //Output: 01 || 11 -> weather: 01 || 11

                //2. For loop to grab the correct time that we want to have for our user.
                String weatherHourWeNeedToGrab = currentHourDay.substring(0,2); //Grabbing the current hour.
                String weatherDayWeNeedToGrab = currentHourDay.substring(2,4); //Grabbing current day.

                //Day counter to display the information gathered depending on which day it is.
                int dayCounter = 0;
                //Set the city that the user inputted.
                cityname.setText(city+", "+countryCode);
                //Set the firstDay to true, so it goes into the loop immeditely and grab the information.
                //Then we set the hour to be the hour that we got from the 'firstDay' and we keep it for the
                //rest of the forecast. example: firstDay: 00:00:00, then the rest will be 00:00:00 but with increase of day.
                boolean firstDay = true;
                /**
                 * 'firstDayOfTheMonth' string
                 * This string is used to know whenever the month changes.
                * With my technique, i increase the day of the month every time we grab information.
                * If it's at 31 and i increase it to 32, then i will never get information.
                * Thus, if the information that the JSONObject provides us is at day 01, then we will grab it.
                * This string will turn null whenever we grab the 01, or else it will grab all the 3 hour weather info every iteration.
                 **/
                String firstDayOfTheMonth = "01";

                //Iterate through all the JSONObjects inside the list JSONArray.
                for (int i = 0; i < jsonItems.length(); i++) {

                    //3. Grabbing the dt_txt to see if it's the results that we want to display.
                    String weatherTimeAndDay = jsonItems.getJSONObject(i).getString("dt_txt");
                    String weatherHour = weatherTimeAndDay.substring(11,13); //Grabbing the hour.
                    String weatherDay = weatherTimeAndDay.substring(8,10); //Grabbing the day.
                    String weatherDate = weatherTimeAndDay.substring(0,10); //Grabbing the date.
                    String weatherTime = weatherTimeAndDay.substring(11,19); //Grabbing the time.
                    //Change the hour we need to grab to the first one that came out closest to our time.

                    logIt("WeatherDayGRAB: " + weatherDayWeNeedToGrab);
                    logIt("WeatherDay: " + weatherDay);
                    logIt("WeatherHour: " + weatherHour);
                    logIt("weatherHourGRAB: " + weatherHourWeNeedToGrab);

                    //4. A condition to check to check if we are grabbing the same hour
                    //And incrementing the day counter until we hit 5 (5-day forecast).
                    if ((Integer.valueOf(weatherHour) == Integer.valueOf(weatherHourWeNeedToGrab) &&
                            (weatherDay.equals(weatherDayWeNeedToGrab) || weatherDay.equals(firstDayOfTheMonth))) || firstDay){
                        weatherHourWeNeedToGrab = weatherHour;
                        //The same hour and fits the day counter -> GRAB RESULTS!
                        dayCounter++;
                        //Turn to false because there can only be one first day.
                        firstDay = false;

                        logIt("Inside the weatherTime condition");

                        //Grabbing the main branch and all it's components.
                        String mainTempKelv = jsonItems.getJSONObject(i).getJSONObject("main").getString("temp");
                        String mainTemp = convertKelvtoCelcius(mainTempKelv);
                        String mainMinTempKelv= jsonItems.getJSONObject(i).getJSONObject("main").getString("temp_min");
                        String mainMinTemp = convertKelvtoCelcius(mainMinTempKelv);
                        String mainMaxTempKelv = jsonItems.getJSONObject(i).getJSONObject("main").getString("temp_max");
                        String mainMaxTemp = convertKelvtoCelcius(mainMaxTempKelv);
                        String mainHumidity = jsonItems.getJSONObject(i).getJSONObject("main").getString("humidity");
                        logIt("mainTemp: " + mainTemp);

                        //Grabbing the weather branch and all it's components.
                        String weatherMain = jsonItems.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                        String weatherDescription = jsonItems.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");

                        //Grabbing the winds.
                        String windSpeed = jsonItems.getJSONObject(i).getJSONObject("wind").getString("speed");

                        //Displaying the results
                        //CONVERT TEMPERATURE TO CELCIUS
                        switch (dayCounter){
                            case 1:
                                weatherDay1.setText(weatherDate + "\n" + weatherTime + "\n" +  weatherMain + "\n" + weatherDescription + "\n" + mainTemp + "\n" + mainMinTemp +
                                        "\n" + mainMaxTemp + "\n" + mainHumidity + "\n" + windSpeed + "\n\n" + uvIndexes[0]);
                            case 2:
                                weatherDay2.setText(weatherDate + "\n" + weatherTime + "\n" +  weatherMain + "\n" + weatherDescription + "\n" + mainTemp + "\n" + mainMinTemp +
                                        "\n" + mainMaxTemp + "\n" + mainHumidity + "\n" + windSpeed + "\n\n" + uvIndexes[1]);
                            case 3:
                                weatherDay3.setText(weatherDate + "\n" + weatherTime + "\n" +  weatherMain + "\n" + weatherDescription + "\n" + mainTemp + "\n" + mainMinTemp +
                                        "\n" + mainMaxTemp + "\n" + mainHumidity + "\n" + windSpeed + "\n\n" + uvIndexes[2]);
                            case 4:
                                weatherDay4.setText(weatherDate + "\n" + weatherTime + "\n" +  weatherMain + "\n" + weatherDescription + "\n" + mainTemp + "\n" + mainMinTemp +
                                        "\n" + mainMaxTemp + "\n" + mainHumidity + "\n" + windSpeed + "\n\n" + uvIndexes[3]);
                            case 5:
                                weatherDay5.setText(weatherDate + "\n" + weatherTime + "\n" +  weatherMain + "\n" + weatherDescription + "\n" + mainTemp + "\n" + mainMinTemp +
                                        "\n" + mainMaxTemp + "\n" + mainHumidity + "\n" + windSpeed + "\n\n" + uvIndexes[4]);
                        }



                        logIt("WeatherDayBeforeIncrease: " + weatherDayWeNeedToGrab);

                        int weatherIncrease;
                            //Increase the weather day counter.
                            weatherIncrease = Integer.valueOf(weatherDay);
                            weatherIncrease++;
                            if (weatherIncrease < 10) {
                                //Adds a zero at the start of the string or else it will search for a '1' or '2', which is invalid.
                                weatherDayWeNeedToGrab = String.valueOf("0" + weatherIncrease);
                            } else {
                                //Simply adds it since it is already a double digit number.
                                weatherDayWeNeedToGrab = String.valueOf(weatherIncrease);
                            }

                            //This prevents having duplicates for the first day of the month
                            if (weatherDay.equals(firstDayOfTheMonth)){
                                firstDayOfTheMonth = null;
                            }


                        logIt("WeatherDayAfterIncrease: " + weatherDayWeNeedToGrab);

                    } else {
                        logIt("Continue...");
                        continue;
                    }

                } //End of JSON information grabbing and displaying for loop.

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            //Alert user that input is invalid and that no response is given to us.

            cityname.setText("");
            dawsonweathertitle.setText("ERROR, CITY NOT FOUND!");
            horizontalscrollView.setVisibility(View.INVISIBLE);

        }

    } // end of parseJSONandDisplayForecast


    /**
     * This method is used to parse the JSON information that is given by the UV index forecast api.
     * This will grab the UV index value for each of the following 5 days.
     * @param jsonUvIndexResults
     * @return
     */
    public String[] parseJSONandReturnUV(String jsonUvIndexResults) {

        String[] uvIndexes = new String[5];

        //Checks if s is null, if it is, then the user typed a bad city name and we did not
        //get any results from the website, which results in a crash when trying to use
        //bufferreader on a bad HttpURLConnection. This will redirect to a error page for user.
        if (jsonUvIndexResults != null) {

            try {
                //Create a JSONArray with the String JSON results from 'doInBackground' method.
                JSONArray jsonArray = new JSONArray(jsonUvIndexResults);

                //Iterate through the JSONArray to grab all the uv values for 5 day range.
                for (int i = 0; i < 5; i++){
                    //Grabbing the uv value for each day.
                    uvIndexes[i] = jsonArray.getJSONObject(i).getString("value");
                }

                //Grabbing the first item to then grab the weather.
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return uvIndexes;

        }

        return uvIndexes;

    } // end of parseJSONandDisplayUV

    /**
     * This method is used to set up all the resources for the UI associated with displaying the
     * results from the JSON file that we grabbed from https://openweathermap.org/.
     * Displaying in a 5 day forecast fashion.
     */
    public void setUpWeatherDisplays(){

        //Finding resources for weather type.
        weatherDay1 = findViewById(R.id.weatherDay1);
        weatherDay2 = findViewById(R.id.weatherDay2);
        weatherDay3 = findViewById(R.id.weatherDay3);
        weatherDay4 = findViewById(R.id.weatherDay4);
        weatherDay5 = findViewById(R.id.weatherDay5);
        cityname = findViewById(R.id.cityname);
        uvIndex = findViewById(R.id.uvIndex);
        dawsonweathertitle = findViewById(R.id.dawsonweathertitle);
        horizontalscrollView = findViewById(R.id.horizontalscrollView);
    }

    /**
     * This method is used to convert the information that is given to us by the weather api (kelvin)
     * to celcius since not everyone is comfortable reading kevlin temperature when they want to know
     * what they have to wear in the morning.
     * @param kelv
     * @return
     */
    public String convertKelvtoCelcius(String kelv){
        Double celcius = Double.valueOf(kelv);
        logIt("celcius: " + celcius);
        celcius -= 273.15;
        NumberFormat formatter = new DecimalFormat("#0.00");
        return String.valueOf(formatter.format(celcius) + "C°");

    }


    /**
     * Method to easily log to logcat
     *
     * @param msg to be printed to logcat
     */
    public static void logIt(String msg) {
        final String TAG = "---------WEATHER: ";
        Log.d(TAG, msg);
    }






}
