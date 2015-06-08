package com.sysu.qqslidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by user on 15/6/2.
 */

/**
 * 自定义ViewGroup
 * 1、onMeasure:决定内部View的宽和高以及自己的宽和高
 * 2、onLayout: 决定子View放置的位置
 * 3、重写onTouchEvent方法
 */

public class SlidingScrollView extends HorizontalScrollView {

    private LinearLayout mWrapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;

    private int mScreenWidth;
    //单位为dp
    private int mMenuRightPadding = 50;

    private boolean isFirstTime = false;

    private int mMenuWidth;


    /**
     * 未使用自定义属性时调用
     * @param context
     * @param attrs
     */
    public SlidingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取屏幕尺寸
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        //把dp转化为px
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!isFirstTime)
        {
            mWrapper = (LinearLayout)getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup)mWrapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;

            isFirstTime = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //通过设置偏移量使menu隐藏
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {


        super.onLayout(changed, l, t, r, b);

        //隐藏
        if(changed)
        {
            this.scrollTo(mMenuWidth,0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action)
        {
            //对于scrollview,只需要处理up事件
            case MotionEvent.ACTION_UP:
                //隐藏在左边的宽度
                int scrollX = getScrollX();

                //拖出小于一半的时候仍然隐藏菜单
                if(scrollX > mMenuWidth/2)
                {
                    this.smoothScrollTo(mMenuWidth,0);
                }else
                {
                    this.smoothScrollTo(0,0);
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
