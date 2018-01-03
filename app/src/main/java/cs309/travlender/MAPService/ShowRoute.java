package cs309.travlender.MAPService;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
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
import java.util.List;

import cs309.travelender.R;

import static com.amap.api.services.routepoisearch.RoutePOISearch.DrivingDefault;
import static com.amap.api.services.share.ShareSearch.BusDefault;

public class ShowRoute extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    public static String DRAWROUTE_LATITUDE = "DRAWROUTE.DRAWROUTE_LATITUDE";
    public static String DRAWROUTE_LONGITUDE = "DRAWROUTE.DRAWROUTE_LONGITUDE";
    public static String DRAWROUTE_TRANSPORTATION = "DRAWROUTE.DRAWROUTE_TRANSPORTATION";
    public static String DRAWROUTE_TITLE = "DRAWROUTE.DRAWROUTE_TITLE";

    private RouteSearch routeSearch;
    private AMap aMap;
    private MapView mMapView;
    private LocationSource.OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;

    private String current_city="深圳";
    private String transportation="drive";
    private double myLat;
    private double myLongt;
    private double aim_Lat;
    private double aim_Longt;

    private Button button;
    private String title = "";

    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_draw_route_whl);
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        Log.d("555","555");
        mMapView = (MapView) findViewById(R.id.draw_route);
//        Log.d("DrawRoute", "88++88  mMapView "+(mMapView==null));
//        Log.d("DrawRoute", "88++88  savedInstanceState "+(savedInstanceState==null));
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mMapView.getMap();
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 隐藏原有定位按钮
        aMap.getUiSettings().setZoomControlsEnabled(false); // 隐藏默认缩放按钮
        button = (Button) findViewById(R.id.draw_route_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                相当于用户确认这个地点，然后返回配置日程的界面
                //                这里可以用intent跳转其他页面

            }
        });

//        获取参数
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            //传入目的地纬度
            aim_Lat = bundle.getDouble(DRAWROUTE_LATITUDE);
            //传入目的地经度
            aim_Longt = bundle.getDouble(DRAWROUTE_LONGITUDE);
            transportation = bundle.getString(DRAWROUTE_TRANSPORTATION);
            title = bundle.getString(DRAWROUTE_TITLE);
            Log.d("99999",""+aim_Lat);
            Log.d("99999",""+aim_Longt);
            Log.d("99999",""+transportation);
            Log.d("99999",""+title);

//            path_plan(new LatLonPoint(myLat,myLongt), new LatLonPoint(aim_Lat, aim_Longt), transportation, current_city);

        }
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat,myLongt), 14));

        setUpLocation(savedInstanceState); //获取当前定位
    }

    public void setButtonText(String s){
        Log.d("DrawRoute", "88++88 setButtonText");
        if(button==null) {
            Log.d("DrawRoute", "88++88 setButtonText");
            button = (Button) findViewById(R.id.draw_route_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                相当于用户确认这个地点，然后返回配置日程的界面
                    //                这里可以用intent跳转其他页面

                }
            });
        }
        button.setVisibility(View.VISIBLE);
        Log.d("DrawRoute", "88++88 VISIBLE");
        button.setText(s);
        Log.d("DrawRoute", "88++88 setText");
    }


    public void setUpLocation(Bundle savedInstanceState) {
        //
        //显示地图
        //
        aMap.setLocationSource(new LocationSource() {
            /**
             * 激活定位
             */
            @Override
            public void activate(OnLocationChangedListener listener) {
                mListener = listener;
                if (mlocationClient == null) {
                    mlocationClient = new AMapLocationClient(getApplicationContext());
                    mLocationOption = new AMapLocationClientOption();

                    //设置定位蓝点的Style
//                    myLocationStyle = new MyLocationStyle();
//                    myLocationStyle.interval(2000);
//                    myLocationStyle.showMyLocation(true);
//                    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
//                    myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_arrow)));
//                    aMap.setMyLocationStyle(myLocationStyle);
//                    aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

                    //设置定位监听
                    mlocationClient.setLocationListener(new AMapLocationListener() {
                        /**
                         * 定位成功后回调函数
                         */
                        @Override
                        public void onLocationChanged(AMapLocation amapLocation) {
                            if (mListener != null && amapLocation != null) {
                                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                                    //定位成功回调信息，设置相关消息
                                    myLat = amapLocation.getLatitude();//获取纬度
                                    myLongt = amapLocation.getLongitude();//获取经度
                                    current_city = amapLocation.getCity();
//                                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLongt), 14));
//                                    Log.d("DrawRoute","88++88 定位成功");
                                    if(state){
//                                        Log.d("","88++88 "+myLat+" "+myLongt);
                                        path_plan(new LatLonPoint(myLat,myLongt), new LatLonPoint(aim_Lat, aim_Longt), transportation, current_city);
                                    }
                                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                                } else {
                                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                                    Log.e("AmapErr", errText);
                                }
                            }
                        }
                    });
                    //设置为高精度定位模式
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                    //设置定位参数
                    mlocationClient.setLocationOption(mLocationOption);
                    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                    // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                    // 在定位结束后，在合适的生命周期调用onDestroy()方法
                    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                    mlocationClient.startLocation();
                }
            }

            /**
             * 停止定位
             */
            @Override
            public void deactivate() {
                mListener = null;
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                }
                mlocationClient = null;
            }
        });
        // 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
    }

    private void path_plan(LatLonPoint from, LatLonPoint to, String transportation, String city) {
        if(routeSearch==null){
            routeSearch = new RouteSearch(getApplicationContext());
        }
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from, to);
        switch (transportation) {
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
                Log.d("DrawRoute", "88++88 drive");
                RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fat, DrivingDefault, null, null, "");
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
            float bus_distance_max = 0;
            float bus_distance_min = Float.MAX_VALUE;
            float bus_distance_avg = 0;
            List<BusPath> busPaths = busRouteResult.getPaths();
            BusPath fastest_path = busPaths.get(0);
            for (BusPath dp : busPaths) {
                List<BusStep> busSteps = dp.getSteps();
                float bus_path_time = 0;
                float bus_path_distance = 0;
                for (BusStep ds : busSteps) {
                    for (RouteBusLineItem routeBusLineItem : ds.getBusLines()) {
                        bus_path_time += routeBusLineItem.getDuration();
                        bus_path_distance += routeBusLineItem.getDistance();
                    }
                }
                if (bus_path_time > bus_time_max) {
                    bus_time_max = bus_path_time;
                    bus_distance_max = bus_path_distance;
                }
                if (bus_path_time < bus_time_min) {
                    bus_time_min = bus_path_time;
                    bus_distance_min = bus_path_distance;
                    fastest_path = dp;
                }
                bus_time_avg += bus_path_time;
                bus_distance_avg += bus_path_distance;
            }
            bus_time_avg /= busPaths.size();
            bus_distance_avg /= busPaths.size();
            setButtonText(title+"\n公交距离："+format_distance(bus_distance_min)+"\n公交用时："+format_time(bus_time_min));

            List<LatLng> latLngs = new ArrayList<LatLng>();
