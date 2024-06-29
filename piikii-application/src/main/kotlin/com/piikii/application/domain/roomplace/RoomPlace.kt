package com.piikii.application.domain.roomplace

import com.piikii.application.domain.generic.Source
import com.piikii.application.domain.generic.ThumbnailLinks
import com.piikii.application.domain.roomcategory.PlaceCategory

data class RoomPlace(
    val id: Long? = 0L,
    val placeCategory: PlaceCategory,
    val url: String? = null,
    val thumbnailLinks: ThumbnailLinks,
    val address: String? = null,
    val phoneNumber: String? = null,
    val starGrade: Float? = null,
    val source: Source,
)
