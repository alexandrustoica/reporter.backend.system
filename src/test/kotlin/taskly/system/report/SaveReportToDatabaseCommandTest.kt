package taskly.system.report

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@DataJpaTest
@ExtendWith(SpringExtension::class)
@EnableJpaRepositories("taskly.system")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SaveReportToDatabaseCommandTest {

    @Autowired
    private lateinit var repository: ReportRepository

    @Test
    internal fun whenSavingReport_UserExists_ExpectReportSaved() {
        println(repository.findAll())
        assertThat(repository, IsNull.notNullValue())
    }
}