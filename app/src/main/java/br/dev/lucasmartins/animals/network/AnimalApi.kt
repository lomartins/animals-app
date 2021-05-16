package br.dev.lucasmartins.animals.network

import br.dev.lucasmartins.animals.model.Animal
import br.dev.lucasmartins.animals.model.ApiKeyResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {

    @GET("getKey")
    fun getApiKey(): Single<ApiKeyResponse>

    @FormUrlEncoded
    @POST("getAnimals")
    fun getAnimals(@Field("key") key: String): Single<List<Animal>>
}