package com.appops.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    public var ID: Int? = null,
    var author: String?,
    var content: String?,
    var description: String?,
    var publishedAt: String?,
    var source: Source?,
    var title: String,
    var url: String,
    var urlToImage: String?
) : Serializable