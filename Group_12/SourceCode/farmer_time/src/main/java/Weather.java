public class Weather {
    private String city;

    private double temperature;

    private String description;

    private long humidity;

    public Weather(String city, double temperature, String description, Long humidity) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
    }

    public String getCity() {
        return this.city;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public String getDescription() {
        return this.description;
    }

    public long getHumidity() {
        return this.humidity;
    }
}