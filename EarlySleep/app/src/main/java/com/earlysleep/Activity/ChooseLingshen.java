package com.earlysleep.Activity;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.earlysleep.Adapter.linsenadapter;
import com.earlysleep.R;
import com.earlysleep.model.Slect;

import com.musketeer.baselibrary.Activity.BaseActivity;
import com.musketeer.baselibrary.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class ChooseLingshen extends BaseActivity {

    @Bind(R.id.headbar_left_imagebutton_two)
    ImageView back;//返回
    @Bind(R.id.headbar_right_imagebutton_two)
    ImageView sure;//queing按钮
    @Bind(R.id.headbar_title_two)
    TextView title;
    @Bind(R.id.default_headbar_two)
    RelativeLayout headerbar_two;
    @Bind(R.id.lingshen_lv)//铃声列表
    ListView linlv;
    @Bind(R.id.jiazai)
    TextView jiazai;

    List<Ringtone> ll;
     MediaPlayer mediaPlayer;
    linsenadapter lad;//adpter

    boolean flag;
    // lla;
    List<String> lpath;
    List<String> name;

    @Override
    public void setContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_choosse_linsen);
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
    //  final linsenadapter lad=new linsenadapter(getApplicationContext(),ll);
//        linlv.setAdapter(lad);
       // startActivity(intent);



        mediaPlayer=new MediaPlayer();
        new Thread() {
            public void run() {


                    ll=getRingtoneList(RingtoneManager.TYPE_ALARM);
                    lad=new linsenadapter(getApplicationContext(),ll);



                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        jiazai.setVisibility(View.GONE);

                            linlv.setAdapter(lad);


                    }

                });
            }
        }.start();

        linlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Slect.setA(position);

                lad.notifyDataSetChanged();


            }
        });



       System.out.println("11111111111116666-----" + RingtoneManager
               .getDefaultUri(RingtoneManager.TYPE_ALARM));



    }

    @Override
    public void initEvent() {
        back.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void initData() {
        title.setText("添加铃声");
    }


    public List<Ringtone> getRingtoneList(int type){

        List<Ringtone> resArr = new ArrayList<>();
        lpath=new ArrayList<>();
        RingtoneManager manager = new RingtoneManager(this);

        manager.setType(type);

        Cursor cursor = manager.getCursor();

        int count = cursor.getCount();
       // cursor.close();
        for(int i = 0 ; i < count ; i ++){

            resArr.add(manager.getRingtone(i));

            lpath.add(manager.getRingtoneUri(i).toString());

        }
      //  save(name,lpath);
        return resArr;

    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.headbar_left_imagebutton_two:


                finish();


                break;
            case R.id.headbar_right_imagebutton_two://确定
                RingtoneManager manager = new RingtoneManager(this);
            //   String path= getRingtoneUriPath(RingtoneManager.TYPE_ALARM,Slect.getA(),"ss");
                ll.get(Slect.getA()).stop();
                Toast.makeText(this, Slect.getA() + "dd", Toast.LENGTH_SHORT).show();
                SharePreferenceUtils.putString(this, "linshenname", ll.get(Slect.getA()).getTitle(this));
                SharePreferenceUtils.putString(this,"linshennamepath",lpath.get(Slect.getA()));


                finish();
                break;


        }
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }



}
