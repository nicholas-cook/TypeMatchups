package com.souvenotes.typematchups

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.souvenotes.typematchups.core.data.models.PokemonTypes
import com.souvenotes.typematchups.ui.theme.BugType
import com.souvenotes.typematchups.ui.theme.DarkType
import com.souvenotes.typematchups.ui.theme.DragonType
import com.souvenotes.typematchups.ui.theme.EkansLightPurple
import com.souvenotes.typematchups.ui.theme.ElectricType
import com.souvenotes.typematchups.ui.theme.FairyType
import com.souvenotes.typematchups.ui.theme.FightingType
import com.souvenotes.typematchups.ui.theme.FireType
import com.souvenotes.typematchups.ui.theme.FlyingType
import com.souvenotes.typematchups.ui.theme.GhostType
import com.souvenotes.typematchups.ui.theme.GrassType
import com.souvenotes.typematchups.ui.theme.GroundType
import com.souvenotes.typematchups.ui.theme.IceType
import com.souvenotes.typematchups.ui.theme.NormalType
import com.souvenotes.typematchups.ui.theme.PoisonType
import com.souvenotes.typematchups.ui.theme.PsychicType
import com.souvenotes.typematchups.ui.theme.RockType
import com.souvenotes.typematchups.ui.theme.SteelType
import com.souvenotes.typematchups.ui.theme.WaterType

enum class DisplayPokemonType(
    @StringRes val displayString: Int,
    val value: String,
    val backgroundColor: Color
) {
    BUG(R.string.type_bug, "bug", BugType),
    DARK(R.string.type_dark, "dark", DarkType),
    DRAGON(R.string.type_dragon, "dragon", DragonType),
    ELECTRIC(R.string.type_electric, "electric", ElectricType),
    FAIRY(R.string.type_fairy, "fairy", FairyType),
    FIGHTING(R.string.type_fighting, "fighting", FightingType),
    FIRE(R.string.type_fire, "fire", FireType),
    FLYING(R.string.type_flying, "flying", FlyingType),
    GHOST(R.string.type_ghost, "ghost", GhostType),
    GRASS(R.string.type_grass, "grass", GrassType),
    GROUND(R.string.type_ground, "ground", GroundType),
    ICE(R.string.type_ice, "ice", IceType),
    NORMAL(R.string.type_normal, "normal", NormalType),
    POISON(R.string.type_poison, "poison", PoisonType),
    PSYCHIC(R.string.type_psychic, "psychic", PsychicType),
    ROCK(R.string.type_rock, "rock", RockType),
    STEEL(R.string.type_steel, "steel", SteelType),
    WATER(R.string.type_water, "water", WaterType),
    NONE(R.string.type_none, "none", EkansLightPurple);

    companion object {
        fun fromPokemonType(pokemonType: PokemonTypes): DisplayPokemonType {
            return entries.firstOrNull { it.value == pokemonType.value } ?: NONE
        }
    }
}

fun DisplayPokemonType.isNone(): Boolean {
    return this == DisplayPokemonType.NONE
}

sealed interface EffectivenessState {
    data class Success(val matchups: Map<String, List<DisplayPokemonType>>) : EffectivenessState
    data class Error(val errorMessage: String) : EffectivenessState
    data object Loading : EffectivenessState
    data object Empty : EffectivenessState
}