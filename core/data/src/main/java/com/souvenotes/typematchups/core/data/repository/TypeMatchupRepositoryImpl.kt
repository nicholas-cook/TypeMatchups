package com.souvenotes.typematchups.core.data.repository

import com.souvenotes.typematchups.core.data.models.Result
import com.souvenotes.typematchups.core.data.models.TypeMatchup
import com.souvenotes.typematchups.core.localstorage.dao.TypeMatchupDao
import com.souvenotes.typematchups.core.localstorage.entity.TypeMatchupEntity
import com.souvenotes.typematchups.core.network.services.TypeMatchupService
import javax.inject.Inject

class TypeMatchupRepositoryImpl @Inject constructor(
    private val typeMatchupService: TypeMatchupService,
    private val typeMatchupDao: TypeMatchupDao
) : TypeMatchupRepository {

    override suspend fun getTypeMatchup(
        primaryType: String,
        secondaryType: String?
    ): Result<TypeMatchup> {
        return try {
            val matchupKey = TypeMatchupEntity.getMatchupKey(primaryType, secondaryType)
            val localTypeMatchup = typeMatchupDao.getTypeMatchupForMatchupKey(matchupKey)
            if (localTypeMatchup != null && System.currentTimeMillis() - localTypeMatchup.lastUpdated < TypeMatchupEntity.MAX_ENTITY_AGE) {
                return Result.Success(TypeMatchup.fromTypeMatchupEntity(localTypeMatchup))
            }

            val response = typeMatchupService.getTypeMatchup(primaryType, secondaryType)
            val data = response.data
            if (data != null) {
                val typeMatchupEntity = TypeMatchupEntity(
                    matchupKey = matchupKey,
                    weakTo = data.getTypeMatchup.defending.effectiveTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    ),
                    doubleWeakTo = data.getTypeMatchup.defending.doubleEffectiveTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    ),
                    resistantTo = data.getTypeMatchup.defending.resistedTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    ),
                    doubleResistantTo = data.getTypeMatchup.defending.doubleResistedTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    ),
                    immuneTo = data.getTypeMatchup.defending.effectlessTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    ),
                    neutral = data.getTypeMatchup.defending.normalTypes.joinToString(
                        TypeMatchupEntity.VALUE_DELIMITER
                    )
                )
                typeMatchupDao.insertTypeMatchup(typeMatchupEntity)
                Result.Success(TypeMatchup.fromTypeMatchupResponse(data.getTypeMatchup.defending))
            } else {
                if (localTypeMatchup != null) {
                    Result.Success(TypeMatchup.fromTypeMatchupEntity(localTypeMatchup))
                } else {
                    Result.Error("No data found")
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}