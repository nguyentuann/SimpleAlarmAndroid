package vn.tutorial.simplealarmandroid.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tutorial.simplealarmandroid.data.repositoryImpl.AlarmRepositoryImpl
import vn.tutorial.simplealarmandroid.domain.repository.AlarmRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlarmRepository(
        alarmRepositoryImp: AlarmRepositoryImpl
    ): AlarmRepository
}