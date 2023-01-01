package com.panda.app.earthquakeapp.di

import com.panda.app.earthquakeapp.data.repository.QuakeRepositoryImpl
import com.panda.app.earthquakeapp.domain.repository.QuakeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsQuakeRepository(
        quakeRepositoryImpl: QuakeRepositoryImpl
    ): QuakeRepository
}