package com.earlysleep.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.earlysleep.R;
import com.earlysleep.Service.lockservice;
import com.earlysleep.model.TimeSeting;
import com.musketeer.baselibrary.Activity.BaseActivity;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class NoticActivity extends BaseActivity {
    @Bind(R.id.notic_time)
    TextView textView;
    @Bind(R.id.zaoshui_now_tv)
    TextView zaoshui_now;
    String time;
    String path;
    MediaPlayer mp;
    int number=60;
    @Override
    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice);
    }

    @Override
    public void initView() {
        Intent i=getIntent();
        Calendar c = Calendar.getInstance();

       int  hour = c.get(Calendar.HOUR_OF_DAY);
       int minute = c.get(Calendar.MINUTE);
        String s="";
        if(minute<10){
           s=hour+":"+"0"+minute;
        }
        else {
            s=hour+":"+minute;
        }
       // Toast.makeText(this,time,Toast.LENGTH_SHORT).show();
        Toast.makeText(this,hour+":"+minute,Toast.LENGTH_SHORT).show();
        List<TimeSeting> newsList = DataSupport.where("time=?",s).find(TimeSeting.class);
        System.out.println("path--------------------------------------" + newsList.size());
        if(newsList.size()==1){
            Toast.makeText(this,"有数据",Toast.LENGTH_SHORT).show();
            path=newsList.get(0).getLinshenuri(); System.out.println("path--------------------------------------"+path);
            System.out.println("path--------------------------------------"+path);
            System.out.println("path--------------------------------------"+path);
            play(path);
        }
        textView.setText("60");
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
    }
    private void play(String path) {
         mp = new MediaPlayer();
        try {

            mp.setDataSource(this, Uri.parse(path));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    @Override
    public void initEvent() {
        zaoshui_now.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.zaoshui_now_tv:


                if(mp.isPlaying()){
                    mp.stop();
                    mp.release();
                }else {
                    mp.release();
                }


                jump();
                break;

        }
    }

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    number--;
                    textView.setText("" + number);

                    if(number > 0){
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                       jump();
                    }
            }

            super.handleMessage(msg);
        }
    };
    public void jump(){
        Intent intent=new Intent();
        intent.setClass(getApplicationContext(), lockservice.class);


        intent.setAction(lockservice.LOCK_ACTION);
        getApplicationContext().startService(intent);

        finish();
    }
}
