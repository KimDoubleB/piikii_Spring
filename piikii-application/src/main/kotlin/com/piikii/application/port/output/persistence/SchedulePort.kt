package com.piikii.application.port.output.persistence

import com.piikii.application.domain.schedule.Schedule
import java.util.UUID

interface ScheduleQueryPort {
    fun findAllByRoomUid(roomUid: UUID): List<Schedule>

    fun findScheduleById(id: Long): Schedule
}

interface ScheduleCommandPort {
    fun saveSchedules(schedules: List<Schedule>)

    fun deleteSchedules(scheduleIds: List<Long>)

    fun updateSchedules(schedules: List<Schedule>)
}
