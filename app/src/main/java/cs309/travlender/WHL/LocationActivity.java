package cs309.travlender.WHL;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
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

public class LocationActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener{

    private List<Point_of_Interest> poi_List = new ArrayList<Point_of_Interest>();
    private Button search_button;
    private EditText input;
    private ListView listView;
    private Query query;
    private PoiSearch search;
    private GeocodeSearch geocoderSearch;
    private String address;
    private List<String> addresses = new ArrayList<String>();
    private Button location_info_button;

    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private MapView mMapView;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
    private Marker locationMarker;
    private RouteSearch routeSearch;

    private double myLat;
    private double myLongt;
    private double aim_Lat;
    private double aim_Longt;

    private String current_city="深圳";
    private String transportation="drive";
    private String location_title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location_whl);
        initView();//初始化三个UI组件：search按钮、EditText和ListView
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        setUpLocation(savedInstanceState);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    //初始化三个UI组件：search按钮、EditText和ListView
    private void initView(){

        //初始化地图下方的悬浮按钮
        location_info_button = (Button) findViewById(R.id.location_info_button);
        location_info_button.setOnTouchListener(new View.OnTouchListener() {
            private int times = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
//                    Log.i("log", "88++88 action_down");
                    return false;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
//                    Log.i("log", "88++88 action_up");
                    return false;
                }
                else if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
//                    Log.i("log", "88++88 action_move");
                    times++;
                    if (times>5){
//                        Log.i("log", "88++88 long");
                        times = 0;
                    }
                    return false;
                }
                return false;
            }
        });
        location_info_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                单击就算是选中
                Log.d("MainActivity","88++88 单击就算是选中");
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                //传入目的地纬度
                bundle.putDouble("to_Latitude", aim_Lat);
                //传入目的地经度
                bundle.putDouble("to_Longitude", aim_Longt);
                //传入目的地名称
                bundle.putString("location_name", location_title);
                //intent传递bundle
                intent.putExtras(bundle);
                setResult(2, intent);
                finish();
            }
        });
        location_info_button.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
