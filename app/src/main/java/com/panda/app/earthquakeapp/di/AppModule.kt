package com.panda.app.earthquakeapp.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.panda.app.earthquakeapp.data.common.Constants
import com.panda.app.earthquakeapp.data.common.Constants.USER_PREFERENCES
import com.panda.app.earthquakeapp.data.database.QuakeDatabase
import com.panda.app.earthquakeapp.data.remote.QuakeApi
import com.panda.app.earthquakeapp.data.repository.QuakeRepositoryImpl
import com.panda.app.earthquakeapp.domain.repository.QuakeRepository
import com.panda.app.earthquakeapp.utils.location.LocationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuakeApi(): QuakeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuakeApi::class.java)
    }


    @Singleton
    @Provides
    fun provideLocation(app: Application): LocationHelper {
        return LocationHelper(app)
    }

    @Provides
    @Singleton
    fun provideSingletonDatabase(app: Application): QuakeDatabase {
        return Room.databaseBuilder(
            app,
            QuakeDatabase::class.java,
            "quake_caching_db"
        ).fallbackToDestructiveMigration().build()

    }


    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}