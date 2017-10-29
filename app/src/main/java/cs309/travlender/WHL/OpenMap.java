package cs309.travlender.WHL;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import cs309.travelender.R;


public class OpenMap extends AppCompatActivity{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "open map", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openmap);
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        //
        //显示地图
        //
        mMapView = (MapView) findViewById(R.id.openmap);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.setGps(true);
                markerOptions.anchor(0.5f, 0.5f);
                markerOptions.position(new LatLng(0, 0));
                markerOptions.snippet("最快1分钟到达").draggable(true).setGps(true);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.small_location)));
                Marker mPositionMark = aMap.addMarker(markerOptions);
                mPositionMark.showInfoWindow();//主动显示indowindow
                mPositionMark.setPositionByPixels(mMapView.getWidth() / 2,mMapView.getHeight() / 2);
                AMapLocationClientOption option=new AMapLocationClientOption();
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                option.setOnceLocation(true);
                mlocationClient.setLocationOption(option);
                mlocationClient.startLocation();
            }
        });
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
}
