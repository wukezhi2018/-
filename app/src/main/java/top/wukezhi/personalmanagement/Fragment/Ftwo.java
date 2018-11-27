package top.wukezhi.personalmanagement.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.Gson_joke.Joke_json;
import top.wukezhi.personalmanagement.Gson_joke.Joke_show;
import top.wukezhi.personalmanagement.R;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.JokeAdapter;
import top.wukezhi.personalmanagement.util.Utility;

public class Ftwo extends Fragment {
    private List<Joke_show> joke_show_list=new ArrayList<>();//展示类数组
    private JokeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Joke_show[] shows={new Joke_show(R.drawable.j1,"一个好笑的东西")
            ,new Joke_show(R.drawable.j2,"一个好笑的东西")
            ,new Joke_show(R.drawable.j3,"一个好笑的东西")
            ,new Joke_show(R.drawable.j4,"一个好笑的东西")
            ,new Joke_show(R.drawable.j5,"一个好笑的东西")
            ,new Joke_show(R.drawable.j6,"一个好笑的东西")
            ,new Joke_show(R.drawable.j7,"一个好笑的东西")
            ,new Joke_show(R.drawable.j8,"一个好笑的东西")
            ,new Joke_show(R.drawable.j9,"一个好笑的东西")
            ,new Joke_show(R.drawable.j10,"一个好笑的东西")
    };
    private CoordinatorLayout background;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ftwo, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        background=(CoordinatorLayout)getActivity().findViewById(R.id.ftwo_background);//背景
        initShows();//随机初始化
        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.ftwo_view);
        //采用2列网格布局
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        //匹配适配器
        adapter =new JokeAdapter(joke_show_list);//传入类型数组列表
        recyclerView.setAdapter(adapter);//设置适配器

        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.ftwo_swipe);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary
                ,R.color.bottomtwo
                ,R.color.bottomone);//设置下拉刷新进度条的颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshJokeShow();//处理刷新
            }
        });
    }
    public void changeBG(int bgId){
        background.setBackgroundResource(bgId);
    }
    private void initShows(){
        joke_show_list.clear();//清空类型数组
        for (int i=0;i<50;i++){
            Random random=new Random();
            int index=random.nextInt(shows.length);
            joke_show_list.add(shows[index]);//随机放入
        }
    }
    //刷新处理随机
    private void refreshJokeShow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);//线程沉睡1s
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {//沉睡结束回到主线程
                    @Override
                    public void run() {
                        initShows();//重新加载
                        adapter.notifyDataSetChanged();//刷新
                        swipeRefreshLayout.setRefreshing(false);//刷新结束隐藏进度条
                    }
                });
            }
        }).start();
    }

}
