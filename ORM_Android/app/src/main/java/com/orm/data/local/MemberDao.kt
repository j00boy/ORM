package com.orm.data.local

import androidx.room.Dao
import com.orm.data.model.Member

@Dao
interface MemberDao {
    suspend fun getClubMembers(): List<Member>

}