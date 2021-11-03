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
        val posts = params.mockedPostsData?.posts
        posts?.draft?.mapToPostItems(PostType.DRAFT)?.let {
            cards.add(
                    PostCard(
                            title = UiStringRes(R.string.my_site_post_card_draft_title),
                            postItems = it
                    )
            )
        }
        posts?.scheduled?.mapToPostItems(PostType.SCHEDULED)?.let {
            cards.add(
                    PostCard(
                            title = UiStringRes(R.string.my_site_post_card_scheduled_title),
                            postItems = it
                    )
            )
        }
        return cards
    }

    private fun List<Post>.mapToPostItems(postType: PostType) = this.map { post ->
        val excerpt = if (postType == PostType.SCHEDULED) {
            "Today at 1:04 PM" // TODO: ashiagr - remove hardcoded text
        } else {
            post.excerpt
        }

        PostItem(
                postType = postType,
                title = post.title?.let { UiStringText(it) } ?: UiStringRes(R.string.my_site_untitled_post),
                excerpt = excerpt?.let { UiStringText(it) },
                featuredImageUrl = post.featuredImageUrl
        )
    }
}
