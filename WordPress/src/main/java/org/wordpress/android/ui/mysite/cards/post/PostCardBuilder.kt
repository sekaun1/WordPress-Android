package org.wordpress.android.ui.mysite.cards.post

import org.wordpress.android.R
import org.wordpress.android.ui.mysite.MySiteCardAndItem.Card.PostCard
import org.wordpress.android.ui.mysite.MySiteCardAndItem.Card.PostCard.PostItem
import org.wordpress.android.ui.mysite.MySiteCardAndItemBuilderParams.PostCardBuilderParams
import org.wordpress.android.ui.mysite.cards.post.mockdata.MockedPostsData.Post
import org.wordpress.android.ui.utils.UiString.UiStringRes
import org.wordpress.android.ui.utils.UiString.UiStringText
import javax.inject.Inject

class PostCardBuilder @Inject constructor() {
    fun build(params: PostCardBuilderParams): List<PostCard> {
        val cards = mutableListOf<PostCard>()
        params.mockedPostsData?.posts?.draft?.mapToPostItems()?.let {
            cards.add(
                    PostCard(
                            title = UiStringRes(R.string.my_site_post_card_draft_title),
                            postItems = it
                    )
            )
        }
        params.mockedPostsData?.posts?.scheduled?.mapToPostItems()?.let {
            cards.add(
                    PostCard(
                            title = UiStringRes(R.string.my_site_post_card_scheduled_title),
                            postItems = it
                    )
            )
        }
        return cards
    }

    private fun List<Post>.mapToPostItems() = this.map { post ->
        PostItem(
                title = post.title?.let { UiStringText(it) },
                description = post.description?.let { UiStringText(it) },
                featuredImageUrl = post.featuredImageUrl
        )
    }
}
