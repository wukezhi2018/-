package top.wukezhi.personalmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.Gson_joke.Joke_json;
import top.wukezhi.personalmanagement.util.HttpUtil;
import top.wukezhi.personalmanagement.util.Utility;

public class JokeActivity extends AppCompatActivity {
    public static final String ID_IMAGE = "image_id";
    private  TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        Intent intent = getIntent();//通过Intent获得传入的水果名和图片id
        int jokeImageId = intent.getIntExtra(ID_IMAGE, 0);
        //获得各种控件实例
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.joke_toorbar);
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.joke_coll_toorbar);
        ImageView imageView = (ImageView) findViewById(R.id.joke_view);
        textView = (TextView) findViewById(R.id.joke_content_text);

        setSupportActionBar(toolbar);//toolbar标准用法

        //启用导航按钮，默认是个返回箭头，id为home
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("一堆笑话");//设置折叠标题栏标题
        //加载图片到标题栏中
        Glide.with(this).load(jokeImageId).into(imageView);
        //调用方法设置内容显示
        requestJoke();
    }
    public void requestJoke() {
        String JokeUrl ="http://v.juhe.cn/joke/randJoke.php?key=93b6e94c1f47b55abfb46c135baa8843";
        HttpUtil.sendOkHttpRequest(JokeUrl, new Callback() {//申请好的apikey拼装出接口地址，发送请求
            @Override
            public void onResponse(Call call, Response response) throws IOException {//数据回调
                final String responseText = response.body().string();//返回出JSON数据
                final List<Joke_json.ResultBean> jokeList = Utility.handleJokeResponse(responseText);//解析出weather对象
                JokeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jokeList != null) {//请求笑话成功
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences
                                            (JokeActivity.this).edit();
                            editor.putString("joke", responseText);
                            //数据库写操作写入joke列
                            editor.apply();//提交
                            showJoke(jokeList);//展示信息
                        } else {
                            Toast.makeText(JokeActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                JokeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JokeActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void showJoke(List<Joke_json.ResultBean> jokelist){
        StringBuilder builder=new StringBuilder();
        int n=1;
        for (Joke_json.ResultBean resultBean:jokelist){
            builder.append("\n第"+n+"个笑话：\n"+resultBean.getContent()+"\n\n");
            n++;
        }
        textView.setText(builder.toString());
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
