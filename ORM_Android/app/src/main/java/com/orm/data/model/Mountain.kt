package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.orm.data.model.recycler.RecyclerViewBasicItem

@Entity(tableName = "mountain")
data class Mountain(
    @PrimaryKey val id: Int,
    val name: String,
    val code: Int,
    val address: String,
    val imageSrc: String?,
    val desc: String?,
    val height: Double,
    val trails: List<Trail>
) {
    companion object {
        fun toRecyclerViewBasicItem(mountain: Mountain): RecyclerViewBasicItem {
            return RecyclerViewBasicItem(
                id = mountain.id,
                imageSrc = mountain.imageSrc
                    ?: "https://media.istockphoto.com/id/2109478335/ko/%EC%82%AC%EC%A7%84/morning-and-spring-view-of-pink-azalea-flowers-at-hwangmaesan-mountain-with-the-background-of.jpg?s=612x612&w=0&k=20&c=IS-ZtgomSJ01DNoz2bLyRvZ8RYzlJ7pDXnmsERwkf3o=",
                title = mountain.name,
                subTitle = mountain.address
            )
        }
    }
}