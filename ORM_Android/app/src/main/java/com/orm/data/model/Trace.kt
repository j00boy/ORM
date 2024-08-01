package com.orm.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.data.model.recycler.RecyclerViewNumberItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.Date

@Parcelize
@Entity(tableName = "trace")
data class Trace(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: Int?,
    val title: String,
    val hikingDate: String?,
    val hikingEndedAt: String? = null,
    val hikingStartedAt: String? = null,
    val maxHeight: Double? = 0.0,
    val mountainId: Int,
    val mountainName: String,
    val coordinates: List<Point>?,
    val trailId: Int? = null,
) : Parcelable {
    companion object {
        fun toRecyclerViewNumberItem(trace: Trace): RecyclerViewNumberItem {
            return RecyclerViewNumberItem(
                id = trace.localId,
                imageSrc = "https://media.istockphoto.com/id/2109478335/ko/%EC%82%AC%EC%A7%84/morning-and-spring-view-of-pink-azalea-flowers-at-hwangmaesan-mountain-with-the-background-of.jpg?s=612x612&w=0&k=20&c=IS-ZtgomSJ01DNoz2bLyRvZ8RYzlJ7pDXnmsERwkf3o=",
                title = trace.title,
                subTitle = "sub Title",
                date = trace.hikingDate.toString(),
                btnText = "측정완료"
            )
        }
    }
}