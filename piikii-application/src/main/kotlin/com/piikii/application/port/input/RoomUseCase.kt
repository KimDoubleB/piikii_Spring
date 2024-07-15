package com.piikii.application.port.input

import com.piikii.application.domain.room.Password
import com.piikii.application.port.input.dto.request.RoomSaveRequestForm
import com.piikii.application.port.input.dto.request.RoomUpdateRequestForm
import com.piikii.application.port.input.dto.response.RoomResponse
import com.piikii.application.port.input.dto.response.SaveRoomResponse
import java.time.LocalDateTime
import java.util.UUID

interface RoomUseCase {
    fun create(request: RoomSaveRequestForm): SaveRoomResponse

    fun modify(request: RoomUpdateRequestForm)

    fun remove(roomUid: UUID)

    fun findById(roomUid: UUID): RoomResponse

    fun changeVoteDeadline(
        roomUid: UUID,
        password: Password,
        voteDeadline: LocalDateTime,
    )
}