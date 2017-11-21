package cs309.travlender.WHL;

/**
 * Created by Dell on 2017/11/3.
 */

public class Transportation {

    public static final String TRANSPORTATION_BUS = "bus";
    public static final String TRANSPORTATION_DRIVE = "drive";
    public static final String TRANSPORTATION_RIDE = "ride";
    public static final String TRANSPORTATION_WALK = "walk";

    private float drive_time;
    private float walk_time;
    private float ride_time;
    private float bus_time_max;
    private float bus_time_min;
    private float bus_time_avg;

    public boolean isReady() {
        return flag;
    }

    private boolean flag = false;

    public float getTransportTime(String transportation){
        switch (transportation) {
            case TRANSPORTATION_BUS:
                return bus_time_avg;
            case TRANSPORTATION_WALK:
                return walk_time;
            case TRANSPORTATION_RIDE:
                return ride_time;
            case TRANSPORTATION_DRIVE:
                return drive_time;
            default:
                return (float) -1.0;
        }
    }

    public void setDrive_time(float drive_time) {
        this.drive_time = drive_time;
        flag = true;
    }

    public void setWalk_time(float walk_time) {
        this.walk_time = walk_time;
        flag = true;
    }

    public void setRide_time(float ride_time) {
        this.ride_time = ride_time;
        flag = true;
    }

    public void setBus_time_max(float bus_time_max) {
        this.bus_time_max = bus_time_max;
        flag = true;
    }

    public void setBus_time_min(float bus_time_min) {
        this.bus_time_min = bus_time_min;
        flag = true;
    }

    public void setBus_time_avg(float bus_time_avg) {
        this.bus_time_avg = bus_time_avg;
        flag = true;
    }

}
