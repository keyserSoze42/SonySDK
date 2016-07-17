/*
 * Copyright 2014 Sony Corporation
 */

package com.keysersoze.sonyandroidlib;

import sony.sdk.cameraremote.utils.SimpleLiveviewSlicer;
import sony.sdk.cameraremote.utils.SimpleLiveviewSlicer.Payload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.keysersoze.sonyandroidlib.ViewFinderLayout;
import com.keysersoze.sonyandroidlib.ViewFinderLayout.StreamErrorListener;

/**
 * A SurfaceView based class to draw liveview frames serially.
 */
public class SimpleStreamSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = SimpleStreamSurfaceView.class.getSimpleName();

    private StreamErrorListener mErrorListener;

    private ViewFinderLayout viewFinderLayout;

    /**
     * Constructor
     * 
     * @param context
     */
    public SimpleStreamSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        viewFinderLayout = new ViewFinderLayout(context);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param attrs
     */
    public SimpleStreamSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        viewFinderLayout = new ViewFinderLayout(context, attrs);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SimpleStreamSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
        viewFinderLayout = new ViewFinderLayout(context, attrs, defStyle);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing.
        viewFinderLayout.surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // do nothing.
        viewFinderLayout.surfaceCreated(holder);
        

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        viewFinderLayout.surfaceDestroyed(holder);
    }

    /**
     * Start retrieving and drawing liveview frame data by new threads.
     * 
     * @return true if the starting is completed successfully, false otherwise.
     * @see SimpleLiveviewSurfaceView#bindRemoteApi(SimpleRemoteApi)
     */
    public boolean start(final String streamUrl, StreamErrorListener listener) {
        viewFinderLayout.setStreamErrorListener(listener);
        viewFinderLayout.setStreamUrl(streamUrl);
        viewFinderLayout.updateRenderingState();
        return true;
    }

    /**
     * Check to see whether start() is already called.
     *
     * @return true if start() is already called, false otherwise.
     */
    public boolean isStarted() {
        return viewFinderLayout.isStarted();
    }

    /**
     * Request to stop retrieving and drawing liveview data.
     */
    public void stop() {
        viewFinderLayout.stop();
    }
}
