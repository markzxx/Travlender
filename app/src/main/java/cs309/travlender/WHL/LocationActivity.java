package cs309.travlender.WHL;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs309.travelender.R;

public class LocationActivity extends AppCompatActivity{

    private List<Point_of_Interest> poi_List = new ArrayList<Point_of_Interest>();
    private Button search_button;
    private EditText input;
    private ListView listView;
    private Query query;
    private PoiSearch search;
    private GeocodeSearch geocoderSearch;
    private String address;
    private List<String> addresses = new ArrayList<String>();

    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private MapView mMapView;
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
    private Marker locationMarker;
    private double myLat;
    private double myLongt;

    private String current_city="深圳";
    private String transportation="drive";

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
        //初始化search按钮，加监听器
        search_button = (Button) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                if (!poi_List.isEmpty()){
                    Bundle bundle=new Bundle();
                    Intent intent = new Intent();
                    //传入目的地纬度
                    bundle.putDouble("to_Latitude", poi_List.get(0).getLatitude());
                    //传入目的地经度
                    bundle.putDouble("to_Longitude", poi_List.get(0).getLongitude());
                    //传入目的地经度
                    bundle.putString("location_name", poi_List.get(0).getName());
                    //intent传递bundle
                    intent.putExtras(bundle);

                    setResult(666, intent);
                    finish();
//                    TravelTimeService.startServiceTravelTime(getApplicationContext(), poi_List.get(0).getLatitude(), poi_List.get(0).getLongitude(), transportation);
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
                search(s.toString());
            }
        };
        input = (EditText)findViewById(R.id.input_destination);
        input.addTextChangedListener(textChange);//给edittext加监听器

        ////ListView，加监听器
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Point_of_Interest poi = poi_List.get(position);
                Bundle bundle=new Bundle();
                Intent intent = new Intent(LocationActivity.this, Path_planning.class);
                //传入目的地纬度
                bundle.putDouble("to_Latitude", poi.getLatitude());
                //传入目的地经度
                bundle.putDouble("to_Longitude", poi.getLongitude());
                //传入目的地经度
                bundle.putString("location_name", poi_List.get(0).getName());
                //intent传递bundle
                intent.putExtras(bundle);

                setResult(666, intent);
                finish();
//                TravelTimeService.startServiceTravelTime(getApplicationContext(), poi.getLatitude(), poi.getLongitude(), transportation);
            }
        });
    }

    //初始化POI的列表
    private void addPOI(String name, String subname, int distance, PoiItem item){
        Random random = new Random();
        Point_of_Interest poi;
        if (random.nextBoolean()==true) poi = new Point_of_Interest(name, R.drawable.red_point, subname, distance, item);
        else poi = new Point_of_Interest(name, R.drawable.blue_point, subname, distance, item);
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
                                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLongt), 14));
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

}
