package com.dmingo.optionbarview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;


/**
 * @description: 条目类型View
 * @author: DMingO
 * @date: 2020/9/3
 */
public class OptionBarView extends View implements Checkable {

    private static final String TAG = "OptionBarView";

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
     * 右图的左边距
     */
    private int rightImageMarginLeft = -1;
    /**
     * 右图的右边距
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
     * 控制整体布局，可复用绘制其他组件
     */
    private Rect optionRect;

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

    /**
     * 左区域的右边界
     */
    private int leftBound;

    /**
     * 右区域的左边界
     */
    private int rightBound;


    private int rightViewType = -1;






    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    public OptionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        ///////////////////////////////////////////////////////////////////////////////////////////////
        //                                                                                           //
        //                         获取自定义属性的值                                                   //
        //                                                                                           //
        ///////////////////////////////////////////////////////////////////////////////////////////////
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionBarView);
        isShowDivideLine = optBoolean(typedArray ,R.styleable.OptionBarView_show_divide_line,false);
        divide_line_top_gravity = optBoolean(typedArray ,R.styleable.OptionBarView_divide_line_top_gravity,false);
        divide_line_color = optColor(typedArray ,R.styleable.OptionBarView_divide_line_color,Color.parseColor("#DCDCDC"));
        divide_line_height = optPixelSize(typedArray , R.styleable.OptionBarView_divide_line_height,dp2px(1));
        divide_line_left_margin = optPixelSize(typedArray , R.styleable.OptionBarView_divide_line_left_margin,dp2px(0));
        divide_line_right_margin = optPixelSize(typedArray , R.styleable.OptionBarView_divide_line_right_margin,dp2px(0));
        leftText = optString(typedArray,R.styleable.OptionBarView_left_text,"");
        rightText = optString(typedArray,R.styleable.OptionBarView_right_text,"");
        title = optString(typedArray,R.styleable.OptionBarView_title,"");
        titleTextSize = optPixelSize(typedArray ,R.styleable.OptionBarView_title_size , sp2px(-1));
        titleTextColor = optColor(typedArray , R.styleable.OptionBarView_title_color , Color.BLACK);
        leftTextMarginLeft = optPixelSize(typedArray,R.styleable.OptionBarView_left_text_margin_left,dp2px(-1));
        rightTextMarginRight = optPixelSize(typedArray,R.styleable.OptionBarView_right_text_margin_right,dp2px(-1));
        leftImageWidth = optPixelSize(typedArray ,R.styleable.OptionBarView_left_src_height , dp2px(-1));
        rightImageWidth = optPixelSize(typedArray ,R.styleable.OptionBarView_right_src_height , dp2px(-1));
        leftImageHeight = optPixelSize(typedArray ,R.styleable.OptionBarView_left_src_width , dp2px(-1));
        rightImageHeight = optPixelSize(typedArray ,R.styleable.OptionBarView_right_src_width , dp2px(-1));
        rightTextSize = optPixelSize(typedArray ,R.styleable.OptionBarView_right_text_size ,sp2px(16));
        leftTextSize = optPixelSize(typedArray ,R.styleable.OptionBarView_left_text_size ,sp2px(16));
        leftImageMarginLeft = optPixelSize(typedArray , R.styleable.OptionBarView_left_image_margin_left,dp2px(-1));
        rightImageMarginLeft = optPixelSize(typedArray , R.styleable.OptionBarView_right_image_margin_left,dp2px(-1));
        leftImageMarginRight = optPixelSize(typedArray , R.styleable.OptionBarView_left_image_margin_right,dp2px(-1));
        rightImageMarginRight = optPixelSize(typedArray , R.styleable.OptionBarView_right_image_margin_right,dp2px(-1));
        leftTextColor = optColor(typedArray,R.styleable.OptionBarView_left_text_color , Color.BLACK);
        rightTextColor = optColor(typedArray,R.styleable.OptionBarView_right_text_color, Color.BLACK);
        mSplitMode = optBoolean(typedArray,R.styleable.OptionBarView_split_mode,false);
        rightViewType = optInt(typedArray,R.styleable.OptionBarView_rightViewType,-1);
        leftImage = BitmapFactory.decodeResource(getResources(), optResourceId(typedArray,R.styleable.OptionBarView_left_src,0));
        //需要加载Vector资源
        if (leftImage == null) {
            Bitmap vectorBitmap = decodeVectorToBitmap(optResourceId(typedArray,R.styleable.OptionBarView_left_src,0));
            if (vectorBitmap != null) {
                leftImage = vectorBitmap;
            }
        }
        if(rightViewType == RightViewType.IMAGE) {
            rightImage = BitmapFactory.decodeResource(getResources(), optResourceId(typedArray,R.styleable.OptionBarView_right_src,0));
            //需要加载Vector资源
            if (rightImage == null) {
                Bitmap vectorBitmap = decodeVectorToBitmap(optResourceId(typedArray,R.styleable.OptionBarView_right_src,0));
                if (vectorBitmap != null) {
                    rightImage = vectorBitmap;
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////
        //                                                                                           //
        //                          Switch 获取初始参数                                                //
        //                                                                                           //
        ///////////////////////////////////////////////////////////////////////////////////////////////
        if(rightViewType == RightViewType.SWITCH){
            switchBackgroundWidth = optPixelSize(typedArray,R.styleable.OptionBarView_switch_background_width,dp2px(45f));

            switchBackgroundHeight = optPixelSize(typedArray,R.styleable.OptionBarView_switch_background_height,dp2px(25f));

            switchShadowEffect = optBoolean(typedArray,
                    R.styleable.OptionBarView_switch_shadow_effect,
                    true);

            uncheckSwitchCircleColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_uncheckcircle_color,
                    0XffAAAAAA);
            uncheckCircleWidth = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_uncheckcircle_width,
                    dp2px(1.5f));
            uncheckCircleOffsetX = dp2px(10);
            uncheckSwitchCircleRadius = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_uncheckcircle_radius,
                    dp2px(4));//dp2px(4);
            switchCheckedLineOffsetX = dp2px(4);
            switchCheckedLineOffsetY = dp2px(4);
            switchShadowRadius = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_shadow_radius,
                    dp2px(2.5f));//dp2px(2.5f);

            switchShadowOffset = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_shadow_offset,
                    dp2px(1.5f));//dp2px(1.5f);

            switchShadowColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_shadow_color,
                    0X33000000);//0X33000000;

            uncheckColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_uncheck_color,
                    0XffDDDDDD);//0XffDDDDDD;

            switchCheckedColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_checked_color,
                    0Xff51d367);//0Xff51d367;

            switchBorderWidth = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_border_width,
                    dp2px(1));//dp2px(1);

            checkIndicatorLineColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_checkline_color,
                    Color.TRANSPARENT);//Color.TRANSPARENT;

            checkIndicatorLineWidth = optPixelSize(typedArray,
                    R.styleable.OptionBarView_switch_checkline_width,
                    dp2px(1f));//dp2px(1.0f);

            checkLineLength = dp2px(6);

            int buttonColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_button_color,
                    Color.WHITE);//Color.WHITE;
            //未选和已选的圆形按钮颜色默认情况都是白色
            uncheckButtonColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_uncheckbutton_color,
                    buttonColor);

            checkedButtonColor = optColor(typedArray,
                    R.styleable.OptionBarView_switch_checkedbutton_color,
                    buttonColor);

            int effectDuration = optInt(typedArray,
                    R.styleable.OptionBarView_switch_effect_duration,
                    300);//300;

            isSwitchChecked = optBoolean(typedArray,
                    R.styleable.OptionBarView_switch_checked,
                    false);

            showSwitchIndicator = optBoolean(typedArray,
                    R.styleable.OptionBarView_switch_show_indicator,
                    true);

            uncheckSwitchBackground = optColor(typedArray,
                    R.styleable.OptionBarView_switch_background,
                    Color.WHITE);//Color.WHITE;

            enableSwitchAnimate = optBoolean(typedArray,
                    R.styleable.OptionBarView_switch_enable_effect,
                    true);

            switchBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            switchButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            switchButtonPaint.setColor(buttonColor);

            if(switchShadowEffect){
                switchButtonPaint.setShadowLayer(
                        switchShadowRadius,
                        0, switchShadowOffset,
                        switchShadowColor);
            }
            //创建一个Switch的背景的圆角矩形
            switchBackgroundRect = new RectF();

            //设置切换前后状态动画记录变量
            switchCurrentViewState = new ViewState();
            beforeState = new ViewState();
            afterState = new ViewState();

            //初始化动画执行器
            switchValueAnimator = ValueAnimator.ofFloat(0f, 1f);
            switchValueAnimator.setDuration(effectDuration);
            switchValueAnimator.setRepeatCount(0);

            switchValueAnimator.addUpdateListener(animatorUpdateListener);
            switchValueAnimator.addListener(switchAnimatorListener);

            super.setClickable(true);
            this.setPadding(0, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        }



        //参数获取完毕后 回收typeArray
        typedArray.recycle();

        optionRect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        // 计算了描绘字体需要的范围
        mPaint.getTextBounds(title, 0, title.length(), mTextBound);

        //右侧图片非空则设置右侧为图片类型
        if(rightImage != null){
            rightViewType = RightViewType.IMAGE;
        }

        //初始化 分割线 画笔
        dividePaint = new Paint();
        dividePaint.setColor(divide_line_color);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(rightViewType == RightViewType.SWITCH){
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if(widthMode == MeasureSpec.UNSPECIFIED
                    || widthMode == MeasureSpec.AT_MOST){
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_SWITCH_BACKGROUND_WIDTH, MeasureSpec.EXACTLY);
            }
            if(heightMode == MeasureSpec.UNSPECIFIED
                    || heightMode == MeasureSpec.AT_MOST){
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_SWITCH_BACKGROUND_HEIGHT, MeasureSpec.EXACTLY);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //右侧View为 Switch状态
        if(rightViewType == RightViewType.SWITCH){
            float viewPadding = Math.max(switchShadowRadius + switchShadowOffset, switchBorderWidth);
            switchBackgroundRight = w - (rightImageMarginRight >= 0 ? rightImageMarginRight : (float)mWidth / 32)-viewPadding;
            switchBackgroundLeft = switchBackgroundRight - switchBackgroundWidth + viewPadding;
            switchBackgroundTop = (float) ( h - switchBackgroundHeight) / 2 + viewPadding;
            switchBackgroundBottom = switchBackgroundHeight + switchBackgroundTop - viewPadding;
            //计算背景圆弧的半径
            viewRadius = (switchBackgroundBottom - switchBackgroundTop) * .5f;

            //按钮的半径
            buttonRadius = viewRadius - switchBorderWidth;


            centerX = (switchBackgroundLeft+ switchBackgroundRight) * .5f;
            centerY = (switchBackgroundTop + switchBackgroundBottom) * .5f;

            switchButtonMinX = switchBackgroundLeft+ viewRadius;
            switchButtonMaxX = switchBackgroundRight - viewRadius;

            if(isChecked()){
                setCheckedViewState(switchCurrentViewState);
            }else{
                setUncheckViewState(switchCurrentViewState);
            }

            isSwitchInit = true;

            postInvalidate();
        }
    }





    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        leftBound = 0;
        rightBound = Integer.MAX_VALUE;

        //抗锯齿处理
        canvas.setDrawFilter(paintFlagsDrawFilter);

        optionRect.left = getPaddingLeft();
        optionRect.right = mWidth - getPaddingRight();
        optionRect.top = getPaddingTop();
        optionRect.bottom = mHeight - getPaddingBottom();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(titleTextSize > leftTextSize ? Math.max(titleTextSize, rightTextSize) : Math.max(leftTextSize, rightTextSize));
