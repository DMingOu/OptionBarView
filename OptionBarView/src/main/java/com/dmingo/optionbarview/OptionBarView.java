package com.dmingo.optionbarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * @description: 条目类型View
 * @author: DMingO
 * @date: 2020/9/3
 */
public class OptionBarView extends View {


    /**
     * 控件的宽
     */
    private int mWidth;
    /**
     * 控件的高
     */
    private int mHeight;

    private Context mContext;

    /**
     * 左图bitmap
     */
    private Bitmap leftImage;
    /**
     * 右图bitmap
     */
    private Bitmap rightImage;

    private boolean isShowLeftImg = true;
    private boolean isShowLeftText = true;
    private boolean isShowRightImg = true;
    private boolean isShowRightText = true;

    //拆分模式(默认是false，也就是一个整体)
    private boolean mSplitMode = false;
    /**
     * 判断按下开始的位置是否在左
     */
    private boolean leftStartTouchDown = false;
    /**
     * 判断按下开始的位置是否在中间
     */
    private boolean centerStartTouchDown = false;
    /**
     * 判断按下开始的位置是否在右
     */
    private boolean rightStartTouchDown = false;
    /**
     * 标题
     */
    private String title = "";
    /**
     * 标题字体大小
     */
    private float titleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 标题颜色
     */
    private int titleTextColor = Color.BLACK;
    /**
     * 左边文字
     */
    private String leftText = "";
    /**
     * 左边文字大小
     */
    private float leftTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 左字左边距
     */
    private int leftTextMarginLeft = -1;
    /**
     * 左图左边距
     */
    private int leftImageMarginLeft = -1;
    /**
     * 左图右边距
     */
    private int leftImageMarginRight = -1;
    /**
     * 左边文字颜色
     */
    private int leftTextColor = Color.BLACK;
    /**
     * 右边文字
     */
    private String rightText = "";
    /**
     * 右边文字大小
     */
    private float rightTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 右边文字颜色
     */
    private int rightTextColor = Color.BLACK;
    /**
     * 右字右边距
     */
    private int rightTextMarginRight = -1;
    /**
     * 右图左边距
     */
    private int rightImageMarginLeft = -1;
    /**
     * 右图右边距
     */
    private int rightImageMarginRight = -1;

    /**
     * 左图的 宽度大小
     */
    private int leftImageWidth = -1;

    private int leftImageHeight = -1;

    private int rightImageWidth = -1;

    private int rightImageHeight = -1;

    private Paint mPaint;
    /**
     * 对文本的约束
     */
    private Rect mTextBound;
    /**
     * 控制整体布局
     */
    private Rect rect;

    /**
     * 是否绘制分隔线
     */
    private Boolean isShowDivideLine = false;

    //分割线左、 右边距
    private int divide_line_left_margin = 0;

    private int divide_line_right_margin = 0;
    //分割线 颜色
    private int divide_line_color = Color.parseColor("#DCDCDC");

    //分割线高度  默认为1px
    private int divide_line_height = 1;

    //分割线的位置是否在上方
    private boolean divide_line_top_gravity = false;

