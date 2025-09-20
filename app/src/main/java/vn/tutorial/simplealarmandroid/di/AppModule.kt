package vn.tutorial.simplealarmandroid.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.tutorial.simplealarmandroid.data.local.dao.AppDAO
import vn.tutorial.simplealarmandroid.data.local.db.AppDatabase
import vn.tutorial.simplealarmandroid.receiver.AlarmReceiver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
        return db
    }

    @Provides
    @Singleton
    fun provideAppDAO(database: AppDatabase): AppDAO {
        return database.appDAO()
    }
}

