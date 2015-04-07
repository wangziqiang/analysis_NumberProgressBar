package com.sf.wzq.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
 * <li>画笔:reached bar's;unreached bar's;text's</li>s
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

    private int default_text_visible = 0;

    private boolean mIfDrawText = true;
    private boolean mDrawReachBar = true;
    private boolean mDrawUnReachBar = true;
    //initialize some painters
    private Paint mReachedBarPaint;
    private Paint mUnreachedBarPaint;
    private Paint mTextPaint;

    private String mPrefix="";
    private String mSuffix="%";
    private float mDrawTextWidth = 0;
    private float mOffset;// the offset between bar and text

    // TODO
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
        // the offset between bar and text
        mOffset = arr.getDimension(R.styleable.NumberProgressBar_progress_text_offset, default_text_offset);
        //9,text visibility
        int _drawText = arr.getInt(R.styleable.NumberProgressBar_progress_text_visibility, default_text_visible);
        if (_drawText != default_text_visible) {
            mIfDrawText = false;
        }

        //当前progress
        setProgress(arr.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        //最大progress
        setMaxProgress(arr.getInt(R.styleable.NumberProgressBar_progress_max, 100));

        arr.recycle();
        initializePainters();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) Math.max(mTextSize,Math.max(mReachedHeight,mUnreachedHeight));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec,true),measure(widthMeasureSpec,false));
    }
    private int measure(int spec,boolean isWidth){
        int result ;
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        int padding = isWidth?getPaddingLeft()+getPaddingRight():getPaddingTop()+getPaddingBottom();
        if(mode == MeasureSpec.EXACTLY){
            return size;
        }else{
            result = isWidth?getSuggestedMinimumWidth():getSuggestedMinimumHeight();
            result += padding;
            if(mode == MeasureSpec.AT_MOST){
                result = isWidth?Math.max(result,size):Math.min(result,size);
            }
        }
        return size;
    }
    private RectF mReachedRectF = new RectF(0,0,0,0);
    private RectF mUnReachedRectF = new RectF(0,0,0,0);
    private String mCurrentDrawText;
    private float mDrawTextStart,mDrawTextEnd;
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO
        if(mIfDrawText){
            drawWithText();
            canvas.drawText(mCurrentDrawText,mDrawTextStart,mDrawTextEnd,mTextPaint);
        }else{
            drawWithoutText();
        }
        if(mDrawReachBar){
            canvas.drawRect(mReachedRectF,mReachedBarPaint);
        }
        if(mDrawUnReachBar){
            canvas.drawRect(mUnReachedRectF,mUnreachedBarPaint);
        }
        super.onDraw(canvas);
    }
    private void drawWithoutText() {
        // calculate reachBar
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2 - mReachedHeight / 2;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) * getProgress() / getMax() + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2 + mReachedHeight / 2;
        // calculate unReachBar
        mUnReachedRectF.left =  mReachedRectF.right;
        mUnReachedRectF.top = getHeight() / 2 - mUnreachedHeight /2 ;
        mUnReachedRectF.right = getWidth() - getPaddingRight();
        mUnReachedRectF.bottom = getHeight() / 2 + mUnreachedHeight /2 ;
    }

    private void drawWithText() {
        // draw the text
        mCurrentDrawText = String.format("%d",getProgress() * 100 / getMax());
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix;
        mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        // draw the reachBar
        if(getProgress() == 0){
            mDrawReachBar = false;
            mDrawTextStart = getPaddingLeft();
        }else{
            mDrawReachBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2 - mReachedHeight / 2;
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight() ) * getProgress() / getMax() - mOffset + getPaddingLeft();
            mReachedRectF.bottom = getHeight() / 2 + mReachedHeight / 2;
            mDrawTextStart = mReachedRectF.right + mOffset;
        }
        mDrawTextEnd = getHeight() / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2;

        if((mDrawTextStart + mDrawTextWidth) >= (getWidth() - getPaddingRight())){
            mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
            mReachedRectF.right = mDrawTextStart - mOffset;
        }

        // draw the unReachBar
        float unReachBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        if(unReachBarStart >= getWidth() - getPaddingRight()){
            mDrawUnReachBar = false;
        }else{
            mDrawUnReachBar = true;
            mUnReachedRectF.left = unReachBarStart;
            mUnReachedRectF.top = getHeight() / 2 - mUnreachedHeight / 2;
            mUnReachedRectF.right = getWidth() - getPaddingRight();
            mUnReachedRectF.bottom = getHeight() / 2 + mUnreachedHeight / 2;
        }
    }

    /**
     * initialize some painters
     */
    private void initializePainters() {
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    public int getProgress() {
        return mCurrentProgress;
    }

    public int getMax() {
        return mMaxProgress;
    }

    private void setMaxProgress(int max) {
        if (max > 0) {
            this.mMaxProgress = max;
            invalidate();
        }
    }
    public void increaseProgressBy(int by){
        if(by > 0){
            setProgress(getProgress() + by);
        }
    }
    private void setProgress(int progress) {
        if (progress <= getMax() && progress > 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }
    private float dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp;
    }
}
