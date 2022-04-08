package com.kiit.eee.ragini.refreshnews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 1807340_RAGINI on 08,April,2022
 * KIIT University B.Tech EEE
 * ragini31bxr@gmail.com
 */
public class HorizentalItemsDividerDecorator  extends RecyclerView.ItemDecoration{
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private static String TAG = "Hello";
    private Drawable mDivider;

    public HorizentalItemsDividerDecorator(Context context) {
        if (context != null) {
            final TypedArray attrs = context.obtainStyledAttributes(ATTRS);
            if (attrs != null) {
                mDivider = attrs.getDrawable(0);
                attrs.recycle();
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawDivider(canvas, parent);
    }

    /**
     * Draw divider on recyclerView
     *
     * @param canvas to draw
     * @param parent recyclerView
     */
    public void drawDivider(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount(); //total no of visible items on screen
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
           // Log.i(TAG, "drawDivider: " + params);
            final int top = child.getBottom() + params.topMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }
}
