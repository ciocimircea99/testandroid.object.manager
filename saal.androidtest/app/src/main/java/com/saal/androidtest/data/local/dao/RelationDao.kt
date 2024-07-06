package com.saal.androidtest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.saal.androidtest.data.local.entity.RelationEntity

@Dao
interface RelationDao {
    @Query("SELECT * FROM relations")
    fun getAllRelations(): List<RelationEntity>

    @Query("SELECT * FROM relations WHERE fromObject = :objectId OR toObject = :objectId")
    suspend fun getRelationshipsForObject(objectId: Long): List<RelationEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRelation(relationEntity: RelationEntity): Long

    @Update
    suspend fun updateRelation(relationEntity: RelationEntity)

    @Delete
    suspend fun deleteRelation(relationEntity: RelationEntity)
}