package taskly.system.section

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sections")
class CriticalSectionController {

    @Autowired
    private lateinit var criticalSectionService: CriticalSectionService

    @ResponseBody
    @PostMapping("/")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getAllNear(@RequestBody area: Area): List<CriticalSection> =
            criticalSectionService.findAllCriticalSectionsNear(area.origin, area.radius)

}