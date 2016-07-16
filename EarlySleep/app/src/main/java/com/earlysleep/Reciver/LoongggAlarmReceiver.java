package com.earlysleep.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.widget.Toast;

import com.earlysleep.Activity.AddTimeActivity;
import com.earlysleep.Activity.NoticActivity;
import com.earlysleep.Service.lockservice;
import com.earlysleep.model.TimeSeting;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by loongggdroid on 2016/3/21.
 * ���ӳ�������������ָ��ʱ�������÷������ڴ���ʱ������
 */
public class LoongggAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
       // String msg = intent.getStringExtra("msg");
       // Toast.makeText(context,"Loggingreciever",Toast.LENGTH_SHORT).show();
        //int flag = intent.getIntExtra("soundOrVibrator", 0);


        Intent i=new Intent(context, NoticActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      context.startActivity(i);

        ///        Intent clockIntent = new Intent(context, lockservice.class);
        //clockIntent.setAction(lockservice.LOCK_ACTION);
      //  context.startService(clockIntent);



    }




}
