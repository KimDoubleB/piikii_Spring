package com.piikii.application.port.input

import com.piikii.application.port.input.dto.request.AddPlaceRequest
import com.piikii.application.port.input.dto.request.ModifyPlaceRequest
import com.piikii.application.port.input.dto.response.PlaceResponse
import com.piikii.application.port.input.dto.response.ScheduleTypeGroupResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface PlaceUseCase {
    fun addPlace(
        targetRoomUid: UUID,
        addPlaceRequest: AddPlaceRequest,
        placeImages: List<MultipartFile>?,
    ): PlaceResponse

    fun findAllByRoomUidGroupByPlaceType(targetRoomUid: UUID): List<ScheduleTypeGroupResponse>

    fun modify(
        targetRoomUid: UUID,
        targetPlaceId: Long,
        modifyPlaceRequest: ModifyPlaceRequest,
        newPlaceImages: List<MultipartFile>?,
    ): PlaceResponse

    fun delete(targetPlaceId: Long)
}