//        mPaint.setTextSize(titleTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        //文字水平居中
        mPaint.setTextAlign(Paint.Align.CENTER);

        //计算垂直居中baseline
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int baseLine = (int) ((optionRect.bottom + optionRect.top - fontMetrics.bottom - fontMetrics.top) / 2);

        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline = optionRect.centerY()+distance;

        if (!title.trim().equals("")) {
            // 正常情况，将字体居中
            mPaint.setColor(titleTextColor);
            canvas.drawText(title, optionRect.centerX(), baseline, mPaint);
            optionRect.bottom -= mTextBound.height();
        }


        if (leftImage != null && isShowLeftImg) {
            // 计算左图范围
            optionRect.left = leftImageMarginLeft >= 0 ? leftImageMarginLeft : mWidth / 32;
            //计算 左右边界坐标值，若有设置左图偏移则使用，否则使用View的宽度/32
            if(leftImageWidth >= 0){
                optionRect.right = optionRect.left + leftImageWidth;
            }else {
                optionRect.right = optionRect.right + mHeight / 2;
            }
            //计算左图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if(leftImageHeight >= 0){
                optionRect.top = ( mHeight - leftImageHeight) / 2;
                optionRect.bottom = leftImageHeight + optionRect.top;
            }else {
                optionRect.top = mHeight / 4;
                optionRect.bottom = mHeight * 3 / 4;
            }
            canvas.drawBitmap(leftImage, null, optionRect, mPaint);

            //有左侧图片，更新左区域的边界
            leftBound =  Math.max(leftBound ,optionRect.right);
        }
        if (rightImage != null && isShowRightImg && rightViewType == RightViewType.IMAGE) {
            // 计算右图范围
            //计算 左右边界坐标值，若有设置右图偏移则使用，否则使用View的宽度/32
            optionRect.right = mWidth - (rightImageMarginRight >= 0 ? rightImageMarginRight : mWidth / 32);
            if(rightImageWidth >= 0){
                optionRect.left = optionRect.right - rightImageWidth;
            }else {
                optionRect.left = optionRect.right - mHeight / 2;
            }
            //计算右图 上下边界的坐标值，若无设置右图高度，默认为高度的 1/2
            if(rightImageHeight >= 0){
                optionRect.top = ( mHeight - rightImageHeight) / 2;
                optionRect.bottom = rightImageHeight + optionRect.top;
            }else {
                optionRect.top = mHeight / 4;
                optionRect.bottom = mHeight * 3 / 4;
            }
            canvas.drawBitmap(rightImage, null, optionRect, mPaint);

            //右侧图片，更新右区域边界
            rightBound = Math.min(rightBound , optionRect.left);
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
            // 计算了描绘字体需要的范围
            mPaint.getTextBounds(leftText, 0, leftText.length(), mTextBound);

            canvas.drawText(leftText, w, baseline, mPaint);
            //有左侧文字，更新左区域的边界
            leftBound = Math.max(w + mTextBound.width() , leftBound);
        }
        if (rightText != null && !rightText.equals("") && isShowRightText) {
            mPaint.setTextSize(rightTextSize);
            mPaint.setColor(rightTextColor);

            int w = mWidth;
            //文字右侧有View
            if (rightViewType != -1) {
                w -= rightImageMarginRight >= 0 ? rightImageMarginRight : (mHeight / 8);//增加右图右间距
                w -= rightImageMarginLeft >= 0 ? rightImageMarginLeft : (mWidth / 32);//增加右图左间距
                w -= Math.max(rightTextMarginRight, 0);//增加右字右间距
                //扣去右侧View的宽度
                if(rightViewType == RightViewType.IMAGE){
                    w -= (optionRect.right - optionRect.left);
                }else if(rightViewType == RightViewType.SWITCH){
                    w -= (switchBackgroundRight - switchBackgroundLeft + viewRadius * .5f);
                }
            } else {
                w -= rightTextMarginRight >= 0 ? rightTextMarginRight : (mWidth / 32);//增加右字右间距
            }

            // 计算了描绘字体需要的范围
            mPaint.getTextBounds(rightText, 0, rightText.length(), mTextBound);
            canvas.drawText(rightText, w - mTextBound.width(), baseline, mPaint);

            //有右侧文字，更新右边区域边界
            rightBound = Math.min(rightBound , w - mTextBound.width());
        }

        //处理分隔线部分
        if(isShowDivideLine){
            int left = divide_line_left_margin;
            int right = mWidth - divide_line_right_margin;
            //绘制分割线时，高度默认为 1px
            if(divide_line_height <= 0){
                divide_line_height = 1;
            }
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

        //判断绘制 Switch
        if(rightViewType == RightViewType.SWITCH){
            //边框宽度
            switchBackgroundPaint.setStrokeWidth(switchBorderWidth);
            switchBackgroundPaint.setStyle(Paint.Style.FILL);

            //绘制关闭状态的背景
            switchBackgroundPaint.setColor(uncheckSwitchBackground);
            drawRoundRect(canvas,
                    switchBackgroundLeft, switchBackgroundTop, switchBackgroundRight, switchBackgroundBottom,
                    viewRadius, switchBackgroundPaint);
            //绘制关闭状态的边框
            switchBackgroundPaint.setStyle(Paint.Style.STROKE);
            switchBackgroundPaint.setColor(uncheckColor);
            drawRoundRect(canvas,
                    switchBackgroundLeft, switchBackgroundTop, switchBackgroundRight, switchBackgroundBottom,
                    viewRadius, switchBackgroundPaint);

            //绘制未选中时的指示器小圆圈
            if(showSwitchIndicator){
                drawUncheckIndicator(canvas);
            }

            //绘制开启时的背景色
            float des = switchCurrentViewState.radius * .5f;//[0-backgroundRadius*0.5f]
            switchBackgroundPaint.setStyle(Paint.Style.STROKE);
            switchBackgroundPaint.setColor(switchCurrentViewState.checkStateColor);
            switchBackgroundPaint.setStrokeWidth(switchBorderWidth + des * 2f);
            drawRoundRect(canvas,
                    switchBackgroundLeft+ des, switchBackgroundTop + des, switchBackgroundRight - des, switchBackgroundBottom - des,
                    viewRadius, switchBackgroundPaint);

            //绘制按钮左边的长条遮挡
            switchBackgroundPaint.setStyle(Paint.Style.FILL);
            switchBackgroundPaint.setStrokeWidth(1);
            drawArc(canvas,
                    switchBackgroundLeft, switchBackgroundTop,
                    switchBackgroundLeft+ 2 * viewRadius, switchBackgroundTop + 2 * viewRadius,
                    90, 180, switchBackgroundPaint);
            canvas.drawRect(
                    switchBackgroundLeft+ viewRadius, switchBackgroundTop,
                    switchCurrentViewState.buttonX, switchBackgroundTop + 2 * viewRadius,
                    switchBackgroundPaint);

            //绘制Switch的小线条
            if(showSwitchIndicator){
                drawCheckedIndicator(canvas);
            }

            //绘制Switch的按钮
            drawButton(canvas, switchCurrentViewState.buttonX, centerY);

            //更新右侧区域的边界
            rightBound = Math.min(rightBound , (int)switchBackgroundLeft);
        }

        //视图绘制后，计算 左区域的边界 以及 右区域的边界
        leftBound += 5;
        if(rightBound < mWidth / 2){
            rightBound = mWidth /2 + 5;
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
//                if (eventX < mWidth / 8) {
//                    leftStartTouchDown = true;
//                } else if (eventX > mWidth * 7 / 8) {
//                    rightStartTouchDown = true;
//                } else {
//                    centerStartTouchDown = true;
//                }
                if (eventX <= leftBound) {
                    leftStartTouchDown = true;
                } else if (eventX >= rightBound ) {
                    rightStartTouchDown = true;
                } else {
                    centerStartTouchDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                if (leftStartTouchDown && x <= leftBound && listener != null) {
                    listener.leftOnClick();
                } else if (rightStartTouchDown && x  >= rightBound && listener != null) {
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
        /*
         * 当右侧View 为 Switch
         */
        if(rightViewType == RightViewType.SWITCH){
            int actionMasked = event.getActionMasked();

            switch (actionMasked){
                case MotionEvent.ACTION_DOWN:{
                    //只触摸到Switch上判定为即将拖动
                    if(event.getX() > switchBackgroundLeft&& event.getX() < switchBackgroundRight){
                        isSwitchTouchingDown = true;
                        touchDownTime = System.currentTimeMillis();
                        //取消准备进入拖动状态
                        removeCallbacks(postPendingDrag);
                        //预设100ms进入拖动状态
                        postDelayed(postPendingDrag, 100);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    float eventX = event.getX();
                    if(isSwitchPendingDragState()){
                        //在准备进入拖动状态过程中，可以拖动按钮位置
                        float fraction = (eventX-switchBackgroundLeft) /(switchBackgroundRight - switchBackgroundLeft);
                        fraction = Math.max(0f, Math.min(1f, fraction));
                        //计算拖动后的圆形按钮的中心x
                        switchCurrentViewState.buttonX = switchButtonMinX
                                + (switchButtonMaxX - switchButtonMinX) * fraction;
                    }else if(isSwitchDragState()){
                        //拖动按钮位置，同时改变对应的背景颜色
                        //计算出滑动的比例？
                        float fraction =  (eventX-switchBackgroundLeft) / (switchBackgroundRight - switchBackgroundLeft);
                        fraction = Math.max(0f, Math.min(1f, fraction));

                        switchCurrentViewState.buttonX = switchButtonMinX
                                + (switchButtonMaxX - switchButtonMinX) * fraction;

                        switchCurrentViewState.checkStateColor = (int) argbEvaluator.evaluate(
                                fraction,
                                uncheckColor,
                                switchCheckedColor
                        );
                        postInvalidate();

                    }
                    break;
                }
                case MotionEvent.ACTION_UP:{
                    isSwitchTouchingDown = false;
                    //取消准备进入拖动状态
                    removeCallbacks(postPendingDrag);

                    if(System.currentTimeMillis() - touchDownTime <= 350){
                        //点击时间小于300ms，认为是点击操作
                        toggle();
                    }else if(isSwitchPendingDragState()){
                        //在准备进入拖动状态过程中就抬起了手指，Switch复位
                        pendingCancelDragState();
                    }else if(isSwitchDragState()){
                        //正在拖动状态抬起了手指，计算按钮位置，设置是否切换状态
                        float eventX = event.getX();
                        float fraction = (eventX-switchBackgroundLeft) / (switchBackgroundRight - switchBackgroundLeft);
                        fraction = Math.max(0f, Math.min(1f, fraction));
                        //是否滑动过了一半
                        boolean newCheck = fraction > 0.5f;

                        if(newCheck == isChecked()){
                            pendingCancelDragState();
                        }else{

                            pendingSettleState(newCheck);
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_CANCEL:{
                    isSwitchTouchingDown = false;

                    removeCallbacks(postPendingDrag);

                    if(isSwitchPendingDragState() || isSwitchDragState()){
                        //复位
                        pendingCancelDragState();
                    }
                    break;
                }
            }
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
        titleTextSize = sp2px(sp);
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
        leftImageMarginRight = dp2px(dp);
        invalidate();
    }

    public void setLeftImageMarginLeft(int dp) {
        this.leftImageMarginLeft = dp2px(dp);
        invalidate();
    }

    public void setLeftTextMarginLeft(int dp) {
        this.leftTextMarginLeft = dp2px(dp);
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
        leftTextSize = sp2px(sp);
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
        leftTextSize = sp2px( sp);
        invalidate();
    }

    public String getRightText(){
        return rightText;
    }

    public void setRightImageMarginLeft(int dp) {
        rightImageMarginLeft = dp2px(dp);
        invalidate();
    }

    public void setRightImageMarginRight(int dp) {
        this.rightImageMarginRight = dp2px(dp);
        invalidate();
    }

    public void setRightTextMarginRight(int dp) {
        this.rightTextMarginRight = dp2px(dp);
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

    public void setOnSwitchCheckedChangeListener(OnSwitchCheckedChangeListener l){
        onSwitchCheckedChangeListener = l;
    }



    //***********************属性的getter和setter方法*************************************//




    //********************************  Switch 部分的方法***********************************//
    //Switch模块的属性
    private  final int DEFAULT_SWITCH_BACKGROUND_WIDTH = dp2px(45);
    private  final int DEFAULT_SWITCH_BACKGROUND_HEIGHT = dp2px(25);
    /**
     * 动画状态：
     * 0.静止
     * 1.进入拖动
     * 2.处于拖动
     * 3.拖动-复位
     * 4.拖动-切换
     * 5.点击切换
     * **/
    private final int ANIMATE_STATE_NONE = 0;
    private final int ANIMATE_STATE_PENDING_DRAG = 1;
    private final int ANIMATE_STATE_DRAGING = 2;
    private final int ANIMATE_STATE_PENDING_RESET = 3;
    private final int ANIMATE_STATE_PENDING_SETTLE = 4;
    private final int ANIMATE_STATE_SWITCH = 5;

    /**
     * 阴影半径
     */
    private int switchShadowRadius;
    /**
     * 阴影Y偏移px
     */
    private int switchShadowOffset;
    /**
     * 阴影颜色
     */
    private int switchShadowColor ;

    /**
     * 背景半径
     */
    private float viewRadius;
    /**
     * 按钮半径
     */
    private float buttonRadius;
    /**
     * 背景宽的值
     */
    private float switchBackgroundWidth;
    /**
     * 背景高的值
     */
    private float switchBackgroundHeight;

    /**
     * 背景位置 坐标
     */
    private float switchBackgroundLeft;
    private float switchBackgroundTop    ;
    private float switchBackgroundRight  ;
    private float switchBackgroundBottom ;
    private float centerX;
    private float centerY;

    /**
     * 关闭后的背景底色
     */
    private int uncheckSwitchBackground;
    /**
     * 背景关闭颜色
     */
    private int uncheckColor;
    /**
     * 背景打开颜色
     */
    private int switchCheckedColor;
    /**
     * 边框宽度px
     */
    private int switchBorderWidth;

    /**
     * 打开后的指示线颜色
     */
    private int checkIndicatorLineColor;
    /**
     * 打开后的指示线宽
     */
    private int checkIndicatorLineWidth;
    /**
     * 打开后的指示线长
     */
    private float checkLineLength;
    /**
     *打开后的指示线位移X
     */
    private float switchCheckedLineOffsetX;
    /**
     *打开后的指示线位移Y
     */
    private float switchCheckedLineOffsetY;
    
    /**
     * 关闭后的圆圈的颜色
     */
    private int uncheckSwitchCircleColor;
    /**
     *关闭圆圈线宽
     */
    private int uncheckCircleWidth;
    /**
     *关闭后的圆圈位移X
     */
    private float uncheckCircleOffsetX;
    /**
     *关闭后的圆圈半径
     */
    private float uncheckSwitchCircleRadius;

    /**
     * 关闭时的圆形按钮的颜色
     */
    private int uncheckButtonColor;
    /**
     * 打开时的圆形按钮的颜色
     */
    private int checkedButtonColor;


    /**
     * 圆形按钮最左边坐标
     */
    private float switchButtonMinX;
    /**
     * 圆形按钮最右边的坐标
     */
    private float switchButtonMaxX;

    /**
     * 按钮画笔
     */
    private Paint switchButtonPaint;
    /**
     * Switch的背景画笔
     */
    private Paint switchBackgroundPaint;

    /**
     * 记录当前状态
     */
    private ViewState switchCurrentViewState;
    private ViewState beforeState;
    private ViewState afterState;

    private RectF switchBackgroundRect;

    /**
     * 动画状态
     */
    private int animateState = ANIMATE_STATE_NONE;

    /**
     *
     */
    private ValueAnimator switchValueAnimator;

    private final android.animation.ArgbEvaluator argbEvaluator
            = new android.animation.ArgbEvaluator();

    /**
     *是否选中
     */
    private boolean isSwitchChecked;
    /**
     * 是否启用动画
     */
    private boolean enableSwitchAnimate;
    /**
     * 是否启用阴影效果
     */
    private boolean switchShadowEffect;
    /**
     * 是否显示指示器
     */
    private boolean showSwitchIndicator;
    /**
     * 是否按下
     */
    private boolean isSwitchTouchingDown = false;
    /**
     *
     */
    private boolean isSwitchInit = false;
    /**
     * Switch是否可以进行状态回调
     */
    private boolean isSwitchStateChangeCallback = false;

    /**
     * 回调接口
     */
    private OnSwitchCheckedChangeListener onSwitchCheckedChangeListener;


    /**
     * 手势按下的时刻
     */
    private long touchDownTime;

    private Runnable postPendingDrag = new Runnable() {
        @Override
        public void run() {
            if(!isSwitchInAnimating()){
                pendingDragState();
            }
        }
    };

    /**
     * Switch UI的变化核心,更新UI的状态
     */
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {

            float value = (Float) animation.getAnimatedValue();

            switch (animateState) {
                //拖动停止后   --属性动画
                case ANIMATE_STATE_PENDING_SETTLE:
                //拖动重置恢复 --属性动画
                case ANIMATE_STATE_PENDING_RESET: {
                    //中间的check线颜色变化
                    switchCurrentViewState.checkedLineColor = (int) argbEvaluator.evaluate(
                            value,
                            beforeState.checkedLineColor,
                            afterState.checkedLineColor
                    );

                    switchCurrentViewState.radius = beforeState.radius
                            + (afterState.radius - beforeState.radius) * value;

                    if(animateState != ANIMATE_STATE_PENDING_DRAG){
                        switchCurrentViewState.buttonX = beforeState.buttonX
                                + (afterState.buttonX - beforeState.buttonX) * value;
                    }
                    //已选状态的颜色
                    switchCurrentViewState.checkStateColor = (int) argbEvaluator.evaluate(
                            value,
                            beforeState.checkStateColor,
                            afterState.checkStateColor
                    );
                    break;
                }
                case ANIMATE_STATE_SWITCH: {
                    switchCurrentViewState.buttonX = beforeState.buttonX
                            + (afterState.buttonX - beforeState.buttonX) * value;

                    float fraction = (switchCurrentViewState.buttonX - switchButtonMinX) / (switchButtonMaxX - switchButtonMinX);

                    switchCurrentViewState.checkStateColor = (int) argbEvaluator.evaluate(
                            fraction,
                            uncheckColor,
                            switchCheckedColor
                    );

                    switchCurrentViewState.radius = fraction * viewRadius;
                    switchCurrentViewState.checkedLineColor = (int) argbEvaluator.evaluate(
                            fraction,
                            Color.TRANSPARENT,
                            checkIndicatorLineColor
                    );
                    break;
                }
                case ANIMATE_STATE_DRAGING:{
                    Log.i(TAG, "onAnimationUpdate: ANIMATE_STATE_DRAGING");
                    break;
                }
                case ANIMATE_STATE_NONE: {
                    Log.i(TAG, "onAnimationUpdate:  ANIMATE_STATE_NONE ");
                    break;
                }
                default:
                    break;
            }
            postInvalidate();
        }
    };

    private Animator.AnimatorListener switchAnimatorListener
            = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            switch (animateState) {
                case ANIMATE_STATE_PENDING_DRAG: {
                    animateState = ANIMATE_STATE_DRAGING;
                    switchCurrentViewState.checkedLineColor = Color.TRANSPARENT;
                    switchCurrentViewState.radius = viewRadius;
                    postInvalidate();
                    break;
                }
                case ANIMATE_STATE_PENDING_RESET: {
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    break;
                }
                case ANIMATE_STATE_PENDING_SETTLE: {
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    broadcastEvent();
                    break;
                }
                case ANIMATE_STATE_SWITCH: {
                    isSwitchChecked = !isSwitchChecked;
                    animateState = ANIMATE_STATE_NONE;
                    postInvalidate();
                    broadcastEvent();
                    break;
                }
                default:
                case ANIMATE_STATE_NONE: {
                    break;
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    /**
     * 是否在动画状态
     * @return 是否在动画状态
     */
    private boolean isSwitchInAnimating(){
        return animateState != ANIMATE_STATE_NONE;
    }

    /**
     * 是否在进入拖动或离开拖动状态
     * @return 是否在进入拖动或离开拖动状态
     */
    private boolean isSwitchPendingDragState(){
        return animateState == ANIMATE_STATE_PENDING_DRAG
                || animateState == ANIMATE_STATE_PENDING_RESET;
    }

    /**
     * 是否在手指拖动状态
     * @return 是否在手指拖动状态
     */
    private boolean isSwitchDragState(){
        return animateState == ANIMATE_STATE_DRAGING;
    }

    /**
     * 设置是否启用阴影效果
     * @param switchShadowEffect true.启用
     */
    public void setShadowEffect(boolean switchShadowEffect) {
        if(this.switchShadowEffect == switchShadowEffect){return;}
        this.switchShadowEffect = switchShadowEffect;

        if(this.switchShadowEffect){
            switchButtonPaint.setShadowLayer(
                    switchShadowRadius,
                    0, switchShadowOffset,
                    switchShadowColor);
        }else{
            switchButtonPaint.setShadowLayer(
                    0,
                    0, 0,
                    0);
        }
    }

    public void setEnableEffect(boolean enable){
        this.enableSwitchAnimate = enable;
    }

    /**
     * 开始进入拖动状态
     */
    private void pendingDragState() {
        if(isSwitchInAnimating()){return;}
        if(!isSwitchTouchingDown){return;}

        if(switchValueAnimator.isRunning()){
            switchValueAnimator.cancel();
        }

        animateState = ANIMATE_STATE_PENDING_DRAG;

        beforeState.copy(switchCurrentViewState);
        afterState.copy(switchCurrentViewState);

        if(isChecked()){
            afterState.checkStateColor = switchCheckedColor;
            afterState.buttonX = switchButtonMaxX;
            afterState.checkedLineColor = switchCheckedColor;
        }else{
            afterState.checkStateColor = uncheckColor;
            afterState.buttonX = switchButtonMinX;
            afterState.radius = viewRadius;
        }

        switchValueAnimator.start();
    }


    /**
     * 取消拖动状态
     */
    private void pendingCancelDragState() {
        if(isSwitchDragState() || isSwitchPendingDragState()){
            if(switchValueAnimator.isRunning()){
                switchValueAnimator.cancel();
            }

            animateState = ANIMATE_STATE_PENDING_RESET;
            beforeState.copy(switchCurrentViewState);

            //开启动画，恢复到原来的状态
            if(isChecked()){
                setCheckedViewState(afterState);
            }else{
                setUncheckViewState(afterState);
            }
            switchValueAnimator.start();
        }
    }


    /**
     * 准备切换到新的状态
     */
    private void pendingSettleState(boolean newCheck) {
        if(switchValueAnimator.isRunning()){
            switchValueAnimator.cancel();
        }

        animateState = ANIMATE_STATE_PENDING_SETTLE;
        beforeState.copy(switchCurrentViewState);
        isSwitchChecked = newCheck;
        if(isChecked()){
            setCheckedViewState(afterState);
        }else{
            setUncheckViewState(afterState);
        }
        switchValueAnimator.start();

    }


    /**
     * 切换到指定状态
     * @param checked 状态
     */
    @Override
    public void setChecked(boolean checked) {
        if(rightViewType != RightViewType.SWITCH){
            return;
        }
        //与当前状态相同则直接刷新
        if(checked == isChecked()){
            postInvalidate();
            return;
        }
        //切换状态
        toggle(enableSwitchAnimate, false);
    }


    @Override
    public boolean isChecked() {
        if(rightViewType == RightViewType.SWITCH){
            return  isSwitchChecked;
        }
        return false;
    }

    @Override
    public void toggle() {
        toggle(true);
    }

    /**
     * 切换到对立的状态
     * @param animate 是否带动画
     */
    public void toggle(boolean animate) {
        toggle(animate, true);
    }

    private void toggle(boolean animate, boolean broadcast) {
        if(rightViewType != RightViewType.SWITCH){return;}

        if(!isEnabled()){return;}

        if(isSwitchStateChangeCallback){
            throw new RuntimeException("You should not switch state in [onCheckedChanged]");
        }
        //未初始 Switch UI
        if(!isSwitchInit){
            isSwitchChecked = ! isSwitchChecked;
            if(broadcast){
                broadcastEvent();
            }
            return;
        }

        if(switchValueAnimator.isRunning()){
            switchValueAnimator.cancel();
        }

        if(!enableSwitchAnimate || !animate){
            isSwitchChecked = !isSwitchChecked;
            if(isChecked()){
                setCheckedViewState(switchCurrentViewState);
            }else{
                setUncheckViewState(switchCurrentViewState);
            }
            postInvalidate();
            if(broadcast){
                broadcastEvent();
            }
            return;
        }

        animateState = ANIMATE_STATE_SWITCH;
        beforeState.copy(switchCurrentViewState);

        if(isChecked()){
            //当前是 checked 则切换到 unchecked
            setUncheckViewState(afterState);
        }else{
            setCheckedViewState(afterState);
        }
        switchValueAnimator.start();
    }

    /**
     * ※切换到 非选中状态 ，记录状态值
     * @param switchCurrentViewState
     */
    private void setUncheckViewState(ViewState switchCurrentViewState){
        switchCurrentViewState.radius = 0;
        switchCurrentViewState.checkStateColor = uncheckColor;
        switchCurrentViewState.checkedLineColor = Color.TRANSPARENT;
        switchCurrentViewState.buttonX = switchButtonMinX;
        switchButtonPaint.setColor(uncheckButtonColor);
    }

    /**
     * ※切换到 选中状态 ，记录状态
     * @param switchCurrentViewState
     */
    private void setCheckedViewState(ViewState switchCurrentViewState){
        switchCurrentViewState.radius = viewRadius;
        switchCurrentViewState.checkStateColor = switchCheckedColor;
        switchCurrentViewState.checkedLineColor = checkIndicatorLineColor;
        switchCurrentViewState.buttonX = switchButtonMaxX;
        switchButtonPaint.setColor(checkedButtonColor);
    }


    /**
     * 回调Switch状态改变事件
     */
    private void broadcastEvent() {
        if(onSwitchCheckedChangeListener != null){
            isSwitchStateChangeCallback = true;
            onSwitchCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
        isSwitchStateChangeCallback = false;
    }


    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(0,0,0,0);
    }


    /**
     * 绘制选中状态指示器
     * @param canvas
     */
    protected void drawCheckedIndicator(Canvas canvas) {
        drawCheckedIndicator(canvas,
                switchCurrentViewState.checkedLineColor,
                checkIndicatorLineWidth,
                switchBackgroundLeft + viewRadius - switchCheckedLineOffsetX, centerY - checkLineLength,
                switchBackgroundLeft + viewRadius - switchCheckedLineOffsetY, centerY + checkLineLength,
                switchBackgroundPaint);
    }


    /**
     * 绘制选中状态指示器
     * @param canvas
     * @param color
     * @param lineWidth
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param switchBackgroundPaint
     */
    protected void drawCheckedIndicator(Canvas canvas, int color, float lineWidth,
                                        float sx, float sy, float ex, float ey, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(sx, sy, ex, ey, paint);
    }


    /**
     * 绘制关闭状态指示器
     * @param canvas
     */
    private void drawUncheckIndicator(Canvas canvas) {
        drawUncheckIndicator(canvas,
                uncheckSwitchCircleColor,
                uncheckCircleWidth,
                switchBackgroundRight - uncheckCircleOffsetX, centerY,
                uncheckSwitchCircleRadius,
                switchBackgroundPaint);
    }

    protected void drawUncheckIndicator(Canvas canvas,
                                        int color,
                                        float lineWidth,
                                        float centerX, float centerY,
                                        float radius,
                                        Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(lineWidth);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    /**
     * 绘制圆弧
     *
     * @param canvas
     * @param switchBackgroundLeft 背景左顶点坐标
     * @param top
     * @param right
     * @param bottom
     * @param startAngle
     * @param sweepAngle
     * @param paint
     */
    private void drawArc(Canvas canvas,
                         float switchBackgroundLeft, float top,
                         float right, float bottom,
                         float startAngle, float sweepAngle,
                         Paint paint){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(switchBackgroundLeft, top, right, bottom,
                    startAngle, sweepAngle, true, paint);
        }else{
            switchBackgroundRect.set(switchBackgroundLeft, top, right, bottom);
            canvas.drawArc(switchBackgroundRect,
                    startAngle, sweepAngle, true, paint);
        }
    }

    /**
     * 绘制带圆弧的矩形
     * @param canvas
     * @param switchBackgroundLeft
     * @param top
     * @param right
     * @param bottom
     * @param backgroundRadius
     * @param paint
     */
    private void drawRoundRect(Canvas canvas,
                               float switchBackgroundLeft, float top,
                               float right, float bottom,
                               float backgroundRadius,
                               Paint paint){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(switchBackgroundLeft, top, right, bottom,
                    backgroundRadius, backgroundRadius, paint);

        }else{
            switchBackgroundRect.set(switchBackgroundLeft, top, right, bottom);
            canvas.drawRoundRect(switchBackgroundRect,
                    backgroundRadius, backgroundRadius, paint);
        }
    }


    /**
     * 绘制圆形按钮
     * @param canvas
     * @param x px
     * @param y px
     */
    private void drawButton(Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, buttonRadius, switchButtonPaint);

        switchBackgroundPaint.setStyle(Paint.Style.STROKE);
        switchBackgroundPaint.setStrokeWidth(1);
        switchBackgroundPaint.setColor(0XffDDDDDD);
        canvas.drawCircle(x, y, buttonRadius, switchBackgroundPaint);
    }



    /**
     * 保存Switch的状态属性
     * */
    private static class ViewState {
        /**
         * 按钮x的坐标[switchButtonMinX-switchButtonMaxX]
         */
        float buttonX;
        /**
         * 状态背景颜色
         */
        int checkStateColor;
        /**
         * 选中线的颜色
         */
        int checkedLineColor;
        /**
         * 状态背景的半径
         */
        float radius;
        ViewState(){}
        private void copy(ViewState source){
            this.buttonX = source.buttonX;
            this.checkStateColor = source.checkStateColor;
            this.checkedLineColor = source.checkedLineColor;
            this.radius = source.radius;
        }
    }

    //********************************  Switch 部分的方法***********************************//


    /**
     * 点击完整的条目的事件回调
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



    private static int optInt(TypedArray typedArray,
                              int index,
                              int def) {
        if(typedArray == null){return def;}
        return typedArray.getInt(index, def);
    }


    private static float optPixelSize(TypedArray typedArray,
                                      int index,
                                      float def) {
        if(typedArray == null){return def;}
        return typedArray.getDimension(index, def);
    }

    private static int optPixelSize(TypedArray typedArray,
                                    int index,
                                    int def) {
        if(typedArray == null){return def;}
        return typedArray.getDimensionPixelOffset(index, def);
    }

    private static int optColor(TypedArray typedArray,
                                int index,
                                int def) {
        if(typedArray == null){return def;}
        return typedArray.getColor(index, def);
    }

    private static boolean optBoolean(TypedArray typedArray,
                                      int index,
                                      boolean def) {
        if(typedArray == null){return def;}
        return typedArray.getBoolean(index, def);
    }

    private static int optResourceId(TypedArray typedArray,
                                         int index,
                                         int def){
        if(typedArray == null){return def;}
        return typedArray.getResourceId(index , def);
    }

    private static  String optString(TypedArray typedArray ,int index ,String def){
        if (typedArray == null) {return  def;}
        //获取字符串可能为空
        String s = typedArray.getString(index);
        if(s != null) return s;
        else return def;
    }









    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, Resources.getSystem().getDisplayMetrics());
    }

    private int dp2px( float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
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
        }else{
            vectorDrawable = getResources().getDrawable(vectorDrawableId);
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


    /**
     * 右侧View的Type
     */
    public @interface RightViewType {

        int IMAGE = 0;

        int SWITCH = 1;
        
        int CHECK = 2;
    }

    /*
     * Switch 状态事件改变回调接口
     */
    public interface OnSwitchCheckedChangeListener{
        void onCheckedChanged(OptionBarView view, boolean isSwitchChecked);
    }

}
