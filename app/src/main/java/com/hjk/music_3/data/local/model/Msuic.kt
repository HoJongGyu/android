package com.hjk.music_3.data.local.model

import com.google.gson.annotations.SerializedName
import java.util.Objects
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by Yassin Ajdi.
 */
@Entity(tableName = "Music")
data class Music (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "bno")
        @SerializedName("bno")
        var bno: Long = 0,

        @SerializedName("title")
        var title: String? = null,

        @ColumnInfo(name = "mp3")
        @SerializedName("mp3")
        var mp3: String? = null,

        @ColumnInfo(name = "image")
        @SerializedName("image")
        var image: String? = null
)
