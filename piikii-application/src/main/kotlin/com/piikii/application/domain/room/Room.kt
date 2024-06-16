package com.piikii.application.domain.room

import com.piikii.application.domain.generic.ThumbnailLinks
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Room(
    val meetingName: String,
    val message: String? = null,
    val address: String? = null,
    val meetDay: LocalDate? = null,
    val thumbnailLinks: ThumbnailLinks? = null,
    val password: Short? = null,
    val voteDeadline: LocalDateTime? = null,
    val roomId: UUID? = null,
)
