package com.keysersoze.sonyandroidlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sony.sdk.cameraremote.SimpleRemoteApi;
import sony.sdk.cameraremote.utils.SimpleLiveviewSlicer;

/**
 * Created by aaron on 7/10/16.
 */

public class ViewFinderLayout extends FrameLayout{

    private static final String TAG = ViewFinderLayout.class.getSimpleName();
    /**
     * The duration, in millisconds, of one frame.
     */
    private static final long FRAME_TIME_MILLIS = 40;

    private final BlockingQueue<byte[]> mJpegQueue = new ArrayBlockingQueue<byte[]>(2);

    private int mPreviousWidth = 0;
    private int mPreviousHeight = 0;
    private boolean mWhileFetching;

    private Paint mPaint;
    private String mText;

    private SurfaceHolder mHolder;
    private boolean mRenderingPaused;

    private Thread mStreamThread;
    private Thread mDrawThread;
    private String streamUrl;

    private StreamErrorListener mErrorListener;

    public ViewFinderLayout(Context context) {
        super(context);
        init();
    }

    public ViewFinderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewFinderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHolder = holder;
        onDetectedFrameSizeChanged(width, height);
        Log.w(TAG, "surfaceChanged");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        mRenderingPaused = false;
        Log.w(TAG, "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
        stop();
        mStreamThread = null;
        mDrawThread = null;
        Log.w(TAG, "surfaceDestroyed");
    }


    public void renderingPaused(SurfaceHolder holder, boolean paused) {
        mRenderingPaused = paused;
        updateRenderingState();
    }

    public void setStreamErrorListener(StreamErrorListener streamErrorListener){
        mErrorListener = streamErrorListener;
    }

    /**
     * Starts or stops rendering according to the {@link LiveCard}'s state.
     */

    public void setStreamUrl(String streamUrl){
        this.streamUrl = streamUrl;
    }

    public void updateRenderingState(){

        if (streamUrl == null) {
            Log.e(TAG, "start() streamUrl is null.");
            mWhileFetching = false;
            if(mErrorListener != null){
                mErrorListener.onError(StreamErrorListener.StreamErrorReason.OPEN_ERROR);    
            }
            
            return;
        }

        if (mWhileFetching) {
            Log.w(TAG, "start() already starting.");
            return;
        }

        mWhileFetching = true;
        mStreamThread = getStreamThread();
        mDrawThread = getDrawThread();
        mStreamThread.start();
        mDrawThread.start();
    }

        /**
     * Check to see whether start() is already called.
     *
     * @return true if start() is already called, false otherwise.
     */
    public boolean isStarted() {
        return mWhileFetching;
    }

    /**
     * Request to stop retrieving and drawing liveview data.
     */
    public void stop() {
        mWhileFetching = false;
    }

    private Thread getStreamThread(){
        Thread newStreamThread = null;
        newStreamThread = new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "Starting retrieving streaming data from server.");
                SimpleLiveviewSlicer slicer = null;

                try {

                    // Create Slicer to open the stream and parse it.
                    slicer = new SimpleLiveviewSlicer();
                    slicer.open(streamUrl);

                    while (mWhileFetching) {
                        final SimpleLiveviewSlicer.Payload payload = slicer.nextPayload();
                        if (payload == null) { // never occurs
                            Log.e(TAG, "Liveview Payload is null.");
                            continue;
                        }

                        if (mJpegQueue.size() == 2) {
                            mJpegQueue.remove();
                        }
                        mJpegQueue.add(payload.jpegData);
                    }
                } catch (IOException e) {
                    Log.w(TAG, "IOException while fetching: " + e.getMessage());
                    if(mErrorListener != null){
                       mErrorListener.onError(StreamErrorListener.StreamErrorReason.IO_EXCEPTION);
                    }
                } finally {
                    if (slicer != null) {
                        slicer.close();
                    }

                    if (mDrawThread != null) {
                        mDrawThread.interrupt();
                    }

                    mJpegQueue.clear();
                    mWhileFetching = false;
                }
            }
        };
        
        return newStreamThread;
    }

    private Thread getDrawThread(){
        Thread newDrawThread = null;
        newDrawThread = new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "Starting drawing stream frame.");
                Bitmap frameBitmap = null;

                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inSampleSize = 1;
                factoryOptions.inBitmap = null;
                factoryOptions.inMutable = true;

                while (mWhileFetching) {
                    try {
                        byte[] jpegData = mJpegQueue.take();
                        frameBitmap = BitmapFactory.decodeByteArray(//
                                jpegData, 0, jpegData.length, factoryOptions);
                    } catch (IllegalArgumentException e) {
                        if (factoryOptions.inBitmap != null) {
                            factoryOptions.inBitmap.recycle();
                            factoryOptions.inBitmap = null;
                        }
                        continue;
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Drawer thread is Interrupted.");
                        break;
                    }
                    factoryOptions.inBitmap = frameBitmap;
                    draw(frameBitmap);
                }

                if (frameBitmap != null) {
                    frameBitmap.recycle();
                }
                mWhileFetching = false;
            }
        };

        return newDrawThread;
    }

    /**
     * Draws the view in the SurfaceHolder's canvas.
     */
    private void draw(Bitmap frame) {
        /*if (frame.getWidth() != mPreviousWidth || frame.getHeight() != mPreviousHeight) {
            onDetectedFrameSizeChanged(frame.getWidth(), frame.getHeight());
            Log.i(TAG, "Drawer thread is called onDetectedFrameSizeChanged.");
            return;
        }*/
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        
        int w = frame.getWidth();
        int h = frame.getHeight();
        Rect src = new Rect(0, 0, w, h);

        float by = Math.min((float) mPreviousWidth / w, (float) mPreviousHeight / h);
        int offsetX = (mPreviousWidth - (int) (w * by)) / 2;
        int offsetY = (mPreviousHeight - (int) (h * by)) / 2;
        Log.i(TAG, "Drawer thread is drawing: frame X: " + Integer.toString(w) + " frame Y: " + Integer.toString(h));
        Rect dst = new Rect(offsetX, offsetY, mPreviousWidth - offsetX, mPreviousHeight - offsetY);
        canvas.drawBitmap(frame, src, dst, mPaint);
        mHolder.unlockCanvasAndPost(canvas);
    }

/**
     * Called when the width or height of liveview frame image is changed.
     * 
     * @param width
     * @param height
     */
    private void onDetectedFrameSizeChanged(int width, int height) {
        Log.d(TAG, "Change of aspect ratio detected");
        mPreviousWidth = 320;
        mPreviousHeight = 180;

        drawBlackFrame();
        drawBlackFrame();
        drawBlackFrame(); // delete triple buffers
    }

    /**
     * Draw black screen.
     */
    private void drawBlackFrame() {
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }
        Log.d(TAG, "drawing black frame");
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(new Rect(0, 0, mPreviousWidth, mPreviousHeight), paint);
        mHolder.unlockCanvasAndPost(canvas);
    }

    public interface StreamErrorListener {

        enum StreamErrorReason {
            IO_EXCEPTION,
            OPEN_ERROR,
        }

        void onError(StreamErrorReason reason);
    }
}
