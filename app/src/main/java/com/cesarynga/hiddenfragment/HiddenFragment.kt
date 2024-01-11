package com.cesarynga.hiddenfragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment


class HiddenFragment : Fragment() {

    private lateinit var initialUrl: String
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialUrl = requireArguments().getString("url", "").run {
            if (last() != '/') {
                this.plus('/')
            } else {
                this
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hidden, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        webView = view.findViewById(R.id.web_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Loading page started", Toast.LENGTH_SHORT).show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Loading page finished", Toast.LENGTH_SHORT).show()
            }
        }
        loadUrl(initialUrl)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden && webView.url != initialUrl) {
            loadUrl(initialUrl)
        }
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
    }
}