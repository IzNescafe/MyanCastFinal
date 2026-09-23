// app/src/test/java/com/example/myancast/ui/news/NewsViewModelTest.kt
package com.example.myancast.ui.news

import com.example.myancast.FakeNewsRepository
import com.example.myancast.domain.model.NewsItem
import com.example.myancast.ui.home.ALL_CATEGORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val fakeNews = listOf(
        NewsItem(id = "1", headline = "သတင်း ၁", category = "နိုင်ငံရေး"),
        NewsItem(id = "2", headline = "သတင်း ၂", category = "အားကစား"),
        NewsItem(id = "3", headline = "သတင်း ၃", category = "နိုင်ငံရေး")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(fakeNews))
        assertTrue(vm.state.value.isLoading)
    }

    @Test
    fun `after load, news and categories are filled`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(fakeNews))
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals(3, state.news.size)
        assertEquals(listOf(ALL_CATEGORY, "နိုင်ငံရေး", "အားကစား"), state.categories)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `selectCategory filters the list`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(fakeNews))
        advanceUntilIdle()

        vm.selectCategory("နိုင်ငံရေး")

        val state = vm.state.value
        assertEquals("နိုင်ငံရေး", state.selectedCategory)
        assertEquals(listOf("1", "3"), state.news.map { it.id })
        assertEquals(3, state.allNews.size)          // မူရင်း list မပျောက်
    }

    @Test
    fun `featured is first item and rest skips it`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(fakeNews))
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals("1", state.featured?.id)
        assertEquals(listOf("2", "3"), state.rest.map { it.id })
    }

    @Test
    fun `featured is null when list is empty`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(emptyList()))
        advanceUntilIdle()

        assertNull(vm.state.value.featured)
        assertTrue(vm.state.value.rest.isEmpty())
    }

    @Test
    fun `repository error shows message and stops loading`() = runTest {
        val vm = NewsViewModel(FakeNewsRepository(error = RuntimeException("no network")))
        advanceUntilIdle()

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.news.isEmpty())
    }
}
