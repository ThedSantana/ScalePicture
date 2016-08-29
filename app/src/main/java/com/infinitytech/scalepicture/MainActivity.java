package com.infinitytech.scalepicture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private float lastDistance = 0;
    private float currentDistance = 0;
    private float scaleSize = 1f;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            //用户进行滑动时进行相关的缩放操作
            case MotionEvent.ACTION_MOVE:
                //当触控点多于两个时进行缩放操作
                if (event.getPointerCount()>=2){
                    //设置当前的两点间的距离
                    setCurrentDistance(event.getX(0),event.getY(0),event.getX(1),event.getY(1));
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                    //获取用户屏幕的宽度
                    Point point = new Point();
                    ((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
                    int screenSize = point.x;
                    //当当前距离大于之前距离时，进行放大操作
                    if (lastDistance <= 0 ){
                        lastDistance = currentDistance;
                    } else{
                        if (currentDistance>lastDistance){
                            //放大参数
                            scaleSize = 1.05f;
                            //设置完放大参数后，将当前距离赋值给lastDistance
                            lastDistance=currentDistance;
                            //当当前距离小于之前距离时，进行缩小操作
                        } else if (currentDistance<lastDistance){
                            //防止图片被缩小的太小，最小不能超过屏幕宽度的1/4
                            if (imageView.getWidth()>screenSize/4){
                                //缩小参数
                                scaleSize = 0.9f;
                            } else {
                                scaleSize = 1f;
                            }
                            lastDistance=currentDistance;
                        }
                        //设置layoutParams进行缩放操作
                        layoutParams.height = (int) (imageView.getHeight()*scaleSize);
                        layoutParams.width = (int) (imageView.getWidth()*scaleSize);
                        imageView.setLayoutParams(layoutParams);
                    }
                }
                break;

            //触摸动作抬起时将两个距离初始化
            case MotionEvent.ACTION_UP:
                lastDistance = 0;
                break;
            default:
                break;
        }
        return true;
    }

    private float setCurrentDistance(float x1, float y1, float x2, float y2){
        currentDistance = (float) Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        return currentDistance;
    }
}
