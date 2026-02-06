package com.souvenotes.typematchups.core.localstorage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_matchups")
data class TypeMatchupEntity(
    @PrimaryKey
    val matchupKey: String,
    val weakTo: String,
    val doubleWeakTo: String,
    val resistantTo: String,
    val doubleResistantTo: String,
    val immuneTo: String,
    val neutral: String,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    companion object {
        const val VALUE_DELIMITER = "|||"
        const val MAX_ENTITY_AGE = 1000L * 60L * 60L * 24L * 30L // 30 days

        fun getMatchupKey(type1: String, type2: String?): String {
            if (type2 == null) {
                return type1
            }
            val sortedTypes = listOf(type1, type2).sorted()
            return "${sortedTypes[0]}$VALUE_DELIMITER${sortedTypes[1]}"
        }
    }
}
