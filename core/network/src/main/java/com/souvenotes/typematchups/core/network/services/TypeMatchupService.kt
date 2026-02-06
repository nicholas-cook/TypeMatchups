package com.souvenotes.typematchups.core.network.services

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.souvenotes.typematchups.TypeMatchupQuery
import com.souvenotes.typematchups.type.TypesEnum
import javax.inject.Inject

class TypeMatchupService @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getTypeMatchup(
        primaryType: String,
        secondaryType: String?
    ): ApolloResponse<TypeMatchupQuery.Data> {
        val secondaryTypeEnum = if (secondaryType != null) {
            TypesEnum.safeValueOf(secondaryType)
        } else {
            null
        }
        return apolloClient.query(
            TypeMatchupQuery(
                TypesEnum.safeValueOf(primaryType),
                Optional.presentIfNotNull(secondaryTypeEnum)
            )
        ).execute()
    }
}