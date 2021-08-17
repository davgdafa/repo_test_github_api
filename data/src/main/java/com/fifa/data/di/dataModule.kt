package com.fifa.data.di

import com.fifa.data.FifaPlayersRepositoryImpl
import com.fifa.data.local.FifaPlayersLocalDataSource
import com.fifa.data.local.FifaPlayersLocalDataSourceImpl
import com.fifa.data.remote.FifaPlayersApiInterface
import com.fifa.data.remote.FifaPlayersRemoteDataSource
import com.fifa.data.remote.FifaPlayersRemoteDataSourceImpl
import com.fifa.data.remote.FifaPlayersService
import com.fifa.data.remote.FifaPlayersServiceImpl
import com.fifa.domain.data.FifaPlayersRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<FifaPlayersRepository> { FifaPlayersRepositoryImpl(get(), get()) }
    factory<FifaPlayersRemoteDataSource> { FifaPlayersRemoteDataSourceImpl(get()) }
    factory<FifaPlayersService> { FifaPlayersServiceImpl(get()) }
    factory<FifaPlayersLocalDataSource> { FifaPlayersLocalDataSourceImpl() }
    factory<FifaPlayersApiInterface> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.easports.com/")
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()))
            .build()
        retrofit.create(FifaPlayersApiInterface::class.java)
    }
}
