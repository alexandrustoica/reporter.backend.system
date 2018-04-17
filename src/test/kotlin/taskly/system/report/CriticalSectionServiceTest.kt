package taskly.system.report

import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.isIn
import org.junit.Assert.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import taskly.system.section.CriticalSection
import taskly.system.section.CriticalSectionRepository
import taskly.system.section.CriticalSectionService

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system.section", "taskly.system.report",
        "taskly.system.user", "taskly.system.notification")
@ComponentScan("taskly.system.section", "taskly.system.report", "taskly.system.notification")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = ["classpath:integrationtests.properties"])
class CriticalSectionServiceTest {

    @Autowired
    private lateinit var service: CriticalSectionService

    @Autowired
    private lateinit var criticalSectionRepository: CriticalSectionRepository

    @Test
    fun whenSavingCriticalSection_ValidCriticalSection_ExpectCriticalSection() {
        // given:
        val subject = CriticalSection()
        // when:
        val result = service.save(subject)
        // then:
        assertThat(result, `is`(equalTo(criticalSectionRepository.findAll().first())))
    }

    @Test
    fun whenDeletingCriticalSection_ValidCriticalSectionId_ExpectCriticalSectionDeleted() {
        // given:
        val subject = CriticalSection().let { criticalSectionRepository.save(it) }
        // when:
        val result = service.delete(subject.id)
        // then:
        assertThat(result?.id, both(`is`(equalTo(subject.id)))
                .and(not(isIn(criticalSectionRepository.findAll().map { it.id }))))
    }

    @Test
    fun whenGettingCriticalSectionById_WithValidId_ExpectCriticalSection() {
        // given:
        val subject = CriticalSection().let { criticalSectionRepository.save(it) }
        // when:
        val result = service.getById(subject.id)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenGettingAllCriticalSectionsNearLocation_WithLocation_ExpectCriticalSections() {
        // given:
        val criticalSections = listOf(CriticalSection(Location(0.0, 0.0)),
                CriticalSection(Location(0.01, 0.01)),
                CriticalSection(Location(0.02, 0.02)))
                .map { criticalSectionRepository.save(it) }
        val subject = criticalSections.subList(0, 2)
        // when:
        val result = service.findAllCriticalSectionsNear(
                Location(0.0, 0.0), byDistance = 3000.0)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenSearchingForCriticalSection_WithValidLocation_ExpectCriticalSection() {
        // given:
        val subject = CriticalSection(Location(0.0, 0.0))
                .let { criticalSectionRepository.save(it) }
        // when:
        val result = service.findAt(Location(0.0, 0.0), byDistance = 3000.0)
        // then:
        assertThat(result, `is`(equalTo(subject)))
    }

    @Test
    fun whenSearchingForCriticalSection_WithInvalidLocation_ExpectNullValue() {
        // given:
        CriticalSection(Location(0.0, 0.0))
                .let { criticalSectionRepository.save(it) }
        // when:
        val result = service.findAt(Location(10.0, 10.0), byDistance = 3000.0)
        // then:
        assertThat(result, `is`(nullValue()))
    }
}