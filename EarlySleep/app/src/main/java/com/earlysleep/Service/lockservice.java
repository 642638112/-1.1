package com.earlysleep.Service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;

import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;


import com.earlysleep.View.ScreenSaverView;
import com.earlysleep.model.Music;
import com.musketeer.baselibrary.util.SharePreferenceUtils;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class lockservice extends Service {

    public static final String LOCK_ACTION = "lock";
    public static final String UNLOCK_ACTION = "unlock";
    private Context mContext;
    private WindowManager mWinMng;
    private ScreenSaverView screenView;
    private TimerTask timerTask;
    long startWhen ;
    long endWhen;
    LockMyReceiver   receiver;
    int time;
    private MediaPlayer mp;
    int count;
    List<Music> dd;//数据库音乐文件
   // public static boolean HUADONGFLA;


    public final static String TAG = "MyBroadcastReceiver";

    public final static String B_PHONE_STATE =
            TelephonyManager.ACTION_PHONE_STATE_CHANGED;
    WindowManager.LayoutParams param;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.print("静茹服务");
        super.onCreate();
        mContext = getApplicationContext();

        mWinMng = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        receiver = new LockMyReceiver();
        IntentFilter intentFilters = new IntentFilter();
        intentFilters.addAction("backtolockservice");
        intentFilters.addAction(B_PHONE_STATE);
        registerReceiver(receiver, intentFilters);
        count=SharePreferenceUtils.getInt(mContext,"musicnumber",0);
         dd= DataSupport.findAll(Music.class);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.print("静茹服务");

        String action = intent.getAction();

        if(TextUtils.equals(action, LOCK_ACTION)){ time=intent.getIntExtra("tiimeseconds", 60);
            addView();  if(SharePreferenceUtils.getBoolean(mContext,"musicisopen",true)){
                palymusic(count);
            }}
        else if(TextUtils.equals(action, UNLOCK_ACTION))
        {
            removeView();
            stopSelf();
        }
        else if(TextUtils.equals(action,"nextmusic")){

            if(count==dd.size()-1){
                palymusic(0);
                count=0;
                }
            else {palymusic(++count);}
            // 把各项参数恢复到初始状态
        }

        else if(TextUtils.equals(action,"premusic")){

            if(count==0){
                count=dd.size()-1;
                palymusic(count);
            }

           else{ palymusic(--count);}
           // 把各项参数恢复到初始状态
        }
        else if(TextUtils.equals(action,"palymusic")){


            if(mp.isPlaying()){
                mp.stop();
            }
            else{  palymusic(count);}


          //  mp.release();
        }








        return super.onStartCommand(intent, flags, startId);
    }
    public void palymusic(int position){
        Log.e("play", "palymusic " +position);
        if(mp!=null) {
            mp.stop();
        }
        startWhen = System.nanoTime();
        screenView.setmusicname(dd.get(position).getName());

        final Intent intent = new Intent();
        intent.setAction("locktoname");
        intent.putExtra("musicname",dd.get(position).getName());
        sendBroadcast(intent);
        endWhen = System.nanoTime();
        Log.e("查找数据库", "Calendar upgrade took " + ((endWhen - startWhen) / 1000000) + "ms");startWhen = System.nanoTime();
        mp = MediaPlayer.create(mContext,dd.get(position).getRawid() );
        mp.setLooping(true);
        mp.start();
        endWhen = System.nanoTime();
        Log.e("加载音乐", "Calendar upgrade took " + ((endWhen - startWhen) / 1000000) + "ms");


    }
    public void addView()
    {	System.out.print("addView");
        if(screenView == null)
        { startWhen = System.nanoTime();
            screenView = new ScreenSaverView(mContext,time);
            //daojishi();
            endWhen = System.nanoTime();
            Log.e("dd", "Calendar upgrade took " + ((endWhen - startWhen) / 1000000) + "ms");
            param = new WindowManager.LayoutParams();
            param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            param.format = PixelFormat.RGBA_8888;
            // mParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            param.width = WindowManager.LayoutParams.MATCH_PARENT;
            param.height = WindowManager.LayoutParams.MATCH_PARENT;
            param.format = PixelFormat.TRANSLUCENT;
            param.flags |= WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;


            mWinMng.addView(screenView, param);
        }
    }

    public void removeView()
    { if(mp.isPlaying()){
        mp.stop();
    }
        //HUADONGFLA=false;
        mp.release();
        if(screenView != null)
        {
            mWinMng.removeView(screenView);
            screenView = null;
        }
        onDestroy();
    }

    public class LockMyReceiver extends BroadcastReceiver {
        int status;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "[Broadcast]"+action);

            //呼入电话
            if(action.equals(lockservice.B_PHONE_STATE)){
                Log.i(TAG, "[Broadcast]PHONE_STATE");
                doReceivePhone(context,intent);
            }




        }

        private void doReceivePhone(Context context, Intent intent) {

            String phoneNumber = intent.getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            TelephonyManager telephony =
                    (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            int state = telephony.getCallState();
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "[Broadcast]等待接电话="+phoneNumber);

                    freshview();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "[Broadcast]电话挂断="+phoneNumber);
                    freshview2();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "[Broadcast]通话中="+phoneNumber);
                    break;
            }
        }


    }

    private void freshview() {
        param.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mWinMng.updateViewLayout(screenView,param);
        Log.e("dd", "更新view " + ((endWhen - startWhen) / 1000000) + "ms");

    }

    private void freshview2() {
        param.type=WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mWinMng.updateViewLayout(screenView,param);
        Log.e("dd", "更新view2 " + ((endWhen - startWhen) / 1000000) + "ms");
    }
    @Override
    public void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
