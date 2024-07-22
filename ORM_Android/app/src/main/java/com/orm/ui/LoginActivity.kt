package com.orm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.orm.BuildConfig
import com.orm.R
import com.orm.data.model.User
import com.orm.databinding.ActivityLoginBinding
import com.orm.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    companion object {
        const val ADDRESS = "users/login/kakao"
    }

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
                            handleAuthorizationCode(it.substringAfter("code="))
                            return true
                        }
                    }
                    return false
                }
            }
        }
    }

    private fun handleAuthorizationCode(code: String) {
        Log.d("handleAuthorizationCode", "Authorization code3: $code")
        val client = OkHttpClient()

        val url = "http://70.12.247.148:8080/users/login/kakao/auth?code=$code"

        val request = Request.Builder()
            .url(url)
            .get() // GET 메소드 사용
            .build()

        Log.d("handleAuthorizationCode", "Request: $request")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TokenRequest", "Failed to request token", e)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Log.d("TokenRequest", response.headers().get("accessToken").toString())
                    response.body()?.string()?.let { responseBody ->
                        Log.d("TokenResponse", "Token response: $responseBody")
                        // Gson을 사용하여 JSON 파싱
                        val gson = Gson()
                        val user = gson.fromJson(responseBody, User::class.java)
                        val userId = user.userId
                        val imageSrc = user.imageSrc
                        val nickname = user.nickname
                        Log.d("body", "User: $userId, $imageSrc, $nickname")

                    }
                } else {
                    Log.e("TokenRequest", "Failed to get token")
                }
            }
        })
    }
}