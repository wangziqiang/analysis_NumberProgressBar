package com.sf.wzq.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.sf.wzq.analysis_numberprogressbar.R;

/**
 * Created by wangziqiang on 2015/3/25.<br>
 * <ul>本项目学习点：
 * <li>1，从零开始学习一个自定义控件的思考步骤.</li>
 * <li>2，本项目基本流程和相关知识点.</li>
 * </ul>
 * 基本流程:
 * <ul>1,初始化
 * <li>属性参数:在values目录下建立attrs.xml文件，添加需要的属性</li>
 * <li>在构造方法中获取自定义的参数，并设置默认值</li>
 * <li>画笔</li>
 * </ul>
 * <ul>2,onMeasure</ul>
 * <ul>3,onDraw</ul>
 */
public class NumberProgressBar extends View {
    /**
     * the current value of reached area
     */
    private int mCurrentProgress = 0;
    /**
     * the max value of the bar
     */
    private int mMaxProgress = 100;
    /**
     * the progress bar's color of reached area
     */
    private int mReachedBarColor;
    /**
     * the progress bar's color of unreached area
     */
    private int mUnreachedBarColor;

    private float mReachedHeight;
    private float mUnreachedHeight;

    private float mTextSize;
    private int mTextColor;

    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);

    private  float default_reached_bar_height;
    private  float default_unreached_bar_height;

    private final int default_text_color = Color.rgb(66, 145, 241);


    public NumberProgressBar(Context context) {
//        super(context);
        this(context,null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs,R.attr.NumberProgressBarStyle);
    }


    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,defStyleAttr,0);
        //获取用户在xml中填写的属性值
        //1，当前progress
        setCurrentProgress(arr.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        //2，最大progress
        setMaxProgress(arr.getInt(R.styleable.NumberProgressBar_progress_max,100));
        //3，reached color
        mReachedBarColor = arr.getColor(R.styleable.NumberProgressBar_progress_reached_color,default_reached_color);
        //4, unreached color
        mUnreachedBarColor = arr.getColor(R.styleable.NumberProgressBar_progress_unreached_color,default_unreached_color);
        //5,reached height
        //6,unreached height
        //7,text size
        //8,text color
        //9,text visibility

    }

    private void setMaxProgress(int max) {
        this.mMaxProgress = max;
    }

    private void setCurrentProgress(int progress){
        this.mCurrentProgress = progress;
    }

}
