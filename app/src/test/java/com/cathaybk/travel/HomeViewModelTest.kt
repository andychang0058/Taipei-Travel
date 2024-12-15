package com.cathaybk.travel

import com.cathaybk.travel.manager.language.Language
import com.cathaybk.travel.manager.language.LanguageManager
import com.cathaybk.travel.model.Attraction
import com.cathaybk.travel.model.AttractionResponse
import com.cathaybk.travel.model.LoadIntent
import com.cathaybk.travel.model.News
import com.cathaybk.travel.model.NewsResponse
import com.cathaybk.travel.model.RequestState
import com.cathaybk.travel.repo.TravelRepo
import com.cathaybk.travel.viewmodel.home.HomePagingData
import com.cathaybk.travel.viewmodel.home.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var languageManager: LanguageManager

    @MockK
    lateinit var travelRepo: TravelRepo

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)

        every { languageManager.selectedLanguage } returns MutableStateFlow(Language.English)
        every { languageManager.displayLanguage } returns MutableStateFlow(Language.English)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should update to success state when both API calls succeed`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.success(NewsResponse(total = 1, data = listOf(News(id = "1"))))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.success(AttractionResponse(total = 1, data = listOf(Attraction(id = "1"))))

        // ViewModel calls refresh() in init(), so we need to create the instance here
        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)

        coVerify(exactly = 1) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
    }

    @Test
    fun `should update to error state when both API calls fail`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.failure(Exception("News API failed"))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.failure(Exception("Attractions API failed"))

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.RefreshError)

        homeViewModel.loadIntent(LoadIntent.Retry)
        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.RefreshError)

        coVerify(exactly = 2) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 2) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
    }

    @Test
    fun `should update to success state when only news API call fails`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.failure(Exception("News API failed"))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.success(AttractionResponse(total = 1, data = listOf(Attraction(id = "1"))))

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state
        fun currData() = homeViewModel.uiState.value.data

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)
        assert(currData() ?.size == 1)
        assert(currData() ?.firstOrNull() is HomePagingData.AttractionData)

        coVerify(exactly = 1) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
    }

    @Test
    fun `should update to success state when only attraction API call fails`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.success(NewsResponse(total = 1, data = listOf(News(id = "1"))))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.failure(Exception("Attractions API failed"))

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state
        fun currData() = homeViewModel.uiState.value.data

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)
        assert(currData()?.size == 1)
        assert(currData()?.firstOrNull() is HomePagingData.NewsData)

        coVerify(exactly = 1) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
    }

    @Test
    fun `test refresh`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.success(NewsResponse(total = 1, data = listOf(News(id = "1"))))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.success(AttractionResponse(total = 1, data = listOf(Attraction(id = "1"))))

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)

        homeViewModel.loadIntent(LoadIntent.Refresh)
        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)

        coVerify(exactly = 2) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 2) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
    }

    @Test
    fun `test load more success`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.success(NewsResponse(total = 30, data = listOf(News(id = "1"))))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.success(
                    AttractionResponse(
                        total = 45,
                        data = List(30) { index -> Attraction(id = index.toString()) }
                    )
                )
        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE + 1) } returns
                Result.success(
                    AttractionResponse(
                        total = 45,
                        data = List(10) { index -> Attraction(id = (30 + index).toString()) }
                    )
                )

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state
        fun currData() = homeViewModel.uiState.value.data

        assert(currState() is RequestState.Refresh)

        // Trigger first load more and expect it to succeed and has next page
        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)
        assert((currState() as? RequestState.Success)?.isReachedEnd == false)
        assert(currData()?.size == 31)

        // Trigger second load more and expect it to succeed and reached end
        homeViewModel.loadIntent(LoadIntent.LoadMore)
        assert(currState() is RequestState.Loading)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)
        assert((currState() as? RequestState.Success)?.isReachedEnd == true)
        assert(currData()?.size == 41)

        // Load more should not be called again when reached end
        homeViewModel.loadIntent(LoadIntent.LoadMore)
        assert(currState() !is RequestState.Loading)

        coVerify(exactly = 1) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE + 1) }
        coVerify(exactly = 0) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE + 2) }
    }

    @Test
    fun `test load more failed`() {
        coEvery { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) } returns
                Result.success(NewsResponse(total = 30, data = listOf(News(id = "1"))))

        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) } returns
                Result.success(
                    AttractionResponse(
                        total = 45,
                        data = List(30) { index -> Attraction(id = index.toString()) }
                    )
                )
        coEvery { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE + 1) } returns
                Result.failure(Exception("Attractions API paging failed"))

        val homeViewModel =
            HomeViewModel(travelRepo = travelRepo, languageManager = languageManager)
        fun currState() = homeViewModel.uiState.value.state
        fun currData() = homeViewModel.uiState.value.data

        assert(currState() is RequestState.Refresh)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.Success)
        assert((currState() as? RequestState.Success)?.isReachedEnd == false)
        assert(currData()?.size == 31)

        // Trigger first load more and expect it to fail
        homeViewModel.loadIntent(LoadIntent.LoadMore)
        assert(currState() is RequestState.Loading)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.LoadError)

        // Retry load more and should increase page
        homeViewModel.loadIntent(LoadIntent.RetryLoadMore)
        assert(currState() is RequestState.Loading)

        homeViewModel.uiState.waitUntilChanged()
        assert(currState() is RequestState.LoadError)

        coVerify(exactly = 1) { travelRepo.getEventNews(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 1) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE) }
        coVerify(exactly = 2) { travelRepo.getAttractions(TravelRepo.INITIAL_PAGE + 1) }
    }
}