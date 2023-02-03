package cs.dawson.dawsonelectriccurrents.weatherrequest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

    /**
     * This is a subclass of FiveDayForecastActivity, for the sole purpose to do an AsyncTask
     * to be able to do an HttpURLConnection to the weather site, to gather information
     * about the weather that the user wants.
     * The three generic types '<String, Void, String[]>' are the types that are passed from method to method.
     * 1-String: When using .execute on this class, you can pass it a string for doInBackground().
     * 2-Void: Whatever onPreExecute sends to doInBackground.
     * 3-String: Whatever onPostExecute will return. (Using weatherRequest.execute().get()).
     *
     * @author Created by Maxime Lacasse on 2017-11-26.
     * @version 1.0
     *
     */
    public class WeatherRequest extends AsyncTask<String, String, String> {
        //Re-creating variables city and apiKey, to be used in this subclass.
        private String city;
        private String countryCode;
        private String apiKey;
        private String format;
        private String latitude;
        private String longitude;

        /**
         * A constructor that takes in the city that the user inputted in the previous activity
         * as well as the all mighty API key which allows access to the JSON files on https://openweathermap.org/
         * @param city
         * @param apiKey
         */
        public WeatherRequest(String city, String apiKey, String format){
            this.city = city;
            this.apiKey = apiKey;
            this.format = format;
            logIt("City in weatherRequest: " + this.city);
        }

        /**
         * This method is called whenever the subclass weatherRequest gets called by the method .execute()
         * It simply calls the its super class as we don't have much to do before the execution.
         */
        @Override
        protected void onPreExecute() {
            logIt("onPreExecute: " + this.city);
            super.onPreExecute();
        }

        /**
         * This method is called whenever onPreExecute() is finished.
         * doInBackground is needed when using HttpURLConnection because in Android, it is forbidden
         * to accomplish a HttpURLConnection in the UI thread. It has to be in a seperate thread, or
         * else it will throw an exception.
         * This method is using HttpURLConnection to go into the given weatherURL and using a
         * private method, httpResultsToString, will grab the JSON file and return a StringBuilder
         * with the JSON file.
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            //The city and the apiKey from the parent class is passed through a String[] params
            //And they are set here. Without this, strangely weatherURLs' city/apikey will be null.
            String city = params[0];
            String apiKey = params[1];

            logIt("countrycode;" + this.countryCode);
            String weatherURL;
            //The weather URL which includes the user the city inputted in the previous activity
            //as well as the API key that was generated for me.
            if (this.format.equals(String.valueOf(1))) {
                this.countryCode = params[2];
                weatherURL = "http://api.openweathermap.org/data/2.5/forecast?q=" + this.city + "," + this.countryCode + this.apiKey;
            } else if (this.format.equals(String.valueOf(2))) {
                this.latitude = params[2];
                this.longitude = params[3];
                weatherURL = "http://api.openweathermap.org/data/2.5/uvi/forecast?lat=" + this.latitude + "&lon=" + this.longitude + this.apiKey;
            } else {
                this.latitude = params[1];
                this.longitude = params[2];
                weatherURL = "http://api.openweathermap.org/data/2.5/weather?lat=" + this.latitude + "&lon=" + this.longitude + this.apiKey;
            }
            logIt("apiKey in background: " + apiKey);
            logIt("background: " + weatherURL);
            //The results will be stored in this variable.
            String result = null;
            try {
                logIt("here");
                //Creating a URL with the weatherURL.
                URL url = new URL(weatherURL);
                //Opening the connection with the given url.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                logIt("here");

                //Creating an inputStream with the open connection.
                InputStream input = new BufferedInputStream(urlConnection.getInputStream());
                //Now that we have the Input stream, we use the private method httpResultsToString
                //To convert the information that we can obtain into a StringBuilder.

                result = httpResultsToString(input);
                logIt("result: " + result);
            } catch (Exception e) {
                e.printStackTrace();
                logIt("BLOCKED!");
            }
            //Returns the StringBuilder JSON file results to onPostExecute().
            return result;
        }

        /**
         * This method is called whenever doInBackground is finished.
         * Simply calls the super.onPostExecute with the results from doInBackground.
         * The JSON information from the website is passed back to the weather activity
         * to do it's parsing within it's own class, for easier setting of the UI.
         * Network background threading is complete.
         * Sockets closed.
         */
        @Override
        protected void onPostExecute(String s) {
            logIt("super");
            super.onPostExecute(s);
        }

    /**
     * This method is used to convert the items received from an InputStream into
     * a Stringbuilder, so that it can be manipulated in another method (onPostExecute).
     * @param inStream The open connection done by the HttpURLConnection.
     * @return A string containing all the information from the InputStream
     */
    private String httpResultsToString(InputStream inStream) {
        //This variable will be used to read every line to be able to append from it.
        String line = "";
        //Creating a stringbuilder, which will be the result returned at the end of the method
        StringBuilder returningResults = new StringBuilder();
        //Creating a InputStreamReader using the inStream from doInBackground method.
        InputStreamReader inStreamResults = new InputStreamReader(inStream);
        //Buffered reading to read much bigger chuncks of data rather than a character at a time.
        BufferedReader readBuffer = new BufferedReader(inStreamResults);

        try {
            //Reading through all the information that the inStream can supply us.
            while ((line = readBuffer.readLine()) != null) {
                //Appending every line to the StringBuilder.
                returningResults.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Returns the string with all the JSON information in it.
        //It is now ready to be converted into a JSONObject.
        return returningResults.toString();
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