    /**
     * 绘制分割线的画笔
     */
    private Paint dividePaint;


    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public OptionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        //获取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionBarView);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            //不允许使用非常量值进入switch，即Module 引用style必须使用if else
            if (attr == R.styleable.OptionBarView_show_divide_line) {
                isShowDivideLine = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.OptionBarView_divide_line_height) {
                divide_line_height = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_divide_line_color) {
                divide_line_color = typedArray.getColor(attr, Color.parseColor("#DCDCDC"));
            } else if (attr == R.styleable.OptionBarView_divide_line_left_margin) {
                divide_line_left_margin = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_divide_line_right_margin) {
                divide_line_right_margin = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_divide_line_top_gravity) {
                divide_line_top_gravity = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.OptionBarView_left_src) {
                leftImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                //无法加载Vector资源
                if (leftImage == null) {
                    Bitmap vectorBitmap = decodeVectorToBitmap(typedArray.getResourceId(attr, 0));
                    if (vectorBitmap != null) {
                        leftImage = vectorBitmap;
                    }
                }
            } else if (attr == R.styleable.OptionBarView_right_src) {
                rightImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                //若无法加载则视为Vector资源，用加载Vector的方法
                if (rightImage == null) {
                    Bitmap vectorBitmap = decodeVectorToBitmap(typedArray.getResourceId(attr, 0));
                    if (vectorBitmap != null) {
                        rightImage = vectorBitmap;
                    }
                }
            } else if (attr == R.styleable.OptionBarView_title_size) {
                titleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_title_color) {
                titleTextColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.OptionBarView_title) {
                title = typedArray.getString(attr);
            } else if (attr == R.styleable.OptionBarView_left_text) {
                leftText = typedArray.getString(attr);
            } else if (attr == R.styleable.OptionBarView_left_text_size) {
                leftTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_text_margin_left) {
                leftTextMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_image_margin_left) {
                leftImageMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_image_margin_right) {
                leftImageMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_src_width) {
                leftImageWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_src_height) {
                leftImageHeight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_src_width) {
                rightImageWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_src_height) {
                rightImageHeight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_left_text_color) {
                leftTextColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.OptionBarView_right_text) {
                rightText = typedArray.getString(attr);
            } else if (attr == R.styleable.OptionBarView_right_text_size) {
                rightTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_text_margin_right) {
                rightTextMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_image_margin_left) {
                rightImageMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_image_margin_right) {
                rightImageMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.OptionBarView_right_text_color) {
                rightTextColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.OptionBarView_split_mode) {
                mSplitMode = typedArray.getBoolean(attr, false);
            }
        }
        //参数获取完毕后 回收typeArray
        typedArray.recycle();

        rect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        // 计算了描绘字体需要的范围
        mPaint.getTextBounds(title, 0, title.length(), mTextBound);

        //初始化 分割线 画笔
        dividePaint = new Paint();
        dividePaint.setColor(divide_line_color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        //抗锯齿处理
        canvas.setDrawFilter(paintFlagsDrawFilter);

        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(titleTextSize > leftTextSize ? Math.max(titleTextSize, rightTextSize) : Math.max(leftTextSize, rightTextSize));
//        mPaint.setTextSize(titleTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        //文字水平居中
        mPaint.setTextAlign(Paint.Align.CENTER);

        //计算垂直居中baseline
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int baseLine = (int) ((rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2);

        if (!title.trim().equals("")) {
            // 正常情况，将字体居中
            mPaint.setColor(titleTextColor);
            canvas.drawText(title, rect.centerX(), baseLine, mPaint);
            rect.bottom -= mTextBound.height();
        }


        if (leftImage != null && isShowLeftImg) {
            // 计算左图范围
            rect.left = leftImageMarginLeft >= 0 ? leftImageMarginLeft : mWidth / 32;
            //计算 左右边界坐标值，若有设置左图偏移则使用，否则使用View的宽度/32
            if(leftImageWidth >= 0){
                rect.right = rect.left + leftImageWidth;
            }else {
                rect.right = rect.right + mHeight / 2;
            }
            //计算左图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if(leftImageHeight >= 0){
                rect.top = ( mHeight - leftImageHeight) / 2;
                rect.bottom = leftImageHeight + rect.top;
            }else {
                rect.top = mHeight / 4;
                rect.bottom = mHeight * 3 / 4;
            }

            canvas.drawBitmap(leftImage, null, rect, mPaint);
        }
        if (rightImage != null && isShowRightImg) {
            // 计算右图范围
            //计算 左右边界坐标值，若有设置右图偏移则使用，否则使用View的宽度/32
            rect.right = mWidth - (rightImageMarginRight >= 0 ? rightImageMarginRight : mWidth / 32);
            if(rightImageWidth >= 0){
                rect.left = rect.right - rightImageWidth;
            }else {
                rect.left = rect.right - mHeight / 2;
            }
            //计算右图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if(rightImageHeight >= 0){
                rect.top = ( mHeight - rightImageHeight) / 2;
                rect.bottom = rightImageHeight + rect.top;
            }else {
                rect.top = mHeight / 4;
                rect.bottom = mHeight * 3 / 4;
            }
            canvas.drawBitmap(rightImage, null, rect, mPaint);
        }
        if (leftText != null && !leftText.equals("") && isShowLeftText) {
            mPaint.setTextSize(leftTextSize);
            mPaint.setColor(leftTextColor);
            int w = 0;
            if (leftImage != null) {
                w += leftImageMarginLeft >= 0 ? leftImageMarginLeft : (mHeight / 8);//增加左图左间距
                w += mHeight / 2;//图宽
                w += leftImageMarginRight >= 0 ? leftImageMarginRight : (mWidth / 32);// 增加左图右间距
                w += Math.max(leftTextMarginLeft, 0);//增加左字左间距
            } else {
                w += leftTextMarginLeft >= 0 ? leftTextMarginLeft : (mWidth / 32);//增加左字左间距
            }

            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(leftText, w, baseLine, mPaint);
        }
        if (rightText != null && !rightText.equals("") && isShowRightText) {
            mPaint.setTextSize(rightTextSize);
            mPaint.setColor(rightTextColor);

            int w = mWidth;
            if (rightImage != null) {
                w -= rightImageMarginRight >= 0 ? rightImageMarginRight : (mHeight / 8);//增加右图右间距
                w -= mHeight / 2;//增加图宽
                w -= rightImageMarginLeft >= 0 ? rightImageMarginLeft : (mWidth / 32);//增加右图左间距
                w -= Math.max(rightTextMarginRight, 0);//增加右字右间距
            } else {
                w -= rightTextMarginRight >= 0 ? rightTextMarginRight : (mWidth / 32);//增加右字右间距
            }

            // 计算了描绘字体需要的范围
            mPaint.getTextBounds(rightText, 0, rightText.length(), mTextBound);
            canvas.drawText(rightText, w - mTextBound.width(), baseLine, mPaint);
        }

        //处理分隔线部分
        if(isShowDivideLine){
            int left = divide_line_left_margin;
            int right = mWidth - divide_line_right_margin;
            if(divide_line_top_gravity){
                int top = 0;
                int bottom = divide_line_height;
                canvas.drawRect(left, top, right, bottom, dividePaint);
            }else {
                int top = mHeight - divide_line_height;
                int bottom = mHeight;
                canvas.drawRect(left, top, right, bottom, dividePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //整体点击模式，不需要判断各区域的点击
        if (!mSplitMode) {
            return super.onTouchEvent(event);
        }
        //分区域点击模式，根据不同点击区域，回调接口不同的方法
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int eventX = (int) event.getX();
                if (eventX < mWidth / 8) {
                    leftStartTouchDown = true;
                } else if (eventX > mWidth * 7 / 8) {
                    rightStartTouchDown = true;
                } else {
                    centerStartTouchDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                if (leftStartTouchDown && x < mWidth / 8 && listener != null) {
                    listener.leftOnClick();
                } else if (rightStartTouchDown && x > mWidth * 7 / 8 && listener != null) {
                    listener.rightOnClick();
                } else if (centerStartTouchDown && listener != null) {
                    listener.centerOnClick();
                }
                leftStartTouchDown = false;
                centerStartTouchDown = false;
                rightStartTouchDown = false;
                break;
            default:
                break;
        }
        return true;
    }

    //***********************属性的getter和setter方法*************************************//
    public String getTitleText(){
        return title;
    }

    public void setTitleText(String text) {
        title = text;
        invalidate();
    }

    public void setTitleText(int stringId) {
        title = mContext.getString(stringId);
        invalidate();
    }

    public void setTitleColor(int color) {
        titleTextColor = color;
        invalidate();
    }

    public void setTitleSize(int sp) {
        titleTextSize = sp2px(mContext, sp);
        invalidate();
    }

    public String getLeftText(){
        return  leftText;
    }

    public void setLeftText(String text) {
        leftText = text;
        invalidate();
    }

    public void setLeftText(int stringId) {
        leftText = mContext.getString(stringId);
        invalidate();
    }

    public void setLeftTextColor(int color) {
        leftTextColor = color;
        invalidate();
    }

    public void setLeftImageMarginRight(int dp) {
        leftImageMarginRight = dp2px(mContext, dp);
        invalidate();
    }

    public void setLeftImageMarginLeft(int dp) {
        this.leftImageMarginLeft = dp2px(mContext, dp);
        invalidate();
    }

    public void setLeftTextMarginLeft(int dp) {
        this.leftTextMarginLeft = dp2px(mContext, dp);
        invalidate();
    }

    public void setLeftImage(Bitmap bitmap) {
        leftImage = bitmap;
        invalidate();
    }

    public void setRightImage(Bitmap bitmap) {
        rightImage = bitmap;
        invalidate();
    }

    public void setLeftImageWidthHeight(int width, int Height){
        this.leftImageWidth = width;
        this.leftImageHeight = Height;
        invalidate();
    }

    public void setRightImageWidthHeight(int width, int Height){
        this.rightImageWidth = width;
        this.rightImageHeight = Height;
        invalidate();
    }

    public void setLeftTextSize(int sp) {
        leftTextSize = sp2px(mContext, sp);
        invalidate();
    }

    public void setRightText(String text) {
        rightText = text;
        invalidate();
    }

    public void setRightText(int stringId) {
        rightText = mContext.getString(stringId);
        invalidate();
    }

    public void setRightTextColor(int color) {
        rightTextColor = color;
        invalidate();
    }

    public void setRightTextSize(int sp) {
        leftTextSize = sp2px(mContext, sp);
        invalidate();
    }

    public String getRightText(){
        return rightText;
    }

    public void setRightImageMarginLeft(int dp) {
        rightImageMarginLeft = dp2px(mContext, dp);
        invalidate();
    }

    public void setRightImageMarginRight(int dp) {
        this.rightImageMarginRight = dp2px(mContext, dp);
        invalidate();
    }

    public void setRightTextMarginRight(int dp) {
        this.rightTextMarginRight = dp2px(mContext, dp);
        invalidate();
    }

    public void showLeftImg(boolean flag) {
        isShowLeftImg = flag;
        invalidate();
    }

    public void showLeftText(boolean flag) {
        isShowLeftText = flag;
        invalidate();
    }

    public void showRightImg(boolean flag) {
        isShowRightImg = flag;
        invalidate();
    }

    public void showRightText(boolean flag) {
        isShowRightText = flag;
        invalidate();
    }

    public void setSplitMode(boolean spliteMode) {
        mSplitMode = spliteMode;
    }

    public boolean getSplitMode() {
        return mSplitMode;
    }

    public boolean getIsShowDivideLine(){
        return isShowDivideLine;
    }

    public void setShowDivideLine(Boolean showDivideLine) {
        isShowDivideLine = showDivideLine;
        invalidate();
    }
    public void setDivideLineColor(int color){
        divide_line_color = color;
        invalidate();
    }

    //***********************属性的getter和setter方法*************************************//


    /**
     * 点击条目的事件回调
     */
    private OnOptionItemClickListener listener;

    public interface OnOptionItemClickListener {
        void leftOnClick();

        void centerOnClick();

        void rightOnClick();
    }

    public void setOnOptionItemClickListener(OnOptionItemClickListener listener) {
        this.listener = listener;
    }


    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 将Vector类型的Drawable转换为Bitmap
     * @param vectorDrawableId vector资源id
     * @return bitmap
     */
    private Bitmap decodeVectorToBitmap(int vectorDrawableId ){
        Drawable vectorDrawable = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            vectorDrawable = this.mContext.getDrawable(vectorDrawableId);
        }else {
            vectorDrawable = getResources().getDrawable(vectorDrawableId,null);
        }

        if(vectorDrawable != null){
            //这里若使用Bitmap.Config.RGB565会导致图片资源黑底
            Bitmap b = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            vectorDrawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            vectorDrawable.draw(canvas);
            return b;
        }
        return null;
    }

}
