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

import org.w3c.dom.Attr;

public class SkyView extends View {
    private int circleColor, textColor, satColor, lowSNR, highSNR, mediumSNR;
    private static int SAT_RADIUS;
    private String text;
    private double mOrientation = 0.0;
    Iterable<GpsSatellite> sats = null;


    public SkyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SAT_RADIUS = dpToPixels(context, 5);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.custom_attributes,
                0, 0);
        try {
            circleColor = a.getInteger(R.styleable.custom_attributes_skyView_circleColor, 0);
            textColor = a.getInteger(R.styleable.custom_attributes_skyView_textColor, 0);
            text = a.getString(R.styleable.custom_attributes_skyView_text);
            satColor = a.getInteger(R.styleable.custom_attributes_skyView_satColor, 0);
            lowSNR = a.getInteger(R.styleable.custom_attributes_skyView_lowSNR,0);
            highSNR = a.getInteger(R.styleable.custom_attributes_skyView_highSNR, 0);
            mediumSNR = a.getInteger(R.styleable.custom_attributes_skyView_mediumSNR, 0);
        } finally {
            a.recycle();
        }
    }

    public void setSats(Iterable<GpsSatellite> sats) {
        this.sats = sats;
    }

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

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(circleColor);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius, paint);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius-100, paint);
        canvas.drawCircle(viewWidthHalf, viewHeightHalf, radius-200, paint);

        drawNorthIndicator(canvas);

        if (sats != null) {
            for (GpsSatellite sat: sats) {
                drawSatellite(canvas, sat.getElevation(), sat.getAzimuth(), sat.getSnr(), sat.getPrn(), sat.usedInFix());
            }
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

        Matrix matrix = new Matrix();
        matrix.postRotate((float) -mOrientation, radius, radius);
        path.transform(matrix);

        Paint mNorthFillPaint;

        mNorthFillPaint = new Paint();
        mNorthFillPaint.setColor(circleColor);
        mNorthFillPaint.setStyle(Paint.Style.FILL);
        mNorthFillPaint.setStrokeWidth(4.0f);
        mNorthFillPaint.setAntiAlias(true);

        c.drawPath(path, mNorthFillPaint);
    }

    @SuppressLint("ResourceAsColor")
    public void drawSatellite(Canvas canvas, float elev, float azim,
                              float snrCn0, int prn, boolean usedInFix) {
        double angle;
        int minScreenDimen;
        float radiusSat;
        float x, y;
        Paint paintStroke = new Paint();
        Paint paintFill = new Paint();
        minScreenDimen = (getWidth() < getHeight()) ? getWidth() : getHeight();
        angle = (float) Math.toRadians(azim);

        radiusSat = elevationToRadius(minScreenDimen, elev)-40;
        x = (float) ((minScreenDimen / 2) + (radiusSat * Math.sin(angle)));
        y = (float) ((minScreenDimen / 2) - (radiusSat * Math.cos(angle)));

        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(4);
        if (snrCn0 <= 10) {
            paintStroke.setColor(lowSNR);
        } else if (snrCn0 >= 20) {
            paintStroke.setColor(highSNR);
        } else {
            paintStroke.setColor(mediumSNR);
        }
        paintStroke.setAntiAlias(true);

        paintFill.setStyle(Paint.Style.FILL);
        if (usedInFix) {
            paintFill.setColor(satColor);
        } else {
            paintFill.setColor(circleColor);
        }
        paintFill.setAntiAlias(true);

        int satRadiusDraw = SAT_RADIUS + 10;

        if (prn >= 1 && prn <= 32) { //GPS
            canvas.drawCircle(x, y, 20, paintStroke);
            canvas.drawCircle(x, y, 20, paintFill);
        } if (prn >= 65 && prn <= 96) { //GLONASS
            canvas.drawRect(x-satRadiusDraw,y-satRadiusDraw,x+satRadiusDraw,y+satRadiusDraw, paintStroke);
            canvas.drawRect(x-satRadiusDraw,y-satRadiusDraw,x+satRadiusDraw,y+satRadiusDraw, paintFill);
        } else if (prn >= 193 && prn <= 200) { //QZSS (Japão)
            drawTriangle(canvas, x, y, paintFill, paintStroke, satRadiusDraw);
        } else if (prn>= 201 && prn <= 235) { //BEIDOU
            drawPentagon(canvas, x, y, paintFill, paintStroke, satRadiusDraw);
        } else if(prn>=301 && prn <= 336) { //GALILEO
            drawHexagon(canvas, x, y, paintFill, paintStroke, satRadiusDraw);
        }


        paintFill.setColor(textColor);
        paintFill.setTextAlign(Paint.Align.CENTER);
        paintFill.setTextSize(16);
        canvas.drawText(prn+"", x, y+5, paintFill);
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

    private void drawTriangle(Canvas c, float x, float y, Paint fillPaint, Paint strokePaint, int satRadiusDraw) {
        float x1, y1;  // Top
        x1 = x;
        y1 = y - satRadiusDraw;

        float x2, y2; // Lower left
        x2 = x - satRadiusDraw;
        y2 = y + satRadiusDraw;

        float x3, y3; // Lower right
        x3 = x + satRadiusDraw;
        y3 = y + satRadiusDraw;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        path.close();

        c.drawPath(path, fillPaint);
        c.drawPath(path, strokePaint);
    }

    private void drawPentagon(Canvas c, float x, float y, Paint fillPaint, Paint strokePaint, int satRadiusDraw) {
        Path path = new Path();
        path.moveTo(x, y - satRadiusDraw);
        path.lineTo(x - satRadiusDraw, y - (satRadiusDraw / 3));
        path.lineTo(x - 2 * (satRadiusDraw / 3), y + satRadiusDraw);
        path.lineTo(x + 2 * (satRadiusDraw / 3), y + satRadiusDraw);
        path.lineTo(x + satRadiusDraw, y - (satRadiusDraw / 3));
        path.close();

        c.drawPath(path, fillPaint);
        c.drawPath(path, strokePaint);
    }

    private void drawHexagon(Canvas c, float x, float y, Paint fillPaint, Paint strokePaint, int satRadiusDraw) {
        final float MULTIPLIER = 0.6f;
        final float SIDE_MULTIPLIER = 1.4f;
        Path path = new Path();
        // Top-left
        path.moveTo(x - satRadiusDraw * MULTIPLIER, y - satRadiusDraw);
        // Left
        path.lineTo(x - satRadiusDraw * SIDE_MULTIPLIER, y);
        // Bottom
        path.lineTo(x - satRadiusDraw * MULTIPLIER, y + satRadiusDraw);
        path.lineTo(x + satRadiusDraw * MULTIPLIER, y + satRadiusDraw);
        // Right
        path.lineTo(x + satRadiusDraw * SIDE_MULTIPLIER, y);
        // Top-right
        path.lineTo(x + satRadiusDraw * MULTIPLIER, y - satRadiusDraw);
        path.close();

        c.drawPath(path, fillPaint);
        c.drawPath(path, strokePaint);
    }

}
