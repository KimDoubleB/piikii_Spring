package com.piikii.application.domain.schedule

import java.util.UUID

data class Schedule(
    val id: Long? = null,
    val roomUid: UUID,
    val name: String,
    val sequence: Int,
    val type: ScheduleType,
)