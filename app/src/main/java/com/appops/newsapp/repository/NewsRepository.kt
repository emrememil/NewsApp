package com.appops.newsapp.repository

import com.appops.newsapp.db.ArticleDatabase
import com.appops.newsapp.models.Article
import com.appops.newsapp.service.ServiceManager

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getNews(countryCode:String, pageNumber:Int) = ServiceManager.api.getNews(countryCode,pageNumber)

    suspend fun upsert(article:Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()
}