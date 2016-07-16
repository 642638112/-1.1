package com.earlysleep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.earlysleep.R;
import com.earlysleep.Reciver.LoongggAlarmReceiver;
import com.earlysleep.manager.AlarmManagerUtil;
import com.earlysleep.model.TimeSeting;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zml on 2016/6/7.
 * 介绍：主页 时间列表 listview的adapter
 */
public class timeadapter extends BaseAdapter {
    private Context context;
    private List<TimeSeting> list;
    private LayoutInflater mInflater = null;
    public timeadapter(Context context, List<TimeSeting> list){
      //  super();
        mInflater = LayoutInflater.from(context);
        this.context=context;
        this.list=list;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_time_lv, null);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_lv_timetv);
            viewHolder.day = (TextView) convertView.findViewById(R.id.item_lv_daytv);
            viewHolder.sw = (Switch) convertView.findViewById(R.id.manpaelv_set_sw);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
   viewHolder.time.setText(list.get(position).getTime());
       String t=list.get(position).getWeeksday();
        viewHolder.sw.setOnCheckedChangeListener(null);
        viewHolder.sw.setChecked(list.get(position).isOfforon());


        /*  final boolean flag=viewHolder.sw.isChecked();
        viewHolder.sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean f;
               // f=!flag;
                f=!flag;
            }
        });

        * */


        viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("点击------------");
                if (isChecked) {
                    //注册广播
                    // list.get(position).get

                    System.out.println("点击------------ischeche：" + position + isChecked);
                    for (int i = 0; i < list.get(position).getSlectnumber().length(); i++) {//设置多次
                        int j = Integer.parseInt(list.get(position).getSlectnumber().charAt(i) + "");
                        AlarmManagerUtil.setAlarm(context, 2, Integer.parseInt(list.get(position).getTimeour()), Integer
                                .parseInt(list.get(position).getTimeminute()), list.get(position).getAlarmnumber()+i, j, "闹钟响了", 1);
                    }

                    TimeSeting ts = new TimeSeting();
                    ts.setOfforon(true);
                    Long l;
                    l = (long) (position + 1);
                    ts.update(l);
                    list.get(position).setOfforon(true);

                } else {
                    System.out.println("点击------------关闭：" + position + isChecked);
                    System.out.println(list.get(position).getSlectnumber().length()+ "");

                    Intent t = new Intent(context.getApplicationContext(), LoongggAlarmReceiver.class);
                    //关闭广播
                    for (int i = 0; i < list.get(position).getSlectnumber().length(); i++) {//设置多次

                        AlarmManagerUtil.cancelAlarm(context, t, list.get(position).getAlarmnumber()+i);
                        System.out.println(list.get(position).getAlarmnumber()+i + "");
                    }
                    TimeSeting ts2 = new TimeSeting();
                    ts2.setToDefault("offoron");
                    Long l;
                    l = (long) (position + 1);
                    ts2.update(l);
                    list.get(position).setOfforon(false);

                }
            }
        });

//        System.out.println(t.size());
   //     System.out.println(t.get(0)+"-----------");
  viewHolder.day.setText(format(t));//需要更改格式
        return convertView;
    }

    /**
     * @param t 传入String 返回 需要的格式
     * @return
     */
    private String format(String t) {
        String s1="";
        if(t==null){

        }
        else if(t.length()==2){
            s1=t;
      }
        else if(t.length()==14){
            s1= "每天";

      }
        else if(t.equals("周一周二周三周四周五")){
          s1= "工作日";
      }
        else {System.out.println(t);
          char[] arr=t.toCharArray();
            for(int i=0;i<arr.length;i++){
                System.out.println(arr[i]);
            }
         String s="";
          for(int i=0;i<arr.length-2;i=i+2){
              s=s+arr[i]+arr[i+1]+"、";
          }
          s=s+arr[arr.length-2]+arr[arr.length-1];
            s1=s;
      }
        return s1;
    }

    public class ViewHolder{
      //  boolean flag;
        TextView time;
        TextView  day;
      private   Switch sw;
    //    List<String> day;
    }
}
