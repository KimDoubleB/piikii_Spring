package com.piikii.input.http.controller

import com.piikii.application.port.input.PlaceUseCase
import com.piikii.application.port.input.dto.request.AddPlaceRequest
import com.piikii.application.port.input.dto.request.ModifyPlaceRequest
import com.piikii.application.port.input.dto.response.PlaceResponse
import com.piikii.application.port.input.dto.response.ScheduleTypeGroupResponse
import com.piikii.input.http.controller.docs.PlaceDocs
import com.piikii.input.http.controller.dto.ResponseForm
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Validated
@RestController
@RequestMapping("/v1/rooms/{roomUid}/places")
class PlaceApi(
    private val placeUseCase: PlaceUseCase,
) : PlaceDocs {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    override fun addPlace(
        @NotNull @PathVariable roomUid: UUID,
        @Valid @RequestPart addPlaceRequest: AddPlaceRequest,
        @RequestPart(required = false) placeImages: List<MultipartFile>?,
    ): ResponseForm<PlaceResponse> {
        return ResponseForm(placeUseCase.addPlace(roomUid, addPlaceRequest, placeImages))
    }

    @GetMapping
    override fun retrieveAll(
        @NotNull @PathVariable roomUid: UUID,
    ): ResponseForm<List<ScheduleTypeGroupResponse>> {
        return ResponseForm(placeUseCase.findAllByRoomUidGroupByPlaceType(roomUid))
    }

    @PatchMapping("/{placeId}")
    override fun modifyPlace(
        @NotNull @PathVariable roomUid: UUID,
        @NotNull @PathVariable placeId: Long,
        @Valid @NotNull @RequestPart modifyPlaceRequest: ModifyPlaceRequest,
        @RequestPart(required = false) newPlaceImages: List<MultipartFile>?,
    ): ResponseForm<PlaceResponse> {
        return ResponseForm(
            placeUseCase.modify(
                targetRoomUid = roomUid,
                targetPlaceId = placeId,
                modifyPlaceRequest = modifyPlaceRequest,
                newPlaceImages = newPlaceImages,
            ),
        )
    }

    @DeleteMapping("/{placeId}")
    override fun deletePlace(
        @NotNull @PathVariable roomUid: UUID,
        @NotNull @PathVariable placeId: Long,
    ): ResponseForm<Unit> {
        placeUseCase.delete(placeId)
        return ResponseForm.EMPTY_RESPONSE
    }
}
