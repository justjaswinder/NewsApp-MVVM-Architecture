package com.bhavanawagh.newsapp_mvvm_architecture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(


    @ColumnInfo(name = "article_id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo("description")
    val description: String? = " ",

    @ColumnInfo("url")
    val url: String? = "",

    @ColumnInfo("urlToImage")
    val imageUrl: String? = "",

    @Embedded val source: Source
)
