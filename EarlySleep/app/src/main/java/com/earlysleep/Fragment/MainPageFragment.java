package com.earlysleep.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.earlysleep.Activity.AddTimeActivity;
import com.earlysleep.Activity.ChooseMusicActivity;
import com.earlysleep.Activity.MainActivity;
import com.earlysleep.Adapter.timeadapter;
import com.earlysleep.R;
import com.earlysleep.Reciver.LoongggAlarmReceiver;
import com.earlysleep.Service.lockservice;
import com.earlysleep.View.MyPopupwindow;
import com.earlysleep.View.SwitchView;
import com.earlysleep.manager.AlarmManagerUtil;
import com.earlysleep.model.TimeSeting;
import com.musketeer.baselibrary.Activity.BaseFragment;
import com.musketeer.baselibrary.util.SharePreferenceUtils;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zml on 2016/6/2.
 * 介绍：
 */
public class MainPageFragment extends BaseFragment {
    @Bind(R.id.headbar_left_imagebutton)
      ImageView setting;//设置按钮
    @Bind(R.id.headbar_right_imagebutton)
      ImageView add;//添加按钮
    @Bind(R.id.headbar_title)
      TextView title;
    @Bind(R.id.default_headbar)
       RelativeLayout headerbar;
    @Bind(R.id.numpickrelay)
    NumberPicker numPick;
    @Bind(R.id.numpickrelay2)
    NumberPicker numPick2;

    @Bind(R.id.switch_view_ll)//switch_view
    LinearLayout switch_view_ll;
    @Bind(R.id.time_lv)//listview 时间列表
    ListView time_lv;
    @Bind(R.id.slectmusic_uv)
            TextView slectmusictv;//选择音乐
    @Bind(R.id.mstimepick_Tv)
    TextView slectmusictime;//选择音乐播放时间
    SwitchView switch_view;
    private Handler handler;
    @Bind(R.id.setting_time_sw)
    Switch setting_time_sw;
    @Bind(R.id.no_timesettin)
    TextView no_timesettin;
    int number=0;//第几个闹钟
    int settingtime=3600;//设置系统默认的锁屏时间  一个小时
    int sethour;
    int setminut;
    List<TimeSeting> s;
    timeadapter t;

