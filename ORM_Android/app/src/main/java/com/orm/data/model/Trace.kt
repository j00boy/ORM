package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.data.model.recycler.RecyclerViewNumberItem
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "trace")
data class Trace(
    @PrimaryKey val id: Int,
    val title: String,
    val hikingDate: String?,
    val hikingEndedAt: String?,
    val hikingStartedAt: String?,
    val maxHeight: Double?,
    val mountainId: Int,
    val mountainName: String,
    val coordinates: List<Point>?,
    val trailId: Int
) {
    companion object {
        fun toRecyclerViewNumberItem(trace: Trace): RecyclerViewNumberItem {
            return RecyclerViewNumberItem(
                id = trace.id,
                imageSrc = "https://media.istockphoto.com/id/2109478335/ko/%EC%82%AC%EC%A7%84/morning-and-spring-view-of-pink-azalea-flowers-at-hwangmaesan-mountain-with-the-background-of.jpg?s=612x612&w=0&k=20&c=IS-ZtgomSJ01DNoz2bLyRvZ8RYzlJ7pDXnmsERwkf3o=",
                title = trace.title,
                subTitle = "sub Title",
                date = trace.hikingDate.toString(),
                btnText = "측정완료"
            )
        }
    }
}