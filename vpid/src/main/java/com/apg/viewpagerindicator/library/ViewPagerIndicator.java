package com.apg.viewpagerindicator.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sattha.p on 2015-11-10.
 */
public class ViewPagerIndicator extends View {

    private final int DEFAULT_INDICATOR_SIZE = 20;
    private final int DEFAULT_SIZE_OFF_SET = 0;
    private final int DEFAULT_STOKE_WIDTH = 4;
    private final int DEFAULT_SPACING = 5;
    private final int DEFAULT_ITEMS = 3;
    private final int DEFAULT_START_POSITION = 0;
    private final int DEFAULT_GRAVITY = Gravity.CENTER.getId();

    private boolean indicatorIsAllFill;
    private int indicatorSizeOffSet;
    private int indicatorSize;
    private int indicatorPosition;
    private int indicatorStokeWidth;
    private int indicatorItems;
    private int indicatorSpacing;
    private int indicatorColor;
    private int indicatorSelectedColor;
    private int indicatorGravity;
    private int mLayoutWidthOffset = 0;
    private int mLayoutWidth;
    private int mLayoutHeight;
    private Paint pen = new Paint();

    public ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        int[] attrsArray = new int[] {
                android.R.attr.id,              // 0
                android.R.attr.background,      // 1
                android.R.attr.layout_width,    // 2
                android.R.attr.layout_height,   // 3
                android.R.attr.gravity          // 4
        };

        TypedArray ta1 = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, defStyleAttr, defStyleRes);
        TypedArray ta2 = context.obtainStyledAttributes(attrs, attrsArray);

        try {
            indicatorColor = ta1.getColor(R.styleable.ViewPagerIndicator_indicator_color, Color.WHITE);
            indicatorSelectedColor = ta1.getColor(R.styleable.ViewPagerIndicator_indicator_selectedColor, Color.WHITE);
            indicatorSize = (int) ta1.getDimension(R.styleable.ViewPagerIndicator_indicator_size, DEFAULT_INDICATOR_SIZE);
            indicatorSizeOffSet = (int) ta1.getDimension(R.styleable.ViewPagerIndicator_indicator_sizeOffSet, DEFAULT_SIZE_OFF_SET);
            indicatorStokeWidth = (int) ta1.getDimension(R.styleable.ViewPagerIndicator_indicator_stokeWidth, DEFAULT_STOKE_WIDTH);
            indicatorSpacing = (int) ta1.getDimension(R.styleable.ViewPagerIndicator_indicator_spacing, DEFAULT_SPACING);
            indicatorItems = ta1.getInteger(R.styleable.ViewPagerIndicator_indicator_items, DEFAULT_ITEMS);
            indicatorPosition = ta1.getInteger(R.styleable.ViewPagerIndicator_indicator_startPosition, DEFAULT_START_POSITION);
            indicatorGravity = ta1.getInt(R.styleable.ViewPagerIndicator_indicator_gravity, DEFAULT_GRAVITY);
            indicatorIsAllFill = ta1.getBoolean(R.styleable.ViewPagerIndicator_indicator_allFill, false);

            mLayoutWidth = ta2.getLayoutDimension(2, ViewGroup.LayoutParams.MATCH_PARENT);
            mLayoutHeight = ta2.getLayoutDimension(3, ViewGroup.LayoutParams.MATCH_PARENT);
        } finally {
            ta1.recycle();
            ta2.recycle();
        }
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

    /**
     * Find desired width of all indicators
     *
     * @return total desired width for this view
     */
    private int getDesiredTotalIndicatorWidth() {
        return (indicatorSize * indicatorItems) + (indicatorSpacing * (indicatorItems - 1));
    }

    /**
     * Find start indent for drawing indicator view, from left side of the view
     *
     * @return start indent for drawing from the left, padding is not well support v.v
     */
    private int getStartIndent() {
        if (indicatorGravity == Gravity.CENTER.getId()) {
            return ((getWidth() - getDesiredTotalIndicatorWidth()) / 2) + getPaddingLeft() - getPaddingRight();
        } else if (indicatorGravity == Gravity.RIGHT.getId()) {
            return getWidth() - getDesiredTotalIndicatorWidth() - getPaddingRight();
        } else {
            return getPaddingLeft();
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

        int desiredHeight = indicatorSize + getPaddingTop() + getPaddingBottom();

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

        int desiredWidth = getDesiredTotalIndicatorWidth() + getPaddingLeft() + getPaddingRight();

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

        mLayoutWidthOffset =  Math.max(0, width - desiredWidth);
        setMeasuredDimension(width, height);
    }

    void getLayoutWidthOffset() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pen.setStyle(Paint.Style.STROKE);
        pen.setStrokeWidth(indicatorStokeWidth);
        pen.setAntiAlias(true);

        drawIndicator(canvas, indicatorItems, indicatorPosition);
    }

    private void drawIndicator(Canvas paper, int indicatorItems, int position) {

        int indent = getStartIndent();
        int cr, cx, cy;

        for (int i = 0; i < indicatorItems; i++) {
            if (position == i) {
                cr = (indicatorSize / 2) - (indicatorStokeWidth / 2);
                cx = cr + (indicatorStokeWidth / 2) + (indicatorSizeOffSet);
                cy = indicatorSize / 2;

                pen.setColor(indicatorSelectedColor);
                pen.setStyle(Paint.Style.FILL_AND_STROKE);
                paper.drawCircle(cx + indent, cy + getPaddingTop(), cr, pen);
                indent += (cr * 2) + (indicatorStokeWidth) + (indicatorSizeOffSet);
            } else {
                cr = (indicatorSize / 2) - (indicatorStokeWidth / 2) - indicatorSizeOffSet;
                cx = cr + (indicatorStokeWidth / 2) + indicatorSizeOffSet;
                cy = indicatorSize / 2;

                pen.setColor(indicatorColor);
                pen.setStyle(indicatorIsAllFill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
                paper.drawCircle(cx + indent, cy + getPaddingTop(), cr, pen);
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
        RIGHT(1);

        private int id;

        Gravity(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
