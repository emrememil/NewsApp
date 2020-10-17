package com.appops.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appops.newsapp.models.Article
import com.appops.newsapp.models.NewsResponse
import com.appops.newsapp.repository.NewsRepository
import com.appops.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage=1

    init {
        getNews("us")
    }

    fun getNews(countryCode:String) = viewModelScope.launch {
        news.postValue(Resource.Loading())
        val  response = newsRepository.getNews(countryCode,newsPage)
        news.postValue(handleNewsResponse(response))

    }

    private fun handleNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()
}