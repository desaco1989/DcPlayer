package com.asha.vrlib;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.asha.vrlib.model.MDFlingConfig;
import com.asha.vrlib.model.MDPinchConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzqiujiadi on 16/5/6.
 * hzqiujiadi ashqalcn@gmail.com
 * <p/>
 * reference
 * https://github.com/boycy815/PinchImageView/blob/master/pinchimageview/src/main/java/com/boycy815/pinchimageview/PinchImageView.java
 */
public class MDTouchHelper {

    private MDVRLibrary.IAdvanceGestureListener mAdvanceGestureListener;
    private List<MDVRLibrary.IGestureListener> mClickListeners = new LinkedList<>();
    private GestureDetector mGestureDetector;
    private int mCurrentMode = 0;
    private PinchInfo mPinchInfo = new PinchInfo();
    private boolean mPinchEnabled;
    private float minScale;
    private float maxScale;
    private float mSensitivity;
    private float defaultScale;
    private float mGlobalScale;
    private ValueAnimator valueAnimator;

    private boolean mFlingEnabled;
    private MDFlingConfig mFlingConfig;

    private static final int MODE_INIT = 0;
    private static final int MODE_PINCH = 1;
    private float mTouchSensitivity;

    public MDTouchHelper(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mCurrentMode == MODE_PINCH) return false;

