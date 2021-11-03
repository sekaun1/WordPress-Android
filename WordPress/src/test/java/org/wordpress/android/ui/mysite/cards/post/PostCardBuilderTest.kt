package org.wordpress.android.ui.mysite.cards.post

import kotlinx.coroutines.InternalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.wordpress.android.BaseUnitTest
import org.wordpress.android.R
import org.wordpress.android.ui.mysite.MySiteCardAndItemBuilderParams.PostCardBuilderParams
import org.wordpress.android.ui.mysite.cards.post.mockdata.MockedPostsData
import org.wordpress.android.ui.mysite.cards.post.mockdata.MockedPostsData.Post
import org.wordpress.android.ui.mysite.cards.post.mockdata.MockedPostsData.Posts
import org.wordpress.android.ui.utils.UiString.UiStringRes

private const val POST_ID = "1"

// This class contains placeholder tests until mock data is removed
@InternalCoroutinesApi
class PostCardBuilderTest : BaseUnitTest() {
    private lateinit var builder: PostCardBuilder
    private val post = Post(id = POST_ID)

    @Before
    fun setUp() {
        builder = PostCardBuilder()
    }

    /* DRAFT POST CARD */

    @Test
    fun `given draft post, when post cards are built, then draft post card exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post))

        val postCards = buildPostCards(mockedPostsData)

        assertThat(postCards.filter { it.title == UiStringRes(R.string.my_site_post_card_draft_title) }).isNotNull
    }

    @Test
    fun `given no draft post, when post cards are built, then draft post card not exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = emptyList())

        val postCards = buildPostCards(mockedPostsData)

        assertThat(postCards.filter { it.title == UiStringRes(R.string.my_site_post_card_draft_title) }).isNull()
    }

    /* SCHEDULED POST CARD */

    @Test
    fun `given scheduled post, when post cards are built, then scheduled post card exists`() {
        val mockedPostsData = getMockedPostsData(scheduledPosts = listOf(post))

        val postCards = buildPostCards(mockedPostsData)

        assertThat(postCards.filter { it.title == UiStringRes(R.string.my_site_post_card_scheduled_title) }).isNotNull
    }

    @Test
    fun `given no scheduled post, when post cards are built, then scheduled post card not exists`() {
        val mockedPostsData = getMockedPostsData(scheduledPosts = emptyList())

        val postCards = buildPostCards(mockedPostsData)

        assertThat(postCards.filter { it.title == UiStringRes(R.string.my_site_post_card_scheduled_title) }).isNull()
    }

    private fun buildPostCards(mockedData: MockedPostsData) = builder.build(PostCardBuilderParams(mockedData))

    private fun getMockedPostsData(
        draftPosts: List<Post>? = null,
        scheduledPosts: List<Post>? = null
    ) = MockedPostsData(
            posts = Posts(
                    hasPublishedPosts = true,
                    draft = draftPosts,
                    scheduled = scheduledPosts
            )
    )
}
