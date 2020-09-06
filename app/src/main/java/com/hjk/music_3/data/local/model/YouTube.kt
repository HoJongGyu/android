package com.hjk.music_3.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "YouTube")
data class YouTube (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "bno")
        @SerializedName("bno")
        var bno: Long = 0,

        @SerializedName("title")
        var title: String? = null,

        @ColumnInfo(name = "mp4")
        @SerializedName("mp4")
        var mp4: String? = null,

        @ColumnInfo(name = "image")
        @SerializedName("image")
        var image: String? = null
)