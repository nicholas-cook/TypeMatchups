package com.souvenotes.typematchups

import android.util.Log
import com.souvenotes.typematchups.core.data.models.Result
import com.souvenotes.typematchups.core.data.models.TypeMatchup
import com.souvenotes.typematchups.core.data.repository.TypeMatchupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.mock
import org.mockito.kotlin.mockStatic
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TypeMatchupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: TypeMatchupRepository
    private lateinit var viewModel: TypeMatchupViewModel

    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(TypeMatchupRepository::class.java)
        viewModel = TypeMatchupViewModel(repository)
        logMock = mockStatic<Log>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        logMock.close()
    }

    @Test
    fun `onEvent with PokemonType1Selected updates state and triggers repository call`() = runTest {
        val type = DisplayPokemonType.FIRE
        val matchup = TypeMatchup(
            doubleWeakTo = emptyList(),
            weakTo = emptyList(),
            neutral = emptyList(),
            immuneTo = emptyList(),
            resistantTo = emptyList(),
            doubleResistantTo = emptyList()
        )
        whenever(repository.getTypeMatchup(type.value, null)).thenReturn(Result.Success(matchup))

        viewModel.onEvent(TypeMatchupEvent.PokemonType1Selected(type))
        testDispatcher.scheduler.advanceUntilIdle()
        val updatedState = viewModel.typeMatchupScreenState.value
        assertEquals(type, updatedState.pokemonType1)
        assertTrue(updatedState.effectiveness is EffectivenessState.Success)
    }

    @Test
    fun `onEvent with PokemonType1Selected NONE sets effectiveness to Empty`() = runTest {
        viewModel.onEvent(TypeMatchupEvent.PokemonType1Selected(DisplayPokemonType.NONE))
        testDispatcher.scheduler.advanceUntilIdle()
        val updatedState = viewModel.typeMatchupScreenState.value
        assertEquals(EffectivenessState.Empty, updatedState.effectiveness)
    }

    @Test
    fun `repository error sets effectiveness to Error`() = runTest {
        val type = DisplayPokemonType.FIRE
        whenever(repository.getTypeMatchup(type.value, null)).thenReturn(Result.Error("error!"))

        viewModel.onEvent(TypeMatchupEvent.PokemonType1Selected(type))
        testDispatcher.scheduler.advanceUntilIdle()
        val updatedState = viewModel.typeMatchupScreenState.value
        assertTrue(updatedState.effectiveness is EffectivenessState.Error)
    }
}