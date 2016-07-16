package com.earlysleep.model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zml on 2016/6/4.
 * 介绍：主页的时间信息包括（设定的具体时间  周几  以及是否打开开关）
 */
public class TimeSeting extends DataSupport {
    public int getAlarmnumber() {
        return alarmnumber;
    }

    public void setAlarmnumber(int alarmnumber) {
        this.alarmnumber = alarmnumber;
    }

    private int alarmnumber;
    private String time;//具体时间 精确到分钟
    private String timeour;
    private String linshenuri;
    private String timeminute;
    private boolean offoron;
    private boolean flag;//是否打开开关

    public void setSlectnumber(String slectnumber) {
        this.slectnumber = slectnumber;
    }

    public String getSlectnumber() {
        return slectnumber;
    }

    private String slectnumber;




    public String getTimeour() {
        return timeour;
    }

    public void setTimeour(String timeour) {
        this.timeour = timeour;
    }

    public String getTimeminute() {
        return timeminute;
    }

    public void setTimeminute(String timeminute) {
        this.timeminute = timeminute;
    }




    public String getLinshenuri() {
        return linshenuri;
    }

    public void setLinshenuri(String linshenuri) {
        this.linshenuri = linshenuri;
    }



    public boolean isOfforon() {
        return offoron;
    }

    public void setOfforon(boolean offoron) {
        this.offoron = offoron;
    }


    public String getWeeksday() {
        return weeksday;
    }

    public void setWeeksday(String weeksday) {
        this.weeksday = weeksday;
    }

    private  String weeksday;









    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
