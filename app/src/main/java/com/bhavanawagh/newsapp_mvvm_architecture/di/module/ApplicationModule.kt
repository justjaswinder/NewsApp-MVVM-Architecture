package com.bhavanawagh.newsapp_mvvm_architecture.di.module

import android.content.Context
import com.bhavanawagh.newsapp_mvvm_architecture.NewsApplication
import com.bhavanawagh.newsapp_mvvm_architecture.data.api.ApiKeyInterceptor
import com.bhavanawagh.newsapp_mvvm_architecture.data.api.CacheInterceptor
import com.bhavanawagh.newsapp_mvvm_architecture.data.api.ForceCacheInterceptor
import com.bhavanawagh.newsapp_mvvm_architecture.data.api.NetworkService
import com.bhavanawagh.newsapp_mvvm_architecture.di.ApplicationContext
import com.bhavanawagh.newsapp_mvvm_architecture.di.BaseUrl
import com.bhavanawagh.newsapp_mvvm_architecture.di.NetworkApiKey
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: NewsApplication) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context {
        return application
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @NetworkApiKey
    @Provides
    fun provideNetworkApiKey(): String = "59268db289864425bc1ca98d11669422"

    @Provides
    fun provideApiKeyInterceptor(@NetworkApiKey networkApiKey: String): ApiKeyInterceptor =
        ApiKeyInterceptor(networkApiKey)

    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor = CacheInterceptor()

    @Provides
    @Singleton
    fun provideForceInterceptor(): ForceCacheInterceptor = ForceCacheInterceptor(application)

    @Provides
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        cacheInterceptor: CacheInterceptor,
        forceCacheInterceptor: ForceCacheInterceptor
    ): OkHttpClient = OkHttpClient()
        .newBuilder()
        .cache(Cache(File(application.cacheDir, "http-cache"), 10L * 1024L * 1024L))
        .addInterceptor(apiKeyInterceptor)
        .addNetworkInterceptor(cacheInterceptor)
        .addInterceptor(forceCacheInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideGsonConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient
    )
            : NetworkService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(NetworkService::class.java)
    }
}