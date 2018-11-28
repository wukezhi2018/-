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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import top.wukezhi.personalmanagement.Gson_joke.Joke_show;
import top.wukezhi.personalmanagement.Gson_news.News;
import top.wukezhi.personalmanagement.JokeActivity;
import top.wukezhi.personalmanagement.NewsActivity;
import top.wukezhi.personalmanagement.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context mcontext;//上下文
    private List<News.ResultBean.DataBean> mNewsList;//存放类型的数组列表
    //创建ViewHolder对象，数据域：图片，名字和卡片布局
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;//卡片布局
        TextView title;//文字部分
        ImageView image;
        TextView author;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            title=(TextView)view.findViewById(R.id.news_title);
            image=(ImageView)view.findViewById(R.id.news_image);
            author=(TextView)view.findViewById(R.id.news_author);
        }
    }
    public NewsAdapter(List<News.ResultBean.DataBean> newslist){
        mNewsList=newslist;//类型数组列表存入数据
    }
    //获取卡片布局对象
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mcontext==null){
            mcontext=parent.getContext();
        }//确保获得上下文
        View view= LayoutInflater.from(mcontext)
                .inflate(R.layout.news_item,parent,false);//加载布局
        final NewsAdapter.ViewHolder holder=new NewsAdapter.ViewHolder(view);//final修饰的引用类型变量对象不可变
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            //卡片点击事件
            @Override
            public void onClick(View v) {
                int position =holder.getAdapterPosition();//获得当前项位置
                News.ResultBean.DataBean news=mNewsList.get(position);//根据位置找到对应的水果
                //从mcontext中的活动跳转到FruitActivity活动
                Intent intent=new Intent(mcontext,NewsActivity.class);
                intent.putExtra(NewsActivity.CONTENT,news);
                mcontext.startActivity(intent);//执行跳转
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position){
        News.ResultBean.DataBean news=mNewsList.get(position);//get（position）根据当前项的位置实例news
        if (news.getThumbnail_pic_s()!=null){
            try {
                holder.image.setVisibility(View.VISIBLE);
                URL url=new URL(news.getThumbnail_pic_s());
                Glide.with(mcontext).load(url).into(holder.image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        holder.title.setText(news.getTitle()+"\n");//设置标题
        holder.author.setText(news.getAuthor_name()+"  "+news.getDate());//作者
    }
    @Override
    public int getItemCount(){
        return mNewsList.size();
    }
}
