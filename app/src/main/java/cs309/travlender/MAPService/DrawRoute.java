package cs309.travlender.MAPService;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
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

public class DrawRoute extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {

    private RouteSearch routeSearch;
    private AMap aMap;
    private MapView mMapView;
    private LocationSource.OnLocationChangedListener mListener;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;

    private String current_city="深圳";
    private String transportation="drive";
    private double myLat;
    private double myLongt;
    private double aim_Lat = 22.549585;
    private double aim_Longt = 114.237211;

    private Button button;
    private String title = "";

    private boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_draw_route);
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        mMapView = (MapView) findViewById(R.id.draw_route);
        Log.d("DrawRoute", "88++88  mMapView "+(mMapView==null));
        Log.d("DrawRoute", "88++88  savedInstanceState "+(savedInstanceState==null));
        try{
            mMapView.onCreate(savedInstanceState);// 此方法必须重写
        }catch (Exception e){}
        aMap = mMapView.getMap();
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
            aim_Lat = bundle.getDouble("Latitude");
            //传入目的地经度
            aim_Longt = bundle.getDouble("Longitude");
            if(bundle.getString("Transportation")!=null){
                transportation = bundle.getString("Transportation");
            }
            if(bundle.getString("Title")!=null){
                title = bundle.getString("Title");
            }else{ }

            path_plan(new LatLonPoint(myLat,myLongt), new LatLonPoint(aim_Lat, aim_Longt), transportation, current_city);

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
                                    Log.d("DrawRoute","88++88 定位成功");
                                    if(state){
                                        Log.d("","88++88 "+myLat+" "+myLongt);
                                        path_plan(new LatLonPoint(myLat,myLongt), new LatLonPoint(aim_Lat, aim_Longt), transportation, current_city);
//                                        state = false;
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

//            List<LatLng> latLngs = new ArrayList<LatLng>();
////            LatLonPoint northeast = dp.getSteps().get(0).getPolyline().get(0);
////            LatLonPoint southwest = dp.getSteps().get(0).getPolyline().get(0);
//            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
//            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);
//            for(DriveStep driveStep : dp.getSteps()){
//                for(LatLonPoint lp : driveStep.getPolyline()){
//                    latLngs.add(convertLatLonPoint(lp));
//                    if(lp.getLatitude() > northeast.getLatitude()){
//                        northeast.setLatitude(lp.getLatitude());
//                    }if(lp.getLongitude() > northeast.getLongitude()){
//                        northeast.setLongitude(lp.getLongitude());
//                    }
//                    if(lp.getLatitude() < southwest.getLatitude()){
//                        southwest.setLatitude(lp.getLatitude());
//                    }if(lp.getLongitude() < southwest.getLongitude()){
//                        southwest.setLongitude(lp.getLongitude());
//                    }
//                }
//            }
//            Log.d("DrawRoute", "88++88 list"+latLngs.size());
//            Polyline polyline =aMap.addPolyline(new PolylineOptions().
//                    addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
//            if(aMap==null){
//                aMap = mMapView.getMap();
//            }
//            try {
//                aMap.moveCamera(new CameraUpdateFactory().newLatLngBounds(new LatLngBounds(convertLatLonPoint(southwest), convertLatLonPoint(northeast)), 14));
//                state = false;
//            }catch (AMapException e){
//                Log.d("DrawRoute", "88++88 AMapException");
//            }
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        Log.d("DrawRoute", "88++88 onDriveRouteSearched");
        Log.d("DrawRoute", "88++88 onDriveRouteSearched "+i+(driveRouteResult == null?"null":"not null"));
        if (i == 1000 && driveRouteResult != null) {
            Log.d("DrawRoute", "88++88 flag");
            List<DrivePath> drivePaths = driveRouteResult.getPaths();
            Log.d("DrawRoute", "88++88 drivePaths "+drivePaths.size());
            float drive_time = 0;
            DrivePath dp = drivePaths.get(0);
            List<DriveStep> driveSteps = dp.getSteps();
            Log.d("DrawRoute", "88++88 driveSteps"+driveSteps.size());
            for (DriveStep ds : driveSteps) {
                drive_time += ds.getDuration();
//                Log.d("DrawRoute", "88++88 driveSteps"+driveSteps.size());
            }
            setButtonText(title+"\n驾车用时："+format_time(drive_time));
            Log.d("DrawRoute", "88++88 time finish");

            List<LatLng> latLngs = new ArrayList<LatLng>();
//            LatLonPoint northeast = dp.getSteps().get(0).getPolyline().get(0);
//            LatLonPoint southwest = dp.getSteps().get(0).getPolyline().get(0);
            LatLonPoint northeast = new LatLonPoint(myLat, myLongt);
            LatLonPoint southwest = new LatLonPoint(myLat, myLongt);
            for(DriveStep driveStep : dp.getSteps()){
                for(LatLonPoint lp : driveStep.getPolyline()){
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
            Log.d("DrawRoute", "88++88 list"+latLngs.size());
            Polyline polyline =aMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
            if(aMap==null){
                aMap = mMapView.getMap();
            }
            try {
                aMap.moveCamera(new CameraUpdateFactory().newLatLngBounds(new LatLngBounds(convertLatLonPoint(southwest), convertLatLonPoint(northeast)), 14));
                state = false;
            }catch (AMapException e){
                Log.d("DrawRoute", "88++88 AMapException");
            }
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
        }
    }

    private String format_time(double time){
        int second = (int)time;
        int hour = second/3600;
        int minute = (second%3600)/60;
        return String.format("%d小时%d分钟%d秒", hour, minute, second%60);
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
