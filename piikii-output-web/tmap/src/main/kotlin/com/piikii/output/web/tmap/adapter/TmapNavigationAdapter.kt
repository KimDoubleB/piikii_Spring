package com.piikii.output.web.tmap.adapter

import com.piikii.application.domain.course.Coordinate
import com.piikii.application.domain.course.Distance
import com.piikii.application.domain.place.Place
import com.piikii.application.port.output.web.NavigationPort
import com.piikii.common.exception.ExceptionCode
import com.piikii.common.exception.PiikiiException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class TmapNavigationAdapter(
    private val tmapApiClient: RestClient,
) : NavigationPort {
    @Cacheable(
        value = ["Distances"],
        key = "#startPlace.id + '_' + #endPlace.id",
        cacheManager = "cacheManager",
        condition = "#startPlace != null",
        unless = "#result == null",
    )
    override fun getDistance(
        startPlace: Place,
        endPlace: Place,
    ): Distance {
        val startCoordinate = startPlace.getCoordinate()
        val endCoordinate = endPlace.getCoordinate()
        return startCoordinate.takeIf { it.isValid() }
            ?.takeIf { endCoordinate.isValid() }
            ?.let { getDistanceFromTmap(startCoordinate, endCoordinate) }
            ?: Distance.EMPTY
    }

    private fun getDistanceFromTmap(
        start: Coordinate,
        end: Coordinate,
    ): Distance {
        return tmapApiClient.post()
            .uri("")
            .body(
                TmapRouteInfoRequest(
                    startX = start.x!!,
                    startY = start.y!!,
                    endX = end.x!!,
                    endY = end.y!!,
                ),
            )
            .retrieve()
            .body<TmapRouteInfoResponse>()
            ?.toDistance()
            ?: throw PiikiiException(
                exceptionCode = ExceptionCode.ROUTE_PROCESS_ERROR,
                detailMessage = "fail to request tmap api call, start(${start.x}, ${start.y}), end(${end.x}, ${end.y})",
            )
    }
}
