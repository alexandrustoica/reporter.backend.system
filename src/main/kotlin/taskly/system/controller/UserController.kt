package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import taskly.system.domain.UserEntity
import taskly.system.repository.UserRepository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Alexandru Stoica
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = arrayOf("*"))
@RequestMapping("/user")
class UserController {

    private val counter = AtomicInteger()

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/create")
    fun create(): UserEntity {
        val user =  UserEntity(1, "Test", "test", "test", "test@test.com", "default", Calendar.getInstance())
        if (userRepository.findOne(1) == null) userRepository.save(user)
        return userRepository.findOne(1)
    }
}