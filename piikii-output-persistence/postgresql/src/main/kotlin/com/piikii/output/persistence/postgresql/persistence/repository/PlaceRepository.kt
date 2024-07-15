package com.piikii.output.persistence.postgresql.persistence.repository

import com.piikii.output.persistence.postgresql.persistence.entity.PlaceEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceRepository : JpaRepository<PlaceEntity, Long> {
    fun findAllByRoomUid(roomUid: UUID): List<PlaceEntity>
}