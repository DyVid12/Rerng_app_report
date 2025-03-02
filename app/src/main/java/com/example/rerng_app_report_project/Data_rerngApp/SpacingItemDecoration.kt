package com.example.rerng_app_report_project.Data_rerngApp

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = spacing / 2
        outRect.right = spacing / 2
        outRect.top = spacing
        outRect.bottom = spacing
    }
}
