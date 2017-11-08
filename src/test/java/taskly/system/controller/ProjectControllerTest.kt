package taskly.system.controller

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import taskly.system.domain.ProjectEntity
import taskly.system.repository.ProjectRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RunWith(MockitoJUnitRunner::class)
class ProjectControllerTest {

    @InjectMocks private
    lateinit var projectController: ProjectController

    @Mock
    private lateinit var projectRepository: ProjectRepository

    @Test
    fun isInsertingProject() {
        val project = ProjectEntity("ProjectTest")
        Mockito.`when`(projectRepository.save(project))
                .thenReturn(ProjectEntity(1, "ProjectTest"))
        val result = projectController.insert(project)
        assertEquals(result, ProjectEntity(1, "ProjectTest"))
    }
}
