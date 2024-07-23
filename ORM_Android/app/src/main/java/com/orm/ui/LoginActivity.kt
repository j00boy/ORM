package com.orm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.BuildConfig
import com.orm.databinding.ActivityLoginBinding
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        const val ADDRESS = "users/login/kakao"
    }

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var webView: WebView
    private lateinit var btnLogin: ImageButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView()
        setupLoginButton()

        userViewModel.token.observe(this) { token ->
            Log.d("LoginActivity", "token: $token")
            if (!token.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        Log.d("LoginActivity", "setupWebView")
        webView = binding.webview
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (url != null && url.startsWith(BuildConfig.BASE_URL + ADDRESS)) {
                    userViewModel.loginKakao(url.substringAfter("code="))
                    return true
                }
                return false
            }

            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?,
            ) {
                handler?.proceed()
            }
        }
    }

    private fun setupLoginButton() {
        btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {
            Log.d("LoginActivity", "clickLoginButton")
            webView.loadUrl(BuildConfig.BASE_URL + ADDRESS)
            webView.settings.domStorageEnabled = true
            webView.visibility = WebView.VISIBLE
        }
    }
}