//                长按显示路线图
                Log.d("MainActivity","88++88 长按显示路线图");
                aMap.clear();
                path_plan(new LatLonPoint(myLat,myLongt), new LatLonPoint(aim_Lat, aim_Longt), transportation, current_city);
                return true;
            }
        });

        //初始化search按钮，加监听器
        search_button = (Button) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                if (!poi_List.isEmpty()){
                    showLocationInMap(poi_List.get(0).getLatitude(), poi_List.get(0).getLongitude(), poi_List.get(0).getPoiItem().getTitle(), poi_List.get(0).getPoiItem().getSnippet());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0) ;
                    aim_Lat = poi_List.get(0).getLatitude();
                    aim_Longt = poi_List.get(0).getLongitude();
                    location_title = poi_List.get(0).getPoiItem().getTitle();
                }
            }
        });
        //初始化EditText，加监听器
        TextWatcher textChange = new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("MainActivity", "88++88 Text change");
                search(s.toString());
                listView.setVisibility(View.VISIBLE);
                location_info_button.setVisibility(View.INVISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
            }
        };
        input = (EditText)findViewById(R.id.input_destination);
        input.addTextChangedListener(textChange);//给edittext加监听器
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    // do some your things
                    if (!poi_List.isEmpty()){
                        showLocationInMap(poi_List.get(0).getLatitude(), poi_List.get(0).getLongitude(), poi_List.get(0).getPoiItem().getTitle(), poi_List.get(0).getPoiItem().getSnippet());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0) ;
                        aim_Lat = poi_List.get(0).getLatitude();
                        aim_Longt = poi_List.get(0).getLongitude();
                        location_title = poi_List.get(0).getPoiItem().getTitle();
                    }
                }
                return false;
            }
        });
        ////ListView，加监听器
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Point_of_Interest poi = poi_List.get(position);
                showLocationInMap(poi.getLatitude(), poi.getLongitude(), poi.getPoiItem().getTitle(), poi.getPoiItem().getSnippet());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0) ;
                aim_Lat = poi.getLatitude();
                aim_Longt = poi.getLongitude();
                location_title = poi.getPoiItem().getTitle();
            }
        });
    }

    //初始化POI的列表
    private void addPOI(String name, String subname, int distance, PoiItem item){
        Point_of_Interest poi;
        poi = new Point_of_Interest(name, R.drawable.search_result, subname, distance, item);
        poi_List.add(poi);
    }

    //搜索POI，传入搜索关键字即可
    private void search(String keyword) {
        // 初始化查询条件
        query = new Query(keyword, null, current_city);
        query.setPageSize(10);
        query.setPageNum(1);
        query.setLocation(new LatLonPoint(myLat, myLongt));
        //Toast.makeText(getApplicationContext(), String.valueOf(query.getLocation()), Toast.LENGTH_SHORT).show();
        query.setDistanceSort(true);

        // 查询兴趣点
        search = new PoiSearch(this, query);
        //设置查询范围和中心点
        search.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLat, myLongt), 199999));
        // 设置回调监听
        search.setOnPoiSearchListener(new OnPoiSearchListener(){
            @Override
            public void onPoiItemSearched(PoiItem arg0, int arg1) {}
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                ArrayList<PoiItem> items = poiResult.getPois();
                poi_List.clear();
                addresses.clear();
                if (items != null && items.size() > 0) {
                    for (PoiItem item : items) {
                        addPOI(item.getTitle(), item.getSnippet(), item.getDistance(), item);
                    }
                    // 给ListView赋值，显示结果
                    Point_of_Interest_Adapter poia = new Point_of_Interest_Adapter(LocationActivity.this, R.layout.point_of_interest_layout, poi_List);
                    listView.setAdapter(poia);
                }
            }
        });
        // 异步搜索
        search.searchPOIAsyn();
        Log.d("MainActivity", "search");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                }else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void setUpLocation(Bundle savedInstanceState){
        //
        //显示地图
        //
        mMapView = (MapView) findViewById(R.id.locating);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        aMap.setLocationSource(new LocationSource(){
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
                    mlocationClient.setLocationListener(new AMapLocationListener(){
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

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.small_location)))
                    .position(new LatLng(myLat, myLongt)));
            locationMarker.showInfoWindow();
        }
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLongt), 14));
    }

    private void showLocationInMap(double Lat, double Longt, String title, String snippet){
        location_info_button.setVisibility(View.VISIBLE); // 设置按钮可见
        location_info_button.setText(title+'\n'+snippet); // 设置按钮文字

        listView.setVisibility(View.INVISIBLE);

        mMapView.setVisibility(View.VISIBLE); // 设置地图可见
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Longt), 14)); // 调整取景
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        aMap.clear(); // 删除地图上原有标记
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 隐藏原有定位按钮
        aMap.getUiSettings().setZoomControlsEnabled(false); // 隐藏默认缩放按钮

//        对目的地添加标记添加
        LatLng latLng = new LatLng(Lat,Longt);
        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.small_location)))
                .position(new LatLng(Lat, Longt)).	draggable(false);
        locationMarker = aMap.addMarker(markerOption);
    }

    private void path_plan(LatLonPoint from, LatLonPoint to, String transportation, String city) {
        if(routeSearch==null){
            routeSearch = new RouteSearch(getApplicationContext());
        }
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fat = new RouteSearch.FromAndTo(from, to);
        Log.d("MainActivity", "88++88 path_plan");
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
            location_info_button.setText(location_title+"\n公交距离："+format_distance(bus_distance_min)+"\n公交用时："+format_time(bus_time_min));

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
            Log.d("DrawRoute", "88++88 onDriveRouteSearched " + i + " " + ((driveRouteResult == null)?"null":" not null"));
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
            location_info_button.setText(location_title+"\n驾车距离："+format_distance(drive_distance)+"\n驾车用时："+format_time(drive_time));

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

            location_info_button.setText(location_title+"\n步行距离："+format_distance(walk_distance)+"\n步行用时："+format_time(walk_time));

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

            location_info_button.setText(location_title+"\n骑行距离："+format_distance(ride_distance)+"\n骑行用时："+format_time(ride_time));

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
        if(aMap==null){
            aMap = mMapView.getMap();
        }
//        画路线
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));

        try {
//            调整画面位置
            aMap.moveCamera(new CameraUpdateFactory().newLatLngBounds(new LatLngBounds(convertLatLonPoint(southwest), convertLatLonPoint(northeast)), 25));
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
//            locationMarker_2.showInfoWindow();

        }catch (AMapException e){
            Log.d("DrawRoute", "88++88 AMapException");
        }
    }

    private LatLng convertLatLonPoint(LatLonPoint lp){
        return new LatLng(lp.getLatitude(), lp.getLongitude());
    }

}
