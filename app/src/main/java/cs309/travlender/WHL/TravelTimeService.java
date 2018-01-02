package cs309.travlender.WHL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.amap.api.services.routepoisearch.RoutePOISearch.DrivingDefault;
import static com.amap.api.services.share.ShareSearch.BusDefault;

public class TravelTimeService extends Service {

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    // 路线规划请求
    private RouteSearch routeSearch;

    private double myLat;
    private double myLongt;
    private double dst_Lat;
    private double dst_Longt;

    private String current_city="深圳";
    private String travel_mode="drive";
    static public Set<Integer> querySet = new HashSet<>();
    private int times;

    // TODO: Rename actions, choose action names that describe tasks that this
    // Action的名字
    private static final String ACTION_TRAVEL_TIME = "com.example.dell.map_3.action.TRAVEL_TIME";
    private static final String ACTION_LOCATION = "com.example.dell.map_3.action.LOCATION";

    // TODO: parameters for ACTION_TRAVEL_TIME
    public static final String EXTRA_PARAM_LATITUDE = "com.example.dell.map_3.extra.LATITUDE";
    public static final String EXTRA_PARAM_LONGITUDE = "com.example.dell.map_3.extra.LONGITUDE";
    public static final String EXTRA_PARAM_TRANSPORTATION = "com.example.dell.map_3.extra.TRANSPORTATION";
    public static final String EXTRA_PARAM_QUERY_ID = "com.example.dell.map_3.extra.QUERY_ID";

    public TravelTimeService() {
    }

    // 开启服务用的函数
    public static void startServiceTravelTime(Context context, double latitude, double longitude, String transportatiton, int id) {
//        Toast.makeText(context,"startActionTravelTime", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, TravelTimeService.class);
        intent.setAction(ACTION_TRAVEL_TIME);
        intent.putExtra(EXTRA_PARAM_LATITUDE, String.valueOf(latitude));
        intent.putExtra(EXTRA_PARAM_LONGITUDE, String.valueOf(longitude));
        intent.putExtra(EXTRA_PARAM_QUERY_ID, String.valueOf(id));
        intent.putExtra(EXTRA_PARAM_TRANSPORTATION, transportatiton);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("MyService", "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
//        Toast.makeText(getApplicationContext(),"onStartCommand", Toast.LENGTH_SHORT).show();
        times = 3;
        final double latitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LATITUDE));
        final double longitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LONGITUDE));
        final String tp = intent.getStringExtra(EXTRA_PARAM_TRANSPORTATION);
        final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_QUERY_ID));
        Toast.makeText(getApplicationContext(),tp, Toast.LENGTH_SHORT).show();
        handleActionTravelTime(latitude, longitude, tp, id, startId);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleActionTravelTime(double to_Latitude, double to_Longitude, String transportation, int id, int startid) {
        // TODO: Handle action
        // 具体执行一个任务
//        Toast.makeText(getApplicationContext(),"开始服务", Toast.LENGTH_SHORT).show();
        switch (transportation){
            case "自驾":
                travel_mode = Transportation.TRANSPORTATION_DRIVE;
                break;
            case "骑行":
                travel_mode = Transportation.TRANSPORTATION_RIDE;
                break;
            case "步行":
                travel_mode = Transportation.TRANSPORTATION_WALK;
                break;
            case "公交":
                travel_mode = Transportation.TRANSPORTATION_BUS;
                break;
        }

        dst_Lat = to_Latitude;
        dst_Longt = to_Longitude;
        location(id, startid);
    }

    private void sendBroadcast(int time, int id){
        Intent intent = new Intent("com.example.dell.map.LocationReceiver");
        intent.putExtra("time", (long)time);
        intent.putExtra("id", id);
        sendBroadcast(intent);
    }

    private void sendAsynQuery(LatLonPoint from, LatLonPoint to, String tp, String city, int id){
        Toast.makeText(getApplicationContext(),"发送查询", Toast.LENGTH_SHORT).show();
        routeSearch = new RouteSearch(getApplicationContext());
        final int _id = id;
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener(){
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//                Toast.makeText(getApplicationContext(),"搜索结果", Toast.LENGTH_SHORT).show();
                if (i == 1000 && busRouteResult != null) {
//                    Toast.makeText(getApplicationContext(),"搜索结果解析", Toast.LENGTH_SHORT).show();
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
                    sendBroadcast((int)bus_time_avg, _id);
                }
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
//                Toast.makeText(getApplicationContext(),"搜索结果", Toast.LENGTH_SHORT).show();
                if (i == 1000 && driveRouteResult != null) {
                    List<DrivePath> drivePaths = driveRouteResult.getPaths();
                    float drive_time = 0;
                    DrivePath dp = drivePaths.get(0);
                    List<DriveStep> driveSteps = dp.getSteps();
                    for (DriveStep ds : driveSteps) {
                        drive_time += ds.getDuration();
                    }
//                    Toast.makeText(getApplicationContext(),"发广播", Toast.LENGTH_SHORT).show();
                    sendBroadcast((int)drive_time, _id);
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
                    sendBroadcast((int)walk_time, _id);
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
                    sendBroadcast((int)ride_time, _id);
                }
            }
        });
        RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from, to);
        switch (tp) {
            case Transportation.TRANSPORTATION_BUS:
                RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(fat, BusDefault, city, 1);
                routeSearch.calculateBusRouteAsyn(busRouteQuery);
                break;
            case Transportation.TRANSPORTATION_WALK:
                RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fat);
                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
                break;
            case Transportation.TRANSPORTATION_RIDE:
                RouteSearch.RideRouteQuery rideRouteQuery = new RouteSearch.RideRouteQuery(fat);
                routeSearch.calculateRideRouteAsyn(rideRouteQuery);
                break;
            case Transportation.TRANSPORTATION_DRIVE:
                RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fat, DrivingDefault, null, null, "");
                routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
                break;
            default:
                Toast.makeText(getApplicationContext(), "请选择合适的交通工具", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    // 开启定位功能
    private void location(int id, int startid){
        final int _id = id;
        final int _startid  = startid;
        //声明定位回调监听器
        mLocationListener = new AMapLocationListener(){
            /**
             * 定位成功后回调函数
             */
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
//                        Toast.makeText(getApplicationContext(), "定位成功", Toast.LENGTH_SHORT).show();
                        myLat = amapLocation.getLatitude();//获取纬度
                        myLongt = amapLocation.getLongitude();//获取经度
                        current_city = amapLocation.getCity();//获取当前城市
                        //定位成功，发起查询请求
                        if(times>0)
                        {
                            sendAsynQuery(new LatLonPoint(myLat, myLongt), new LatLonPoint(dst_Lat, dst_Longt), travel_mode, current_city, _id);
                            times--;
                            System.out.println(times);
                        }
                        else {
                            stopSelf(_startid);
                        }
//                        amapLocation.getAddress();//地址
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //如果单次定位发生超时情况，定位随即终止；连续定位状态下当前这一次定位会返回超时，但按照既定周期的定位请求会继续发起。
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }
}
