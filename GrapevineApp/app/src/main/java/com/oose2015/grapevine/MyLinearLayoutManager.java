package com.oose2015.grapevine;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Tom on 12/10/2015.
 */
public class MyLinearLayoutManager extends LinearLayoutManager {

    /**
     * Constructor for my custom LinearLayoutManager
     * @param context app context
     * @param orientation vertical or horizontal
     * @param reverseLayout reverse or not
     */
    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * Can you scroll on RecyclerViews or not
     * @return false
     */
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
