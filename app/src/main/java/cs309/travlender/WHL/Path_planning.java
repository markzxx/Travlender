package cs309.travlender.WHL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RideStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.RideRouteQuery;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.ArrayList;
import java.util.List;

import cs309.travelender.R;

import static com.amap.api.services.routepoisearch.RoutePOISearch.DrivingDefault;
import static com.amap.api.services.share.ShareSearch.BusDefault;

public class Path_planning extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    private RouteSearch routeSearch;
    Transportation transportation = new Transportation();

    private static double out_from_Latitude, out_from_Longitude, out_to_Latitude, out_to_Longitude;
    private String out_transportation, out_city;

    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_path_planning_whl);
        routeSearch = new RouteSearch(getApplicationContext());
        Bundle bundle = this.getIntent().getExtras();
        //传入目的地纬度
        double to_Latitude = bundle.getDouble("to_Latitude");
        //传入目的地经度
        double to_Longitude = bundle.getDouble("to_Longitude");
        //传入出发地纬度
        double from_Latitude = bundle.getDouble("from_Latitude");
        //传入出发地经度
        double from_Longitude = bundle.getDouble("from_Longitude");
        //传入选择的交通方式
        String transport = bundle.getString("transportation");

//        TextView textView = (TextView) findViewById(R.id.show_route);
//        String s = "\nto_Latitude: "+String.valueOf(to_Latitude);
//        s += "\nto_Longitude: "+String.valueOf(to_Longitude);
//        s += "\nfrom_Latitude: "+String.valueOf(from_Latitude);
//        s += "\nfrom_Longitude: "+String.valueOf(from_Longitude);
//        s += "\ntransport: "+String.valueOf(transport);
//        textView.setText(s);

        initBroadcast();

        LatLonPoint from = new LatLonPoint(from_Latitude, from_Longitude);
        LatLonPoint to = new LatLonPoint(to_Latitude, to_Longitude);
        float f = get_travel_time(from_Latitude, from_Longitude, to_Latitude, to_Longitude, transport, "深圳");
        Toast.makeText(Path_planning.this, String.valueOf(f), Toast.LENGTH_SHORT).show();
        //path_plan(from, to, transportation, "深圳");
    }

    public float get_travel_time(double from_Latitude, double from_Longitude, double to_Latitude, double to_Longitude, String transport, String city) {
        /*
         * from_Latitude 起点纬度
         * from_Longitude 起点经度
         * to_Latitude 终点纬度度
         * to_Longitude 终点经度
         * transportation 选择的交通方式
         * city 城市
         */
        this.out_from_Latitude = from_Latitude;
        this.out_from_Longitude = from_Longitude;
        this.out_to_Latitude = to_Latitude;
        this.out_to_Longitude = to_Longitude;
        this.out_transportation = transport;
        this.out_city = city;

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                path_plan(new LatLonPoint(out_from_Latitude, out_from_Longitude), new LatLonPoint(out_to_Latitude, out_to_Longitude), out_transportation, out_city);
            }
        }).start();
        for (int i=0;i<5;i++){
            try{
                Thread.sleep(3000);
                if (transportation.isReady())
                    return transportation.getTransportTime(transport);
            }catch (Exception e){
                Log.d("Path_planning", "thread broken");
            }
        }
        return (float)-1.0;
    }

    public void path_plan(LatLonPoint from, LatLonPoint to, String transportation, String city) {
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from, to);
        switch (transportation) {
            case Transportation.TRANSPORTATION_BUS:
                BusRouteQuery busRouteQuery = new BusRouteQuery(fat, BusDefault, city, 1);
                routeSearch.calculateBusRouteAsyn(busRouteQuery);
                break;
            case Transportation.TRANSPORTATION_WALK:
                WalkRouteQuery walkRouteQuery = new WalkRouteQuery(fat);
                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
                break;
            case Transportation.TRANSPORTATION_RIDE:
                RideRouteQuery rideRouteQuery = new RideRouteQuery(fat);
                routeSearch.calculateRideRouteAsyn(rideRouteQuery);
                break;
            case Transportation.TRANSPORTATION_DRIVE:
                DriveRouteQuery driveRouteQuery = new DriveRouteQuery(fat, DrivingDefault, null, null, "");
                routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
                break;
            default:
                Toast.makeText(getApplicationContext(), "请选择合适的交通工具", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        if (i == 1000 && busRouteResult != null) {
            ArrayList<Float> time_RouteBusLineItem = new ArrayList<Float>();
            float bus_time_max = 0;
            float bus_time_min = Float.MAX_VALUE;
            float bus_time_avg = 0;
            List<BusPath> busPaths = busRouteResult.getPaths();
            for (BusPath dp : busPaths) {
                List<BusStep> busSteps = dp.getSteps();
                float bus_path_time = 0;
                for (BusStep ds : busSteps) {
                    for (RouteBusLineItem routeBusLineItem : ds.getBusLines()) {
                        bus_path_time += routeBusLineItem.getDuration();
                    }
                }
                if (bus_path_time > bus_time_max) {
                    bus_time_max = bus_path_time;
                }
                if (bus_path_time < bus_time_min) {
                    bus_time_min = bus_path_time;
                }
                bus_time_avg += bus_path_time;
            }
            bus_time_avg /= busPaths.size();
            transportation.setBus_time_min(bus_time_min);
            transportation.setBus_time_max(bus_time_max);
            transportation.setBus_time_avg(bus_time_avg);
            sendBroadcast(bus_time_avg);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000 && driveRouteResult != null) {
            List<DrivePath> drivePaths = driveRouteResult.getPaths();
            float drive_time = 0;
            DrivePath dp = drivePaths.get(0);
            List<DriveStep> driveSteps = dp.getSteps();
            for (DriveStep ds : driveSteps) {
                drive_time += ds.getDuration();
            }
            transportation.setDrive_time(drive_time);
            sendBroadcast(drive_time);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (i == 1000 && walkRouteResult != null) {
            List<WalkPath> walkPaths = walkRouteResult.getPaths();
            float walk_time = 0;
            for (WalkPath dp : walkPaths) {
                List<WalkStep> walkSteps = dp.getSteps();
                for (WalkStep ds : walkSteps) {
                    walk_time += ds.getDuration();
                }
            }
            transportation.setWalk_time(walk_time);
            sendBroadcast(walk_time);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        if (i == 1000) {
            List<RidePath> ridePaths = rideRouteResult.getPaths();
            float ride_time = 0;
            for (RidePath dp : ridePaths) {
                List<RideStep> rideSteps = dp.getSteps();
                for (RideStep ds : rideSteps) {
                    ride_time += ds.getDuration();
                }
            }
            transportation.setRide_time(ride_time);
            sendBroadcast(ride_time);
        }
    }

    public String format_time(double time){
        int second = (int)time;
        int hour = second/3600;
        int minute = (second%3600)/60;
        return String.format("%d小时%d分钟%d秒", hour, minute, second);
    }

    private void initBroadcast(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    private void sendBroadcast(double time){
        Intent intent = new Intent("com.example.dell.map.LocationReceiver");
        intent.putExtra("time", String.valueOf(time));
        localBroadcastManager.sendBroadcast(intent);
    }

}
//                Toast.makeText(getApplicationContext(), String.format("%d",i), Toast.LENGTH_SHORT).show();
//                if (i==1000){
//                    Toast.makeText(getApplicationContext(), "收到异步回调", Toast.LENGTH_SHORT).show();
//                    List<DrivePath> drivePaths = driveRouteResult.getPaths();
//                    TextView textView = (TextView) findViewById(R.id.show_route);
//                    ArrayList<String> infor = new ArrayList<String>();
//                    time_sum = 0;
//                    infor.add(String.format("打车费用：%.1f元\n", driveRouteResult.getTaxiCost()));
//                    for (DrivePath dp : drivePaths){
//                        infor.add(dp.getStrategy()+"\n");
//                        List<DriveStep> driveSteps = dp.getSteps();
//                        for (DriveStep ds : driveSteps){
//                            time_sum+=ds.getDuration();
//                            infor.add(String.format("\n路名：%s, 距离：%s, 时间：%.1f秒\n", ds.getRoad(), ds.getDistance(), ds.getDuration()));
//                            infor.add(String.format("导航主要操作：%s\n", ds.getAction()));
//                            infor.add(String.format("导航辅助操作：%s\n", ds.getAssistantAction()));
//                            infor.add(String.format("驾车路段的行驶指示：%s\n", ds.getInstruction()));
//                        }
//                    }
//                    String s = "";
//                    for (String line : infor){
//                        s+=line;
//                    }
//                    s = String.format("路程总时间：%s\n",format_time(time_sum)) + s;
//                    textView.setText(s);
//                }