package com.orm.data.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.orm.data.model.recycler.RecyclerViewNumberItem
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
@Entity
data class Trace(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val id: Int?,
    val title: String,
    val hikingDate: String?,
    val hikingEndedAt: String? = null,
    val hikingStartedAt: String? = null,
    val maxHeight: Double? = 0.0,
    val mountainId: Int,
    val mountainName: String?,
    val coordinates: List<Point>?,
    val trailId: Int? = -1,
    var recordId: Long? = null,
    val imgPath: String? = null,
) : Parcelable {
    companion object {
        fun toRecyclerViewNumberItem(trace: Trace): RecyclerViewNumberItem {
            return RecyclerViewNumberItem(
                id = trace.localId,
                imageSrc = trace.imgPath ?: "",
                title = trace.title,
                subTitle = trace.mountainName ?: "",
                date = trace.hikingDate.toString(),
                btnText = if (trace.maxHeight != 0.0) "측정완료" else "측정전"
            )
        }
    }
}