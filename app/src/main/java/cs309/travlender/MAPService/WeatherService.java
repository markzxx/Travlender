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
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.util.HashSet;
import java.util.Set;

public class WeatherService extends Service {

    private GeocodeSearch geocoderSearch;
    private String address;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    // 天气查询
    private WeatherSearch mweathersearch;
    private WeatherSearchQuery mquery;

    private double myLat;
    private double myLongt;
    private double dst_Lat;
    private double dst_Longt;

    private String current_city="深圳";
    private String travel_mode="drive";
    static public Set<Integer> querySet = new HashSet<>();
    private int startid;

    // TODO: Rename actions, choose action names that describe tasks that this
    // Action的名字
    private static final String ACTION_WEATHER_DESTINATION = "com.example.dell.map_3.action.WEATHER_DESTINATION";
    private static final String ACTION_WEATHER_LOCATION = "com.example.dell.map_3.action.WEATHER_LOCATION";

    // TODO: parameters for ACTION_TRAVEL_TIME
    public static final String EXTRA_PARAM_LATITUDE = "com.example.dell.map_3.extra.LATITUDE";
    public static final String EXTRA_PARAM_LONGITUDE = "com.example.dell.map_3.extra.LONGITUDE";
    public static final String EXTRA_PARAM_QUERY_ID = "com.example.dell.map_3.extra.QUERY_ID";

    // 查询目的地天气用的函数
    public static void startServiceWeatherWithDestination(Context context, double latitude, double longitude, int id) {
//        Toast.makeText(context,"startActionTravelTime", Toast.LENGTH_SHORT).show();
//        Log.e("MyService", "startActionTravelTime");
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_WEATHER_DESTINATION);
        intent.putExtra(EXTRA_PARAM_LATITUDE, String.valueOf(latitude));
        intent.putExtra(EXTRA_PARAM_LONGITUDE, String.valueOf(longitude));
        intent.putExtra(EXTRA_PARAM_QUERY_ID, String.valueOf(id));
        context.startService(intent);
    }

    // 查询目的地天气用的函数
    public static void startServiceWeatherLocally(Context context, int id) {
//        Toast.makeText(context,"startActionTravelTime", Toast.LENGTH_SHORT).show();
        Log.e("MyService", "startActionTravelTime");
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_WEATHER_LOCATION);
        intent.putExtra(EXTRA_PARAM_QUERY_ID, String.valueOf(id));
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
//        Log.d("MyService", "onStartCommand");
        this.startid = startId;
        if (intent.getAction() == ACTION_WEATHER_DESTINATION) {
            final double latitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LATITUDE));
            final double longitude = Double.parseDouble(intent.getStringExtra(EXTRA_PARAM_LONGITUDE));
            final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_QUERY_ID));
            handleActionWeatherWithDestination(latitude, longitude, id);
//            return super.onStartCommand(intent, flags, startId);
        }
        else if (intent.getAction() == ACTION_WEATHER_LOCATION){
            final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_QUERY_ID));
            handleActionWeatherLocally(id);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleActionWeatherLocally(int id) {
        // TODO: Handle action
        // 具体执行一个任务
        Log.d("WeatherService", "handleActionWeatherLocally");
        location(id);
    }

    private void handleActionWeatherWithDestination(double to_Latitude, double to_Longitude, int id) {
        // TODO: Handle action
        // 具体执行一个任务
        Log.d("WeatherService", "handleActionWeatherWithDestination");
        dst_Lat = to_Latitude;
        dst_Longt = to_Longitude;
        convertPOIAddress(new LatLonPoint(dst_Lat, dst_Longt), id);
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
                        if(!querySet.contains(_id))
                            sendWeatherQueryAsyn(current_city, _id);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("MyService", "onDestroy executed");
    }

        //（ 经纬度 --> 地址 ）
    private void convertPOIAddress(LatLonPoint latLonPoint, int id){
        final int _id = id;
        if (latLonPoint == null) {
            address = "";
            return;
        }
        //地理编码搜索类
        geocoderSearch = new GeocodeSearch(this);
        //设置异步回调监听
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener(){
            //（ 地址 --> 经纬度 ）根据给定的地理名称和查询城市，返回地理编码的结果列表
            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int resultID){}
            //（ 经纬度 --> 地址 ）根据给定的经纬度和最大结果数返回逆地理编码的结果列表
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultID){
//                address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                Log.d("WeatherService", "检索地址："+regeocodeResult.getRegeocodeAddress().getFormatAddress());
                String city = regeocodeResult.getRegeocodeAddress().getCity();
                sendWeatherQueryAsyn(city, _id);
            }
        });
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


    private void sendWeatherQueryAsyn(String city, int id){
        final int _id = id;
        mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener(){
            /**
             * 实时天气查询回调
             */
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult , int rCode) {
                if (rCode == 1000) {
                    if (weatherLiveResult != null&&weatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                        Log.d("WeatherService", "发布时间："+weatherlive.getReportTime());
                        Log.d("WeatherService", "城市："+weatherlive.getCity());
                        Log.d("WeatherService", "天气："+weatherlive.getWeather());
                        Log.d("WeatherService", "温度："+weatherlive.getTemperature());
                        Log.d("WeatherService", "湿度："+weatherlive.getHumidity());
                        Log.d("WeatherService", "风力："+weatherlive.getWindPower());
                        sendBroadcast(weatherlive, _id);
                    }else {
                        Log.d("WeatherService", "查询结果为空值");
                    }
                }else {
                    Log.d("WeatherService", "查询失败");
                }
            }
            /**
             * 预报天气查询回调
             */
            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode){

            }
        });
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    private void sendBroadcast(LocalWeatherLive weatherlive, int id){
        Intent intent = new Intent(WeatherServiceReceiver.BROADCAST_SIGNAL);
        intent.putExtra(WeatherServiceReceiver.getCity, weatherlive.getCity());
        intent.putExtra(WeatherServiceReceiver.getHumidity, weatherlive.getHumidity());
        intent.putExtra(WeatherServiceReceiver.getReportTime, weatherlive.getReportTime());
        intent.putExtra(WeatherServiceReceiver.getTemperature, weatherlive.getTemperature());
        intent.putExtra(WeatherServiceReceiver.getWeather, weatherlive.getWeather());
        intent.putExtra(WeatherServiceReceiver.getWindPower, weatherlive.getWindPower());
        intent.putExtra("id", String.valueOf(id));
        sendBroadcast(intent);
    }
}
