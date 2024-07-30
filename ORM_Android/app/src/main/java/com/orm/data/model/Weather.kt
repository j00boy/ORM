package com.orm.data.model

data class Weather(
    val response: Response
) {

    data class Response(
        val header: Header,
        val body: Body
    )

    data class Header(
        val resultCode: Int,
        val resultMsg: String
    )

    data class Body(
        val dataType: String,
        val items: Items
    )

    data class Items(
        val item: List<WeatherItem>
    )

    data class WeatherItem(
        val rn1: String,    // 강수 형태
        val reh: String,    // 습도
        val sky: String,    // 하늘 상태
        val tmp: String,    // 기온
        val baseDate: String // 예보 날짜 (형식: yyyyMMdd)
    )
}

