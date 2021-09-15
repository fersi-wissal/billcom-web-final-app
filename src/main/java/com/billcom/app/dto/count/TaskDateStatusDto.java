package com.billcom.app.dto.count;


import com.billcom.app.dto.TaskDto;

public class TaskDateStatusDto {
          private TaskDto task;
          private long difference;
		
		public TaskDateStatusDto(TaskDto task, long difference) {
			this.task = task;
			this.difference = difference;
		}
		public TaskDto getTask() {
			return task;
		}
		public void setTask(TaskDto task) {
			this.task = task;
		}
		public long getDifference() {
			return difference;
		}
		public void setDifference(long difference) {
			this.difference = difference;
		}
		  
}
       