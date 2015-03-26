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
    /**
     * the height of the reached bar
     */
    private float mReachedHeight;
    /**
     * the height of the unreached bar
     */
    private float mUnreachedHeight;
    /**
     * the text size of the progress
     */
    private float mTextSize;
    /**
     * the text color of the progress
     */
    private int mTextColor;
    /**
     * the offset between progress value and bar
     */
    private int mTextOffset;
    //~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * default value of reached bar's color
     */
    private final int default_reached_color = Color.rgb(66, 145, 241);
    /**
     * default value of unreached bar's color
     */
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    /**
     * default value of reached bar's height
     */
    private float default_reached_bar_height;
    /**
     * default value of unreached bar's height
     */
    private float default_unreached_bar_height;
    /**
     * default value of text's color
     */
    private final int default_text_color = Color.rgb(66, 145, 241);
    /**
     * default value of text's size
     */
    private final float default_text_size;
    /**
     * default offset of text's size
     */
    private final float default_text_offset;

    private int default_text_visibile = 0;

    private boolean mIfDrawText = true;

    public NumberProgressBar(Context context) {
//        super(context);
        this(context, null);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, R.attr.NumberProgressBarStyle);
    }


    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // initialize some default dimension
        default_reached_bar_height = dp2px(1.5f);
        default_unreached_bar_height = dp2px(1.0f);
        default_text_offset = dp2px(3.0f);
        default_text_size = sp2px(10f);


        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar, defStyleAttr, 0);
        //获取用户在xml中填写的属性值
        //1，当前progress
        setCurrentProgress(arr.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        //2，最大progress
        setMaxProgress(arr.getInt(R.styleable.NumberProgressBar_progress_max, 100));
        //3，reached color
        mReachedBarColor = arr.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        //4, unreached color
        mUnreachedBarColor = arr.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        //5,reached height
        mReachedHeight = arr.getDimension(R.styleable.NumberProgressBar_progress_reached_height, default_reached_bar_height);
        //6,unreached height
        mUnreachedHeight = arr.getDimension(R.styleable.NumberProgressBar_progress_unreached_height, default_unreached_bar_height);
        //7,text size
        mTextSize = arr.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);
        //8,text color
        mTextColor = arr.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        //9,text visibility
        int _drawText = arr.getInt(R.styleable.NumberProgressBar_progress_text_visibility, default_text_visibile);
        if (_drawText != default_text_visibile) {
            mIfDrawText = false;
        }
        arr.recycle();
        initializePainters();
    }

    /**
     * initialize some painters TODO
     */
    private void initializePainters() {

    }

    private float dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp;
    }

    private int getMax() {
        return mMaxProgress;
    }

    private void setMaxProgress(int max) {
        if (max > 0) {
            this.mMaxProgress = max;
            invalidate();
        }
    }

    private void setCurrentProgress(int progress) {
        if (progress < getMax() && progress > 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

}
