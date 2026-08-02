package net.farman.graphqldemo.appoloclient

import com.apollographql.apollo.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://countries.trevorblades.com/graphql")
    .httpExposeErrorBody(true)
    .build()
