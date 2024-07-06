package com.saal.androidtest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saal.androidtest.data.local.dao.ObjectDao
import com.saal.androidtest.data.local.dao.RelationDao
import com.saal.androidtest.data.local.entity.ObjectEntity
import com.saal.androidtest.data.local.entity.RelationEntity

@Database(
    entities = [ObjectEntity::class, RelationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun objectDao(): ObjectDao
    abstract fun relationDao(): RelationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}