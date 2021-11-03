package org.wordpress.android.ui.mysite.cards.post

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.Callback
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.wordpress.android.ui.mysite.MySiteCardAndItem.Card.PostCard.PostItem
import org.wordpress.android.ui.utils.UiHelpers

class PostItemsAdapter(
    private val uiHelpers: UiHelpers
) : Adapter<PostItemViewHolder>() {
    private val items = mutableListOf<PostItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        return PostItemViewHolder(parent, uiHelpers)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun update(newItems: List<PostItem>) {
        val diffResult = DiffUtil.calculateDiff(PostItemsDiffUtil(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class PostItemsDiffUtil(
        private val oldList: List<PostItem>,
        private val newList: List<PostItem>
    ) : Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newList[newItemPosition]
            val oldItem = oldList[oldItemPosition]

            return (oldItem == newItem)
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
    }
}
