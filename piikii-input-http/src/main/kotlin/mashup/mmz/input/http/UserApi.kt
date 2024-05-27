package mashup.mmz.input.http

import mashup.mmz.application.port.input.UserUsecase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApi(
    private val userUsecase: UserUsecase
)  {

    @GetMapping("/test")
    fun test() {
        println("dsfklfjdsalkfjsadlkfjdsalkf")
        userUsecase.save("sdf")
    }
}