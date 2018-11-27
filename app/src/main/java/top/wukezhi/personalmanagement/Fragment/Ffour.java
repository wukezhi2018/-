package top.wukezhi.personalmanagement.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.ChooseAreaActivity;
import top.wukezhi.personalmanagement.Gson_weather.Forecast;
import top.wukezhi.personalmanagement.Gson_weather.Weather;
import top.wukezhi.personalmanagement.R;
import top.wukezhi.personalmanagement.WelcomeActivity;
import top.wukezhi.personalmanagement.server.AutoUpdateService;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.Utility;

public class Ffour extends Fragment {
    public SwipeRefreshLayout swipeRefresh;
    private ScrollView weatherLayout;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pmText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private String mWeatherId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ffour, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bingPicImg = (ImageView)getActivity().findViewById(R.id.four_bing_pic_img);
        weatherLayout = (ScrollView)getActivity().findViewById(R.id.four_weather_layout);
        degreeText = (TextView) getActivity().findViewById(R.id.degree_text);
        weatherInfoText = (TextView) getActivity().findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)getActivity(). findViewById(R.id.forecast_layout);
        aqiText = (TextView)getActivity(). findViewById(R.id.aqi_text);
        pmText = (TextView)getActivity(). findViewById(R.id.pm_text);
        comfortText = (TextView)getActivity().findViewById(R.id.comfort_text);
        carWashText = (TextView) getActivity().findViewById(R.id.car_wash_text);
        sportText = (TextView) getActivity().findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.four_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent,
                R.color.bottomfive);//设置下刷颜色变化

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //获取shared对象，应用程序包名为前缀
        String weatherString = prefs.getString("weather", null);//查找weather列的值，找不到为空
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = "CN101270107";
            weatherLayout.setVisibility(View.INVISIBLE);//隐藏天气页面
            requestWeather(mWeatherId);
        }
        //下拉监听事件
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        loadBingPic();
    }
    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=825b1b676d674d6587726731857552d1";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {//申请好的apikey拼装出接口地址，发送请求
            @Override
            public void onResponse(Call call, Response response) throws IOException {//数据回调
                final String responseText = response.body().string();//返回出JSON数据
                final Weather weather = Utility.handleWeatherResponse(responseText);//解析出weather对象
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {//请求天气成功
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("weather", responseText);
                            //数据库写操作写入weather列
                            editor.apply();//提交
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);//下拉结束并隐藏进度条
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);//下拉结束并隐藏进度条
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        TextView forecast_city=(TextView)getActivity().findViewById(R.id.forecast_city);
        forecast_city.setText(cityName+"预报");
        /*String updateTime = weather.basic.update.updateTime.split(" ")[1];*/
        String degree = weather.now.temperatrue + "℃";
        String weatherInfo = weather.now.more.info;
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperatrue.max);
            minText.setText(forecast.temperatrue.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pmText.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);
    }
}
