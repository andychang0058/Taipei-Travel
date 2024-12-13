package com.cathaybk.travel.viewmodel.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cathaybk.travel.model.News
import com.cathaybk.travel.repo.TravelRepo

class NewsPagingSource(
    private val travelRepo: TravelRepo,
) : PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val page = params.key ?: 1
            travelRepo.getEventNews(page).fold(
                onSuccess = {
                    val nextKey = if (it.data?.isEmpty() == true ||
                        (it.data?.size ?: 0) < params.loadSize == true
                    ) {
                        null
                    } else {
                        page + 1
                    }
                    LoadResult.Page(
                        data = it.data ?: emptyList(),
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = nextKey,
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}