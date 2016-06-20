package com.horizon.gank.hgank.util;

import android.graphics.Bitmap;

import com.horizon.gank.hgank.model.bean.GanKData;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class SmallPicInfo implements Serializable {
    public GanKData data;
    public int left;
    public int top;
    public int width;
    public int height;
    public int position;
    public byte[] bmp;

    public SmallPicInfo(GanKData data, int left, int top, int width, int height, int position, Bitmap bmp) {
        this.data = data;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.position = position;
        if(bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            this.bmp = baos.toByteArray();
        }
    }


}