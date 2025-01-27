package com.piikii.output.persistence.postgresql.persistence.entity

import com.piikii.application.domain.generic.LongTypeId
import com.piikii.application.domain.generic.UuidTypeId
import com.piikii.application.domain.user.User
import com.piikii.output.persistence.postgresql.persistence.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "piikii_user", schema = "piikii")
@SQLRestriction("is_deleted = false")
@DynamicUpdate
class UserEntity(
    @Column(name = "user_uid", nullable = false)
    val userUid: UuidTypeId,
    @Column(name = "room_id", nullable = false)
    val roomId: LongTypeId,
) : BaseEntity() {
    fun toDomain(): User {
        return User(
            userUid = this.userUid,
            roomId = this.roomId,
        )
    }

    companion object {
        fun from(user: User): UserEntity {
            return UserEntity(
                userUid = user.userUid,
                roomId = user.roomId,
            )
        }
    }
}
