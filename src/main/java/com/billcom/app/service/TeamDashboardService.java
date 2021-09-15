package com.billcom.app.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.billcom.app.dto.count.CountDto;
import com.billcom.app.dto.count.CountTaskTeamMemberDto;
import com.billcom.app.dto.count.TaskDateStatusDto;
import com.billcom.app.dto.count.TaskMemberDelay;
import com.billcom.app.dto.count.TeamKpi;
import com.billcom.app.dto.count.DetailKPI;
import com.billcom.app.entity.Status;
import com.billcom.app.entity.Task;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.TaskRepository;
import com.billcom.app.repository.TeamMemberRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.security.SecurityUtils;

@Service
public class TeamDashboardService {
	private TaskRepository taskRepository;
	private SecurityUtils securityUtils;

	private TeamRepository teamRepository;
	private TeamMemberRepository teamMemberRepository;

	public TeamDashboardService(TaskRepository taskRepository, TeamRepository teamRepository,
			TeamMemberRepository teamMemberRepository, 	SecurityUtils securityUtils
) {
		this.taskRepository = taskRepository;
		this.teamRepository = teamRepository;
		this.securityUtils = securityUtils;

		this.teamMemberRepository = teamMemberRepository;
	}
	public UserApp getCurrentUser() {
		return securityUtils.getLoggedUser();
	}
	/**
	 * donner un aperçu sur l'avancement de chaque team member (todo = 55% ,done 5%)
	 * 
	 * @param teamMember
	 * @return List<CountTaskDto>
	 */
	public List<CountDto> getTaskStatusBuGroup(TeamMember teamMember) {

		List<Task> taskMember = taskRepository.findAllByTeamMember(teamMember);
		Map<Status, Long> countMap = taskMember.stream()
				.collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

		List<CountDto> countTaskDtoList = new ArrayList<>();

		Iterator<Entry<Status, Long>> iterator = countMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Status, Long> entryy = iterator.next();
			Status status = entryy.getKey();
			if (!taskMember.isEmpty()) {
			CountDto countTaskDto = new CountDto(status.getStatusDescritpion(),
					(entryy.getValue() * 100) / taskMember.size());
			countTaskDtoList.add(countTaskDto);

			}
			else {
				CountDto countTaskDto = new CountDto(status.getStatusDescritpion(),0);
				countTaskDtoList.add(countTaskDto);
			}
		}

