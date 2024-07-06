package com.saal.androidtest.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "relations")
data class RelationEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var fromObject: Long,
    var toObject: Long,
)