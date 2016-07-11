package com.keysersoze.sonyandroidlib.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.widget.Slider;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import sony.sdk.cameraremote.SimpleRemoteApi;
import sony.sdk.cameraremote.utils.thread.ThreadPoolHelper;

/**
 * Created by aaron on 7/9/16.
 */

public class ZoomSlider extends Slider {
    private SimpleRemoteApi mRemoteApi = null;
    private ThreadPoolHelper threadPoolHelper;

    public ZoomSlider(Context context) {
        super(context);
        init();
    }

    public ZoomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPositionChangeListener();
        mRemoteApi = SimpleRemoteApi.getInstance();
        threadPoolHelper = ThreadPoolHelper.getInstance();

    }

    public void initPositionChangeListener(){

        super.setOnPositionChangeListener(new OnPositionChangeListener (){
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                String zoomDirection = "in";
                if(oldValue < newValue){
                    zoomDirection = "in";
                }else {
                    zoomDirection = "out";
                }

                final String finalZoomDirection = zoomDirection;

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mRemoteApi.actZoom(finalZoomDirection, "1shot");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                };

                ExecutorService executorService = threadPoolHelper.getExecutorService();
                executorService.execute(runnable);
            }
        });
    }
}
