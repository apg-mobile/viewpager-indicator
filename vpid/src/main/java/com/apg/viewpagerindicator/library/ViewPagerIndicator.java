package com.apg.viewpagerindicator.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sattha.p on 2015-11-10.
 */
public class ViewPagerIndicator extends View {

    private final int DEFAULT_SIZE_OFF_SET = 0;
    private final int DEFAULT_HEIGHT = 20;
    private final int DEFAULT_STOKE_WIDTH = 4;
    private final int DEFAULT_SPACING = 5;
    private final int DEFAULT_ITEMS = 3;
    private final int DEFAULT_START_POSITION = 0;
    private final int DEFAULT_GRAVITY = Gravity.CENTER.getId();


    private int indicatorSizeOffSet;
    private int indicatorPosition;
    private int indicatorStokeWidth;
    private int indicatorItems;
    private int indicatorSpacing;
    private int indicatorColor;
    private int indicatorSelectedColor;
    private int indicatorGravity;
    private int mLayoutOffset = 0;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private boolean indicatorIsAllFill;

    private Paint pen = new Paint();

    public ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        int[] attrsArray = new int[] {
                android.R.attr.id,              // 0
                android.R.attr.background,      // 1
                android.R.attr.layout_width,    // 2
                android.R.attr.layout_height,   // 3
                android.R.attr.gravity          // 4
        };


        TypedArray taa = context.obtainStyledAttributes(attrs, attrsArray);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);

        try {
            indicatorColor = ta.getColor(R.styleable.ViewPagerIndicator_indicator_color, Color.WHITE);
            indicatorSelectedColor = ta.getColor(R.styleable.ViewPagerIndicator_indicator_selectedColor, Color.WHITE);
            indicatorSizeOffSet = (int) ta.getDimension(R.styleable.ViewPagerIndicator_indicator_sizeOffSet, DEFAULT_SIZE_OFF_SET);
            indicatorStokeWidth = (int) ta.getDimension(R.styleable.ViewPagerIndicator_indicator_stokeWidth, DEFAULT_STOKE_WIDTH);
            indicatorSpacing = (int) ta.getDimension(R.styleable.ViewPagerIndicator_indicator_spacing, DEFAULT_SPACING);
            indicatorItems = ta.getInteger(R.styleable.ViewPagerIndicator_indicator_items, DEFAULT_ITEMS);
            indicatorPosition = ta.getInteger(R.styleable.ViewPagerIndicator_indicator_startPosition, DEFAULT_START_POSITION);
            indicatorGravity = ta.getInt(R.styleable.ViewPagerIndicator_indicator_gravity, DEFAULT_GRAVITY);
            indicatorIsAllFill = ta.getBoolean(R.styleable.ViewPagerIndicator_indicator_allFill, false);

            mLayoutWidth = taa.getLayoutDimension(2, ViewGroup.LayoutParams.MATCH_PARENT);
            mLayoutHeight = taa.getLayoutDimension(3, ViewGroup.LayoutParams.MATCH_PARENT);
        } finally {
            ta.recycle();
            taa.recycle();
        }
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(ViewPager pager) {
        try {
            setIndicatorItems(pager.getAdapter().getCount());
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentPosition(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public void setIndicatorItems(int items) {
        this.indicatorItems = items;
        this.invalidate();
    }

    public void setCurrentPosition(int position) {
        this.indicatorPosition = position;
        this.invalidate();
    }

    public void setIndicatorColor(int color) {
        this.indicatorColor = color;
        this.invalidate();
    }

    public int getCurrentIndicatorPosition() {
        return indicatorPosition;
    }

    private int getTotalIndicatorWidth(int heightSize) {
        int indicatorWidth;
        if (mLayoutHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            indicatorWidth = DEFAULT_HEIGHT;
        } else if (mLayoutHeight == ViewGroup.LayoutParams.MATCH_PARENT){
            indicatorWidth = heightSize;
        } else {
            indicatorWidth = mLayoutHeight;
        }
        return (indicatorWidth * indicatorItems) + (indicatorSpacing * (indicatorItems - 1));
    }

    private int getOffSetSpace(int heightSize, int widthSize) {
        int contentSize = getTotalIndicatorWidth(heightSize);
        return Math.abs(contentSize - widthSize);
    }

    private int getIndent(int offSetSpace) {
        if (indicatorGravity == Gravity.CENTER.getId()) {
            return offSetSpace / 2;
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width;
        int height;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = getTotalIndicatorWidth(heightSize);
        mLayoutOffset = getOffSetSpace(heightSize, widthSize);
        int desiredHeight = DEFAULT_HEIGHT;

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        pen.setStyle(Paint.Style.STROKE);
        pen.setStrokeWidth(indicatorStokeWidth);
        pen.setAntiAlias(true);

        drawIndicator(canvas, indicatorItems, indicatorPosition);
    }

    private void drawIndicator(Canvas paper, int indicatorItems, int position) {

        int indent = getIndent(mLayoutOffset);
        int cr, cx, cy;


        for (int i = 0; i < indicatorItems; i++) {
            if (position == i) {
                cr = (getHeight() / 2) - getPaddingTop() - getPaddingBottom() - (indicatorStokeWidth / 2);
                cx = cr + getPaddingLeft() + (indicatorStokeWidth / 2) + (indicatorSizeOffSet);
                cy = (getHeight() / 2) + getPaddingTop() - getPaddingBottom();

                pen.setColor(indicatorSelectedColor);
                pen.setStyle(Paint.Style.FILL_AND_STROKE);
                paper.drawCircle(cx + indent, cy, cr, pen);
                indent += (cr * 2) + (indicatorStokeWidth) + (indicatorSizeOffSet);
            } else {
                cr = (getHeight() / 2) - getPaddingTop() - getPaddingBottom() - (indicatorStokeWidth / 2) - indicatorSizeOffSet;
                cx = cr + getPaddingLeft() + (indicatorStokeWidth / 2) + indicatorSizeOffSet;
                cy = (getHeight() / 2) + getPaddingTop() - (getPaddingBottom());

                pen.setColor(indicatorColor);
                pen.setStyle(indicatorIsAllFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
                paper.drawCircle(cx + indent, cy, cr, pen);
                indent += (cr * 2) + (indicatorStokeWidth) + (indicatorSizeOffSet);
            }

            if ((i+1) < indicatorItems) {
                indent += indicatorSpacing;
            }
        }
    }

    private enum Gravity {
        LEFT(-1),
        CENTER(0),
        RIGHT(2);

        private int id;

        Gravity(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
