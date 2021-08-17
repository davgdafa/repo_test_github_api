package com.got.data.di

import com.got.data.GotPlayersRepositoryImpl
import com.got.data.local.GotPlayersLocalDataSource
import com.got.data.local.GotPlayersLocalDataSourceImpl
import com.got.data.remote.GotPlayersApiInterface
import com.got.data.remote.GotPlayersRemoteDataSource
import com.got.data.remote.GotPlayersRemoteDataSourceImpl
import com.got.data.remote.GotPlayersService
import com.got.data.remote.GotPlayersServiceImpl
import com.got.domain.data.GotPlayersRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<GotPlayersRepository> { GotPlayersRepositoryImpl(get(), get()) }
    factory<GotPlayersRemoteDataSource> { GotPlayersRemoteDataSourceImpl(get()) }
    factory<GotPlayersService> { GotPlayersServiceImpl(get()) }
    factory<GotPlayersLocalDataSource> { GotPlayersLocalDataSourceImpl() }
    factory<GotPlayersApiInterface> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://thronesapi.com/")
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()))
            .build()
        retrofit.create(GotPlayersApiInterface::class.java)
    }
}
