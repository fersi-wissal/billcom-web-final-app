package com.billcom.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.billcom.app.dto.TaskDto;
import com.billcom.app.enumeration.TaskPriority;



@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String taskName;
	private LocalDateTime creationDate;

	private LocalDateTime deleveryDate;
	private LocalDateTime effectiveDueDate;

	private String descriptionTask;

	@ElementCollection
	private List<String> files;
	
	private String createdBy;

	@Enumerated(EnumType.STRING)
	private TaskPriority taskPriority;

	@ManyToOne
	private TeamMember teamMember;

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
	private Status status;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	private Set<Comment> comments;

	public Task() {
		taskPriority = TaskPriority.lowPriority;

	}

	

	public Task(String taskName, LocalDateTime deleveryDate, String descriptionTask, List<String> files) {
		this();
		this.taskName = taskName;
		this.deleveryDate = deleveryDate;
		this.descriptionTask = descriptionTask;
		this.files = files;
	}


	public Task(long id, String taskName, String descriptionTask, TaskPriority taskPriority, Status status,
			LocalDateTime deleveryDate, String createdBy) {
		this();
		this.id = id;
		this.taskName = taskName;
		this.descriptionTask = descriptionTask;
		this.taskPriority = taskPriority;
		this.status = status;
		this.deleveryDate = deleveryDate;
		this.createdBy = createdBy;
	}
	public Task(long id, String taskName, String descriptionTask, TaskPriority taskPriority, Status status,
			LocalDateTime deleveryDate, String createdBy,TeamMember teamMember) {
		this();
		this.id = id;
		this.taskName = taskName;
		this.descriptionTask = descriptionTask;
		this.taskPriority = taskPriority;
		this.status = status;
		this.deleveryDate = deleveryDate;
		this.createdBy = createdBy;
		this.teamMember = teamMember;
	}
	
	
	
	
	
	
	
	public TaskDto fromTaskToTaskDto(Task task) {
		return new TaskDto(task.getId(),task.getTaskName());
	}


	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public TaskPriority getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(TaskPriority taskPriority) {
		this.taskPriority = taskPriority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getDeleveryDate() {
		return deleveryDate;
	}

	public void setDeleveryDate(LocalDateTime deleveryDate) {
		this.deleveryDate = deleveryDate;
	}


	public String getDescriptionTask() {
		return this.descriptionTask;
	}

	public void setDescriptionTask(String descriptionTask) {
		this.descriptionTask = descriptionTask;
	}



	public TeamMember getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}


	public LocalDateTime getEffectiveDueDate() {
		return effectiveDueDate;
	}

	public void setEffectiveDueDate(LocalDateTime effectiveDueDate) {
		this.effectiveDueDate = effectiveDueDate;
	}



	@Override
	public String toString() {
		return "Task [id=" + id + ", taskName=" + taskName + ", creationDate=" + creationDate + ", deleveryDate="
				+ deleveryDate + ", effectiveDueDate=" + effectiveDueDate + ", descriptionTask=" + descriptionTask
				+ ", files=" + files + ", createdBy=" + createdBy + ", taskPriority=" + taskPriority + ", teamMember="
				+ teamMember + ", status=" + status + ", comments=" + comments + "]";
	}






}