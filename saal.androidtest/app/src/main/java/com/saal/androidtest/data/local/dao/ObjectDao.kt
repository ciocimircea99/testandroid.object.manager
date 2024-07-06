package com.saal.androidtest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.saal.androidtest.data.local.entity.ObjectEntity
import com.saal.androidtest.domain.model.ObjectModel

@Dao
interface ObjectDao {

    @Query("SELECT * FROM objects ORDER BY id ASC")
    suspend fun getAllObjects(): List<ObjectEntity>

    @Query("SELECT * FROM objects WHERE id = :id")
    suspend fun getObjectById(id: Long): ObjectEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addObject(objectEntity: ObjectEntity): Long

    @Update
    suspend fun updateObject(objectEntity: ObjectEntity)

    @Delete
    suspend fun deleteObject(objectEntity: ObjectEntity)

    @Query("SELECT * FROM objects WHERE id IN (:relatedObjectIds)")
    fun getObjectsByIds(relatedObjectIds: List<Long>): List<ObjectModel>
}