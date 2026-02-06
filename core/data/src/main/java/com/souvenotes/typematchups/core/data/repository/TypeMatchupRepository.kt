package com.souvenotes.typematchups.core.data.repository

import com.souvenotes.typematchups.core.data.models.Result
import com.souvenotes.typematchups.core.data.models.TypeMatchup

interface TypeMatchupRepository {
    suspend fun getTypeMatchup(primaryType: String, secondaryType: String?): Result<TypeMatchup>
}