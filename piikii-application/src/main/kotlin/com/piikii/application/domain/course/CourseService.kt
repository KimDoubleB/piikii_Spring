package com.piikii.application.domain.course

import com.piikii.application.domain.generic.LongTypeId
import com.piikii.application.domain.generic.UuidTypeId
import com.piikii.application.domain.place.Place
import com.piikii.application.domain.schedule.Schedule
import com.piikii.application.port.input.CourseUseCase
import com.piikii.application.port.input.dto.response.CourseResponse
import com.piikii.application.port.output.persistence.CourseQueryPort
import com.piikii.application.port.output.persistence.PlaceCommandPort
import com.piikii.application.port.output.persistence.PlaceQueryPort
import com.piikii.application.port.output.persistence.RoomQueryPort
import com.piikii.application.port.output.persistence.ScheduleQueryPort
import com.piikii.application.port.output.persistence.VoteQueryPort
import com.piikii.application.port.output.web.NavigationPort
import com.piikii.common.exception.ExceptionCode
import com.piikii.common.exception.PiikiiException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CourseService(
    private val courseQueryPort: CourseQueryPort,
    private val roomQueryPort: RoomQueryPort,
    private val scheduleQueryPort: ScheduleQueryPort,
    private val placeQueryPort: PlaceQueryPort,
    private val placeCommandPort: PlaceCommandPort,
    private val voteQueryPort: VoteQueryPort,
    private val navigationPort: NavigationPort,
) : CourseUseCase {
    override fun isCourseExist(roomUid: UuidTypeId): Boolean {
        return courseQueryPort.isCourseExist(roomUid)
    }

    @Transactional
    override fun retrieveCourse(roomUid: UuidTypeId): CourseResponse {
        val room = roomQueryPort.findById(roomUid)

        if (room.isNotVoteExpired()) {
            throw PiikiiException(
                exceptionCode = ExceptionCode.ACCESS_DENIED,
                detailMessage = VOTE_NOT_END,
            )
        }

        val schedules = scheduleQueryPort.findAllByRoomUid(roomUid)
        val places = placeQueryPort.findAllByRoomUid(roomUid)

        val placeIds = places.map { it.id }
        val votes = voteQueryPort.findAllByPlaceIds(placeIds)
        val agreeCountByPlaceId = voteQueryPort.findAgreeCountByPlaceId(votes)

        return CourseResponse.from(
            room = room,
            placeBySchedule = getPlaceBySchedule(schedules, places, agreeCountByPlaceId),
        )
    }

    @Transactional
    override fun updateCoursePlace(
        roomUid: UuidTypeId,
        placeId: LongTypeId,
    ) {
        val place = placeQueryPort.findByPlaceId(placeId)

        if (place.isInvalidRoomUid(roomUid)) {
            throw PiikiiException(
                exceptionCode = ExceptionCode.ILLEGAL_ARGUMENT_EXCEPTION,
                detailMessage = "Room UUID: $roomUid, Room UUID in Place: ${place.roomUid}",
            )
        }

        placeQueryPort.findConfirmedByScheduleId(place.scheduleId)?.let { confirmedPlace ->
            placeCommandPort.update(confirmedPlace.id, confirmedPlace.copy(confirmed = false))
        }
        placeCommandPort.update(place.id, place.copy(confirmed = true))
    }

    private fun getPlaceBySchedule(
        schedules: List<Schedule>,
        places: List<Place>,
        agreeCountByPlaceId: Map<Long, Int>,
    ): Map<Schedule, CoursePlace> {
        val initCourse: Map<Schedule, CoursePlace> = emptyMap()
        return mapPlacesBySchedule(schedules, places)
            .entries.fold(initCourse) { course, (schedule, places) ->
                val confirmedPlace = getConfirmedPlace(schedule, places, agreeCountByPlaceId)
                confirmedPlace?.let {
                    val preCoursePlace = course.values.lastOrNull()
                    val curCoursePlace = getCoursePlace(schedule, preCoursePlace?.place, it)
                    course + (schedule to curCoursePlace)
                } ?: course
            }
    }

    private fun mapPlacesBySchedule(
        schedules: List<Schedule>,
        places: List<Place>,
    ): Map<Schedule, List<Place>> {
        val placesByScheduleId = places.groupBy { it.scheduleId }
        return schedules.associateWith { schedule ->
            placesByScheduleId[schedule.id]
                ?: throw PiikiiException(
                    exceptionCode = ExceptionCode.ILLEGAL_ARGUMENT_EXCEPTION,
                    detailMessage = "$EMPTY_CONFIRMED_PLACE Schedule ID: ${schedule.id}",
                )
        }
    }

    private fun getCoursePlace(
        schedule: Schedule,
        prePlace: Place?,
        confirmedPlace: Place,
    ): CoursePlace {
        return CoursePlace(
            schedule = schedule,
            prePlace = prePlace,
            place = confirmedPlace,
            distance =
                prePlace?.let {
                    navigationPort.getDistance(it, confirmedPlace)
                } ?: Distance.EMPTY,
        )
    }

    private fun getConfirmedPlace(
        schedule: Schedule,
        places: List<Place>,
        agreeCountByPlaceId: Map<Long, Int>,
    ): Place? {
        val confirmedPlaces = places.filter { it.confirmed }

        if (confirmedPlaces.size > 1) {
            throw PiikiiException(
                exceptionCode = ExceptionCode.CONFLICT,
                detailMessage = "$MULTIPLE_CONFIRMED_PLACE Schedule ID: ${schedule.id}",
            )
        }

        return confirmedPlaces.firstOrNull() ?: confirmPlace(places, agreeCountByPlaceId)
    }

    private fun confirmPlace(
        places: List<Place>,
        agreeCountByPlaceId: Map<Long, Int>,
    ): Place? {
        return places
            .mapNotNull { place ->
                // place.id에 해당하는 agree count가 존재하면 place와 count를 페어로 매핑
                agreeCountByPlaceId[place.id.getValue()]?.let { count -> place to count }
            }
            // agree count 내림차순 정렬, (count 동일)place.id 오름차순 정렬
            .maxWithOrNull(compareBy({ it.second }, { -it.first.id.getValue() }))
            ?.let { (selectedPlace, _) ->
                // 최다 찬성 득표 수 place: confirmed 상태로 변경
                placeCommandPort.update(
                    targetPlaceId = selectedPlace.id,
                    place = selectedPlace.copy(confirmed = true),
                )
                selectedPlace
            }
    }

    companion object {
        const val VOTE_NOT_END = "투표가 진행 중입니다."
        const val MULTIPLE_CONFIRMED_PLACE = "코스로 결정된 장소가 2개 이상입니다. 데이터를 확인해주세요."
        const val EMPTY_CONFIRMED_PLACE = "코스 후보 장소가 없습니다. 데이터를 확인해주세요."
    }
}
