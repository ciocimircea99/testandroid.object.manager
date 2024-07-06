package com.saal.androidtest.domain.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class RelationModel(
    var id: Long = 0,
    var fromObject: Long,
    var toObject: Long,
) : Parcelable