package com.earlysleep.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earlysleep.R;
import com.earlysleep.Service.lockservice;
import com.earlysleep.model.Music;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ScreenSaverView extends RelativeLayout
{	
	private Context mContext;
	private View rootView;
	long startWhen ;
	long endWhen;
	private Button btnUnlock;
	private int seconds;
	private TextView tv;
	private  TextView music_name;
	Timer timer;

	ImageView playmusic;
	ImageView nextmusic;
	ImageView premusic;
	//private MediaPlayer mp;
	public ScreenSaverView(final Context context,int seconde)
	{
		super(context);
		mContext = context;
		seconds=seconde;


		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		startWhen = System.nanoTime();
		rootView = inflater.inflate(R.layout.screen_saver, this);endWhen = System.nanoTime();
		Log.e("ddsss", "Calendar upgrade took " + ((endWhen - startWhen) / 1000000) + "ms");
		tv = (TextView) rootView.findViewById(R.id.daojishi_tv);
		playmusic= (ImageView) rootView.findViewById(R.id.music_play);
		nextmusic= (ImageView) rootView.findViewById(R.id.music_next);
		premusic= (ImageView) rootView.findViewById(R.id.music_pre);
		music_name= (TextView) rootView.findViewById(R.id.music_name);
		tv.setText(seconds/60+":"+(seconds%60));
		 timer = new Timer();
		timer.schedule(task, 1000, 1000);
		btnUnlock = (Button) rootView.findViewById(R.id.giveuo_bt);

		LocskMyReceiver ll=new LocskMyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("locktoname");
		context.registerReceiver(ll,filter);
		btnUnlock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//mp.stop();;
				//mp.release();
				System.out.print("duabjhu");
				timer.cancel();//点击退出时，终止时间计时器
				Intent i = new Intent(mContext, lockservice.class);
				i.setAction(lockservice.UNLOCK_ACTION);

				mContext.startService(i);
			}
		});

		playmusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, lockservice.class);
				i.setAction("palymusic");

				mContext.startService(i);
			}
		});
		nextmusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, lockservice.class);
				i.setAction("nextmusic");

				mContext.startService(i);
			}
		});
		premusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, lockservice.class);
				i.setAction("premusic");

				mContext.startService(i);
			}
		});
	}

	final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
				case 1:


						tv.setText(seconds/60+":"+(seconds%60));








					if(seconds < 0){
						timer.cancel();
						//mp.stop();;
					//	mp.release();
						Intent i = new Intent(mContext, lockservice.class);
						i.setAction(lockservice.UNLOCK_ACTION);
						mContext.startService(i);


					}
			}
		}
	};
/*
* */
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			seconds--;
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	public static void  setmusicname(String s){
		//music_name.setText(s);
	}


	public class LocskMyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String control = intent.getStringExtra("musicname");


			music_name.setText(control);




		}
	}
}
