package com.solidecoteknologi.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private  const val PREFERENCES = "token"
@Module
@InstallIn(SingletonComponent::class)
object Config {
    // API Config
    private const val BASE_URL = "https://solidecoteknologi.com/api/"
    private val interceptor: HttpLoggingInterceptor
        get(){
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level  = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client = OkHttpClient.Builder()
        .connectTimeout(600L, TimeUnit.SECONDS)
        .readTimeout(600L, TimeUnit.SECONDS)
        .addInterceptor(interceptor).build()

    @Singleton
    @Provides
    fun provideMasterService(): Service =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(Service::class.java)

    // Data Store
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context) : DataStore<Preferences> {
        return  PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences()}
            ),
            migrations =  listOf(SharedPreferencesMigration(appContext, PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(PREFERENCES) }
        )
    }

}