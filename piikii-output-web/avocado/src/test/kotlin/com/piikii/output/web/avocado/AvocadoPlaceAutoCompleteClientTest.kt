package com.piikii.output.web.avocado

import com.piikii.application.domain.generic.Origin
import com.piikii.application.domain.place.OriginMapId
import com.piikii.output.web.avocado.adapter.AvocadoPlaceAutoCompleteClient
import com.piikii.output.web.avocado.parser.MapUrlIdParser
import com.piikii.output.web.avocado.parser.ShareUrlIdParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@Disabled
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [TestConfiguration::class])
class AvocadoPlaceAutoCompleteClientTest {
    @Autowired
    lateinit var shareUrlIdParser: ShareUrlIdParser

    @Autowired
    lateinit var mapUrlIdParser: MapUrlIdParser

    @Autowired
    lateinit var mapMobileUrlIdParser: MapUrlIdParser

    @Autowired
    lateinit var avocadoPlaceAutoCompleteClient: AvocadoPlaceAutoCompleteClient

    @Test
    fun shareUrlIdParserTest() {
        val url = "주소를 입력하세요"

        val parse = shareUrlIdParser.parseOriginMapId(url)
        println("parse = $parse")
    }

    @Test
    fun mapUrlIdParserTest() {
        val url = "주소를 입력하세요"

        val pattern = mapUrlIdParser.getParserBySupportedUrl(url)
        assertThat(pattern).isNotNull()

        val parse = mapUrlIdParser.parseOriginMapId(url)
        println("parse = $parse")
    }

    @Test
    fun getAutoCompletedPlaceTest() {
        val url = "주소를 입력하세요"
        val id = 123L

        val originPlace = avocadoPlaceAutoCompleteClient.getAutoCompletedPlace(url, OriginMapId.of(id, Origin.AVOCADO))
        println("originPlace = $originPlace")
    }
}