package cz.thehumr.sportactivitydiary.model;

/**
 * Created by Ondřej on 22.03.2016.
 */
public class Weather {
    private String name;
    private String iconURL;
    private double temperature;
    private double feelsTemperature;
    private double wind;

    public Weather() {
    }

    public Weather(String name, String iconURL, double temperature, double feelsTemperature, double wind) {
        this.name = name;
        this.iconURL = iconURL;
        this.temperature = temperature;
        this.feelsTemperature = feelsTemperature;
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsTemperature() {
        return feelsTemperature;
    }

    public void setFeelsTemperature(double feelsTemperature) {
        this.feelsTemperature = feelsTemperature;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }
}
