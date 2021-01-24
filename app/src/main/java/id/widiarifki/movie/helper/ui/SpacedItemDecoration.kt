package id.widiarifki.movie.helper.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpacedItemDecoration : RecyclerView.ItemDecoration {

    private var mSpace = 14
    private var mView : View? = null

    constructor()
    constructor(context: Context?, dimen: Int? = null) : super() {
        dimen?.let {
            val pixelSize = context?.resources?.getDimensionPixelSize(dimen) ?: 0
            this.mSpace = pixelSize
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        mView = view

        val layoutManager = parent.layoutManager
        val listItemCount = state.itemCount
        val adapterItemCount = parent.adapter?.itemCount ?: 0
        var itemPosition = parent.getChildAdapterPosition(view)

        if(listItemCount > adapterItemCount){
            if(itemPosition == 0){
                return
            }else{
                itemPosition -= 1
            }
        }

        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                setStaggeredGridLayoutOffset(outRect, layoutManager.orientation, layoutManager.spanCount, adapterItemCount, itemPosition)
            }
            is GridLayoutManager -> {
                setGridLayoutOffset(outRect, layoutManager.orientation, layoutManager.spanCount, adapterItemCount, itemPosition)
            }
            is LinearLayoutManager -> {
                setLinearLayoutOffset(outRect, layoutManager.orientation, itemPosition)
            }
        }
    }

    private fun setLinearLayoutOffset(outRect: Rect, layoutOrientation: Int, itemPosition: Int){
        when (layoutOrientation) {
            LinearLayoutManager.VERTICAL -> {
                outRect.bottom = mSpace
                if(itemPosition == 0) {
                    outRect.top = mSpace
                }
            }
            LinearLayoutManager.HORIZONTAL -> {
                outRect.right = mSpace
                if(itemPosition == 0) {
                    outRect.left = mSpace
                }
            }
        }
    }

    private fun setGridLayoutOffset(outRect: Rect, layoutOrientation: Int, spanCount: Int, adapterItemCount: Int, itemPosition: Int){
        val position = itemPosition + 1

        when (layoutOrientation) {
            GridLayoutManager.VERTICAL -> {
                if(position.rem(spanCount) == 0){
                    outRect.right = 0
                }else{
                    outRect.right = mSpace
                }

                outRect.top = mSpace
                // Set margin top, except the last
                /*if((adapterItemCount.rem(spanCount) != 0 && position < adapterItemCount)
                    || (adapterItemCount.rem(spanCount) == 0 && position <= (adapterItemCount - spanCount))){
                    outRect.top = mSpace
                }*/
            }

            GridLayoutManager.HORIZONTAL -> {
                if(position.rem(spanCount) == 0){
                    outRect.bottom = 0
                }else{
                    outRect.bottom = mSpace
                }

                outRect.left = mSpace
                // Set bottom space for all column, except the last
                /*if((adapterItemCount.rem(spanCount) != 0 && position < adapterItemCount)
                    || (adapterItemCount.rem(spanCount) == 0 && position <= (adapterItemCount - spanCount))){
                    outRect.left = mSpace
                }*/
            }
        }
    }

    private fun setStaggeredGridLayoutOffset(outRect: Rect, layoutOrientation: Int, spanCount: Int, itemCount: Int, itemPosition: Int){
        val position = itemPosition + 1
        val layoutParams = mView?.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = layoutParams.spanIndex
        val singleItemSpace = mSpace/2

        when(layoutOrientation) {
            GridLayoutManager.VERTICAL -> {
                if(spanIndex == 1){
                    outRect.left = singleItemSpace;
                } else{
                    outRect.right = singleItemSpace;
                }

                if((itemCount.rem(spanCount) != 0 && position < itemCount)
                    || (itemCount.rem(spanCount) == 0 && position <= (itemCount - spanCount))){
                    // Except last row...
                    outRect.bottom = mSpace
                }
            }

            GridLayoutManager.HORIZONTAL -> {
                if(spanIndex == 1){
                    outRect.bottom = singleItemSpace;
                } else{
                    outRect.top = singleItemSpace;
                }

                if((itemCount.rem(spanCount) != 0 && position < itemCount)
                    || (itemCount.rem(spanCount) == 0 && position <= (itemCount - spanCount))){
                    // Except last column...
                    outRect.right = mSpace
                }
            }
        }
    }
}