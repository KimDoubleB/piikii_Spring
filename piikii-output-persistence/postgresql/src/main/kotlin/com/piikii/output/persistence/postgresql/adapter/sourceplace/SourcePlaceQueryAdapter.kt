package com.piikii.output.persistence.postgresql.adapter.sourceplace

import com.piikii.application.domain.model.SourcePlace
import com.piikii.application.port.output.persistence.SourceQueryPort
import org.springframework.stereotype.Repository

@Repository
class SourcePlaceQueryAdapter(
) : SourceQueryPort {

    override fun retrieve(id: Long): SourcePlace {
        TODO("Not yet implemented")
    }

    override fun retrieveAll(ids: List<Long>): List<SourcePlace> {
        TODO("Not yet implemented")
    }
}
