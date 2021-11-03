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
import org.wordpress.android.ui.utils.UiString.UiStringText

private const val POST_ID = "1"
private const val POST_TITLE = "title"
private const val POST_EXCERPT = "excerpt"
private const val FEATURED_IMAGE_URL = "/image/url"

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

    /* POST CARD ITEM - TITLE */

    @Test
    fun `given post with title, when post card is built, then post title exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(title = POST_TITLE)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().title).isEqualTo(UiStringText(POST_TITLE))
    }

    @Test
    fun `given post without title, when draft post card is built, then untitled title exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(title = null)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().title).isEqualTo(UiStringRes(R.string.my_site_untitled_post))
    }

    /* POST CARD ITEM - EXCERPT */

    @Test
    fun `given post with excerpt, when post card is built, then excerpt exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(excerpt = POST_EXCERPT)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().excerpt).isEqualTo(UiStringText(POST_EXCERPT))
    }

    @Test
    fun `given post without excerpt, when post card is built, then excerpt not exists`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(excerpt = null)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().excerpt).isNull()
    }

    /* POST CARD ITEM - FEATURED IMAGE */

    @Test
    fun `given post with featured image, when post card is built, then featured image visible`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(featuredImageUrl = FEATURED_IMAGE_URL)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().isFeaturedImageVisible).isTrue
    }

    @Test
    fun `given post without featured image, when post card is built, then featured image not visible`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post.copy(featuredImageUrl = null)))

        val postCard = buildPostCards(mockedPostsData).first()

        assertThat(postCard.postItems.first().isFeaturedImageVisible).isFalse
    }

    /* POST CARD ITEM - TIME ICON */

    @Test
    fun `given draft post, when draft post card is built, then time icon is not visible`() {
        val mockedPostsData = getMockedPostsData(draftPosts = listOf(post))

        val draftPostCard = buildPostCards(mockedPostsData).first()

        assertThat(draftPostCard.postItems.first().isTimeIconVisible).isFalse
    }

    @Test
    fun `given scheduled post, when scheduled post card is built, then time icon is visible`() {
        val mockedPostsData = getMockedPostsData(scheduledPosts = listOf(post))

        val scheduledPostCard = buildPostCards(mockedPostsData).first()

        assertThat(scheduledPostCard.postItems.first().isTimeIconVisible).isTrue
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