    private static final String[] strs = new String[] {
        "first", "second", "third", "fourth", "fifth"
    };
    MainActivity activity;
    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BaseView = inflater.inflate(R.layout.fragemnt_mainpage, null);
         activity=(MainActivity) getActivity();
        /**
         * 得到Activity的Handler
         */
        handler=activity.handler;
    }

    @Override
    public void initView() {
        switch_view=new SwitchView(getActivity());
        switch_view_ll.addView(switch_view);
        headerbar.setBackgroundColor(getResources().getColor(R.color.titleblue));
        setting.setVisibility(View.VISIBLE);
        setting.setImageResource(R.mipmap.setting);
        setting.setBackground(getResources().getDrawable(R.drawable.button_default_bg));
        add.setVisibility(View.VISIBLE);
        add.setImageResource(R.mipmap.add);
        add.setBackground(getResources().getDrawable(R.drawable.button_default_bg));
        title.setTextColor(getResources().getColor(R.color.white));
        setting_time_sw.setChecked(SharePreferenceUtils.getBoolean(getActivity(),"musicisopen",true));
        numPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sethour = newVal;
                System.out.println(newVal + "");
            }
        });

        numPick2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               //numPick2.setValue((newVal < oldVal)?oldVal-15:oldVal+15);
               // sethour=(newVal < oldVal)?oldVal-15:oldVal+15;
                setminut=newVal;
                System.out.println(""+newVal);
            }
        });
        setting_time_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    SharePreferenceUtils.putBoolean(getActivity(), "musicisopen", true);
                }
                else {
                    SharePreferenceUtils.putBoolean(getActivity(), "musicisopen", false);
                }
            }
        });
    }

    @Override
    public void initEvent() {
        setting.setOnClickListener(this);
        add.setOnClickListener(this);
        slectmusictv.setOnClickListener(this);
        slectmusictime.setOnClickListener(this);
        switch_view.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    //  Toast.makeText(getActivity(), "meixuanzhong", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(getActivity(), "选中", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getActivity().getApplicationContext(),"xuanzhong",Toast.LENGTH_SHORT).show();
                    if ((sethour == 0) && (setminut == 0)) {
                    } else {
                        settingtime = sethour * 3600 + setminut * 60;
                    }


                    System.out.println(sethour + "" + setminut);
                    Intent intent = new Intent();

                    intent.setClass(activity.getApplicationContext(), lockservice.class);
                    intent.putExtra("tiimeseconds", settingtime);

                    intent.setAction(lockservice.LOCK_ACTION);
                    activity.getApplicationContext().startService(intent);

                }
            }
        });
    }

    /**
     *
     */
    @Override
    public void initData() {
        numPick.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//设置numPick
        numPick.setMaxValue(24);
        numPick.setMinValue(0);
        numPick.setValue(1);


        setNumberPickerDividerColor(numPick);

       numPick2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//设置numPick
      //  String[] minute = {"0","15","30","45","60"};
       // numPick2.setDisplayedValues(minute);
        numPick2.setMinValue(0);
        numPick2.setMaxValue(60);
       // minute.length - 1);
        numPick2.setValue(0);
        setNumberPickerDividerColor(numPick2);
        title.setText(R.string.mianpege_title_naem);

         s=  qurydata(); t=new timeadapter(getActivity(),s);
       // time_lv.setd
        time_lv.setAdapter(t);
        time_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position + "dianji", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AddTimeActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("type", "change");
                startActivity(intent);
            }
        });



    }
    private void cancleclock(int j) {
        TimeSeting ts = DataSupport.find(TimeSeting.class, j+1);
       // System.out.println("ts"+ts.);
        Intent t = new Intent(getActivity().getApplicationContext(), LoongggAlarmReceiver.class);
        //关闭广播
        for (int i = 0; i <ts.getSlectnumber().length(); i++) {//设置多次

            AlarmManagerUtil.cancelAlarm(getActivity(), t, ts.getAlarmnumber() + i);
            System.out.println(ts.getAlarmnumber()+i + "");
        }
    }
    private List<TimeSeting>  qurydata() {
     //   TimeSeting timeSeting = DataSupport.find(TimeSeting.class, 1);
    //    if(timeSeting.s)
      //  System.out.println("timeSeting:time="+timeSeting.getTime());
        List<TimeSeting> allNews = DataSupport.findAll(TimeSeting.class);
        number=allNews.size();
        return allNews;
    }

    @Override
    public void onResume() {

        switch_view.setChecked(true);
        System.out.println("onResume");
        s=qurydata();
        if(s.size()==0){
            no_timesettin.setVisibility(View.VISIBLE);
        }
        else {
            no_timesettin.setVisibility(View.GONE);
        }
        slectmusictv.setText(SharePreferenceUtils.getString(getActivity(),"selectmusicname","无"));
        t=new timeadapter(getActivity(),s);
        System.out.println(s.size());
        time_lv.setAdapter(t);

        t.notifyDataSetChanged();
     //   switch_view.setChecked(false);
        super.onResume();
        //qurydata();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.headbar_left_imagebutton:
                    showMenu();
                break;
            case R.id.headbar_right_imagebutton:
                Intent it=new Intent(getActivity(),AddTimeActivity.class);
                it.putExtra("number",number+1);
                it.putExtra("type","normal");


                startActivity(it);
                break;
            //case
            case R.id.slectmusic_uv://打开音乐列表
                startActivity(ChooseMusicActivity.class);


            case R.id.mstimepick_Tv://选择音乐播放时间 弹出poupwindow
                showPopupWindow(slectmusictime);


        }
    }

    private void showMenu() {
        handler.sendEmptyMessage(100);//将点击事件返回给MainActivity进行处理

    //    SlidingMenu menu = MainActivity.()


    }

    /**
     * @param numberPicker s设置numberpicker的分割线颜色  设为透明
     */
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
//设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.transparent)));
// pf.set(picker, new Div)
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    private void showPopupWindow(View view) {
        MyPopupwindow myPopupwindow=new MyPopupwindow(getActivity(),view);
        myPopupwindow.showPopupWindow(view);
    }
}
