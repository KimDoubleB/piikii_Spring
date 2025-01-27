package com.piikii.input.http.controller.docs

import com.piikii.application.domain.room.Password
import com.piikii.application.port.input.dto.request.RoomSaveRequestForm
import com.piikii.application.port.input.dto.request.RoomUpdateRequestForm
import com.piikii.application.port.input.dto.response.RoomResponse
import com.piikii.application.port.input.dto.response.SaveRoomResponse
import com.piikii.input.http.controller.dto.ResponseForm
import com.piikii.input.http.controller.dto.response.ExceptionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.UUID

@Tag(name = "RoomApi", description = "방 관련 API")
interface RoomApiDocs {
    class SuccessSaveRoomResponse : ResponseForm<SaveRoomResponse>()

    class SuccessRoomResponse : ResponseForm<RoomResponse>()

    @Operation(summary = "방 생성 API", description = "방(Room)을 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "CREATED success",
                content = [Content(schema = Schema(implementation = SuccessSaveRoomResponse::class))],
            ),
        ],
    )
    fun createRoom(
        @Parameter(
            description = "방 생성 요청 정보",
            required = true,
            schema = Schema(implementation = RoomSaveRequestForm::class),
        )
        @Valid
        @NotNull
        @io.swagger.v3.oas.annotations.parameters.RequestBody
        request: RoomSaveRequestForm,
    ): ResponseForm<SaveRoomResponse>

    @Operation(summary = "방 수정 API", description = "방(Room) 정보를 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "UPDATED success",
                content = [Content(schema = Schema(implementation = SuccessRoomResponse::class))],
            ),
        ],
    )
    fun modifyRoom(
        @Parameter(
            description = "방 수정 요청 정보",
            required = true,
            schema = Schema(implementation = RoomUpdateRequestForm::class),
        )
        @Valid
        @NotNull
        @io.swagger.v3.oas.annotations.parameters.RequestBody
        request: RoomUpdateRequestForm,
    ): ResponseForm<Unit>

    @Operation(summary = "방 삭제 API", description = "방(Room) 정보를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "DELETED success",
                content = [Content(schema = Schema(implementation = ResponseForm::class))],
            ),
        ],
    )
    fun deleteRoom(
        @Parameter(
            name = "roomUid",
            description = "삭제하고자 하는 방 id",
            required = true,
            `in` = ParameterIn.PATH,
        ) @NotNull roomUid: UUID,
    ): ResponseForm<Unit>

    @Operation(summary = "방 조회 API", description = "방(Room) 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "GET success",
                content = [Content(schema = Schema(implementation = SuccessRoomResponse::class))],
            ),
        ],
    )
    fun retrieveRoom(
        @Parameter(
            name = "roomUid",
            description = "조회하고자 하는 방 id",
            required = true,
            `in` = ParameterIn.PATH,
        ) @NotNull roomUid: UUID,
    ): ResponseForm<RoomResponse>

    @Operation(summary = "방 비밀번호 검증 API", description = "방(Room) 비밀번호를 검증합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "GET success",
                content = [Content(schema = Schema(implementation = ResponseForm::class))],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Failed to verify",
                content = [Content(schema = Schema(implementation = ExceptionResponse::class))],
            ),
        ],
    )
    fun verifyPassword(
        @Parameter(
            name = "roomUid",
            description = "검증하고자 하는 방 id",
            required = true,
            `in` = ParameterIn.PATH,
        ) @NotNull roomUid: UUID,
        @Parameter(
            description = "방 비밀번호",
            example = "1234",
            required = true,
            `in` = ParameterIn.HEADER,
        ) @NotNull @Size(min = 4, max = 4, message = "비밀번호는 반드시 4자리여야 합니다.")
        password: Password,
    ): ResponseForm<Unit>
}
