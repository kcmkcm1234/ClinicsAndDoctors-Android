package com.clinicsanddoctors.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.clinicsanddoctors.R;

/**
 * Created by evaristo on 8/3/17.
 */

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

    Paint paint = new Paint();

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildCount()>0) {
            int dividerLeft = parent.getChildAt(0).findViewById(R.id.mDescription).getLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;

                paint.setColor(Color.BLACK);
                c.drawLine(dividerLeft,dividerTop,dividerRight,dividerTop,paint);
            }
        }
    }
}
