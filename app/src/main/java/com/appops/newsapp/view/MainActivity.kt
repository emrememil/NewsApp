package com.appops.newsapp.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appops.newsapp.R
import com.appops.newsapp.adapter.NewsAdapter
import com.appops.newsapp.db.ArticleDatabase
import com.appops.newsapp.repository.NewsRepository
import com.appops.newsapp.util.Resource
import com.appops.newsapp.view.fragments.ArticleFragment
import com.appops.newsapp.viewmodel.NewsViewModel
import com.appops.newsapp.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var fragmentTransaction:FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAppBar()
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            var articleFragment = ArticleFragment()
            articleFragment.sendData(it.url)
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.container, articleFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        fillNews()

        swipeRefreshLayout.setOnRefreshListener {
            fillNews()
            swipeRefreshLayout.isRefreshing=false
        }

    }

    private fun fillNews() {
        viewModel.news.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        //newsAdapter.differ.submitList(newsResponse.articles)
                        for (i in 0..newsResponse.articles.size - 1) {
                            viewModel.saveArticle(newsResponse.articles[i])
                        }
                        viewModel.getSavedNews().observe(this, Observer { articles ->
                            newsAdapter.differ.submitList(articles)
                        })
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("NewsResourceErrorTag", "an error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun initAppBar() {
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.abs_layout);
    }

    fun initializeCustomActionBar() {

        val actionBar: ActionBar? = this.supportActionBar
        actionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        actionBar?.setCustomView(R.layout.appbar)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.show()
        val tvDone = findViewById<TextView>(R.id.tv_action_done)
        tvDone.setOnClickListener(clickListener)
        val imgView = findViewById<ImageView>(R.id.img_action_close)
        imgView.setOnClickListener(clickListener)



    }

    val clickListener = View.OnClickListener {view ->

        when (view.getId()) {
            R.id.tv_action_done -> {
                supportFragmentManager.popBackStack()
            initAppBar()
            }
            R.id.img_action_close -> {
                supportFragmentManager.popBackStack()
                initAppBar()
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter=newsAdapter
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val width = metrics.widthPixels

            if(width >1440 ) {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }


}