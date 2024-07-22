package com.orm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.kakao.sdk.common.KakaoSdk


@HiltAndroidApp
class OrmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // kakao login
        KakaoSdk.init(this, "14b29e03607d10644d7dcc49768a2bff")
    }

}