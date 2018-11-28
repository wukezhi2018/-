package top.wukezhi.personalmanagement;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.wukezhi.personalmanagement.util.HttpUtil;

public class LockActivity extends AppCompatActivity {
    private ImageView lock_img;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPassword1;
    private Button lockBack;
    private Button lockBt;

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
        setContentView(R.layout.activity_lock);
        loadBingPic();
        lock_img=(ImageView)findViewById(R.id.lock_img);
        oldPassword=(EditText)findViewById(R.id.lock_old_password);
        newPassword=(EditText)findViewById(R.id.lock_new_password);
        newPassword1=(EditText)findViewById(R.id.lock_new_password1);
        lockBack=(Button)findViewById(R.id.lock_backbt);
        lockBt=(Button)findViewById(R.id.lock_bt);
        lockBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    updatePassword(v);//修改密码
                }
            }
        });
        lockBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void updatePassword(final View view){
        BmobUser.updateCurrentUserPassword(oldPassword.getText().toString(),newPassword.getText().toString(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(LockActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LockActivity.this, "修改密码不成功(可能旧密码不正确)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public  boolean checkInput(){
        if (TextUtils.isEmpty(oldPassword.getText())) {
            Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPassword.getText())) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(newPassword1.getText())) {
            Toast.makeText(this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.equals(newPassword.getText(), newPassword1.getText())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                LockActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LockActivity.this).load(bingPic).into(lock_img);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
