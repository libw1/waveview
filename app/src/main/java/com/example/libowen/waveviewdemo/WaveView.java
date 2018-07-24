package com.example.libowen.waveviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by libowen on 18-7-24.
 */

public class WaveView extends View {

    private int waveLength;
    private int waveColor;
    private int waveOfferY;
    private int waveHeight;
    private int wavePicture;
    private int width;
    private int height;
    private Paint paint = new Paint();
    private Path path = new Path();
    private int dx = 0;
    private int dy = 0;
    private Bitmap bitmap;
    private Region region;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        waveColor = typedArray.getColor(R.styleable.WaveView_wave_color, Color.BLUE);
        waveHeight = (int) typedArray.getDimension(R.styleable.WaveView_wave_height, 30);
        waveOfferY = (int) typedArray.getDimension(R.styleable.WaveView_wave_offer_y, 500);
        waveLength = (int) typedArray.getDimension(R.styleable.WaveView_wave_length, 500);
        wavePicture = typedArray.getResourceId(R.styleable.WaveView_wave_picture,0);
        typedArray.recycle();
        if (wavePicture > 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), wavePicture);
            bitmap = getCircleBitmap(bitmap);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.dji_app_icon);
        }
        paint.setAntiAlias(true);
        paint.setColor(waveColor);
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(0.6f, 0.6f);
        Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        width = newBM.getWidth();
        height = newBM.getHeight();
        if (width > height){
            width = height;
        }
        Bitmap bm = Bitmap.createBitmap(width,width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.reset();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(newBM, 0, 0, paint);
        return bm;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPath();
        canvas.drawPath(path,paint);
        if (bitmap != null) {
            Rect bounds = region.getBounds();
            if (bounds.top > 0 || bounds.right > 0) {
                if (bounds.top < waveOfferY) {
                    canvas.drawBitmap(bitmap, bounds.right - bitmap.getWidth() / 2, bounds.top - bitmap.getHeight(), paint);
                } else {
                    canvas.drawBitmap(bitmap, bounds.right - bitmap.getWidth() / 2, bounds.bottom - bitmap.getHeight(), paint);
                }
            } else {
                canvas.drawBitmap(bitmap, width / 2 - bitmap.getWidth() / 2, waveOfferY - bitmap.getHeight(), paint);
            }

        }
    }

    private void initPath() {
        path.reset();
        path.moveTo(- waveLength + dx, waveOfferY);
        for (int w = -waveLength; w < width + waveHeight; w += waveLength){
            path.rQuadTo(waveLength / 4, - waveHeight, waveLength / 2,0);
            path.rQuadTo(waveLength / 4, waveHeight, waveLength / 2,0);
        }
        region = new Region();
        float x = width / 2.0f;
        Region clip = new Region((int) (x - 0.1),0, width /2 ,height);
        region.setPath(path,clip);

        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    public int getWaveLength(){
        return waveLength;
    }

    public void setDx(int dx){
        this.dx = dx;
        invalidate();
    }

    public int getDx(){
        return dx;
    }

}
