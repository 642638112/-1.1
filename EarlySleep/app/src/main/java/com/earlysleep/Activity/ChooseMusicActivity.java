package com.earlysleep.Activity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.earlysleep.R;

import com.earlysleep.Service.MusicService;
import com.earlysleep.model.AppConstant;
import com.earlysleep.model.Music;
import com.earlysleep.model.Slect;
import com.musketeer.baselibrary.Activity.BaseActivity;
import com.earlysleep.Adapter.musicdapter;
import com.musketeer.baselibrary.util.SharePreferenceUtils;


import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class ChooseMusicActivity extends BaseActivity {
    private SQLiteDatabase db;
    @Bind(R.id.headbar_left_imagebutton_two)
    ImageView back;//返回
    @Bind(R.id.headbar_right_imagebutton_two)
    ImageView sure;//queing按钮
    @Bind(R.id.headbar_title_two)
    TextView title;
    @Bind(R.id.default_headbar_two)
    RelativeLayout headerbar_two;

    @Bind(R.id.music_lv)//音乐列表
    ListView music_lv;
    musicdapter mdapter;
    List<Music> ll=new ArrayList<>();//音乐集合
    private Object data;
    MediaPlayer mp=new MediaPlayer();   MediaPlayer mediaPlayer01;
    @Override
    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_choosse_music);
    }

    @Override
    public void initView() {
        headerbar_two.setBackgroundColor(getResources().getColor(R.color.titleblue));
        back.setVisibility(View.VISIBLE);
        back.setImageResource(R.mipmap.back);
        back.setBackground(getResources().getDrawable(R.drawable.button_default_bg));
        sure.setVisibility(View.VISIBLE);
        sure.setImageResource(R.mipmap.sure);
        sure.setBackground(getResources().getDrawable(R.drawable.button_default_bg));
        title.setTextColor(getResources().getColor(R.color.white));
        final List<Music>  listmusic=getdata();
        mdapter=new musicdapter(this,listmusic,mp);
        music_lv.setAdapter(mdapter);

       // long i = R.raw.ff;
        music_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Slect.setB(position);

                mdapter.notifyDataSetChanged();
                if(mediaPlayer01!=null){
                    mediaPlayer01.stop();
                }

                mediaPlayer01 = MediaPlayer.create(getBaseContext(), listmusic.get(position).getRawid());
                mediaPlayer01.start();

                System.out.println("点击 " + position);
                SharePreferenceUtils.putInt(getApplicationContext(),"musicnumber",position);
                Intent intent = new Intent(getApplicationContext(),MusicService.class);
                intent.setAction("com.earlysleep.media.MUSIC_SERVICE");
                intent.putExtra("listPosition", 0);
                intent.putExtra("url", listmusic.get(position).getPath());
                intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
               // ..startService(intent);

            }
        });

    }

    @Override
    public void initEvent() {
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void initData() {
        title.setText("选择音乐");
        int r=R.raw.ff;

    }






    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.headbar_left_imagebutton_two:

                if(mediaPlayer01.isPlaying()){
                    mediaPlayer01.stop();
                    mediaPlayer01.release();
                }
                finish();


                break;
            case R.id.headbar_right_imagebutton_two://确定
                //EncodingUtil

                SharePreferenceUtils.putString(this,"selectmusicurl",ll.get(Slect.getB()).getPath());
                SharePreferenceUtils.putString(this, "selectmusicname", ll.get(Slect.getB()).getName());
                SharePreferenceUtils.putString(this, "selectmusicid", ll.get(Slect.getB()).getName());
                if(mediaPlayer01.isPlaying()){
                    mediaPlayer01.stop();
                    mediaPlayer01.release();
                }
                finish();
                break;


        }
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }


    public List<Music> getdata() {
        Field[] fields=R.raw.class.getDeclaredFields();
        int rawId;
        String rawName="";
        try { for(int i=0;i<fields.length;i++){
            rawId = fields[i].getInt(R.raw.class);

            if(i==0){
                rawName="鸟鸣";
            }
            if(i==1){rawName="小溪";}
            if(i==2){rawName="下雨";}
            if(i==3){
                rawName = "溪流";
            }
            Uri uri = Uri.parse("android.resource://"+this.getPackageName()+"/"+ rawId);
            Music music=new Music();
            music.setName(rawName);
            music.setUri(uri);
            music.setPath(uri.toString());
            music.setRawid(rawId);
            System.out.println(uri.toString());

            ll.add(music);
            }
            if(SharePreferenceUtils.getBoolean(this, "issaved",false)==false){
                db = Connector.getDatabase();
                System.out.println("第一次 ");
                DataSupport.saveAll(ll);
            }
            SharePreferenceUtils.putBoolean(this, "issaved", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ll;
    }
}
