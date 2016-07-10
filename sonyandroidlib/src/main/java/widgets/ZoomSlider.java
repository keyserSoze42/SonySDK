package widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.widget.Slider;

import java.io.IOException;

import sony.sdk.cameraremote.SimpleRemoteApi;

/**
 * Created by aaron on 7/9/16.
 */

public class ZoomSlider extends Slider {
    private SimpleRemoteApi mRemoteApi = null;

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
    }

    public void initPositionChangeListener(){
        super.setOnPositionChangeListener(new OnPositionChangeListener (){
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {

                try {
                    mRemoteApi.actZoom("out", "1shot");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
