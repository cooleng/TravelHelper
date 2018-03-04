package com.xiaomo.travelhelper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xiaomo.travelhelper.R;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.ButterKnife;

/**
 * 顶部标签天气描述 view
 */

public class WeatherDescView extends View {

    @BindColor(R.color.white) int mTextColor;

    private Context mContext;
    private int mWidth;
    private int mFitWidth;
    private int mHeight;
    private String mTemperature = "19";
    private String mAirLevel = "22";
    private String mAddress ="广州";
    private String mAirDesc ="优";


    public WeatherDescView(Context context) {
        this(context,null);
    }

    public WeatherDescView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherDescView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ButterKnife.bind(this);
        this.mContext = context;
        TypedArray ta =  mContext.obtainStyledAttributes(attrs, R.styleable.WeatherDescView);
        this.mTextColor = ta.getColor(R.styleable.WeatherDescView_textColor,mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取 view 宽和高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 适配宽度
        int fitWidthMeasureSpec = widthMeasureSpec;
        if( mFitWidth > 0 && mFitWidth != mWidth){
            fitWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getMode(widthMeasureSpec),mFitWidth);
        }

        super.onMeasure(fitWidthMeasureSpec,heightMeasureSpec);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = getPaint();
        canvas.translate(0,5*mHeight/6f);

        // 温度
        paint.setTextSize(5*mHeight/6);
        canvas.drawText(mTemperature,0,0,paint);
        float temperatureWidth = paint.measureText(mTemperature);
        paint.setTextSize(2*mHeight/6);
        canvas.drawText("。",temperatureWidth + 10,-3*mHeight/6f,paint);

        // 地点
        float circleWidth = paint.measureText("。");
        paint.setTextSize(2*mHeight/6);
        canvas.drawText(mAddress,temperatureWidth + circleWidth + 20,-2*mHeight/6f,paint);
        float addressWidth = paint.measureText(mAddress);
        mFitWidth = (int) (temperatureWidth + circleWidth + 20 + addressWidth);

        // 空气
        canvas.drawText(mAirLevel,temperatureWidth + circleWidth + 20,0,paint);
        float airLevelWidth = paint.measureText(mAirLevel);
        canvas.drawText(mAirDesc,temperatureWidth + circleWidth + airLevelWidth+ 30,0,paint);
        float airDescWidth = paint.measureText(mAirDesc);

        // 计算适配宽度
        int tmp = (int) (temperatureWidth + circleWidth + 30 + airLevelWidth + airDescWidth);
        mFitWidth = Math.max(mFitWidth,tmp);

        // 宽度不相等则适配
        if(mFitWidth != mWidth){
            mWidth = mFitWidth;
            this.setMinimumWidth(mFitWidth);
        }

    }

    /**
     * 修改文本颜色
     * @param textColor
     */
    public void setTextColor(int textColor){
        this.mTextColor = textColor;
        this.invalidate();
    }

    /**
     * 修改天气描述
     * @param temperature
     * @param address
     * @param airLevel
     * @param airDesc
     */
    public void setWeatherDesc(String temperature,String address,String airLevel,String airDesc){
        this.mTemperature =  temperature;
        this.mAddress = address;
        this.mAirDesc = airDesc;
        this.mAirLevel = airLevel;
        this.invalidate();
    }


    private Paint getPaint(){
        Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTextColor);

        return paint;
    }



}
