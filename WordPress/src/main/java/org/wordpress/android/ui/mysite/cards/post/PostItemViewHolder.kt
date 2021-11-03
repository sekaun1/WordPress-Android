package org.wordpress.android.ui.mysite.cards.post

import android.view.ViewGroup
import org.wordpress.android.WordPress
import org.wordpress.android.databinding.PostItemBinding
import org.wordpress.android.ui.mysite.MySiteCardAndItem.Card.PostCard.PostItem
import org.wordpress.android.ui.mysite.MySiteCardAndItemViewHolder
import org.wordpress.android.ui.utils.UiHelpers
import org.wordpress.android.util.image.ImageManager
import org.wordpress.android.util.image.ImageType.PHOTO_ROUNDED_CORNERS
import org.wordpress.android.util.setVisible
import org.wordpress.android.util.viewBinding

class PostItemViewHolder(
    parent: ViewGroup,
    private val imageManager: ImageManager,
    private val uiHelpers: UiHelpers
) : MySiteCardAndItemViewHolder<PostItemBinding>(
        parent.viewBinding(PostItemBinding::inflate)
) {
    fun bind(postItem: PostItem) = with(binding) {
        uiHelpers.setTextOrHide(title, postItem.title)
        uiHelpers.setTextOrHide(excerpt, postItem.excerpt)
        imageManager.loadImageWithCorners(
                featuredImage,
                PHOTO_ROUNDED_CORNERS,
                postItem.featuredImageUrl ?: "",
                uiHelpers.getPxOfUiDimen(WordPress.getContext(), postItem.featuredImageCornerRadius)
        )
        iconTime.setVisible(postItem.isTimeIconVisible)
    }
}
