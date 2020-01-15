package com.ocwvar.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

public class BaseLineView extends AppCompatTextView {

    private Context mContext;

    //背景画笔
    private Paint mBackgroundPaint;
    //按钮文字画笔
    private  Paint mTextPaint;

    //背景颜色
    private int mBackgroundColor;
    //文字颜色
    private int mTextColor;
    //记录当前文字
    private  static CharSequence mCurrentText ;
    private int size;
    private int mpadding = 10;//单词距离两端的距离
    private int baseX;
    private int baseY;
    private int baseYY;

    public BaseLineView(Context context) {
        this(context, null);

    }

    public BaseLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mContext = context;
            initAttrs(context, attrs);
            init();
        }
    }
    private float textSize;
    private void initAttrs(Context context, AttributeSet attrs) {
        Log.d("dsgdsg", "initAttrs: "+baseY);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseLineCreate);
        mBackgroundColor = a.getColor(R.styleable.BaseLineCreate_BaseLine_backgroud_color, Color.GREEN);
        mTextColor = a.getColor(R.styleable.BaseLineCreate_BaseLine_text_color, Color.GREEN);
        textSize = a.getDimensionPixelSize(R.styleable.BaseLineCreate_BaseLine_text_size,44);
        a.recycle();
    }
    //设置线条颜色
    public void setBaseLineColor(int color){
        mBackgroundColor=color;
    }
    public void getBaseLineColor(int color){
        mBackgroundColor=color;
    }


    private void init() {
        //设置背景画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setColor(mBackgroundColor); //设置画笔的颜色


        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(mTextColor);
        //字体加粗，倾斜（可要可不要）
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);
        mTextPaint.setTypeface(font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //解决文字有时候画不出问题
            setLayerType(LAYER_TYPE_SOFTWARE, mTextPaint);
        }
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (!isInEditMode()) {
        if (mCurrentText == null) {
            mCurrentText = "";
        }
        // 计算Baseline绘制的起点X轴坐标
        baseX = (int) (canvas.getWidth() / 2 - mTextPaint.measureText(mCurrentText.toString()) / 2);
        // 计算Baseline绘制的Y坐标
        baseY = (int) ((canvas.getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
//        baseYY = (int) ((canvas.getHeight() / 2) + ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
        baseYY = (int) ((canvas.getHeight() / 2) + ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
        size = Math.max(getMeasuredWidth(),(int)mTextPaint.measureText(mCurrentText.toString()) )+2*mpadding;

        final float  textWidth = mTextPaint.measureText(mCurrentText.toString());
        //画线
        mBackgroundPaint.setStrokeWidth(6);
        canvas.drawLine(baseX, 0, baseX+textWidth, 0, mBackgroundPaint);
        mBackgroundPaint.setStrokeWidth(1);
        canvas.drawLine(baseX,baseYY,baseX+textWidth,baseYY,mBackgroundPaint);
        mBackgroundPaint.setStrokeWidth(1);
        canvas.drawLine(baseX,baseY,baseX+textWidth,baseY,mBackgroundPaint);
        mBackgroundPaint.setStrokeWidth(6);
        canvas.drawLine(baseX,getHeight(),baseX+textWidth,getHeight(),mBackgroundPaint);



        mTextPaint.setShader(null);
        mTextPaint.setColor(mTextColor);
        canvas.drawText(mCurrentText.toString(), baseX, baseY, mTextPaint);
        }
    }


    /**
     * 设置按钮文字
     */
    public synchronized void setCurrentText(CharSequence charSequence) {
        this.mCurrentText = charSequence;
        invalidate();

//        postInvalidate();
    }

    public CharSequence getCurrentText(){
        return  mCurrentText;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mCurrentText = ss.currentText;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentText!=null?mCurrentText.toString():null);
    }

    public static class SavedState extends BaseSavedState {
        private String currentText;
        public SavedState(Parcelable parcel,  String currentText) {
            super(parcel);
            this.currentText = currentText;
        }

        private SavedState(Parcel in) {
            super(in);
            currentText = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(currentText);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
