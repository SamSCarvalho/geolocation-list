package com.example.geolocationlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class SkyView extends View {
    private int circleColor, textColor, satColor;
    private static int SAT_RADIUS;
    private String text;
    private double mOrientation = 0.0;
    Canvas canvas = new Canvas();
    Iterable<GpsSatellite> sats = null;

    public SkyView(Context context, @Nullable AttributeSet attr) {
        super(context, attr);
        SAT_RADIUS = dpToPixels(context, 5);
        TypedArray a = context.getTheme().obtainStyledAttributes( attr, R.styleable.custom_attributes,
                0, 0);
        try {
            circleColor = a.getInteger(R.styleable.custom_attributes_skyView_circleColor, 0);
            textColor = a.getInteger(R.styleable.custom_attributes_skyView_textColor, 0);
            text = a.getString(R.styleable.custom_attributes_skyView_text);
            satColor = a.getInteger(R.styleable.custom_attributes_skyView_satColor, 0);
        } finally {
            a.recycle();
        }
    }

    @SuppressLint("WrongCall")
    public void setSats(Iterable<GpsSatellite> sats) {
        this.sats = sats;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;
        int radius = 0;
        if(viewWidthHalf>viewHeightHalf)
            radius=viewHeightHalf-40;
        else
            radius=viewWidthHalf-40;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius-100, paint);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius-200, paint);

        drawNorthIndicator(canvas);

        if (sats != null) {
            drawSatellite(canvas, paint, 140,40,11,2,false);
            String coords = "";
            for (GpsSatellite sat: sats) {
                drawSatellite(canvas, paint, sat.getElevation(), sat.getAzimuth(), sat.getSnr(), sat.getPrn(), sat.usedInFix());
                coords+=sat.getPrn()+";"+sat.getAzimuth()+";"+sat.getElevation()+";"
                        +sat.getSnr()+";"+sat.usedInFix()+"\n";
            }
            Log.d("SATELITES_SKY_VIEW", coords);
        }


    }

    private void drawNorthIndicator(Canvas c) {
        int s = (getWidth() < getHeight()) ? getWidth() : getHeight();
        float radius = s / 2;
        double angle = Math.toRadians(-mOrientation);
        final float ARROW_HEIGHT_SCALE = 0.05f;
        final float ARROW_WIDTH_SCALE = 0.1f;

        float x1, y1;  // Tip of arrow
        x1 = radius;
        y1 = elevationToRadius(s, 90.0f);

        float x2, y2;
        x2 = x1 + radius * ARROW_HEIGHT_SCALE;
        y2 = y1 + radius * ARROW_WIDTH_SCALE;

        float x3, y3;
        x3 = x1 - radius * ARROW_HEIGHT_SCALE;
        y3 = y1 + radius * ARROW_WIDTH_SCALE;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        path.close();

        // Rotate arrow around center point
        Matrix matrix = new Matrix();
        matrix.postRotate((float) -mOrientation, radius, radius);
        path.transform(matrix);

        Paint mNorthPaint, mNorthFillPaint;

        mNorthPaint = new Paint();
        mNorthPaint.setColor(Color.BLACK);
        mNorthPaint.setStyle(Paint.Style.STROKE);
        mNorthPaint.setStrokeWidth(4.0f);
        mNorthPaint.setAntiAlias(true);

        mNorthFillPaint = new Paint();
        mNorthFillPaint.setColor(Color.GRAY);
        mNorthFillPaint.setStyle(Paint.Style.FILL);
        mNorthFillPaint.setStrokeWidth(4.0f);
        mNorthFillPaint.setAntiAlias(true);

        c.drawPath(path, mNorthPaint);
        c.drawPath(path, mNorthFillPaint);
    }

    @SuppressLint("ResourceAsColor")
    public void drawSatellite(Canvas canvas, Paint paint, float elev, float azim,
                              float snrCn0, int prn, boolean usedInFix) {
        double angle;
        int minScreenDimen;
        float radiusSat;
        float x, y;

        minScreenDimen = (getWidth() < getHeight()) ? getWidth() : getHeight();
        angle = (float) Math.toRadians(azim);

        radiusSat = elevationToRadius(minScreenDimen, elev)-40;
        x = (float) ((minScreenDimen / 2) + (radiusSat * Math.sin(angle)));
        y = (float) ((minScreenDimen / 2) - (radiusSat * Math.cos(angle)));

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(R.color.colorPrimary);
        canvas.drawCircle(x, y, 20, paint);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(16);
        canvas.drawText(prn+"", x, y, paint);
    }

    public static float elevationToRadius(int s, float elev) {
        return ((s / 2) - SAT_RADIUS) * (1.0f - (elev / 90.0f));
    }

    public static int dpToPixels(Context context, float dp) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

}
