package com.cjw.demo1_flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 流失布局
 * Created by Administrator on 2017/10/6.
 */

public class FlowLayout extends ViewGroup {

    private SparseArray<FlowLayoutLine> mFlowLayoutLineSp;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFlowLayoutLineSp = new SparseArray<>();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFlowLayoutLineSp.clear();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();

        if (MeasureSpec.EXACTLY == widthMode && MeasureSpec.EXACTLY == heightMode
                || childCount <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int currentLineWidth = 0;
        int tempCurrentLineWidth;
        int currentLineMaxHeight = 0;
        int currentLineIndex = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            tempCurrentLineWidth = currentLineWidth + layoutParams.leftMargin
                    + childView.getMeasuredWidth() + layoutParams.rightMargin;

            int height = layoutParams.topMargin + childView.getMeasuredHeight()
                    + layoutParams.bottomMargin;

            if (tempCurrentLineWidth > widthSize) {
                // change Line
                currentLineIndex++;
                currentLineWidth = tempCurrentLineWidth - currentLineWidth;
                currentLineMaxHeight = height;
            } else {
                currentLineWidth = tempCurrentLineWidth;
                currentLineMaxHeight = Math.max(currentLineMaxHeight, height);
            }
            saveViewToCache(childView, currentLineIndex, currentLineWidth, currentLineMaxHeight);
        }

        int size = mFlowLayoutLineSp.size();
        int maxLineWidth = 0;
        int totalViewHeight = 0;
        for (int i = 0; i < size; i++) {
            FlowLayoutLine layoutLine = mFlowLayoutLineSp.get(i);
            maxLineWidth = Math.max(maxLineWidth, layoutLine.lineWidth);
            totalViewHeight += layoutLine.maxViewHeight;
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(maxLineWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(totalViewHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    private void saveViewToCache(View childView, int currentLineIndex, int lineWidth,
                                 int currentLineMaxHeight) {
        FlowLayoutLine flowLayoutLine = mFlowLayoutLineSp.get(currentLineIndex);
        if (flowLayoutLine == null) {
            flowLayoutLine = new FlowLayoutLine();
            flowLayoutLine.viewList = new ArrayList<>();
        }
        flowLayoutLine.lineWidth = lineWidth;
        flowLayoutLine.maxViewHeight = currentLineMaxHeight;
        flowLayoutLine.viewList.add(childView);
        mFlowLayoutLineSp.put(currentLineIndex, flowLayoutLine);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int lineCount = mFlowLayoutLineSp.size();
        int currentLineHeight = 0;

        for (int i = 0; i < lineCount; i++) {
            FlowLayoutLine flowLayoutLine = mFlowLayoutLineSp.get(i);

            int currentLineWidth = 0;
            for (View childView : flowLayoutLine.viewList) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                int measuredWidth = childView.getMeasuredWidth();
                int measuredHeight = childView.getMeasuredHeight();

                int childViewLeft = currentLineWidth + layoutParams.leftMargin;
                int childViewRight = childViewLeft + measuredWidth;
                int childViewTop = currentLineHeight + (flowLayoutLine.maxViewHeight
                        - measuredHeight - layoutParams.topMargin - layoutParams.bottomMargin) / 2;
                int childViewBottom = childViewTop + measuredHeight;
                currentLineWidth = childViewRight + layoutParams.rightMargin;

                childView.layout(childViewLeft, childViewTop, childViewRight, childViewBottom);
            }
            currentLineHeight += flowLayoutLine.maxViewHeight;
        }
    }
}