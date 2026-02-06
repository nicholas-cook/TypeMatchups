package com.souvenotes.typematchups.core.data.models

import com.souvenotes.typematchups.TypeMatchupQuery
import com.souvenotes.typematchups.core.localstorage.entity.TypeMatchupEntity
import com.souvenotes.typematchups.core.localstorage.entity.TypeMatchupEntity.Companion.VALUE_DELIMITER

enum class PokemonTypes(val value: String) {
    BUG("bug"),
    DARK("dark"),
    DRAGON("dragon"),
    ELECTRIC("electric"),
    FAIRY("fairy"),
    FIGHTING("fighting"),
    FIRE("fire"),
    FLYING("flying"),
    GHOST("ghost"),
    GRASS("grass"),
    GROUND("ground"),
    ICE("ice"),
    NORMAL("normal"),
    POISON("poison"),
    PSYCHIC("psychic"),
    ROCK("rock"),
    STEEL("steel"),
    WATER("water"),
    UNKNOWN("unknown");

    companion object {
        fun safeFromName(fromName: String): PokemonTypes {
            return entries.firstOrNull { it.value == fromName.lowercase() } ?: UNKNOWN
        }
    }
}

data class TypeMatchup(
    val weakTo: List<PokemonTypes> = emptyList(),
    val doubleWeakTo: List<PokemonTypes> = emptyList(),
    val resistantTo: List<PokemonTypes> = emptyList(),
    val doubleResistantTo: List<PokemonTypes> = emptyList(),
    val immuneTo: List<PokemonTypes> = emptyList(),
    val neutral: List<PokemonTypes> = emptyList()
) {
    companion object {
        fun fromTypeMatchupResponse(defending: TypeMatchupQuery.Defending): TypeMatchup {
            return TypeMatchup(
                weakTo = defending.effectiveTypes.map { PokemonTypes.safeFromName(it) },
                doubleWeakTo = defending.doubleEffectiveTypes.map { PokemonTypes.safeFromName(it) },
                resistantTo = defending.resistedTypes.map { PokemonTypes.safeFromName(it) },
                doubleResistantTo = defending.doubleResistedTypes.map { PokemonTypes.safeFromName(it) },
                immuneTo = defending.effectlessTypes.map { PokemonTypes.safeFromName(it) },
                neutral = defending.normalTypes.map { PokemonTypes.safeFromName(it) }
            )
        }

        fun fromTypeMatchupEntity(typeMatchup: TypeMatchupEntity): TypeMatchup {
            return TypeMatchup(
                weakTo = typeMatchup.weakTo.split(VALUE_DELIMITER).filter { it.isNotEmpty() }
                    .map { PokemonTypes.safeFromName(it) }.ifEmpty { emptyList() },
                doubleWeakTo = typeMatchup.doubleWeakTo.split(VALUE_DELIMITER)
                    .filter { it.isNotEmpty() }.map { PokemonTypes.safeFromName(it) }
                    .ifEmpty { emptyList() },
                resistantTo = typeMatchup.resistantTo.split(VALUE_DELIMITER)
                    .filter { it.isNotEmpty() }.map { PokemonTypes.safeFromName(it) }
                    .ifEmpty { emptyList() },
                doubleResistantTo = typeMatchup.doubleResistantTo.split(VALUE_DELIMITER)
                    .filter { it.isNotEmpty() }.map { PokemonTypes.safeFromName(it) }
                    .ifEmpty { emptyList() },
                immuneTo = typeMatchup.immuneTo.split(VALUE_DELIMITER).filter { it.isNotEmpty() }
                    .map { PokemonTypes.safeFromName(it) }.ifEmpty { emptyList() },
                neutral = typeMatchup.neutral.split(VALUE_DELIMITER).filter { it.isNotEmpty() }
                    .map { PokemonTypes.safeFromName(it) }.ifEmpty { emptyList() }
            )
        }
    }
}