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
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePagingData> {
        return try {
            val page = params.key ?: 1
            if (page == 1) {
                return loadFirstPaging(params)
            }

            val resultData = mutableListOf<HomePagingData>()
            travelRepo.getAttractions(page = page).fold(
                onSuccess = {
                    it.data?.forEach { attraction ->
                        resultData.add(HomePagingData.AttractionData(attraction))
                    }
                },
                onFailure = {
                    return LoadResult.Error(it)
                }
            )

            return LoadResult.Page(
                data = resultData,
                prevKey = page - 1,
                nextKey = if (resultData.isEmpty() || resultData.size < params.loadSize) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun loadFirstPaging(params: LoadParams<Int>): LoadResult<Int, HomePagingData> {
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

            val data = mutableListOf<HomePagingData>().apply {
                add(HomePagingData.NewsData(newsResult.getOrNull()?.data ?: emptyList()))
                attractionsResult.getOrNull()?.data?.forEach { attraction ->
                    add(HomePagingData.AttractionData(attraction))
                }
            }

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.size < params.loadSize) null else 2,
            )
        }
        return loadResult
    }
}