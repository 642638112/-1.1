package com.earlysleep.Adapter;

import android.content.Context;
import android.media.Image;
import android.media.Ringtone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earlysleep.R;
import com.earlysleep.model.Slect;


import java.util.List;

/**
 * Created by zml on 2016/6/7.
 * 介绍：主页 时间列表 listview的adapter
 */
public class linsenadapter extends BaseAdapter {
    private Context context;
    private List<Ringtone> list;
    private LayoutInflater mInflater = null;
    public linsenadapter(Context context, List<Ringtone> list){
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
                    R.layout.item_linsen_lv, null);
            viewHolder = new ViewHolder();
            viewHolder.sequence= (TextView) convertView.findViewById(R.id.linshennsequence_tv);
            viewHolder.title= (TextView) convertView.findViewById(R.id.linshenname_tv);
            viewHolder.linshennsequence_rl= (RelativeLayout) convertView.findViewById(R.id.linshennsequence_rl);
            viewHolder.image= (ImageView) convertView.findViewById(R.id.linshenname_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ViewHolder finalViewHolder = viewHolder;


        if(Slect.getA()==position){

            viewHolder.image.setVisibility(View.VISIBLE);
            list.get(position).play();
        }
        else {
            viewHolder.image.setVisibility(View.GONE);
            list.get(position).stop();
        }

        viewHolder.sequence.setText(position+"");
        viewHolder.title.setText(list.get(position).getTitle(context));
        return convertView;
    }




    public class ViewHolder{
        TextView sequence;
        TextView title;
        RelativeLayout linshennsequence_rl;
        ImageView image;

    }
}