                for (MDVRLibrary.IGestureListener listener : mClickListeners) {
                    listener.onClick(e);
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (mCurrentMode == MODE_PINCH) return false;

                if (mAdvanceGestureListener != null) {
                    mAdvanceGestureListener.onDrag(scaled(distanceX), scaled(distanceY));
                }
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (mCurrentMode == MODE_PINCH) return false;
                if (!mFlingEnabled) return false;

                animStart(velocityX, velocityY);
                return true;
            }
        });
    }

    private float scaled(float input) {
        return input / mGlobalScale * mTouchSensitivity;
    }

    private void animCancel() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void animStart(float velocityX, float velocityY) {
        animCancel();

        PropertyValuesHolder hvx = PropertyValuesHolder.ofFloat("vx", velocityX, 0);
        PropertyValuesHolder hvy = PropertyValuesHolder.ofFloat("vy", velocityY, 0);
        valueAnimator = ValueAnimator.ofPropertyValuesHolder(hvx, hvy).setDuration(mFlingConfig.getDuring());
        valueAnimator.setInterpolator(mFlingConfig.getInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private long lastTime = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long now = animation.getCurrentPlayTime();
                long dur = (now - lastTime);
                float sx = (float) animation.getAnimatedValue("vx") * dur / -1000 * mFlingConfig.getSensitivity();
                float sy = (float) animation.getAnimatedValue("vy") * dur / -1000 * mFlingConfig.getSensitivity();
                lastTime = now;

                if (mAdvanceGestureListener != null) {
                    mAdvanceGestureListener.onDrag(scaled(sx), scaled(sy));
                }
            }
        });
        valueAnimator.start();
    }

    private void sendTouchMsg(String msgType, String msgConetent) {
        if (mITouchCallback != null) {
            mITouchCallback.sendMsg(msgType, msgConetent);
        }
    }

    private int startX0 = 0, startY0 = 0;//第一根手指触摸屏幕
    private int startX1 = 0, startY1 = 0;//第2根手指触摸屏幕

    public boolean handleTouchEvent(MotionEvent event) {//TODO
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {//up
            if (mCurrentMode == MODE_PINCH) {
                // end anim
            }
            mCurrentMode = MODE_INIT;
            sendTouchMsg("action_up", "cancel_touch");
            startX0 = 0;
            startY0 = 0;
            startX1 = 0;
            startY1 = 0;
        } else if (action == MotionEvent.ACTION_POINTER_UP) {//up
            // one point up
            if (mCurrentMode == MODE_PINCH) {
                // more than 2 pointer
                if (event.getPointerCount() > 2) {
                    if (event.getAction() >> 8 == 0) {
                        // 0 up
                        markPinchInfo(event.getX(1), event.getY(1), event.getX(2), event.getY(2));
                    } else if (event.getAction() >> 8 == 1) {
                        // 1 up
                        markPinchInfo(event.getX(0), event.getY(0), event.getX(2), event.getY(2));
                    }
                }
            }
            sendTouchMsg("action_up", "cancel_touch");
            startX0 = 0;
            startY0 = 0;
            startX1 = 0;
            startY1 = 0;
        } else if (action == MotionEvent.ACTION_MOVE) {//move
            // >= 2 pointer
            if (mCurrentMode == MODE_PINCH && event.getPointerCount() > 1) {
                float distance = calDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                // float[] lineCenter = MathUtils.getCenterPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                // mLastMovePoint.set(lineCenter[0], lineCenter[1]);
                handlePinch(distance);
            }
            int endX0=0,endY0=0,endX1=0,endY1=0;
            if(event.getPointerCount() > 1){
                try{
                    endX0 = (int)event.getX(event.getPointerId(0));
                    endY0 = (int)event.getY(event.getPointerId(0));
                    endX1 = (int)event.getX(event.getPointerId(1));
                    endY1 = (int)event.getY(event.getPointerId(1));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else{
                try{
                    endX0 = (int)event.getX(event.getPointerId(0));
                    endY0 = (int)event.getY(event.getPointerId(0));

                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }

            if (Math.abs(endX0 - startX0) > 20 || Math.abs(endY0 - startY0) > 20 || Math.abs(endX1 - startX1) > 20 || Math.abs(endY1 - startY1) > 20) {//滑动距离大于20才去切换
                sendTouchMsg("action_move", "can_touch");
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {//down
            // >= 2 pointer
            mCurrentMode = MODE_PINCH;
            markPinchInfo(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
            if(event.getPointerCount() > 1){
                try{
                    startX0 = (int)event.getX(event.getPointerId(0));
                    startY0 = (int)event.getY(event.getPointerId(0));
                    startX1 = (int)event.getX(event.getPointerId(1));
                    startY1 = (int)event.getY(event.getPointerId(1));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else{
                try{
                    startX0 = (int)event.getX(event.getPointerId(0));
                    startY0 = (int)event.getY(event.getPointerId(0));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
//            Log.e("desaco", "MotionEvent.ACTION_POINTER_DOWN  startX0=" + startX0 + ",,,startY0=" + startY0 + ",,startX1=" + startX1 + ",,startY1=" + startY1);
        } else if (action == MotionEvent.ACTION_DOWN) {//down
            animCancel();
            if(event.getPointerCount() > 1){
                try{
                    startX0 = (int)event.getX(event.getPointerId(0));
                    startY0 = (int)event.getY(event.getPointerId(0));
                    startX1 = (int)event.getX(event.getPointerId(1));
                    startY1 = (int)event.getY(event.getPointerId(1));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else{
                try{
                    startX0 = (int)event.getX(event.getPointerId(0));
                    startY0 = (int)event.getY(event.getPointerId(0));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }

        mGestureDetector.onTouchEvent(event);
        return true;
    }


    public void scaleTo(float scale) {
        setScaleInner(mPinchInfo.setScale(scale));
    }

    public void reset() {
        setScaleInner(mPinchInfo.reset());
    }

    private void handlePinch(float distance) {
        if (mPinchEnabled) {
            setScaleInner(mPinchInfo.pinch(distance));
        }
    }

    private void setScaleInner(float scale) {
        if (mAdvanceGestureListener != null)
            mAdvanceGestureListener.onPinch(scale);
        mGlobalScale = scale;
    }

    private void markPinchInfo(float x1, float y1, float x2, float y2) {
        mPinchInfo.mark(x1, y1, x2, y2);
    }

    private static float calDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));//勾股
    }

    public void addClickListener(MDVRLibrary.IGestureListener gestureListener) {
        if (gestureListener != null) mClickListeners.add(gestureListener);
    }

    public void setAdvanceGestureListener(MDVRLibrary.IAdvanceGestureListener listener) {
        this.mAdvanceGestureListener = listener;
    }

    public boolean isPinchEnabled() {
        return mPinchEnabled;
    }

    public void setPinchEnabled(boolean mPinchEnabled) {
        this.mPinchEnabled = mPinchEnabled;
    }

    public void setPinchConfig(MDPinchConfig pinchConfig) {
        this.minScale = pinchConfig.getMin();
        this.maxScale = pinchConfig.getMax();
        this.mSensitivity = pinchConfig.getSensitivity();
        this.defaultScale = pinchConfig.getDefaultValue();
        this.defaultScale = Math.max(minScale, this.defaultScale);
        this.defaultScale = Math.min(maxScale, this.defaultScale);
        setScaleInner(this.defaultScale);
    }

    public boolean isFlingEnabled() {
        return mFlingEnabled;
    }

    public void setFlingEnabled(boolean flingEnabled) {
        this.mFlingEnabled = flingEnabled;
    }

    public void setFlingConfig(MDFlingConfig flingConfig) {
        this.mFlingConfig = flingConfig;
    }

    public void setTouchSensitivity(float touchSensitivity) {
        this.mTouchSensitivity = touchSensitivity;
    }

    private class PinchInfo {
        private float x1;
        private float y1;
        private float x2;
        private float y2;
        private float oDistance;
        private float prevScale;
        private float currentScale;

        public void mark(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            oDistance = calDistance(x1, y1, x2, y2);
            prevScale = currentScale;
        }

        public float pinch(float distance) {
            if (oDistance == 0) oDistance = distance;
            float scale = distance / oDistance - 1;
            scale *= mSensitivity * 3;
            currentScale = prevScale + scale;
            // range
            currentScale = Math.max(currentScale, minScale);
            currentScale = Math.min(currentScale, maxScale);
            return currentScale;
        }

        public float setScale(float scale) {
            prevScale = scale;
            currentScale = scale;
            return currentScale;
        }

        public float reset() {
            return setScale(defaultScale);
        }
    }

    private ITouchCallback mITouchCallback;//TODO 定义接口，通过接口传递信息

    public void setICallback(ITouchCallback touchCallback) {
        mITouchCallback = touchCallback;
    }
}
