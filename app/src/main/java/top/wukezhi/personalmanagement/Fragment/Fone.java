package top.wukezhi.personalmanagement.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.Gson_calendar.FoneCalendar;
import top.wukezhi.personalmanagement.Gson_news.News;
import top.wukezhi.personalmanagement.R;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.Utility;

public class Fone extends Fragment {
    private TextView showCalendar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fone, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showCalendar=(TextView)getActivity().findViewById(R.id.fone_calendar);
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        time=year+"-"+month+"-"+day;
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.fone_swipe);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary
                ,R.color.bottomtwo
                ,R.color.bottomone);//设置下拉刷新进度条的颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCalenar(time);
            }
        });
        requestCalenar(time);
    }
    public void requestCalenar(String time) {
        String calendarUrl ="http://v.juhe.cn/calendar/day?date="+time+"&key=da79568c0c215857245e1e3b0533aee4";
        HttpUtil.sendOkHttpRequest(calendarUrl, new Callback() {//申请好的apikey拼装出接口地址，发送请求
            @Override
            public void onResponse(Call call, Response response) throws IOException {//数据回调
                final String responseText = response.body().string();//返回出JSON数据
                final FoneCalendar.ResultBean.DataBean calendar=
                        Utility.handleCalendarResponse(responseText);//解析出calendar对象
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (calendar != null) {//请求成功
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences
                                            (getActivity()).edit();
                            editor.putString("calendar", responseText);
                            //数据库写操作写入calendar列
                            editor.apply();//提交
                            showCalendar(calendar);
                        } else {
                            Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
    public void showCalendar(FoneCalendar.ResultBean.DataBean calendar){
        String show=("当前日期："+"\n"+calendar.getDate()+"\n"
                +calendar.getWeekday()+"\n"
                +calendar.getAnimalsYear()+"\n"
                +calendar.getLunar()+"\n"
                +calendar.getLunarYear()+"\n\n"
                +"宜："+"\n"+calendar.getSuit()+"\n\n"
                +"忌："+"\n"+calendar.getAvoid()+"\n"
        );
        showCalendar.setText(show);
    }
}