		return countTaskDtoList;}

	

	/**
	 * Avancement Tasks Task delevered on delay + detail Task Task Delevered on
	 * Adnaced + detail
	 * 
	 * @param id Team
	 * @return les détails pour chaque team Member du team
	 */
	public List<CountTaskTeamMemberDto> countTaskTeamMemberDto(Long id) {
		Set<TeamMember> teamMember = teamRepository.findById(id).get().getTeamMember();
		List<CountTaskTeamMemberDto> listCountTaskTeamMemberDto = new ArrayList<>();
		for (TeamMember t : teamMember) {
			List<CountDto> listCountTaskDto = getTaskStatusBuGroup(t);
			List<TaskDateStatusDto> taskDateStatusDto = getDelayedTaskMember(t);
			List<TaskDateStatusDto> tasksAdvancedList = getAdvancedTaskMembe(t);
			CountTaskTeamMemberDto countTaskDto = new CountTaskTeamMemberDto(t, listCountTaskDto, taskDateStatusDto,
					tasksAdvancedList);
			listCountTaskTeamMemberDto.add(countTaskDto);
		}

		return listCountTaskTeamMemberDto;
	}

	public Set<TaskMemberDelay> countTaskDelayed(Long id) {
		
		Set<TeamMember> teamMember = teamRepository.findById(id).get().getTeamMember();
		Set<TaskMemberDelay> taskMemberDelay = new HashSet<>();

		for (TeamMember t : teamMember) {
			List<Task> taskMember = taskRepository.findAllByTeamMember(t);
			List<TaskDateStatusDto> taskDateStatusDto = new ArrayList<>();
			for (Task task : taskMember) {

				if ((task.getDeleveryDate().isBefore(LocalDateTime.now()))
						& (!task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done"))) {
					long differenceInDays = ChronoUnit.DAYS.between(task.getDeleveryDate(), LocalDateTime.now());

					TaskDateStatusDto taskDtoStatuts = new TaskDateStatusDto(task.fromTaskToTaskDto(task),
							differenceInDays);
					taskDateStatusDto.add(taskDtoStatuts);

				}

			}
			TaskMemberDelay taskMemberDelayDto = new TaskMemberDelay(t, taskDateStatusDto);
			taskMemberDelay.add(taskMemberDelayDto);

		}
		return taskMemberDelay;
	}

	/**
	 * for every TeamMember
	 * 
	 * @param teamMember
	 * @return List of Tasks Delayed ( task + number of days)
	 */

	public List<TaskDateStatusDto> getDelayedTaskMember(TeamMember teamMember) {
		List<Task> taskMember = taskRepository.findAllByTeamMember(teamMember);
		List<TaskDateStatusDto> taskDateStatusDto = new ArrayList<>();
		for (Task task : taskMember) {

			if ((task.getDeleveryDate().isBefore(LocalDateTime.now()))
					& (!task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done"))) {
				long differenceInDays = ChronoUnit.DAYS.between(task.getDeleveryDate(), LocalDateTime.now());

				TaskDateStatusDto taskDtoStatuts = new TaskDateStatusDto(task.fromTaskToTaskDto(task),
						differenceInDays);
				taskDateStatusDto.add(taskDtoStatuts);

			}

		}

		return taskDateStatusDto;
	}

	/**
	 * for every TeamMember
	 * 
	 * @param teamMember
	 * @return List of Tasks In Advanced ( task + number of days)
	 */

	public List<TaskDateStatusDto> getAdvancedTaskMembe(TeamMember teamMember) {
		List<Task> taskMember = taskRepository.findAllByTeamMember(teamMember);
		List<TaskDateStatusDto> taskDateStatusDto = new ArrayList<>();
		for (Task task : taskMember) {

			if ((task.getDeleveryDate().isAfter(LocalDateTime.now()))
					& (task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done"))) {
				long differenceInDays = ChronoUnit.DAYS.between(LocalDateTime.now(), task.getDeleveryDate());

				TaskDateStatusDto taskDtoStatuts = new TaskDateStatusDto(task.fromTaskToTaskDto(task),
						differenceInDays);
				taskDateStatusDto.add(taskDtoStatuts);

			}

		}

		return taskDateStatusDto;
	}

	public CountDto countDelayReate(long id) {
		
		TeamMember teamMember = teamMemberRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("team Member Not Found"));
		float nb;
		float nbb;
		List<Task> taskTodo = taskRepository.findAllByTeamMember(teamMember).stream()
				.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now()))).collect(Collectors.toList());

		nb = taskRepository.findAllByTeamMember(teamMember).stream()
				.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now()))).count();

		nbb = taskTodo.stream().filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("done")))
				.count();

		return new CountDto("delay", (nbb / nb) * 100);
	}

	public List<CountDto> countKpiTeam(long id) {

		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("team Not Found"));
		Set<TeamMember> teamMemberList = team.getTeamMember();
		List<CountDto> countKpiTeamList = new ArrayList<>();
		float nbPlannedTask = 0;
		float nbDelayedTask = 0;
		float nbAdvancedTask = 0;
		for (TeamMember t : teamMemberList) {
			nbPlannedTask = nbPlannedTask + taskRepository.findAllByTeamMember(t).stream()
					.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now()))).count();


			nbDelayedTask = nbDelayedTask + taskRepository.findAllByTeamMember(t).stream()
					.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now()))).collect(Collectors.toList())
					.stream().filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("done")))
					.count();
			nbAdvancedTask = nbAdvancedTask + taskRepository.findAllByTeamMember(t).stream()
					.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now()))).collect(Collectors.toList())
					.stream().filter(task -> (task.getStatus().getStatusDescritpion().equalsIgnoreCase("done")))
					.count();

		}
		if (nbPlannedTask > 0) {
		countKpiTeamList.add(new CountDto("delay rate", (nbDelayedTask / nbPlannedTask) * 100));
		countKpiTeamList.add(new CountDto("Progress rate", (nbAdvancedTask / nbPlannedTask) * 100));}
		return countKpiTeamList;
	}

	public List<DetailKPI> countKPIweek(long id) {
	return MonthStatistic(id ,LocalDateTime.now().getMonth(),LocalDateTime.now().getYear());
	}
	
	public List<DetailKPI> MonthStatistic(long id ,Month month,int year) {
		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("team Not Found"));
		Set<TeamMember> teamMemberList = team.getTeamMember();
		List<DetailKPI> weekList = new ArrayList<>();
	
		int i;
	
		for (i = 1; i < 5; i++) {
			int nb = i;
			long nbDone  = 0;
			long nbTask = 0;
			long nbDelay = 0;
			long nbAdvanced =0;
			for (TeamMember t : teamMemberList) {
				nbDone = nbDone + taskRepository.findAllByTeamMember(t).stream()
						.filter(task -> (task.getDeleveryDate().getMonth() == month & task.getDeleveryDate().getYear() == year))
						.filter(task -> (task.getDeleveryDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH) == nb))
						.filter(task -> task.getStatus().getStatusDescritpion().equalsIgnoreCase("DONE")).count();
				
				 nbTask = nbTask+ taskRepository.findAllByTeamMember(t).stream()
						.filter(task -> (task.getDeleveryDate().getMonth() == month & task.getDeleveryDate().getYear() == year ))
						.filter(task -> (task.getDeleveryDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH) == nb)).count();


				 nbDelay = nbDelay +  taskRepository.findAllByTeamMember(t).stream()
						.filter(task -> (task.getDeleveryDate().getMonth() == month  & task.getDeleveryDate().getYear() == year ))
						.filter(task -> (task.getDeleveryDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH) == nb))
						.filter(task -> (task.getDeleveryDate().isBefore(LocalDateTime.now())))
						.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("DONE"))).count();
					

				
				 nbAdvanced = nbAdvanced +taskRepository.findAllByTeamMember(t).stream()
						.filter(task -> (task.getDeleveryDate().getMonth() == month   & task.getDeleveryDate().getYear() == year))
						.filter(task -> (task.getDeleveryDate().get(ChronoField.ALIGNED_WEEK_OF_MONTH) == nb))
						.filter(task -> (task.getDeleveryDate().isAfter(LocalDateTime.now())))
						.filter(task -> (task.getStatus().getStatusDescritpion().equalsIgnoreCase("DONE"))).count();
				
			}
			List <CountDto> list = new ArrayList<>();
			CountDto c2 = new CountDto(" Number Task", nbTask);

			list.add(c2);
			CountDto c = new CountDto(" Task Done", nbDone);
			
			list.add(c);
			
			CountDto c3 = new CountDto("Task Delay", nbDelay);
			list.add(c3);
			CountDto c4 = new CountDto(" Task Advanced", nbAdvanced);
			list.add(c4);
			DetailKPI weekKPI = new DetailKPI("week "+ nb, list);
			weekList.add(weekKPI);
			
		}
		return weekList;
	}

    public List <TeamKpi> listTeamKpi(){
    List <Team> teamList =	teamRepository.findAll().stream().filter(team -> team.getLeader().getUser().getId() == getCurrentUser().getId())
		.collect(Collectors.toList());
    	
    List <TeamKpi> teamKpiList = new ArrayList<>();
    for(Team team : teamList )	{
    
   		List<Task> taskMember = new ArrayList<>();
          for (TeamMember teamMember : team.getTeamMember()  )   {
        	  
             taskMember.addAll(taskRepository.findAllByTeamMember(teamMember));

          }
          Map<Status, Long> countMap = taskMember.stream()
  				.collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

  		List<CountDto> countTaskDtoList = new ArrayList<>();

  		Iterator<Entry<Status, Long>> iterator = countMap.entrySet().iterator();
  		while (iterator.hasNext()) {
  			Entry<Status, Long> entryy = iterator.next();
  			Status status = entryy.getKey();
  			if (!taskMember.isEmpty()) {
  			CountDto countTaskDto = new CountDto(status.getStatusDescritpion(),
  					(entryy.getValue() * 100) / taskMember.size());
  			countTaskDtoList.add(countTaskDto);

  			}
  			else {
  				CountDto countTaskDto = new CountDto(status.getStatusDescritpion(),0);
  				countTaskDtoList.add(countTaskDto);
  			}
  		}


  		teamKpiList.add(new TeamKpi(team.getTeamName(), team.getId(), countTaskDtoList));
    	
    }
    
    
    
    	return teamKpiList;
    }




}
