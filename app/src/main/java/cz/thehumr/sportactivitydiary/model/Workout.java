package cz.thehumr.sportactivitydiary.model;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ondřej on 07.03.2016.
 */
public class Workout {
    private int id;
    private String type;
    private String date;
    private String time;
    private String duration;
    private double distance;
    private String description;
    private double speed;
    private double feeling;
    private Location location;
    private Weather weather;


    public Workout() {
    }

    public Workout(int id, String type, String date, String time, String duration, double distance, String description, double speed, Location location) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.distance = distance;
        this.description = description;
        this.speed = speed;
        this.location = location;
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date mDate = sdf.parse(duration);
            long time = mDate.getTime();
            this.speed = distance / time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getFeeling() {
        return feeling;
    }

    public void setFeeling(double feeling) {
        this.feeling = feeling;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}


