package com.appops.newsapp.view.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.appops.newsapp.R
import com.appops.newsapp.view.MainActivity
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.appbar.view.*


class ArticleFragment : Fragment() {

    private var url = "http://google.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater!!.inflate(R.layout.fragment_article, container, false)
        var mWebView = rootView.findViewById<WebView>(R.id.webView)
        mWebView.settings.javaScriptEnabled = true
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl(url)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mContext : FragmentActivity? = FragmentActivity()
        mContext = this!!.activity as MainActivity
        mContext.initializeCustomActionBar()
    }

    fun sendData(url:String){
        if (url!=null){
            this.url=url
        }
    }
}
