package cz.thehumr.sportactivitydiary.model;

/**
 * Created by Ondřej on 22.03.2016.
 */
public class Location {
    private double latitude;
    private double longitude;
    private String name;
    private int numOfWorkouts;

    public Location() {
    }

    public Location(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfWorkouts() {
        return numOfWorkouts;
    }

    public void setNumOfWorkouts(int numOfWorkouts) {
        this.numOfWorkouts = numOfWorkouts;
    }
}