//            LatLonPoint northeast = dp.getSteps().get(0).getPolyline().get(0);
//            LatLonPoint southwest = dp.getSteps().get(0).getPolyline().get(0);
            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);
            latLngs.add(convertLatLonPoint(southwest));
            for(BusStep driveStep : fastest_path.getSteps()){
                for(RouteBusLineItem tbli : driveStep.getBusLines()){
                    for(LatLonPoint lp : tbli.getPolyline()){
                        latLngs.add(convertLatLonPoint(lp));
                        if(lp.getLatitude() > northeast.getLatitude()){
                            northeast.setLatitude(lp.getLatitude());
                        }if(lp.getLongitude() > northeast.getLongitude()){
                            northeast.setLongitude(lp.getLongitude());
                        }
                        if(lp.getLatitude() < southwest.getLatitude()){
                            southwest.setLatitude(lp.getLatitude());
                        }if(lp.getLongitude() < southwest.getLongitude()){
                            southwest.setLongitude(lp.getLongitude());
                        }
                    }
                }
            }
            Log.d("DrawRoute", "88++88 list"+latLngs.size());
            latLngs.add(convertLatLonPoint(new LatLonPoint(aim_Lat, aim_Longt)));
            drawPolyLines(latLngs, southwest, northeast);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == 1000 && driveRouteResult != null) {
            List<DrivePath> drivePaths = driveRouteResult.getPaths();
            float drive_time = 0;
            float drive_distance = 0;
            DrivePath dp = drivePaths.get(0);
            List<DriveStep> driveSteps = dp.getSteps();

            List<LatLng> latLngs = new ArrayList<LatLng>();
//            LatLonPoint northeast = dp.getSteps().get(0).getPolyline().get(0);
//            LatLonPoint southwest = dp.getSteps().get(0).getPolyline().get(0);
            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);

            for (DriveStep ds : driveSteps) {
                drive_time += ds.getDuration();
                drive_distance += ds.getDistance();
                for(LatLonPoint lp : ds.getPolyline()){
                    latLngs.add(convertLatLonPoint(lp));
                    if(lp.getLatitude() > northeast.getLatitude()){
                        northeast.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() > northeast.getLongitude()){
                        northeast.setLongitude(lp.getLongitude());
                    }
                    if(lp.getLatitude() < southwest.getLatitude()){
                        southwest.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() < southwest.getLongitude()){
                        southwest.setLongitude(lp.getLongitude());
                    }
                }
            }
            setButtonText(title+"\n驾车距离："+format_distance(drive_distance)+"\n驾车用时："+format_time(drive_time));

            drawPolyLines(latLngs, southwest, northeast);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (i == 1000 && walkRouteResult != null) {
            float walk_time = 0;
            float walk_distance = 0;
            WalkPath walkPath = walkRouteResult.getPaths().get(0);
            List<WalkStep> walkSteps = walkPath.getSteps();

            List<LatLng> latLngs = new ArrayList<LatLng>();
            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);

            for (WalkStep ds : walkSteps) {
                walk_time += ds.getDuration();
                walk_distance += ds.getDistance();
                for(LatLonPoint lp : ds.getPolyline()){
                    latLngs.add(convertLatLonPoint(lp));
                    if(lp.getLatitude() > northeast.getLatitude()){
                        northeast.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() > northeast.getLongitude()){
                        northeast.setLongitude(lp.getLongitude());
                    }
                    if(lp.getLatitude() < southwest.getLatitude()){
                        southwest.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() < southwest.getLongitude()){
                        southwest.setLongitude(lp.getLongitude());
                    }
                }
            }

            setButtonText(title+"\n步行距离："+format_distance(walk_distance)+"\n步行用时："+format_time(walk_time));

            drawPolyLines(latLngs, southwest, northeast);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        if (i == 1000) {
            float ride_time = 0;
            float ride_distance = 0;
            RidePath ridePath = rideRouteResult.getPaths().get(0);
            List<RideStep> rideSteps = ridePath.getSteps();

            List<LatLng> latLngs = new ArrayList<LatLng>();
            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);

            for (RideStep ds : rideSteps) {
                ride_time += ds.getDuration();
                ride_distance += ds.getDistance();
                for(LatLonPoint lp : ds.getPolyline()){
                    latLngs.add(convertLatLonPoint(lp));
                    if(lp.getLatitude() > northeast.getLatitude()){
                        northeast.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() > northeast.getLongitude()){
                        northeast.setLongitude(lp.getLongitude());
                    }
                    if(lp.getLatitude() < southwest.getLatitude()){
                        southwest.setLatitude(lp.getLatitude());
                    }if(lp.getLongitude() < southwest.getLongitude()){
                        southwest.setLongitude(lp.getLongitude());
                    }
                }
            }

            setButtonText(title+"\n骑行距离："+format_distance(ride_distance)+"\n骑行用时："+format_time(ride_time));

            drawPolyLines(latLngs, southwest, northeast);
        }
    }

    private String format_time(double time){
        int second = (int)time;
        int hour = second/3600;
        int minute = (second%3600)/60;
        return String.format("%d小时%d分钟%d秒", hour, minute, second%60);
    }

    private String format_distance(double meters){
        if (meters<100){
            return String.format("100米内");
        }
        else if (meters<1000){
            return String.format("%d米", (int)meters);
        }
        else{
            return String.format("%.1f千米", (meters/1000));
        }
    }

    private void drawPolyLines(List<LatLng> latLngs, LatLonPoint southwest, LatLonPoint northeast){
        state = false;
        if(aMap==null){
            aMap = mMapView.getMap();
        }
        latLngs.add(0, new LatLng(myLat, myLongt));
        latLngs.add(new LatLng(aim_Lat, aim_Longt));
//        画路线
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));

        try {
//            调整画面位置
            aMap.moveCamera(new CameraUpdateFactory().newLatLngBounds(new LatLngBounds(convertLatLonPoint(southwest), convertLatLonPoint(northeast)), 14));
//            给目的地添加logo
            MarkerOptions markerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.small_location)))
                    .position(new LatLng(aim_Lat,aim_Longt)).	draggable(false);//.title("目的地");
            Marker locationMarker_1 = aMap.addMarker(markerOption);
