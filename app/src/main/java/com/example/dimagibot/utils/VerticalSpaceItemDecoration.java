package com.example.dimagibot.utils;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * $ |-| ! V @ M
 * Created by Shivam Pokhriyal on 2019-10-22.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public VerticalSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.bottom = space;
//        }
        outRect.top = space;
    }
}
