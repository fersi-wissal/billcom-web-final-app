package com.billcom.app.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.UserDto;
import com.billcom.app.dto.project.ProjectDto;
import com.billcom.app.dto.project.ProjectListDto;
import com.billcom.app.entity.Project;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.enumeration.TeamProjectStatus;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.ProjectRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("Project Service Test")
public class ProjectServiceTest {

	@InjectMocks
	ProjectService projectService;
	@Mock
	Project project;
	@Mock
	ProjectDto projectDto;
	@Mock
	SecurityUtils securityUtils;
	@Mock
	ProjectListDto projectListDto;
	@Mock
	ProjectRepository projectRepository;

	/** Test get CurrentUser */
	@Test
	void should_return_user_logged() {
		// given
		UserApp user = new UserApp();
		given(projectService.getCurrentUser()).willReturn(user);
		// when & then
		Assertions.assertThat(projectService.getCurrentUser()).isNotNull();
		Assertions.assertThat(projectService.getCurrentUser().getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void should_return_List_project() {
		// given

		final ProjectService projectService = spy(this.projectService);
		// when
		projectService.getListProject();
		// then
		Mockito.verify(projectRepository, Mockito.times(1)).findAll();

	}

	@Test
	void should_throw_exception_when_project_not_Found() {
		// given

		Mockito.doReturn(Optional.empty()).when(projectRepository).findById(999l);
		// when
		Assertions.assertThatThrownBy(() -> {
			projectService.getProjectDeatil(999l);
		}).isInstanceOf(NotFoundException.class);
	}
	@Test
	void should_save_user() throws IOException {
		//given
		final ProjectService projectService = spy(this.projectService);

		ProjectDto projectDto  = new ProjectDto();
		List<MultipartFile> multipartFiles = new ArrayList<>();
		// when
		projectService.saveProject(projectDto,multipartFiles);
		Project project = projectDto.fromDtoToProject();


		// then
		Mockito.verify(projectService, Mockito.times(1)).saveProject(projectDto,multipartFiles);
	
	
	
	}

	@Test
	void should_return_only_project_Not_Completed() {

		Project project1 = new Project(1, "gestion produit", TeamProjectStatus.COMPLETED);
		Project project2 = new Project(2, "gestion immobilière", TeamProjectStatus.COMPLETED);
		Project project3 = new Project(3, "gestion facturation", TeamProjectStatus.COMPLETED);

		List<Project> projectList = new ArrayList<Project>();
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);
		
		Mockito.doReturn(projectList).when(projectRepository).findAll();


		// when

		Set<ProjectListDto> result = projectService.getListProjectNotCompleted();
		
		// then

		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).hasSize(0);

	}

	@Test
	void should_return_project_when_getting_known_id() {
		// given
		final ProjectService projectService = spy(this.projectService);

		Project project1 = new Project(1, "gestion produit", TeamProjectStatus.COMPLETED);
		Mockito.doReturn(Optional.of(project1)).when(projectRepository).findById(project1.getId());
		// when
		projectService.getProjectDeatil(project1.getId());

		//
		Mockito.verify(projectService, Mockito.times(1)).getProjectDeatil(project1.getId());

	}

	
	
	
	
	
	
	
}
