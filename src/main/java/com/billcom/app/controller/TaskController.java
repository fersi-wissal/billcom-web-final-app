package com.billcom.app.controller;




import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.core.io.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.CommentDto;
import com.billcom.app.dto.TaskDto;
import com.billcom.app.dto.TaskStatusDto;
import com.billcom.app.dto.TeamTask;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.entity.Comment;
import com.billcom.app.entity.Task;
import com.billcom.app.service.TaskService;

@RestController
@RequestMapping(value="task")

public class TaskController {
	

	private TaskService taskService;

	public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}
	
	@PostMapping("/createTask")
//	@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public Task createTask(@RequestBody TaskDto taskDto) {
		return taskService.saveTask(taskDto);
	}
    @GetMapping("/getTaskDetails/{idTask}")
    public Task getTaskDetails(@PathVariable long idTask) {
    	return taskService.getTaskDetails(idTask) ;   }
	
	@PutMapping("/updateStatusTask/{idTask}")
	@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public Task updateTaskStatus(@PathVariable long idTask, @RequestBody String status) {
		return taskService.updateStatusTask(idTask, status);
	}


	@GetMapping("/getTasksMember/{idTeamMember}")
	//@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public List<TaskStatusDto> getTasksMember(@PathVariable long idTeamMember) {
		return taskService.getTasksMember(idTeamMember);
	}
	
	@GetMapping("/getTasksTeam/{idTeam}")
	//@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public List<TaskStatusDto> getTasksTeam(@PathVariable long idTeam) {
		return taskService.getTasksTeam(idTeam);
	}
	
	
	
	

	@DeleteMapping("/deleteTask/{idTask}")
	@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public void deleteTask(@PathVariable long idTask) {
		taskService.deleteTask(idTask);
	}

	@GetMapping("/getTasksAllTeamMember/{id}")
	@PreAuthorize("hasRole('manager') or hasRole('leader')")
	public List<List<Task>> getTasksAllTeamMember(@PathVariable long id) {
		 return taskService.getTasksAllTeamMember(id);
	}
	
	
	@PostMapping("/addCommentToTask/{idTask}")
	public Task addCommentToTask(@PathVariable long idTask, @RequestBody CommentDto commentDto) {
		
		 return taskService.addCommentToTask(idTask, commentDto);
	}
	@GetMapping("/getTasksComment/{idTask}")
	public Set <Comment> getTasksComment(@PathVariable long idTask) {
		 return taskService.getTasksComment(idTask);
	}
	@PutMapping("setCommentOfTask/{idComment}")
	public Comment setCommentOfTask(@PathVariable long idComment, @RequestBody CommentDto commentDto ) {
		return taskService.setCommentOfTask(idComment, commentDto);
	}
	@DeleteMapping("deleteComment/{idComment}")
	public void deleteComment(@PathVariable long idComment) {
		taskService.deleteComment(idComment);
	}
	@GetMapping("getComment/{idComment}")
	public Comment getComment(@PathVariable long idComment) {
		return taskService.getComment(idComment);
	}
	@GetMapping("/getTaskEnterDate/{idTask}")
	public List<Task> getTasksBeetweenDates(@PathVariable long idTask, @RequestParam String startedDate, String finalDate) {
		return taskService.getTasksBeetweenDates(idTask,startedDate,finalDate);
	}
    @RequestMapping(
	    path = "/uploadFiles/{idTask}", 
	    method = RequestMethod.POST,
	               consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)

	public List<String> uploadFiles(@PathVariable long idTask,@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {
		return taskService.uploadFiles(idTask, multipartFiles);
	}
    @GetMapping("/download/{filename}")
    public  ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {

    	return taskService.downloadFiles(filename);
    }
    @GetMapping("/getTasksCount/{idTeamMember}")
    public  List<CountDto>  getTaskStatusBuGroup(@PathVariable long idTeamMember)  {

    	return taskService.getTaskStatusBuGroup(idTeamMember);
    }
    
    @GetMapping("/getTeamMembersTasksTwoDate/{idTeamMember}")
	public List<TeamTask> teamMembersTaskTwoDate(@PathVariable long idTeamMember, @RequestParam String startedDate, String finalDate) {
		return taskService.getTeamMembersTaskTwoDate(idTeamMember,startedDate,finalDate);
	}
    
    @PutMapping("/setDueDate/{idTask}")
  	public  void setDueDate(@PathVariable long idTask, @RequestBody  String finalDate) {
  		 taskService.setDueDate(idTask,finalDate);
  	}
    
    
    
    
    

}
