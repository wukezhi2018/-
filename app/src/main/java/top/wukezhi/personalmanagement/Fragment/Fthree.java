package top.wukezhi.personalmanagement.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.Gson_joke.Joke_json;
import top.wukezhi.personalmanagement.Gson_news.News;
import top.wukezhi.personalmanagement.JokeActivity;
import top.wukezhi.personalmanagement.R;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.JokeAdapter;
import top.wukezhi.personalmanagement.util.NewsAdapter;
import top.wukezhi.personalmanagement.util.Utility;

public class Fthree extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fthree, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestNews();
        recyclerView=(RecyclerView)getActivity().findViewById(R.id.fthree_view);
        //采用2列网格布局
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        //匹配适配器

        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.fthree_swipe);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary
                ,R.color.bottomtwo
                ,R.color.bottomone);//设置下拉刷新进度条的颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNews();
            }
        });
    }
    public void setAdapter(List<News.ResultBean.DataBean> newslist){
        adapter =new NewsAdapter(newslist);//传入类型数组列表
        recyclerView.setAdapter(adapter);//设置适配器
    }
    public void requestNews() {
        String NewsUrl ="http://v.juhe.cn/toutiao/index?type=top&key=5f6cad3eab281826cb4eb406528e813d";
        HttpUtil.sendOkHttpRequest(NewsUrl, new Callback() {//申请好的apikey拼装出接口地址，发送请求
            @Override
            public void onResponse(Call call, Response response) throws IOException {//数据回调
                final String responseText = response.body().string();//返回出JSON数据
                final List<News.ResultBean.DataBean> newsList=
                        Utility.handleNewsResponse(responseText);//解析出new对象
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newsList != null) {//请求成功
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences
                                            (getActivity()).edit();
                            editor.putString("news", responseText);
                            //数据库写操作写入news列
                            editor.apply();//提交
                            setAdapter(newsList);
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
}

