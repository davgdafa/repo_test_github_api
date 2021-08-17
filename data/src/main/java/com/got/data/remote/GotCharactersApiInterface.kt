package com.got.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface GotCharactersApiInterface {
    @GET("api/v2/Characters")
    suspend fun getGotCharacters(): Response<List<CharacterData>?>
}
