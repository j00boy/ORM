package com.orm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.orm.R
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var btnLogin: ImageButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        webView = findViewById(R.id.webview)
        btnLogin = findViewById(R.id.btn_login)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        btnLogin.setOnClickListener {
            webView.visibility = WebView.VISIBLE
            webView.loadUrl("http://70.12.247.148:8080/users/login/kakao")
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        val code = it.substringAfter("code=")
                        Log.d("AuthorizationCode", "Authorization code1: $code")
                        if (it.startsWith("http://70.12.247.148:8080/users/login/kakao")) {
                            handleAuthorizationCode(code)
                            return true
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
        }
    }

    private fun handleAuthorizationCode(code: String) {
        Log.d("handleAuthorizationCode", "Authorization code3: $code")
        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("code", code)
            .build()

        val request = Request.Builder()
            .url("http://70.12.247.148:8080/users/login/kakao/token")
            .post(formBody)
            .build()
        Log.d("handleAuthorizationCode", "Authorization code3: $request")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TokenRequest", "Failed to request token", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
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

    data class User(
        val userId: String,
        val imageSrc: String,
        val nickname: String
    )
}