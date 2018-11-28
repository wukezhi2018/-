package top.wukezhi.personalmanagement.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import top.wukezhi.personalmanagement.Gson_calendar.FoneCalendar;
import top.wukezhi.personalmanagement.Gson_joke.Joke_json;
import top.wukezhi.personalmanagement.Gson_news.News;
import top.wukezhi.personalmanagement.Gson_weather.Weather;
import top.wukezhi.personalmanagement.db.City;
import top.wukezhi.personalmanagement.db.County;
import top.wukezhi.personalmanagement.db.Province;

//由于服务器返回的省市县数据都是json格式所以创建一个类来解析处理这种数据
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvince=new JSONArray(response);
                for (int i=0;i<allProvince.length();i++){
                    JSONObject provinceObject=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }return false;
    }
    /**
     * 解析处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countiesObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countiesObject.getString("name"));
                    county.setWeatherId(countiesObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch(JSONException e){
                e.printStackTrace();
            }
        }return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类,fromJson()方法将JSON数据转化成weather对象
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //解析出joke类数组
    public static List<Joke_json.ResultBean> handleJokeResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            String joke=jsonArray.toString();
            return new Gson().fromJson(joke,new TypeToken<List<Joke_json.ResultBean>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //解析出news类数组
    public static List<News.ResultBean.DataBean> handleNewsResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject jsonObject1=jsonObject.getJSONObject("result");
            String news=jsonObject1.getJSONArray("data").toString();
            return new Gson().fromJson(news,new TypeToken<List<News.ResultBean.DataBean>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //解析日历
    public static FoneCalendar.ResultBean.DataBean handleCalendarResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONObject jsonObject1=jsonObject.getJSONObject("result");
            String calendar=jsonObject1.getJSONObject("data").toString();
            return new Gson().fromJson(calendar, FoneCalendar.ResultBean.DataBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
