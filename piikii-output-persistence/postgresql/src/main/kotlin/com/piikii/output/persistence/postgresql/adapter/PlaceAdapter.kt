package com.piikii.output.persistence.postgresql.adapter

import com.piikii.application.domain.place.Place
import com.piikii.application.port.output.persistence.PlaceCommandPort
import com.piikii.application.port.output.persistence.PlaceQueryPort
import com.piikii.common.exception.ExceptionCode
import com.piikii.common.exception.PiikiiException
import com.piikii.output.persistence.postgresql.persistence.entity.PlaceEntity
import com.piikii.output.persistence.postgresql.persistence.repository.PlaceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
@Transactional(readOnly = true)
class PlaceAdapter(
    private val placeRepository: PlaceRepository,
) : PlaceQueryPort, PlaceCommandPort {
    @Transactional
    override fun save(
        roomUid: UUID,
        scheduleId: Long,
        place: Place,
    ): Place {
        val placeEntity =
            PlaceEntity(
                roomUid = roomUid,
                scheduleId = scheduleId,
                place = place,
            )
        return placeRepository.save(placeEntity).toDomain()
    }

    @Transactional
    override fun update(
        targetPlaceId: Long,
        place: Place,
    ): Place {
        val placeEntity =
            placeRepository.findByIdOrNull(targetPlaceId) ?: throw PiikiiException(
                exceptionCode = ExceptionCode.NOT_FOUNDED,
                detailMessage = "targetPlaceId : $targetPlaceId",
            )
        placeEntity.update(place)
        return placeEntity.toDomain()
    }

    @Transactional
    override fun delete(targetPlaceId: Long) {
        placeRepository.findByIdOrNull(targetPlaceId) ?: throw PiikiiException(
            exceptionCode = ExceptionCode.NOT_FOUNDED,
            detailMessage = "targetPlaceId : $targetPlaceId",
        )
        placeRepository.deleteById(targetPlaceId)
    }

    override fun findByPlaceId(placeId: Long): Place {
        return placeRepository.findByIdOrNull(placeId)?.toDomain() ?: throw PiikiiException(
            exceptionCode = ExceptionCode.NOT_FOUNDED,
            detailMessage = "placeId : $placeId",
        )
    }

    override fun findAllByPlaceIds(placeIds: List<Long>): List<Place> {
        return placeRepository.findAllById(placeIds).map { it.toDomain() }
    }

    override fun findAllByRoomUid(roomUid: UUID): List<Place> {
        return placeRepository.findAllByRoomUid(roomUid).map { it.toDomain() }
    }
}