package com.sysu.qqslidemenu;

import android.content.Context;
import android.content.res.TypedArray;
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

/**
 * 自定义属性：允许用户设置菜单离屏幕右边的边距
 * 在attr.xml文件中增加属性
 * 在使用view的地方增加命名空间和使用属性
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
        this(context, attrs, 0);
    }

    public SlidingScrollView(Context context) {
        this(context, null);
    }

    /**
     * 当使用了自定义属性是会调用此构造方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SlidingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SlidingScrollView,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i=0;i<n;i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.SlidingScrollView_rightPadding:
                    //第二个参数时默认值
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,
                                    context.getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        //获取屏幕尺寸
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
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
