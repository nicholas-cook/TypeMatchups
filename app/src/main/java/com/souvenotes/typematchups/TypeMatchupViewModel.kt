package com.souvenotes.typematchups

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvenotes.typematchups.core.data.models.Result
import com.souvenotes.typematchups.core.data.repository.TypeMatchupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeMatchupViewModel @Inject constructor(private val typeMatchupRepository: TypeMatchupRepository) :
    ViewModel() {

    private val _typeMatchupScreenState = MutableStateFlow(TypeMatchupScreenState())
    val typeMatchupScreenState = _typeMatchupScreenState.asStateFlow()

    fun onEvent(event: TypeMatchupEvent) {
        _typeMatchupScreenState.value = _typeMatchupScreenState.value.reduce(event)
        getTypeMatchup()
    }

    private fun getTypeMatchup() {
        val type1 = _typeMatchupScreenState.value.pokemonType1
        if (type1.isNone()) {
            _typeMatchupScreenState.value =
                _typeMatchupScreenState.value.copy(effectiveness = EffectivenessState.Empty)
            return
        }
        val type2 = if (_typeMatchupScreenState.value.pokemonType2.isNone()) {
            null
        } else {
            _typeMatchupScreenState.value.pokemonType2
        }
        viewModelScope.launch {
            _typeMatchupScreenState.value =
                _typeMatchupScreenState.value.copy(effectiveness = EffectivenessState.Loading)
            when (val result = typeMatchupRepository.getTypeMatchup(type1.value, type2?.value)) {
                is Result.Success -> {
                    val typeMatchup = result.data
                    val typeMatchups = linkedMapOf(
                        "4" to typeMatchup.doubleWeakTo.map { DisplayPokemonType.fromPokemonType(it) },
                        "2" to typeMatchup.weakTo.map { DisplayPokemonType.fromPokemonType(it) },
                        "1" to typeMatchup.neutral.map { DisplayPokemonType.fromPokemonType(it) },
                        "0" to typeMatchup.immuneTo.map { DisplayPokemonType.fromPokemonType(it) },
                        "1/2" to typeMatchup.resistantTo.map { DisplayPokemonType.fromPokemonType(it) },
                        "1/4" to typeMatchup.doubleResistantTo.map {
                            DisplayPokemonType.fromPokemonType(
                                it
                            )
                        }
                    )
                    _typeMatchupScreenState.value = _typeMatchupScreenState.value.copy(
                        effectiveness = EffectivenessState.Success(typeMatchups.filterValues { it.isNotEmpty() })
                    )
                }

                is Result.Error -> {
                    Log.e("TypeMatchupViewModel", result.errorMessage.orEmpty())
                    _typeMatchupScreenState.value = _typeMatchupScreenState.value.copy(
                        effectiveness = EffectivenessState.Error(result.errorMessage.orEmpty())
                    )
                }
            }
        }
    }
}

data class TypeMatchupScreenState(
    val pokemonType1: DisplayPokemonType = DisplayPokemonType.NONE,
    val pokemonType2: DisplayPokemonType = DisplayPokemonType.NONE,
    val moveType: DisplayPokemonType = DisplayPokemonType.NONE,
    val typeOptions: List<DisplayPokemonType> = DisplayPokemonType.entries,
    val type1Options: List<DisplayPokemonType> = DisplayPokemonType.entries,
    val type2Options: List<DisplayPokemonType> = emptyList(),
    val type2Enabled: Boolean = false,
    val effectiveness: EffectivenessState = EffectivenessState.Empty
) {
    fun reduce(event: TypeMatchupEvent): TypeMatchupScreenState {
        return when (event) {
            is TypeMatchupEvent.PokemonType1Selected -> copy(
                pokemonType1 = event.type,
                type2Options = if (event.type != DisplayPokemonType.NONE) {
                    type1Options - event.type
                } else {
                    type1Options
                },
                type2Enabled = event.type != DisplayPokemonType.NONE,
                pokemonType2 = if (event.type == DisplayPokemonType.NONE) {
                    DisplayPokemonType.NONE
                } else {
                    pokemonType2
                }
            )

            is TypeMatchupEvent.PokemonType2Selected -> copy(
                pokemonType2 = event.type,
                type1Options = typeOptions - event.type
            )
        }
    }
}

sealed interface TypeMatchupEvent {
    data class PokemonType1Selected(val type: DisplayPokemonType) : TypeMatchupEvent
    data class PokemonType2Selected(val type: DisplayPokemonType) : TypeMatchupEvent
}