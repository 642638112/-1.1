package com.earlysleep.model;

import android.net.Uri;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class Music extends DataSupport {
  private   String name;

    public int getRawid() {
        return rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }

    private int rawid;
    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private Uri uri;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
