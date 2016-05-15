package cz.thehumr.sportactivitydiary.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.thehumr.sportactivitydiary.model.Weather;

/**
 * Created by Ondřej on 24.04.2016.
 */
public class WeatherApi {
    public static JSONObject getJsonObject(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        StringBuilder jsonResult = new StringBuilder();

        int read;
        char[] buff = new char[1024];

        while ((read = in.read(buff)) != -1) {
            jsonResult.append(buff, 0, read);
        }

        return new JSONObject(jsonResult.toString());
    }

    public static Weather getWeatherByLocation(String latitude, String longitude, String date){
        String url = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=0c989a4aefb8466e896131504162203&q=" + latitude + "," + longitude + "&date=" + date + "&tp=24&format=json";
        JSONObject jsonObject = null;
        try {
            jsonObject = WeatherApi.getJsonObject(url);

            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
            JSONArray jsonArrayWeather = jsonObjectData.getJSONArray("weather");
            JSONObject jsonObjectWeatherData = jsonArrayWeather.getJSONObject(0);
            JSONArray jsonArrayHourly = jsonObjectWeatherData.getJSONArray("hourly");
            JSONObject jsonObjectHourly = jsonArrayHourly.getJSONObject(0);

            double temperature = jsonObjectHourly.getDouble("tempC");
            double wind = jsonObjectHourly.getDouble("windspeedKmph");
            double feelsTemperature = jsonObjectHourly.getDouble("FeelsLikeC");

            JSONArray jsonArrayWeatherDesc = jsonObjectHourly.getJSONArray("weatherDesc");
            JSONObject jsonObjectWeatherDesc = jsonArrayWeatherDesc.getJSONObject(0);
            String name = jsonObjectWeatherDesc.getString("value");

            JSONArray jsonArrayWeatherIconURL = jsonObjectHourly.getJSONArray("weatherIconUrl");
            JSONObject jsonObjectWeatherIconURL = jsonArrayWeatherIconURL.getJSONObject(0);
            String iconURL = jsonObjectWeatherIconURL.getString("value");

            Weather weather = new Weather(name, iconURL, temperature, feelsTemperature, wind);

            return weather;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
