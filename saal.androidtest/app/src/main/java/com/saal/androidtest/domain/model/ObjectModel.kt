package com.saal.androidtest.domain.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class ObjectModel(
    var id: Long = 0,
    var name: String?,
    var description: String?,
    var type: String?
) : Parcelable {

    override fun toString(): String {
        return "$id$name$description$type"
    }

    companion object {
        fun Empty(): ObjectModel = ObjectModel(0, "", "", "")
    }
}