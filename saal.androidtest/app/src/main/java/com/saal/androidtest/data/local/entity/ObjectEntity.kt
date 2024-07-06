package com.saal.androidtest.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "objects")
data class ObjectEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String?,
    var description: String?,
    var type: String?
)