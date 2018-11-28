package top.wukezhi.personalmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.b.V;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.Gson_joke.Joke_json;
import top.wukezhi.personalmanagement.Gson_news.News;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.Utility;

public class NewsActivity extends AppCompatActivity{
    public static final String CONTENT = "CONTENT";
    private News.ResultBean.DataBean news;
    private TextView content_title;
    private TextView content;
    private ImageView imageView1;
    private ImageView imageView2;
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
        setContentView(R.layout.activity_news);
        news=(News.ResultBean.DataBean)getIntent().getSerializableExtra("CONTENT");
        //获得各种控件实例
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.news_toorbar);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.news_coll_toorbar);
        ImageView imageView = (ImageView) findViewById(R.id.news_view);
        content = (TextView) findViewById(R.id.news_content_text);
        content_title=(TextView)findViewById(R.id.news_content_title);
        imageView1=(ImageView)findViewById(R.id.news_img1);
        imageView2=(ImageView)findViewById(R.id.news_img2);


        setSupportActionBar(toolbar);//toolbar标准用法
        //启用导航按钮，默认是个返回箭头，id为home
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(news.getTitle());//设置折叠标题栏标题
        //加载图片到标题栏中
        try {
           URL url = new URL(news.getThumbnail_pic_s());
           Glide.with(this).load(url).into(imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //调用方法设置内容显示
        getDataByJsoup();
    }
    private void getDataByJsoup(){

        // 开启一个新线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 网络加载HTML文档
                    Document doc = Jsoup.connect(news.getUrl())
                            .get(); // 使用GET方法访问URL
                    Elements elements=doc.select("div.article-title");
                    Elements elements1=doc.select("div.article-src-time");
                    Elements elements2=doc.select("div#content");
                    showText(elements.text(),elements1.text(),elements2.text());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void showText(final String header,final String origin,final String con){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content_title.setText(header);
                content.setText(con);
                try{

                    if(news.getThumbnail_pic_s02()!=null){
                        imageView1.setVisibility(View.VISIBLE);
                        URL url1=new URL(news.getThumbnail_pic_s02());
                        Glide.with(NewsActivity.this).load(url1).into(imageView1);
                    }
                    if(news.getThumbnail_pic_s03()!=null){
                        imageView2.setVisibility(View.VISIBLE);
                        URL url2=new URL(news.getThumbnail_pic_s03());
                        Glide.with(NewsActivity.this).load(url2).into(imageView2);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;//销毁活动，即返回上个活动
        }
        return super.onOptionsItemSelected(item);
    }
}