//            locationMarker_1.showInfoWindow();
//            给出发地（当前定位）添加logo
            MarkerOptions markerOption_2 = new MarkerOptions()
                    .anchor((float) 0.5, (float)0.5)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_arrow)))
                    .position(new LatLng(myLat,myLongt)).	draggable(false);//.title("您的位置");
            Marker locationMarker_2 = aMap.addMarker(markerOption_2);

        }catch (AMapException e){
            Log.d("DrawRoute", "88++88 AMapException");
        }
    }


//    public String format_time(double time){
//        Log.d("DrawRoute", "88++88 format_time");
//        int second = (int)time;
//        int hour = second/3600;
//        int minute = (second%3600)/60;
//        second = second%60;
//        Log.d("DrawRoute", "88++88 "+String.format("%d %d %d", hour, minute, second));
////        String hour_string = hour>0?String.format("%d小时", hour):"";
////        String minute_string = minute>0?String.format("%分钟", minute):"";
////        String second_string = second>0?String.format("%d秒", second):"";
////        Log.d("DrawRoute", "88++88 999 "+hour_string+" "+minute_string+" "+second_string);
//        Log.d("DrawRoute", "88++88 finish format_time");
////        return hour_string+minute_string+second_string;
//        return String.format("%d小时%分钟%d秒", hour, minute, second);
//    }

    private LatLng convertLatLonPoint(LatLonPoint lp){
        return new LatLng(lp.getLatitude(), lp.getLongitude());
    }
}
