package org.wordpress.android.ui.stats.refresh.lists.sections.insights.usecases

import kotlinx.coroutines.experimental.CoroutineDispatcher
import org.wordpress.android.R
import org.wordpress.android.R.drawable
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.stats.TagsModel
import org.wordpress.android.fluxc.model.stats.TagsModel.TagModel
import org.wordpress.android.fluxc.store.InsightsStore
import org.wordpress.android.fluxc.store.StatsStore.InsightsTypes.TAGS_AND_CATEGORIES
import org.wordpress.android.modules.UI_THREAD
import org.wordpress.android.ui.stats.refresh.lists.NavigationTarget.ViewTag
import org.wordpress.android.ui.stats.refresh.lists.NavigationTarget.ViewTagsAndCategoriesStats
import org.wordpress.android.ui.stats.refresh.lists.sections.BaseStatsUseCase.StatefulUseCase
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.Divider
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.Empty
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.ExpandableItem
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.Link
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.ListItemWithIcon
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.NavigationAction
import org.wordpress.android.ui.stats.refresh.lists.sections.BlockListItem.Title
import org.wordpress.android.ui.stats.refresh.lists.sections.insights.usecases.TagsAndCategoriesUseCase.TagsAndCategoriesUiState
import org.wordpress.android.ui.stats.refresh.utils.toFormattedString
import org.wordpress.android.viewmodel.ResourceProvider
import javax.inject.Inject
import javax.inject.Named

private const val PAGE_SIZE = 6

class TagsAndCategoriesUseCase
@Inject constructor(
    @Named(UI_THREAD) private val mainDispatcher: CoroutineDispatcher,
    private val insightsStore: InsightsStore,
    private val resourceProvider: ResourceProvider
) : StatefulUseCase<TagsModel, TagsAndCategoriesUiState>(
        TAGS_AND_CATEGORIES,
        mainDispatcher,
        TagsAndCategoriesUiState(null)
) {
    private val onLinkClick: () -> Unit = {
        navigateTo(ViewTagsAndCategoriesStats())
    }

    private val onTagClick: (String) -> Unit = {
        navigateTo(ViewTag(it))
    }

    override suspend fun fetchRemoteData(site: SiteModel, forced: Boolean) {
        val response = insightsStore.fetchTags(site, PAGE_SIZE, forced)
        val model = response.model
        val error = response.error

        when {
            error != null -> onError(error.message ?: error.type.name)
            else -> model?.let { onModel(model) }
        }
    }

    override suspend fun loadCachedData(site: SiteModel) {
        val model = insightsStore.getTags(site, PAGE_SIZE)
        model?.let { onModel(model) }
    }

    override fun buildStatefulUiModel(domainModel: TagsModel, uiState: TagsAndCategoriesUiState): List<BlockListItem> {
        val items = mutableListOf<BlockListItem>()
        items.add(Title(R.string.stats_view_tags_and_categories))
        if (domainModel.tags.isEmpty()) {
            items.add(Empty)
        } else {
            val tagsList = mutableListOf<BlockListItem>()
            domainModel.tags.forEachIndexed { index, tag ->
                when {
                    tag.items.size == 1 -> {
                        tagsList.add(mapTag(tag, index, domainModel.tags.size))
                    }
                    else -> {
                        val isExpanded = areTagsEqual(tag, uiState.expandedTag)
                        tagsList.add(ExpandableItem(
                                mapCategory(tag, index, domainModel.tags.size),
                                isExpanded
                        ) { changedExpandedState ->
                            onUiState(uiState.copy(expandedTag = if (changedExpandedState) tag else null))
                        })
                        if (isExpanded) {
                            tagsList.addAll(tag.items.map { subTag -> mapItem(subTag) })
                            tagsList.add(Divider)
                        }
                    }
                }
            }

            items.addAll(tagsList)
            if (domainModel.hasMore) {
                items.add(
                        Link(
                                text = R.string.stats_insights_view_more,
                                navigateAction = NavigationAction.create(onLinkClick)
                        )
                )
            }
        }
        return items
    }

    private fun areTagsEqual(tagA: TagModel, tagB: TagModel?): Boolean {
        return tagA.items == tagB?.items && tagA.views == tagB.views
    }

    private fun mapTag(tag: TagsModel.TagModel, index: Int, listSize: Int): ListItemWithIcon {
        val item = tag.items.first()
        return ListItemWithIcon(
                icon = getIcon(item.type),
                text = item.name,
                value = tag.views.toFormattedString(),
                showDivider = index < listSize - 1,
                navigationAction = NavigationAction.create(item.link, onTagClick)
        )
    }

    private fun mapCategory(tag: TagsModel.TagModel, index: Int, listSize: Int): ListItemWithIcon {
        val text = tag.items.foldIndexed("") { itemIndex, acc, item ->
            when (itemIndex) {
                0 -> item.name
                else -> resourceProvider.getString(R.string.stats_category_folded_name, acc, item.name)
            }
        }
        return ListItemWithIcon(
                icon = R.drawable.ic_folder_multiple_grey_dark_24dp,
                text = text,
                value = tag.views.toFormattedString(),
                showDivider = index < listSize - 1
        )
    }

    private fun mapItem(item: TagModel.Item): ListItemWithIcon {
        return ListItemWithIcon(
                icon = getIcon(item.type),
                text = item.name,
                showDivider = false,
                navigationAction = NavigationAction.create(item.link, onTagClick)
        )
    }

    private fun getIcon(type: String) =
            if (type == "tag") drawable.ic_tag_grey_dark_24dp else drawable.ic_folder_grey_dark_24dp

    data class TagsAndCategoriesUiState(val expandedTag: TagModel? = null)
}
