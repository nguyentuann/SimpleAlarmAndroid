package vn.tutorial.simplealarmandroid.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import vn.tutorial.simplealarmandroid.data.entity.AlarmEntity
import vn.tutorial.simplealarmandroid.local.dao.AppDAO

@Database(entities = [AlarmEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
}
