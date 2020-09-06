package com.hjk.music_3.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "User")
data class User (

        @PrimaryKey
        @ColumnInfo(name="bno")
        @SerializedName("bno")
        var bno: Long=0,

        @ColumnInfo(name="id")
        @SerializedName("id")
        var id: String?=null,

        @ColumnInfo(name="pwd")
        @SerializedName("pwd")
        var pwd: String?=null,

        @ColumnInfo(name="name")
        @SerializedName("name")
        var name: String?=null,

        @ColumnInfo(name="image")
        @SerializedName("image")
        var image: String? =null,


        @ColumnInfo(name="save_music")
        @SerializedName("save_music")
        var save_music: Long=0,

        @ColumnInfo(name="save_back")
        @SerializedName("save_back")
        var save_back: Long=0,

        @ColumnInfo(name="save_login")
        @SerializedName("save_login")
        var save_login: Long=0



)
