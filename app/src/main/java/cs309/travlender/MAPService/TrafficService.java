package cs309.travlender.MAPService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.TMC;
import com.amap.api.services.route.WalkRouteResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.amap.api.services.routepoisearch.RoutePOISearch.DrivingDefault;

public class TrafficService extends Service  implements RouteSearch.OnRouteSearchListener {

    public static String TRAFFIC_STATE_1 = "畅通";
    public static String TRAFFIC_STATE_2 = "略有拥堵";
    public static String TRAFFIC_STATE_3 = "拥堵";
    public static String TRAFFIC_STATE_4 = "非常拥堵";

    private GeocodeSearch geocoderSearch;
    private String address;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    private RouteSearch routeSearch;

    private double myLat;
    private double myLongt;
    private double dst_Lat;
    private double dst_Longt;

    private String current_city="深圳";
    private String travel_mode="drive";
    static public Set<Integer> querySet = new HashSet<>();
    private int startid;
    private int _id = -12306;

    // TODO: Rename actions, choose action names that describe tasks that this
    // Action的名字
    private static final String ACTION_TRAFFIC = "com.example.dell.map_3.action.ACTION_TRAFFIC";

    // TODO: parameters for ACTION_TRAVEL_TIME
    public static final String EXTRA_PARAM_LATITUDE = "com.example.dell.map_3.extra.LATITUDE";
    public static final String EXTRA_PARAM_LONGITUDE = "com.example.dell.map_3.extra.LONGITUDE";
    public static final String EXTRA_PARAM_QUERY_ID = "com.example.dell.map_3.extra.QUERY_ID";

    public TrafficService() {
    }

    // 查询从当前位置到目的地交通情况用的函数
    public static void startServiceTraffic(Context context, double latitude, double longitude, int id) {
        Intent intent = new Intent(context, TrafficService.class);
        intent.setAction(ACTION_TRAFFIC);
        intent.putExtra(EXTRA_PARAM_LATITUDE, String.valueOf(latitude));
        intent.putExtra(EXTRA_PARAM_LONGITUDE, String.valueOf(longitude));
        intent.putExtra(EXTRA_PARAM_QUERY_ID, String.valueOf(id));
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
//        Toast.makeText(getApplicationContext(),"onStartCommand", Toast.LENGTH_SHORT).show();
//        Log.d("MyService", "onStartCommand");
        this.startid = startId;
        if (intent.getAction() == ACTION_TRAFFIC) {
            final double latitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LATITUDE));
            final double longitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LONGITUDE));
            final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_QUERY_ID));
            _id = id;
            handleActionTraffic(latitude, longitude, id);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleActionTraffic(double to_Latitude, double to_Longitude, int id){
        Log.d("TrafficService", "处理交通情况查询请求");
        dst_Lat = to_Latitude;
        dst_Longt = to_Longitude;
        location(id);
    }

    // 开启定位功能
    private void location(int id){
        final int _id = id;
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
                        if(!querySet.contains(_id)) {
                            path_plan(new LatLonPoint(myLat, myLongt), new LatLonPoint(dst_Lat, dst_Longt), current_city);
                        }
                        else {
                            stopSelf(startid);
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

    private void path_plan(LatLonPoint from, LatLonPoint to, String city) {
        if(routeSearch==null){
            routeSearch = new RouteSearch(getApplicationContext());
        }
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from, to);
        Log.d("TrafficService", "path_plan");
        RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fat, DrivingDefault, null, null, "");
        routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000 && driveRouteResult != null) {
            DrivePath drivePath = driveRouteResult.getPaths().get(0);
            List<String> roads_situation = new LinkedList<String>();
            int good_traffic = 0;
            for (DriveStep ds : drivePath.getSteps()){
                Log.d("TrafficService", "道路名称："+ds.getRoad());
                Log.d("TrafficService", "行驶指示："+ds.	getInstruction());
                roads_situation.add(ds.getTMCs().get(0).getStatus());
                if (ds.getTMCs().get(0).getStatus() == "畅通"){
                    good_traffic++;
                }
                for (TMC tmc : ds.getTMCs()){
                    Log.d("TrafficService", "路况："+tmc.getStatus());
                }
            }
            /**
             *  根据畅通路段占所有经过路段的比例来评定交通状况
             */
            if (good_traffic*1.0/roads_situation.size() > 0.8){
                sendBroadcast(TRAFFIC_STATE_1);
            }
            else if (good_traffic*1.0/roads_situation.size() > 0.5) {
                sendBroadcast(TRAFFIC_STATE_2);
            }
            else if (good_traffic*1.0/roads_situation.size() > 0.3) {
                sendBroadcast(TRAFFIC_STATE_3);
            }
            else {
                sendBroadcast(TRAFFIC_STATE_4);
            }
        }
    }

    private void sendBroadcast(String traffic_situation){
        Intent intent = new Intent(TrafficServiceReceiver.BROADCAST_SIGNAL);
        if (_id!=-12306){
            intent.putExtra("id", String.valueOf(_id));
        }
        intent.putExtra(TrafficServiceReceiver.GET_TRAFFIC_SITUATION, traffic_situation);
        sendBroadcast(intent);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (i == 1000 && walkRouteResult != null) {
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        if (i == 1000) {
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        if (i == 1000 && busRouteResult != null) {
        }
    }

}
