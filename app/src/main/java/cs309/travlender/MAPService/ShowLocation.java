package cs309.travlender.MAPService;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import cs309.travelender.R;

public class ShowLocation extends AppCompatActivity {

    private AMap aMap;
    private MapView mMapView;
    private Marker locationMarker;
    private double Lat;
    private double Longt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_location);
        mMapView = (MapView) findViewById(R.id.show_location);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mMapView.getMap();

        Button button = (Button) findViewById(R.id.show_location_button);
        button.setText("返回");
//        获取参数
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            //传入目的地纬度
            Lat = bundle.getDouble("to_Latitude");
            //传入目的地经度
            Longt = bundle.getDouble("to_Longitude");
            String title = bundle.getString("location_name");
            String snippet = bundle.getString("Snippet");
            button.setText(title+'\n'+snippet);
        }

//        调整取景
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Longt), 14));
        LatLng latLng = new LatLng(Lat,Longt);
        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.small_location)))
                .position(new LatLng(Lat, Longt)).	draggable(false).snippet("110").title("666");
        locationMarker = aMap.addMarker(markerOption);



//        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                相当于用户确认这个地点，然后返回配置日程的界面
//                这里可以用intent跳转其他页面

            }
        });
    }
}
