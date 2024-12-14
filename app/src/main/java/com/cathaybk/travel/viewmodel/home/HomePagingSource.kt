package com.cathaybk.travel.viewmodel.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cathaybk.travel.repo.TravelRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class HomePagingSource(
    private val travelRepo: TravelRepo,
) : PagingSource<Int, HomePagingData>() {

    override fun getRefreshKey(state: PagingState<Int, HomePagingData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingData> {
        return try {
            val page = params.key ?: 1
            if (page == 1) loadFirstPaging() else loadNextPage(page, params)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadNextPage(
        page: Int,
        params: LoadParams<Int>
    ): LoadResult<Int, HomePagingData> = withContext(Dispatchers.IO) {

        travelRepo.getAttractions(page = page).fold(
            onSuccess = { response ->
                val attractions = response.data.orEmpty()
                val nextKey =
                    if (attractions.isEmpty() || attractions.size < params.loadSize) null else page + 1

                LoadResult.Page(
                    data = attractions.map { HomePagingData.AttractionData(it) },
                    prevKey = null,
                    nextKey = nextKey,
                )
            },
            onFailure = { error ->
                LoadResult.Error(error)
            }
        )
    }

    private suspend fun loadFirstPaging(): LoadResult<Int, HomePagingData> {
        val loadResult: LoadResult<Int, HomePagingData> = withContext(Dispatchers.IO) {
            val newsDeferred = async { travelRepo.getEventNews(1) }
            val attractionsDeferred = async { travelRepo.getAttractions(1) }

            val newsResult = newsDeferred.await()
            val attractionsResult = attractionsDeferred.await()

            if (newsResult.isFailure && attractionsResult.isFailure) {
                val error = newsResult.exceptionOrNull()
                    ?: attractionsResult.exceptionOrNull()
                    ?: Exception("Unknown error")
                return@withContext LoadResult.Error(error)
            }

            val data = buildList {
                add(HomePagingData.NewsData(newsResult.getOrNull()?.data ?: emptyList()))
                attractionsResult.getOrNull()?.data?.forEach { attraction ->
                    add(HomePagingData.AttractionData(attraction))
                }
            }

            val attractionSize = data.count { it is HomePagingData.AttractionData }

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (attractionSize <= 0) null else 2,
            )
        }
        return loadResult
    }
}