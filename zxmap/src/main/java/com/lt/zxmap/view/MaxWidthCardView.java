package com.lt.zxmap.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class MaxWidthCardView extends CardView {
    public MaxWidthCardView(@NonNull Context context) {
        super(context);
    }

    public MaxWidthCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxWidthCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        if (width > MeaUtil.dp2px(getContext(), 340)) {
//            width = MeaUtil.dp2px(getContext(), 340);
//        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, mode), heightMeasureSpec);
    }
}
