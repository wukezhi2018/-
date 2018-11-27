package top.wukezhi.personalmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import top.wukezhi.personalmanagement.Fragment.Ffour;
import top.wukezhi.personalmanagement.Fragment.Fone;
import top.wukezhi.personalmanagement.Fragment.Fthree;
import top.wukezhi.personalmanagement.Fragment.Ftwo;
import top.wukezhi.personalmanagement.Fragment.ViewPagerAdapter;
import top.wukezhi.personalmanagement.Fragment.ZoomOutPageTransformer;

import static top.wukezhi.personalmanagement.util.key.appkey;

public class WelcomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;//滑动页
    private String username;//用户名
    private TextView name;//滑动页用户名
    private Button title_bt;//标题导航
    private BottomNavigationView bottomNavigationView;//底部导航栏
    private ViewPager viewPager;//主体内容
    private ViewPagerAdapter viewPagerAdapter;//内容适配
    private Button welcome_title_add;
    private Button welcome_title_list;//右上角图标按钮
    private String list_weather;//城市代码
    private Ffour ffour;//天气碎片
    private Ftwo ftwo;
    private TextView welcome_title;
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
        setContentView(R.layout.activity_welcome);
        date();//获得传递的数据
        Bmob.initialize(this,appkey);
        welcome_title=(TextView)findViewById(R.id.welcome_top); //标题
        welcome_title_add=(Button)findViewById(R.id.welcome_title_add);
        welcome_title_list=(Button)findViewById(R.id.welcome_title_list);//选择城市
        welcome_title_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //天气城市选项
                Intent intent=new Intent(WelcomeActivity.this,ChooseAreaActivity.class);
                startActivityForResult(intent,1);
            }
        });
        drawerLayout=(DrawerLayout)findViewById(R.id.welcome_drawer_layout);
        //标题导航监听
        title_bt=(Button)findViewById(R.id.welcom_title_button);
        title_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //滑动页面选项
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pin_drop:
                        //百度定位
                        Intent intent_pin_drop=new Intent(
                                WelcomeActivity.this,LbsActivity.class);
                        startActivity(intent_pin_drop);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.lock:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit_app:
                        //退出app
                        Intent intent_exit_app=new Intent(WelcomeActivity.this,MainActivity.class);
                        intent_exit_app.putExtra(MainActivity.EXIT_APP, true);
                        startActivity(intent_exit_app);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.help:
                        //显示联系方式
                        Toast.makeText(WelcomeActivity.this,"请联系qq1012364741",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        //清空缓存
                        BmobUser.logOut();
                        Intent intent_exit=new Intent(WelcomeActivity.this,MainActivity.class);
                        startActivity(intent_exit);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });
        //获取滑动控件，并传入数据
        View v=navView.getHeaderView(0);
        name=(TextView)v.findViewById(R.id.nav_header_username);
        name.setText(username);
        //底部导航栏与内容适配
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.welcome_bottom_nav_view);
        viewPager=(ViewPager)findViewById(R.id.welcome_viewpager);
        //设置适配
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        //添加碎片
        ftwo=new Ftwo();//获取笑话实例
        ffour=new Ffour();//获取天气实例
        viewPagerAdapter.addFragment(new Fone());
        viewPagerAdapter.addFragment(ftwo);
        viewPagerAdapter.addFragment(new Fthree());
        viewPagerAdapter.addFragment(ffour);
        //为内容添加适配器
        viewPager.setAdapter(viewPagerAdapter);
        //底部导航栏设置监听事件
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.welcom_title);//标题颜色
                RelativeLayout relativeLayout1=(RelativeLayout)findViewById(R.id.nav_header);//滑动页颜色
                //获取到菜单项的Id
                int itemId = menuItem.getItemId();
                //当第一项被选择时FOne显示，以此类推
                switch (itemId) {
                    case R.id.bb_menu_write:
                        welcome_title.setText("记录");
                        bottomNavigationView.setBackgroundResource(R.color.bottomone);//底部导航颜色
                        relativeLayout.setBackgroundResource(R.color.bottomone);
                        relativeLayout1.setBackgroundResource(R.color.bottomone);
                        welcome_title_add.setVisibility(View.VISIBLE);
                        welcome_title_list.setVisibility(View.GONE);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.bb_menu_joke:
                        welcome_title.setText("笑话");
                        ftwo.changeBG(R.color.bottomtwo);
                        bottomNavigationView.setBackgroundResource(R.color.bottomtwo);
                        relativeLayout.setBackgroundResource(R.color.bottomtwo);
                        relativeLayout1.setBackgroundResource(R.color.bottomtwo);
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.GONE);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.bb_menu_new:
                        welcome_title.setText("新闻");
                        bottomNavigationView.setBackgroundResource(R.color.bottomthree);
                        relativeLayout.setBackgroundResource(R.color.bottomthree);
                        relativeLayout1.setBackgroundResource(R.color.bottomthree);
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.GONE);
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.bb_menu_weather:
                        welcome_title.setText("天气");
                        bottomNavigationView.setBackgroundResource(R.color.bottomfour);
                        relativeLayout.setBackgroundResource(R.color.bottomfour);
                        relativeLayout1.setBackgroundResource(R.color.bottomfour);
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(3);
                        break;

                }
                // true 会显示这个Item被选中的效果 false 则不会
                return true;
            }
        });
        viewPager.setPageTransformer(false,new ZoomOutPageTransformer());
        //内容滑动设置监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*页面滑动状态停止前一直调用
                position：当前点击滑动页面的位置
                positionOffset：当前页面偏移的百分比
                positionOffsetPixels：当前页面偏移的像素位置*/

            }

            @Override
            public void onPageSelected(int position) {
//                当 ViewPager 滑动后设置BottomNavigationView 选中相应选项
                /*滑动后显示的页面和滑动前不同，调用
                position：选中显示页面的位置*/
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                //显示右上角图标
                switch (bottomNavigationView.getMenu().getItem(position).getItemId())
                {//显示与隐藏右上角图标
                    case R.id.bb_menu_write:
                        welcome_title.setText("记录");
                        welcome_title_add.setVisibility(View.VISIBLE);
                        welcome_title_list.setVisibility(View.GONE);
                        break;
                    case R.id.bb_menu_joke:
                        welcome_title.setText("笑话");
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.GONE);
                        break;
                    case R.id.bb_menu_new:
                        welcome_title.setText("新闻");
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.GONE);
                        break;
                    case R.id.bb_menu_weather:
                        welcome_title.setText("天气");
                        welcome_title_add.setVisibility(View.GONE);
                        welcome_title_list.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /* 页面状态改变时调用
                state：当前页面的状态
                SCROLL_STATE_IDLE：空闲状态
                SCROLL_STATE_DRAGGING：滑动状态
                SCROLL_STATE_SETTLING：滑动后滑翔的状态*/

            }
        });
    }
    //选择某个城市天气返回响应
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    list_weather=data.getStringExtra("data_return");
                    if (list_weather!=null){
                        ffour.swipeRefresh.setRefreshing(true);
                        ffour.requestWeather(list_weather);
                    }
                }break;
                default:
        }
    }
    //获得传递的数据
    public void date(){
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
    }
    //点击返回键退出应用
    @Override
    public void onBackPressed() {
        Intent intent1=new Intent(WelcomeActivity.this,MainActivity.class);
        intent1.putExtra(MainActivity.EXIT_APP, true);
        startActivity(intent1);
        //super.onBackPressed();//注释掉这行,back键不退出activity
    }

}
