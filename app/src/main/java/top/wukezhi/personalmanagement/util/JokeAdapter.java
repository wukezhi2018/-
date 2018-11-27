package top.wukezhi.personalmanagement.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import top.wukezhi.personalmanagement.Gson_joke.Joke_show;
import top.wukezhi.personalmanagement.JokeActivity;
import top.wukezhi.personalmanagement.R;
//匹配笑话列表分为图片和文字部分
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {
    private Context mcontext;//上下文
    private List<Joke_show> mJoke_show_list;//存放类型的数组列表
//创建ViewHolder对象，数据域：图片，名字和卡片布局
    static class ViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;//卡片布局
    ImageView idImage;//图片
    TextView name;//文字部分
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            idImage=(ImageView)view.findViewById(R.id.joke_image);
            name=(TextView)view.findViewById(R.id.joke_name);
        }
    }
    public JokeAdapter(List<Joke_show> Joke_show_list){
        mJoke_show_list=Joke_show_list;//类型数组列表存入数据
    }
    //获取卡片布局对象
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mcontext==null){
            mcontext=parent.getContext();
        }//确保获得上下文
        View view= LayoutInflater.from(mcontext)
                .inflate(R.layout.joke_item,parent,false);//加载布局
        final ViewHolder holder=new ViewHolder(view);//final修饰的引用类型变量对象不可变
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            //卡片点击事件
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();//获得当前项位置
                Joke_show show=mJoke_show_list.get(position);//根据位置找到对应的水果
                //从mcontext中的活动跳转到FruitActivity活动
                Intent intent=new Intent(mcontext,JokeActivity.class);
                intent.putExtra(JokeActivity.ID_IMAGE,show.getIdImage());
                mcontext.startActivity(intent);//执行跳转
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Joke_show joke_show=mJoke_show_list.get(position);//get（position）根据当前项的Joke_show位置实例joke_show
        holder.name.setText(joke_show.getName());//设置文字
        Glide.with(mcontext).load(joke_show.getIdImage()).into(holder.idImage);//加载图片
    }
    @Override
    public int getItemCount(){
        return mJoke_show_list.size();//判断Fruit类型数组的长度
    }
}