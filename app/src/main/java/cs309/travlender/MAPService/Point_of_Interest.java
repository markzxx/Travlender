package cs309.travlender.MAPService;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

/**
 * Created by Dell on 2017/10/28.
 */

public class Point_of_Interest {
    private String name;
    private String subname;
    private int imageId;
    private int distance;
    private double latitude;
    private double longitude;
    private PoiItem poiItem;

    public Point_of_Interest(String name, int imageId, String subname, int distance, PoiItem poiItem){
        this.imageId = imageId;
        this.name = name;
        this.subname = subname;
        this.distance = distance;
        this.poiItem = poiItem;
        latitude = poiItem.getLatLonPoint().getLatitude();
        longitude = poiItem.getLatLonPoint().getLongitude();
    }

    public String getName(){
        return name;
    }

    public double getLatitude(){ return latitude; }

    public double getLongitude(){ return longitude; }

    public PoiItem getPoiItem(){ return poiItem; }

    public LatLonPoint getLatLonPoint(){ return new LatLonPoint(latitude, longitude); }

    public String getSubname() { return subname; }

    public int getImageId(){
        return imageId;
    }

    public String getDistance() {
        if (distance>999){
            return String.format("%.1fkm", (float)distance/1000);
        }
        else
            return String.format("%dm", distance);
    }

}
