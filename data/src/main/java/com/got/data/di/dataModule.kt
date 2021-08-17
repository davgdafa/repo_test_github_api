package com.got.data.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.got.data.GotCharactersRepositoryImpl
import com.got.data.local.GotCharactersLocalDataSource
import com.got.data.local.GotCharactersLocalDataSourceImpl
import com.got.data.local.room.database.AppDatabase
import com.got.data.remote.GotCharactersApiInterface
import com.got.data.remote.GotCharactersRemoteDataSource
import com.got.data.remote.GotCharactersRemoteDataSourceImpl
import com.got.data.remote.GotCharactersService
import com.got.data.remote.GotCharactersServiceImpl
import com.got.domain.data.GotCharactersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<GotCharactersRepository> { GotCharactersRepositoryImpl(get(), get()) }
    factory<GotCharactersRemoteDataSource> { GotCharactersRemoteDataSourceImpl(get()) }
    factory<GotCharactersService> { GotCharactersServiceImpl(get()) }
    factory<GotCharactersLocalDataSource> { GotCharactersLocalDataSourceImpl(get()) }
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "gotcharacterentity").build().gotCharacterDao() }
    factory<GotCharactersApiInterface> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://thronesapi.com/")
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()))
            .build()
        retrofit.create(GotCharactersApiInterface::class.java)
    }
}
