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

    private SparseArray<FlowLineBean> mViewSp;
    private int mCurrentLine;

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewSp = new SparseArray<>();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCurrentLine = 0;
        mViewSp.clear();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (MeasureSpec.EXACTLY == widthMode && MeasureSpec.EXACTLY == heightMode) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int flowLayoutWidth = 0;
        int flowLayoutHeight = 0;
        int currentWidth = 0;
        int currentHeight = 0;

        int childCount = getChildCount();
        if (childCount > 0) {
            mCurrentLine = 1;
        }

        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) itemView.getLayoutParams();

            int measuredWidth = itemView.getMeasuredWidth();
            int measuredHeight = itemView.getMeasuredHeight();
            int childWidth = params.leftMargin + measuredWidth + params.rightMargin;
            int childTop = params.topMargin + measuredHeight + params.bottomMargin;

            if (currentWidth + childWidth > widthSize) {
                // 换行
                flowLayoutWidth = Math.max(flowLayoutWidth, currentWidth);
                flowLayoutHeight += currentHeight;
                mCurrentLine++;
                currentWidth = childWidth;
                currentHeight = childTop;

            } else {
                // 不换行
                currentWidth += childWidth;
                currentHeight = Math.max(currentHeight, childTop);

                if (i == childCount - 1) {
                    flowLayoutWidth = Math.max(flowLayoutWidth, currentWidth);
                    flowLayoutHeight += currentHeight;
                }
            }
            saveViewToSpCache(itemView, currentWidth, currentHeight);
        }

        int widthSpec = MeasureSpec.makeMeasureSpec(flowLayoutWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(flowLayoutHeight, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    private void saveViewToSpCache(View view, int lineWidth, int lineHeight) {
        FlowLineBean lineBean = mViewSp.get(mCurrentLine);
        if (lineBean == null) {
            lineBean = new FlowLineBean();
            lineBean.viewList = new ArrayList<>();
        }
        lineBean.viewList.add(view);
        lineBean.maxWidth = lineWidth;
        lineBean.maxHeight = lineHeight;
        mViewSp.put(mCurrentLine, lineBean);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int currentWidth = 0;
        int currentHeight = 0;
        int left, top;

        for (int i = 0; i < mViewSp.size(); i++) {
            FlowLineBean lineBean = mViewSp.get(i + 1);
            for (View view : lineBean.viewList) {
                MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();

                left = currentWidth + params.leftMargin;
                top = currentHeight + (lineBean.maxHeight - measuredHeight - params.topMargin
                        - params.bottomMargin) / 2;
                view.layout(left, top, left + measuredWidth, top + measuredHeight);

                currentWidth = left + measuredWidth + params.rightMargin;
            }
            currentWidth = 0;
            currentHeight += lineBean.maxHeight;
        }
    }
}