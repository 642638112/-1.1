package com.earlysleep.View;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.earlysleep.R;

/**
 * Created by Administrator on 2016/7/14 0014.
 *
 * 需要修改样式 和添加点击事件
 */
public class MyPopupwindow {
    Context context;
    View v;
    public MyPopupwindow(Context context,View v){
        this.context=context;
        this.v=v;
    }

    public void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_window, null);
          final String[] strs = new String[] {
                "20", "35", "50", "65", "100"
        };//定
        ListView lv = (ListView) contentView.findViewById(R.id.pop_listview);
        lv.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, strs));
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.iphone5s));
        popupWindow.showAsDropDown(view);

        //lv.setAdapter();
    }
}
