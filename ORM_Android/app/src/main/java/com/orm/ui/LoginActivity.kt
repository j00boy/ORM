package com.orm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.orm.BuildConfig
import com.orm.R
import com.orm.data.model.User
import com.orm.data.repository.UserRepository
import com.orm.databinding.ActivityLoginBinding
import com.orm.databinding.ActivityMainBinding
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

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

        webView = binding.webview
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        btnLogin = binding.btnLogin
        btnLogin.setOnClickListener {
            webView.visibility = WebView.VISIBLE
            webView.loadUrl(BuildConfig.BASE_URL + ADDRESS)
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        if (it.startsWith(BuildConfig.BASE_URL + ADDRESS)) {
                            userViewModel.loginKaKao(it.substringAfter("code="))

                            userViewModel.token.observe(this@LoginActivity) { token ->
                                if (!token.isNullOrEmpty()) {
                                    Log.d("token", token)
                                    val intent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            return true
                        }
                    }
                    return false
                }
            }
        }
    }
}
