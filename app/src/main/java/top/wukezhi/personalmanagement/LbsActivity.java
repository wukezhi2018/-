package top.wukezhi.personalmanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.Person;
import top.wukezhi.personalmanagement.util.key;

public class LbsActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private boolean isFirstLocate=true;
    private TextView positionText;
    private Button lbs_back;
    private ImageView lbs_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            //系统版本号判断只有5.0及以上系统才会进入
            View decorView = getWindow().getDecorView();//拿到当前活动的DecorView
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //setSystemUiVisibility()方法改变系统的UI显示，参数表示活动的布局会显示在状态栏上面，使得背景图与状态栏融合在一起
            //但是背景图和状态栏会紧贴借助fitsSystemWindows="true"解决在weather布局中的线性布局加入
            getWindow().setStatusBarColor(Color.TRANSPARENT);//状态栏设置为透明
        }
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        //初始化sdk，用getApplicationContext（）获取全局context参数，必须在setcontentview之前
        setContentView(R.layout.activity_lbs);
        positionText=(TextView)findViewById(R.id.position_text_view);
        lbs_back=(Button)findViewById(R.id.lbs_button);//返回上个活动
        lbs_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lbs_image=(ImageView)findViewById(R.id.lbs_image);
        loadBingPic();//背景图
        //集中进行权限申请
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);//list转化为数组
            ActivityCompat.requestPermissions(this,permissions,1);
        }else{
            requestLocation();//已经全部同意权限执行定位
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    private void requestLocation(){
        initLocation();
        mLocationClient.start();//定位结果回调前面注册的监听器中，启动定位sdk
    }
    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);//5秒更新一次位置
        option.setOpenGps(true);//打开gps
        // 将定位模式指定成传感器模式，即只能gps定位
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);//是否需要地址信息
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition=new StringBuilder();
                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
                    currentPosition.append("经线：").append(location.getLongitude()).append("\n");
                    currentPosition.append("国家：").append(location.getCountry()).append("\n");
                    currentPosition.append("省：").append(location.getProvince()).append("\n");
                    currentPosition.append("市：").append(location.getCity()).append("\n");
                    currentPosition.append("区：").append(location.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(location.getStreet()).append("\n");
                    currentPosition.append("定位方式、：");
                    if(location.getLocType()==BDLocation.TypeGpsLocation){
                        currentPosition.append("GPS");
                    }else if(location.getLocType()==BDLocation.TypeNetWorkLocation){
                        currentPosition.append("网络");
                    }
                    positionText.setText(currentPosition);//显示地址
                    setAddress(currentPosition);
                }
            });
        }
        public void onConnectHotSpotMessage(String s,int i){
        }
    }
    public void setAddress(StringBuilder address){
        Person p2 = new Person();
        p2.setName(getIntent().getStringExtra("username"));
        p2.setAddress(address.toString());
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {

            }
        });
    }
    /**
     * 加载必应每日一图
     */
    public void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LbsActivity.this).load(bingPic).into(lbs_image);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
    //权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for (int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意权限",Toast.LENGTH_SHORT).show();
                            finish();//所有权限必须同意否则关闭
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
